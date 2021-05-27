package com.pvt73.recycling.model.service.littered_place;

import com.pvt73.recycling.exception.ResourceAlreadyExistException;
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
import java.util.HashSet;
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
        if (repository.existsByCoordinatesAndCleaningStatusIsNot(newLitteredPlace.getCoordinates(), CleaningStatus.CLEAN))
            throw new ResourceAlreadyExistException("latlng", newLitteredPlace.getCoordinates(), "littered place already exist");

        newLitteredPlace.setEvent(false);
        newLitteredPlace.setCleanedBy(null);
        newLitteredPlace.setCleanedAt(null);
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
    public List<LitteredPlace> findAllNearbyCleaningStatus(@NonNull CleaningStatus status, @NonNull LatLng coordinates, int offset, int limit) {
        List<LitteredPlace> litteredPlaceList = repository.findAllByEventFalseAndCleaningStatus(status);

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

                    deleteLitteredImageSetComplement(place, newPlace);
                    place.setLitteredImageSet(newPlace.getLitteredImageSet());

                    deleteCleanedImageSetComplement(place, newPlace);
                    place.setCleanedImageSet(newPlace.getCleanedImageSet());

                    place.setDescription(newPlace.getDescription());
                    place.setUserId(newPlace.getUserId());

                    checkCleanByExist(newPlace);
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

    private void deleteLitteredImageSetComplement(LitteredPlace place, LitteredPlace newPlace) {
        Set<Image> toRemove = new HashSet<>(place.getLitteredImageSet());
        toRemove.removeAll(newPlace.getLitteredImageSet());
        imageService.deleteAll(toRemove);
    }

    private void deleteCleanedImageSetComplement(LitteredPlace place, LitteredPlace newPlace) {
        Set<Image> toRemove = new HashSet<>(place.getCleanedImageSet());
        toRemove.removeAll(newPlace.getCleanedImageSet());
        imageService.deleteAll(toRemove);
    }

    private void checkCleanByExist(LitteredPlace newPlace) {
        if (newPlace.getCleaningStatus() == CleaningStatus.CLEAN && (
                newPlace.getCleanedBy() == null || newPlace.getCleanedBy().isBlank())) {
            throw new IllegalArgumentException("cleanedBy must be provided");
        }
    }

    @Override
    public void delete(int litteredPlaceId) {

        LitteredPlace litteredPlace = repository.findById(litteredPlaceId)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("litteredPlaceId", litteredPlaceId, "littered place not found.");
                });

        imageService.deleteAll(litteredPlace.getLitteredImageSet());
        imageService.deleteAll(litteredPlace.getCleanedImageSet());

        repository.delete(litteredPlace);
    }


    public Image addImage(int litteredPlaceId, MultipartFile file, boolean clean) {
        Set<Image> imageSet = new HashSet<>();
        LitteredPlace litteredPlace = findByID(litteredPlaceId);
        Image image = imageService.creat(file, clean);
        imageSet.add(image);

        if (clean) {
            imageSet.addAll(litteredPlace.getCleanedImageSet());
            litteredPlace.setCleanedImageSet(imageSet);
        } else {
            imageSet.addAll(litteredPlace.getLitteredImageSet());
            litteredPlace.setLitteredImageSet(imageSet);
        }

        repository.save(litteredPlace);
        return image;
    }

    public void deleteImage(int litteredPlaceId, @NonNull String imageId) {
        LitteredPlace litteredPlace = findByID(litteredPlaceId);

        if (!litteredPlace.containImage(imageId))
            throw new ResourceNotFoundException("imageId", imageId, "image not found.");

        imageService.delete(imageId);

        Set<Image> litteredImageSet = litteredPlace.getLitteredImageSet();
        litteredImageSet.removeIf(image -> image.getId().equals(imageId));
        litteredPlace.setLitteredImageSet(litteredImageSet);

        Set<Image> cleanedImageSet = litteredPlace.getCleanedImageSet();
        cleanedImageSet.removeIf(image -> image.getId().equals(imageId));
        litteredPlace.setCleanedImageSet(cleanedImageSet);

        repository.save(litteredPlace);
    }

    public Image findImageById(int litteredPlaceId, @NonNull String imageId) {
        LitteredPlace litteredPlace = findByID(litteredPlaceId);

        for (Image image : litteredPlace.getLitteredImageSet()) {
            if (image.getId().equals(imageId))
                return image;
        }

        for (Image image : litteredPlace.getCleanedImageSet()) {
            if (image.getId().equals(imageId))
                return image;
        }

        throw new ResourceNotFoundException("imageId", imageId, "image not found.");
    }


    public Set<Image> findAllImage(int litteredPlaceId) {
        return repository.findById(litteredPlaceId)
                .map(litteredPlace -> {
                    Set<Image> imageSet = new HashSet<>();
                    imageSet.addAll(litteredPlace.getCleanedImageSet());
                    imageSet.addAll(litteredPlace.getLitteredImageSet());
                    return imageSet;
                })
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

    @Override
    public int countReportedBy(String userId) {
        return repository.countAllByUserIdEquals(userId);
    }


    private LitteredPlace findByID(int id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("id", id, "littered place not found.");
                });
    }
}
