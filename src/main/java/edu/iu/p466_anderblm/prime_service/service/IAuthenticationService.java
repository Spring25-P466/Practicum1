package edu.iu.p466_anderblm.prime_service.service;

import edu.iu.p466_anderblm.prime_service.model.Customer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;

// Define an interface for authentication services
public interface IAuthenticationService {
    // Method to register a customer; returns true if successful, false otherwise
    boolean register(Customer customer) throws IOException;

    // Load user details by username (typically used by Spring Security during authentication)
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    // Method to login a customer using their username and password
    boolean login(String username, String password) throws IOException;
}
