package com.pvt73.recycling.model.service.imageService;

import com.pvt73.recycling.model.dao.Image;
import com.pvt73.recycling.model.dao.LatLng;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    boolean isNotImage(MultipartFile file);

    Image uploadImage(int userId, boolean clean, LatLng coordinates, String description, MultipartFile file);

    void delete(String id);
}
