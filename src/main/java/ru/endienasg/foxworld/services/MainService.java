package ru.endienasg.foxworld.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.endienasg.foxworld.models.CapeModel;
import ru.endienasg.foxworld.models.MetaDataVariants;
import ru.endienasg.foxworld.models.MetadataModel;
import ru.endienasg.foxworld.models.SkinModel;
import ru.endienasg.foxworld.models.responses.TextureFullResponse;
import ru.endienasg.foxworld.models.responses.TextureResponse;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class MainService {
    public CompletableFuture<?> GetSkinsAndCapes(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                SkinModel skinModel = (SkinModel) GetSkin(name).get();
                CapeModel capeModel = (CapeModel) GetCape(name).get();
                if(capeModel != null){
                   TextureFullResponse response = new TextureFullResponse();
                   response.setSKIN(skinModel);
                   response.setCAPE(capeModel);
                    return response;
                }else{
                    TextureResponse response = new TextureResponse();
                    response.setSKIN(skinModel);
                    return response;
                }
            } catch (Exception e) {
                log.warn(name + " haven't skin" + e.getMessage());
                return "";
            }
        });
    }

    public CompletableFuture<?> GetCape(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {

                byte[] fileArray = Files.readAllBytes(Paths.get("capes/" + name + ".png"));
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update(fileArray);
                byte[] hash = digest.digest();
                String capeHash = Base64.getEncoder().encodeToString(hash);
                CapeModel model = new CapeModel();
                model.setDigest(capeHash);
                model.setUrl("https://diplom.foxworld.online/main/capes/get/" + name + ".png");

                return model;
            } catch (Exception e) {
                return null;
            }
        });
    }

    public CompletableFuture<?> GetSkin(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                byte[] fileArray = Files.readAllBytes(Paths.get("skins/" + name + ".png"));
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update(fileArray);
                byte[] hash = digest.digest();
                String skinHash = Base64.getEncoder().encodeToString(hash);

                SkinModel skinModel = new SkinModel();

                skinModel.setUrl("https://diplom.foxworld.online/main/skins/get/" + name + ".png");
                skinModel.setDigest(skinHash);

                InputStream st = new ByteArrayInputStream(fileArray);
                BufferedImage bufferedImage = ImageIO.read(st);
                ColorModel model = bufferedImage.getColorModel();
                float fraction = (float) bufferedImage.getWidth() / 8;
                float x = fraction * (float) 6.75;
                float y = fraction * (float) 2.5;
                int pixel = bufferedImage.getRGB(Math.round(x), Math.round(y));
                bufferedImage.getAlphaRaster();
                Color c = new Color(pixel, true);
                if (c.getAlpha() == 0) {
                    MetadataModel meta = new MetadataModel();
                    meta.setModel(MetaDataVariants.slim);
                    skinModel.setMetadata(meta);
                } else {
                    MetadataModel meta = new MetadataModel();
                    meta.setModel(MetaDataVariants.normal);
                    skinModel.setMetadata(meta);
                }
                return skinModel;
            } catch (Exception e) {
                try {
                    byte[] defaultFileArr = Files.readAllBytes(Paths.get("skins/default.png"));
                    MessageDigest digest = MessageDigest.getInstance("MD5");
                    digest.update(defaultFileArr);
                    byte[] hash = digest.digest();
                    String skinHash = Base64.getEncoder().encodeToString(hash);

                    SkinModel skinModel = new SkinModel();
                    skinModel.setUrl("https://diplom.foxworld.online/main/skins/get/default.png");
                    skinModel.setDigest(skinHash);
                    MetadataModel meta = new MetadataModel();
                    meta.setModel(MetaDataVariants.normal);
                    skinModel.setMetadata(meta);
                    return skinModel;
                } catch (Exception ex) {
                    return "";
                }
            }
        });
    }

    public CompletableFuture<?> GetSkinFile(String name){
        return CompletableFuture.supplyAsync(()->{
            try{
                byte[] file = Files.readAllBytes(Paths.get("skins/" + name + ".png"));
                return file;
            }catch (Exception e){
                try{
                    byte[] file = Files.readAllBytes(Paths.get("skins/default.png"));
                    return file;
                }catch (Exception ex){
                    return "";
                }
            }
        });
    }

    public CompletableFuture<?> GetCapeFile(String name){
        return CompletableFuture.supplyAsync(()->{
            try{
                byte[] file = Files.readAllBytes(Paths.get("capes/" + name + ".png"));
                return file;
            }catch (Exception e){
                return "";
            }
        });
    }

    public CompletableFuture<?> UploadSkin(MultipartFile file, String name){
        return CompletableFuture.supplyAsync(()->{
            try {
                byte[] fileToWrite = file.getBytes();
                try(FileOutputStream stream = new FileOutputStream("skins/" + name + ".png")){
                    stream.write(fileToWrite);
                }catch (Exception e){
                    return e.getMessage();
                }
                return "";
            } catch (Exception e) {
                return e.getMessage();
            }
        });
    }

    public CompletableFuture<?> GetAsset(String name){
        return CompletableFuture.supplyAsync(()->{
            try {
                byte[] fileToSend = Files.readAllBytes(Paths.get("files/" + name));
                return fileToSend;
            } catch (IOException e) {
                return "";
            }
        });
    }


}
