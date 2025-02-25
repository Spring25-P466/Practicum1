package edu.iu.p466_anderblm.prime_service.repository;


import com.sun.org.slf4j.internal.LoggerFactory;
import com.sun.tools.javac.util.List;
import edu.iu.p466_anderblm.prime_service.model.Customer;
import org.springframework.stereotype.Repository;
import sun.nio.cs.StandardCharsets;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Logger;

@Repository // Marks this class as a Spring repository (data access component)
public class AuthenticationFileRepository implements IAuthenticationRepository {

    // Create a logger instance for logging errors or important events
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFileRepository.class);

    // Define the file path for storing customer data
    private static final String DATABASE_NAME = "data/customers.txt";
    // Define the system-dependent newline character
    private static final String NEW_LINE = System.lineSeparator();

    // Constructor ensures that the storage file exists
    public AuthenticationFileRepository() {
        File file = new File(DATABASE_NAME);           // Create a File object for the database file
        file.getParentFile().mkdirs();                  // Ensure the parent directory exists
        try {
            file.createNewFile();                       // Attempt to create the file if it does not exist
        } catch (IOException e) {
            LOG.error(e.getMessage());                  // Log an error if file creation fails
        }
    }

    @Override
    public Customer findByUsername(String username) throws IOException {
        Path path = Paths.get(DATABASE_NAME);           // Convert the database filename to a Path object
        List<String> data = Files.readAllLines(path);     // Read all lines from the file into a list of strings
        // Iterate through each line to search for the username
        for (String line : data) {
            if (!line.trim().isEmpty()) {               // Skip empty lines
                String[] properties = line.split(",");  // Split the line by comma into an array (username,password)
                // Check if the username matches (ignoring case and trimming extra spaces)
                if (properties[0].trim().equalsIgnoreCase(username.trim())) {
                    // If a match is found, create and return a new Customer instance with the stored username and password
                    return new Customer(properties[0].trim(), properties[1].trim());
                }
            }
        }
        // Return null if no matching username is found
        return null;
    }

    @Override
    public boolean save(Customer customer) throws IOException {
        // Check if a customer with the same username already exists
        Customer x = findByUsername(customer.getUsername());
        if (x == null) { // If not found, proceed to save the new customer
            Path path = Paths.get(DATABASE_NAME);       // Convert the file path string to a Path object
            // Format the customer data as "username,password"
            String data = String.format("%1$s,%2$s",
                    customer.getUsername().trim(),
                    customer.getPassword().trim());
            data += NEW_LINE;                           // Append a newline character
            // Write the data to the file using UTF-8 encoding, creating the file if needed and appending to it
            Files.write(path, data.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
            return true; // Return true indicating the save was successful
        }
        return false; // Return false if a customer with that username already exists
    }
}
