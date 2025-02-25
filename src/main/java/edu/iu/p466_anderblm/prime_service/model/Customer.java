package edu.iu.p466_anderblm.prime_service.model; // Package declaration

// Define a simple Customer model with username and password properties
public class Customer {

    // Private fields for username and password
    private String username;
    private String password;

    // Constructor to initialize a Customer with a username and password
    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }
}

