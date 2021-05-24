package com.pvt73.recycling.model.service.image;

import com.pvt73.recycling.model.dao.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface ImageService {
    boolean isNotImage(MultipartFile file);

    Image creat(MultipartFile file);

    void delete(String imageId);

    void deleteAll(Set<Image> imageSet);

}
