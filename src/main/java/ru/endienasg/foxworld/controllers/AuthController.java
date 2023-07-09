package ru.endienasg.foxworld.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.endienasg.foxworld.models.requests.AuthRequest;
import ru.endienasg.foxworld.models.requests.RegistryRequest;
import ru.endienasg.foxworld.repositories.IUserRepository;
import ru.endienasg.foxworld.services.AuthService;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("auth")
@CrossOrigin
public class AuthController {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private AuthService authService = new AuthService();
    @Async
    @GetMapping("getAll")
    public CompletableFuture<?> Test(){
        return CompletableFuture.supplyAsync(() -> {
            return userRepository.findAll();
        });
    }
    @Async
    @PostMapping("registry")
    public CompletableFuture<?> Registry(@RequestBody RegistryRequest request){
        return CompletableFuture.supplyAsync(()->{
            return authService.Registry(request,userRepository,passwordEncoder);
        });
    }

    @Async
    @PostMapping("auth")
    public CompletableFuture<?> Auth(@RequestBody AuthRequest request){
        return CompletableFuture.supplyAsync(()->{
           return authService.Auth(request,userRepository, passwordEncoder);
        });
    }
}
