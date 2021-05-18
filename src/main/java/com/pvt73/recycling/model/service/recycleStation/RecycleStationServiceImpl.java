package com.pvt73.recycling.model.service.recycleStation;

import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.dao.RecycleStation;
import com.pvt73.recycling.model.util.DistanceAndPagingUtil;
import com.pvt73.recycling.repository.RecycleStationRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RecycleStationServiceImpl implements RecycleStationService {
    private final RecycleStationRepository repository;


    @Override
    public List<RecycleStation> getNearby(@NonNull LatLng coordinates, int offset, int limit) {
        List<RecycleStation> recycleStationList = repository.findAll();

        recycleStationList.sort(Comparator.comparingDouble(
                recycleStation -> DistanceAndPagingUtil.calculateDistanceBetweenGpsCoordinates(
                        coordinates, recycleStation.getCoordinates())));


        int[] pageAndSize = DistanceAndPagingUtil.calculatePageAndSize(offset, limit, recycleStationList.size());

        return recycleStationList.subList(pageAndSize[0], pageAndSize[1]);
    }

}
