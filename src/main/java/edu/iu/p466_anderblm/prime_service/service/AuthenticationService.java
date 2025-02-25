package edu.iu.p466_anderblm.prime_service.service; // Package declaration

// Import the Customer model and the repository interface
import edu.iu.p466_anderblm.prime_service.model.Customer;
import edu.iu.p466_anderblm.prime_service.repository.IAuthenticationRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException; // Import IOException

@Service // Marks this class as a service component managed by Spring
public class AuthenticationService implements IAuthenticationService {

    // Declare a dependency on the authentication repository
    IAuthenticationRepository authenticationRepository;

    // Constructor for dependency injection of the repository
    public AuthenticationService(IAuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    // Register a new customer by encoding their password and saving their data
    @Override
    public boolean register(Customer customer) throws IOException {
        // Create an instance of BCryptPasswordEncoder to hash the password
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        // Encode the customer's plain text password
        String passwordEncoded = bc.encode(customer.getPassword());
        // Set the encoded password on the customer object
        customer.setPassword(passwordEncoded);
        // Save the customer using the repository and return the result (true if successful)
        return authenticationRepository.save(customer);
    }

    // Load user details by username (typically used by Spring Security during authentication)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // Retrieve the customer from the repository using the username
            Customer customer = authenticationRepository.findByUsername(username);
            if (customer == null) {
                // If no customer is found, throw an exception indicating the username is not found
                throw new UsernameNotFoundException("");
            }
            // Build and return a UserDetails object using Spring Security's User builder
            return User
                    .withUsername(username)
                    .password(customer.getPassword()) // Use the encoded password
                    .build();
        } catch (IOException e) {
            // Wrap any IOException in a RuntimeException
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean login(String username, String password) throws IOException {
        return false;
    }

    @RestController
    public class AuthenticationController{

        private TokenService tokenService;
        private final AuthenticationManager authenticationManager;
        private final IAuthenticationService authenticationService;
        public AuthenticationController(IAuthenticationService authenticationService,
                                        AuthenticationManager authenticationManager,
                                        TokenService tokenService){
            this.authenticationService = authenticationService;
            this.authenticationManager = authenticationManager;
            this.tokenService = tokenService;
        }
        @PostMapping("/register")
        public boolean register(@RequestBody Customer customer){
            try{
                return authenticationService.register(customer);
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }
        @PostMapping("/login")
        public String login(@RequestBody Customer customer){
            Authentication authentication = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    customer.getUsername()
                                    , customer.getPassword()));

            return tokenService.generateToken(authentication);
        }
    }

}
