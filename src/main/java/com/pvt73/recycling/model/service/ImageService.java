package com.pvt73.recycling.model.service;

import com.pvt73.recycling.model.dao.Image;
import com.pvt73.recycling.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {

    private final ImageRepository service;

    public ImageService(ImageRepository service) {
        this.service = service;
    }

    public Image saveImage(MultipartFile file) throws IOException {
        Image toSave = new Image(true, 11.11111, 22.333333, file.getContentType(), file.getBytes(), file.getOriginalFilename());
        return service.save(toSave);
    }

    public Image getImage(String name) {

        return service.findByName(name);
    }

    public void deleteAll(){
        service.deleteAll();
    }
}
