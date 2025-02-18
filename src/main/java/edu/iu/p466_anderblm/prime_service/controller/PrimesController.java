package edu.iu.p466_anderblm.prime_service.controller;

import edu.iu.p466_anderblm.prime_service.service.IPrimeService;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/primes")
public class PrimesController {
    IPrimeService primeService;

    public PrimesController(IPrimeService primeService)
    {
        this.primeService = primeService;
    }


    @GetMapping("/{n}")
    public boolean isPrime(@PathVariable int n) {
        return primeService.isPrime(n);
    }

}
