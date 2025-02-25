package edu.iu.p466_anderblm.prime_service.controller;


import edu.iu.p466_anderblm.prime_service.service.IPrimeService;

import org.springframework.web.bind.annotation.*;

@RestController          // Indicates that this class is a REST controller
@CrossOrigin             // Enables Cross-Origin Resource Sharing (CORS) for this controller
@RequestMapping("/primes")// Sets the base URL for all endpoints in this controller
public class PrimesController {

    // Declare a dependency on the prime-checking service
    IPrimeService primeService;

    // Constructor for dependency injection of the prime service
    public PrimesController(IPrimeService primeService) {
        this.primeService = primeService;
    }

    // Map HTTP GET requests to "/primes/{n}" where {n} is a path variable
    @GetMapping("/{n}")
    public boolean isPrime(@PathVariable int n) { // @PathVariable binds the URL segment to the parameter 'n'
        // Delegate the check to the prime service and return the result (true if prime, false otherwise)
        return primeService.isPrime(n);
    }
}

