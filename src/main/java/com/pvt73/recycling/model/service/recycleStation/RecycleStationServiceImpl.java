package com.pvt73.recycling.model.service.recycleStation;

import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.dao.RecycleStation;
import com.pvt73.recycling.model.util.DistanceAndPagingUtil;
import com.pvt73.recycling.repository.RecycleStationRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RecycleStationServiceImpl implements RecycleStationService {
    private final RecycleStationRepository repository;
    private final DistanceAndPagingUtil util;


    @Override
    public List<RecycleStation> getNearby(@NonNull LatLng coordinates, int page, int size, Integer distance) {
        return distance != null && distance > 0 ?
                getWithinDistance(coordinates, distance) :
                getPagedAndSorted(coordinates, page, size);
    }

    private List<RecycleStation> getPagedAndSorted(LatLng coordinates, int page, int size) {
        List<RecycleStation> recycleStationList = repository.findAll();

        recycleStationList.sort(Comparator.comparingDouble(
                recycleStation -> util.calculateDistanceBetweenGpsCoordinates(
                        coordinates, recycleStation.getCoordinates())));


        int[] pageAndSize = util.calculatePageAndSize(page, size, recycleStationList.size());

        return recycleStationList.subList(pageAndSize[0], pageAndSize[1]);
    }


    private List<RecycleStation> getWithinDistance(LatLng coordinates, int distansInMeter) {
        List<RecycleStation> withinDistance = new ArrayList<>();
        for (RecycleStation recycleStation : repository.findAll()) {

            double current = util.calculateDistanceBetweenGpsCoordinates(coordinates, recycleStation.getCoordinates());
            if (current < distansInMeter)
                withinDistance.add(recycleStation);

        }
        return withinDistance;
    }

}
