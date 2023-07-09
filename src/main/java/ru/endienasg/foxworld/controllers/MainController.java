package ru.endienasg.foxworld.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("main")
@CrossOrigin
public class MainController {
    @GetMapping("test")
    public CompletableFuture<?> Test(){
        return CompletableFuture.supplyAsync(()->{
            return "work";
        });
    }
}
