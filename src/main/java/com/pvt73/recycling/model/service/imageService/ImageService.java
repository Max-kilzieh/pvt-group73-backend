package com.pvt73.recycling.model.service.imageService;

import com.pvt73.recycling.model.dao.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    boolean isNotImage(MultipartFile file);

    Image uploadImage(int userId, boolean clean, double latitude, double longitude, String description, MultipartFile file) throws IOException;

    void delete(String id);
}
