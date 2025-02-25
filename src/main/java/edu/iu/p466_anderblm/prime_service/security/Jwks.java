package edu.iu.p466_anderblm.prime_service.security;

import java.security.KeyPair;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

// Utility class to generate JSON Web Keys (JWK) for JWT signing and verification
public class Jwks {

    // Private constructor prevents instantiation (utility class)
    private Jwks() {}

    // Static method to generate an RSAKey for JWT operations
    public static RSAKey generateRsa() {
        // Generate an RSA key pair using the KeyGeneratorUtils utility
        KeyPair keyPair = KeyGeneratorUtils.generateRsaKey();
        // Extract the public key and cast it to RSAPublicKey
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // Extract the private key and cast it to RSAPrivateKey
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // Build and return an RSAKey instance:
        // - Set the public key for JWT verification.
        // - Set the private key for signing tokens.
        // - Generate a random key ID using a UUID.
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }
}
