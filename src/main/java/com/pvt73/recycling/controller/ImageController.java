package com.pvt73.recycling.controller;

import com.pvt73.recycling.model.dao.Image;
import com.pvt73.recycling.model.service.ImageService.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController()
@RequestMapping("image")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    @PostMapping(value = "/upload", headers = "content-type=multipart/*")
    public ResponseEntity<Image> uploadImage(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty())
            return ResponseEntity.badRequest().build();


        return ResponseEntity.ok(imageService.uploadImage(1, true, 1.1111, 2.2222, file));
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam String id) {
        imageService.delete(id);
    }


}
