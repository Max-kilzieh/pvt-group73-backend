package com.pvt73.recycling.controller;

import com.pvt73.recycling.model.dao.Image;
import com.pvt73.recycling.model.service.ImageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/t")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

//    @RequestMapping(value = "/upload",method = RequestMethod.POST)
//    public Image uploadImage(@RequestParam("image") MultipartFile image) throws IOException {
//
//        return imageService.saveImage(image);
//    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("name") String name) {
        Image image = imageService.getImage(name);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }


        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getImageType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"")
                .body(new ByteArrayResource(image.getData()));
    }

    @GetMapping("/delete-all")
    public void deleteAll() {
        imageService.deleteAll();

    }
    @PostMapping("/post")
    public String postTest(){
        return "I am working!";
    }
}
