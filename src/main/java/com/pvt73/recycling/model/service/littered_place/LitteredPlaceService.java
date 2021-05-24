package com.pvt73.recycling.model.service.littered_place;

import com.pvt73.recycling.model.dao.Image;
import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.dao.LitteredPlace;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface LitteredPlaceService {
    LitteredPlace creat(LitteredPlace litteredPlace);

    LitteredPlace findById(int id, LatLng currentCoordinates);

    List<LitteredPlace> findAllNearby(LatLng coordinates, int offset, int limit);

    LitteredPlace update(LitteredPlace litteredPlace, int litteredPlaceId);

    void delete(int litteredPlaceId);

    int countCleanedBy(String userId);

    Image addImage(int litteredPlaceId, MultipartFile file, boolean clean);

    void deleteImage(int litteredPlaceId, String imageId);

    Image findImageById(int litteredPlaceId, String imageId);

    Set<Image> findAllImage(int litteredPlaceId);

    boolean isNotImage(MultipartFile file);

}
