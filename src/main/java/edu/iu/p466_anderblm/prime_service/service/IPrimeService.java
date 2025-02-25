package edu.iu.p466_anderblm.prime_service.service;

// Define an interface for checking prime numbers
public interface IPrimeService {
    // Method to determine if a given number (as a long) is prime; returns true if prime, false otherwise
    boolean isPrime(long n);
}

