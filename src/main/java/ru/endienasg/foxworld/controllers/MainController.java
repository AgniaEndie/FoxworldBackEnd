package ru.endienasg.foxworld.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.endienasg.foxworld.services.MainService;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("main")
@CrossOrigin
public class MainController {
    private MainService service = new MainService();

    @GetMapping("test")
    public CompletableFuture<?> Test(){
        return CompletableFuture.supplyAsync(()->{
            return "work";
        });
    }
    @Async
    @GetMapping("skins/{skins}")
    public CompletableFuture<?> GetSkinsAndCapes(@PathVariable String skins){
        return CompletableFuture.supplyAsync(()->{
           return service.GetSkinsAndCapes(skins);
        });
    }

    @Async
    @GetMapping(value = "skins/get/{skins}.png", produces = MediaType.IMAGE_PNG_VALUE)
    public CompletableFuture<?> GetSkinFile(@PathVariable String skins){
        return CompletableFuture.supplyAsync(()->{
            return service.GetSkinFile(skins);
        });
    }

    @Async
    @GetMapping(value = "capes/get/{cape}.png", produces = MediaType.IMAGE_PNG_VALUE)
    public CompletableFuture<?> GetCapeFile(@PathVariable String cape){
        return CompletableFuture.supplyAsync(()->{
            return service.GetCapeFile(cape);
        });
    }

    @Async
    @PostMapping("skins/add/{name}")
    public CompletableFuture<?> UploadSkin(@RequestParam("file") MultipartFile file, @PathVariable String name){
        return CompletableFuture.supplyAsync(()->{
           return service.UploadSkin(file,name);
        });
    }

    @Async
    @GetMapping(value = "assets/get/{name}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public CompletableFuture<?> GetAsset(@PathVariable("name") String name, HttpServletResponse response){
        return CompletableFuture.supplyAsync(()->{
           return  service.GetAsset(name);
        });
    }

}
