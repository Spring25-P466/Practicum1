package edu.iu.p466_anderblm.prime_service.security;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.beans.Customizer;
import java.security.interfaces.RSAKey;

@Configuration                // Indicates that this class provides Spring bean definitions
@EnableWebSecurity          // Enables web security features in Spring Security
@EnableMethodSecurity       // Enables method-level security (e.g., annotations like @PreAuthorize)
public class SecurityConfig {

    // Field to hold the generated RSA key for JWT operations
    private RSAKey rsaKey;

    // Constructor: generates an RSA key using the Jwks utility at startup
    public SecurityConfig() {
        this.rsaKey = Jwks.generateRsa();
    }

    // Bean that supplies a JWKSource (JSON Web Key source) for JWT encoding/decoding
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        // Create a JWKSet containing the RSA key
        JWKSet jwkSet = new JWKSet(rsaKey);
        // Return a lambda that selects keys from the JWKSet based on the provided selector
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    // Bean for the JwtEncoder that signs and encodes JWT tokens using the JWK source
    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwks) {
        return new NimbusJwtEncoder(jwks);
    }

    // Bean for the JwtDecoder that verifies and decodes JWT tokens using the RSA public key
    @Bean
    JwtDecoder jwtDecoder() throws JOSEException {
        // Build the decoder using the RSA public key extracted from our rsaKey
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    }

    // Bean to define the security filter chain for handling HTTP requests
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Enable Cross-Origin Resource Sharing (CORS) using default settings
                // (Note: 'Customer.withDefualts()' appears to be a typo and should likely be 'withDefaults()')
                .cors(Customer.withDefualts())
                // Disable CSRF protection since JWT-based stateless authentication is used
                .csrf(x -> x.disable())
                // Configure URL authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Permit all POST requests to /register and /login (public endpoints)
                        .requestMatchers(HttpMethod.POST, "/register", "/login").permitAll()
                        // Require authentication for all other requests
                        .anyRequest().authenticated()
                )
                // Configure session management to be stateless (no HTTP sessions maintained)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configure the application as an OAuth2 resource server using JWTs for authentication
                .oath2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                // Build and return the complete security filter chain
                .build();
    }

    // Bean to create the AuthenticationManager responsible for processing authentication requests
    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService) {
        // Create a DAO-based authentication provider
        var authProvider = new DaoAuthenticationProvider();
        // Set the UserDetailsService that loads user-specific data during authentication
        authProvider.setUserDetailsService(userDetailsService);
        // Set the password encoder to BCrypt for secure password hashing
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        // Return an AuthenticationManager that uses the configured authentication provider
        return new ProviderManager(authProvider);
    }
}
