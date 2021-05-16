package com.pvt73.recycling.model.service.TrashCan;

import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.dao.TrashCan;
import com.pvt73.recycling.model.util.DistanceAndPagingUtil;
import com.pvt73.recycling.repository.TrashCanRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TrashCanServiceImpl implements TrashCanService {

    private final TrashCanRepository repository;
    private final DistanceAndPagingUtil util;

    public List<TrashCan> getNearby(@NonNull LatLng coordinates, int page, int size, Integer distance) {

        return distance != null && distance > 0 ?
                getWithinDistance(coordinates, distance) :
                getPagedAndSorted(coordinates, page, size);
    }


    private List<TrashCan> getPagedAndSorted(LatLng coordinates, int page, int size) {

        List<TrashCan> trashCanList = repository.findAll();

        trashCanList.sort(Comparator.comparingDouble(
                trashCan -> util.calculateDistanceBetweenGpsCoordinates(
                        coordinates, trashCan.getCoordinates())));

        int[] pageAndSize = util.calculatePageAndSize(page, size, trashCanList.size());

        return trashCanList.subList(pageAndSize[0], pageAndSize[1]);

    }


    private List<TrashCan> getWithinDistance(LatLng coordinates, int distansInMeter) {
        List<TrashCan> withinDistance = new ArrayList<>();
        for (TrashCan trashCan : repository.findAll()) {

            double current = util.calculateDistanceBetweenGpsCoordinates(coordinates, trashCan.getCoordinates());
            if (current < distansInMeter)
                withinDistance.add(trashCan);

        }
        return withinDistance;
    }


}
