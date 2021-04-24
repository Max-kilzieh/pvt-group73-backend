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
@RequestMapping("image")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "/upload")
    public Image uploadImage(@RequestParam("image") MultipartFile image) throws IOException {

        return imageService.saveImage(image);
    }

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

    @DeleteMapping("/delete-all")
    public void deleteAll() {
        imageService.deleteAll();
    }

}
