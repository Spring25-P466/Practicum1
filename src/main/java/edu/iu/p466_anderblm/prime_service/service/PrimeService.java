package edu.iu.p466_anderblm.prime_service.service;


import org.springframework.stereotype.Service;

@Service
public class PrimeService implements IPrimeService {
    @Override
    public boolean isPrime(long n) {
        if(n == 2) return true;
        for (int i = 2; i < n; i++) {
            if(n % i == 0) return false;
        }
        return true;
    }
}
