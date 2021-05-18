package com.pvt73.recycling.model.service.image;

import com.pvt73.recycling.model.dao.Image;
import com.pvt73.recycling.model.dao.LatLng;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    boolean isNotImage(MultipartFile file);

    Image creat(String userId, boolean clean, LatLng coordinates, String description, MultipartFile file);

    void delete(String id);

    Image findById(String id);
}
