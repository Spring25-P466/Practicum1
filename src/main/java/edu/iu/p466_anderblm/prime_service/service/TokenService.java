package edu.iu.p466_anderblm.prime_service.service;

// Import classes for handling authentication details and JWT encoding
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

// Import classes for handling time and collections
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service // Annotation to indicate that this class is a service component managed by Spring
public class TokenService {

    // Field to hold the JwtEncoder instance, which will be injected by Spring
    private final JwtEncoder encoder;

    // Constructor for dependency injection of JwtEncoder
    public TokenService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    // Method to generate a JWT token based on the provided Authentication object
    public String generateToken(Authentication authentication) {
        // Capture the current time as the moment the token is issued
        Instant now = Instant.now();

        // Build a space-separated string of all authorities granted to the authenticated user
        String scope = authentication
                .getAuthorities().stream()                      // Create a stream from the collection of GrantedAuthority objects
                .map(GrantedAuthority::getAuthority)            // Extract the string representation of each authority
                .collect(Collectors.joining(" "));              // Join them into a single string with spaces separating each authority

        // Build the JWT claims set with the necessary token information
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")                                 // Set the issuer claim (could be your app's name or URL)
                .issuedAt(now)                                  // Set the token's issuance time
                .expiresAt(now.plus(1, ChronoUnit.HOURS))         // Set the token to expire one hour from now
                .subject(authentication.getName())              // Set the subject claim to the authenticated user's name
                .claim("scope", scope)                          // Add a custom claim "scope" with the granted authorities
                .build();                                       // Finalize and build the JwtClaimsSet

        // Encode the claims into a JWT token and return the token's value as a string
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}

