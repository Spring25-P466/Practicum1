package edu.iu.p466_anderblm.prime_service.service;

import org.springframework.stereotype.Service;

@Service // Marks this class as a service component managed by Spring
public class PrimeService implements IPrimeService {

    // Implements the method to check if a number is prime
    @Override
    public boolean isPrime(long n) {
        // Special case: 2 is prime
        if(n == 2) return true;
        // Check divisibility for numbers from 2 up to n-1
        for (int i = 2; i < n; i++) {
            // If n is divisible by any number i, then n is not prime
            if(n % i == 0) return false;
        }
        // If no divisors are found, n is prime
        return true;
    }
}
