package com.pvt73.recycling.model.service.littered_place;

import com.pvt73.recycling.exception.ResourceNotFoundException;
import com.pvt73.recycling.model.dao.CleaningStatus;
import com.pvt73.recycling.model.dao.Image;
import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.dao.LitteredPlace;
import com.pvt73.recycling.model.service.image.ImageService;
import com.pvt73.recycling.model.util.DistanceAndPagingUtil;
import com.pvt73.recycling.repository.LitteredPlaceRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class LitteredPlaceServiceImpl implements LitteredPlaceService {
    private final LitteredPlaceRepository repository;
    private final ImageService imageService;

    @Override
    public LitteredPlace creat(@NonNull LitteredPlace newLitteredPlace) {
        newLitteredPlace.setEvent(false);
        newLitteredPlace.setCleanedBy(null);

        newLitteredPlace.setCleaningStatus(CleaningStatus.NOT_CLEAN);

        return repository.save(newLitteredPlace);

    }

    @Override
    public LitteredPlace findById(int id, @NonNull LatLng coordinates) {
        LitteredPlace place = repository.findById(id)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("id", id, "littered place not found.");
                });

        place.setDistance(DistanceAndPagingUtil
                .calculateDistanceBetweenGpsCoordinates(coordinates, place.getCoordinates()));

        return place;
    }

    @Override
    public List<LitteredPlace> findAllNearby(@NonNull LatLng coordinates, int offset, int limit) {
        List<LitteredPlace> litteredPlaceList = repository.findAllByEventFalse();

        litteredPlaceList.removeIf(litteredPlace -> {
            LocalDateTime expireDate = litteredPlace.getCleanedAt();
            if (expireDate != null) {
                expireDate = expireDate.plusDays(1);
                return LocalDateTime.now().isAfter(expireDate);
            }
            return false;
        });

        litteredPlaceList.forEach(place -> place.setDistance(
                DistanceAndPagingUtil.calculateDistanceBetweenGpsCoordinates(
                        coordinates, place.getCoordinates())));

        litteredPlaceList.sort(Comparator.comparingDouble(LitteredPlace::getDistance));

        int[] pageAndSize = DistanceAndPagingUtil.calculatePageAndSize(offset, limit, litteredPlaceList.size());

        return litteredPlaceList.subList(pageAndSize[0], pageAndSize[1]);
    }

    @Override
    public LitteredPlace update(@NonNull LitteredPlace newPlace, int id) {
        return repository.findById(id)
                .map(place -> {
                    place.setCoordinates(newPlace.getCoordinates());
                    place.setAddress(newPlace.getAddress());
                    place.setImageSet(newPlace.getImageSet());
                    place.setDescription(newPlace.getDescription());
                    place.setUserId(newPlace.getUserId());

                    if (newPlace.getCleaningStatus() == CleaningStatus.CLEAN && (
                            newPlace.getCleanedBy() == null || newPlace.getCleanedBy().isBlank())) {
                        throw new IllegalArgumentException("cleanedBy must be provided");
                    }

                    if (newPlace.getCleaningStatus() == CleaningStatus.CLEAN)
                        place.setCleanedBy(newPlace.getCleanedBy());


                    place.setCleaningStatus(newPlace.getCleaningStatus());
                    place.setEvent(newPlace.isEvent());
                    return repository.save(place);
                })
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("id", id, "littered place not found.");
                });
    }

    @Override
    public void delete(int litteredPlaceId) {

        LitteredPlace litteredPlace = repository.findById(litteredPlaceId)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("litteredPlaceId", litteredPlaceId, "littered place not found.");
                });

        imageService.deleteAll(litteredPlace.getImageSet());
        repository.delete(litteredPlace);
    }


    public Image addImage(int litteredPlaceId, MultipartFile file) {
        LitteredPlace litteredPlace = findByID(litteredPlaceId);
        Set<Image> imageSet = litteredPlace.getImageSet();
        Image image = imageService.creat(file);
        imageSet.add(image);

        litteredPlace.setImageSet(imageSet);
        repository.save(litteredPlace);
        return image;
    }

    public void deleteImage(int litteredPlaceId, @NonNull String imageId) {
        LitteredPlace litteredPlace = findByID(litteredPlaceId);

        if (!litteredPlace.containImage(imageId))
            throw new ResourceNotFoundException("imageId", imageId, "image not found.");

        imageService.delete(imageId);

        Set<Image> imageSet = litteredPlace.getImageSet();
        imageSet.removeIf(image -> image.getId().equals(imageId));
        litteredPlace.setImageSet(imageSet);
        repository.save(litteredPlace);
    }

    public Image findImageById(int litteredPlaceId, @NonNull String imageId) {
        LitteredPlace litteredPlace = findByID(litteredPlaceId);

        for (Image image : litteredPlace.getImageSet()) {
            if (image.getId().equals(imageId))
                return image;
        }

        throw new ResourceNotFoundException("imageId", imageId, "image not found.");
    }


    public Set<Image> findAllImage(int litteredPlaceId) {
        return repository.findById(litteredPlaceId).map(LitteredPlace::getImageSet)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("id", litteredPlaceId, "littered place not found.");
                });
    }

    public boolean isNotImage(MultipartFile file) {
        return imageService.isNotImage(file);
    }

    public int countCleanedBy(String userId) {
        return repository.countAllByCleanedByEquals(userId);
    }


    private LitteredPlace findByID(int id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("id", id, "littered place not found.");
                });
    }
}