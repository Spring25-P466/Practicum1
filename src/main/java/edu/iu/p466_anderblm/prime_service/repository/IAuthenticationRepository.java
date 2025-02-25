package edu.iu.p466_anderblm.prime_service.repository;

import edu.iu.p466_anderblm.prime_service.model.Customer;
import java.io.IOException;

// Define an interface that outlines methods for saving and retrieving Customer objects
public interface IAuthenticationRepository {
    // Method to save a customer; may throw an IOException if something goes wrong
    boolean save(Customer customer) throws IOException;

    // Method to find a customer by their username; returns a Customer or null if not found
    Customer findByUsername(String username) throws IOException;
}
