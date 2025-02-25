package edu.iu.p466_anderblm.prime_service.security;


import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

@Component // Marks this class as a Spring-managed component (bean)
public class KeyGeneratorUtils {

    // Private constructor to prevent instantiation (utility class)
    private KeyGeneratorUtils() {}

    // Static method to generate an RSA key pair
    static KeyPair generateRsaKey() {
        KeyPair keypair; // Variable to hold the generated key pair
        try {
            // Create a KeyPairGenerator for the RSA algorithm
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            // Initialize the generator with a key size of 2048 bits for strong security
            keyPairGenerator.initialize(2048);
            // Generate the RSA key pair (public and private keys)
            keypair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            // Wrap and rethrow any exception as an IllegalStateException
            throw new IllegalStateException(ex);
        }
        // Return the generated RSA key pair
        return keypair;
    }
}



