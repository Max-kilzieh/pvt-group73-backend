package com.pvt73.recycling.model.service.garbage_disposal;

import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.dao.RecycleStation;
import com.pvt73.recycling.model.dao.TrashCan;
import com.pvt73.recycling.model.util.DistanceAndPagingUtil;
import com.pvt73.recycling.repository.RecycleStationRepository;
import com.pvt73.recycling.repository.TrashCanRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GarbageDisposalServiceImpl implements GarbageDisposalService {
    private final RecycleStationRepository recycleStationRepository;
    private final TrashCanRepository trashCanRepository;


    @Override
    public List<RecycleStation> getRecycleStationsNearby(@NonNull LatLng coordinates, int offset, int limit) {
        List<RecycleStation> recycleStationList = recycleStationRepository.findAll();

        recycleStationList.forEach(recycleStation -> recycleStation.setDistance(
                DistanceAndPagingUtil.calculateDistanceBetweenGpsCoordinates(
                        coordinates, recycleStation.getCoordinates())));

        recycleStationList.sort(Comparator.comparingDouble(RecycleStation::getDistance));

        int[] pageAndSize = DistanceAndPagingUtil.calculatePageAndSize(offset, limit, recycleStationList.size());

        return recycleStationList.subList(pageAndSize[0], pageAndSize[1]);
    }

    @Override
    public List<TrashCan> getTrashCansNearby(@NonNull LatLng coordinates, int offset, int limit) {
        List<TrashCan> trashCanList = trashCanRepository.findAll();

        trashCanList.forEach(trashCan -> trashCan.setDistance(
                DistanceAndPagingUtil.calculateDistanceBetweenGpsCoordinates(
                        coordinates, trashCan.getCoordinates())));

        trashCanList.sort(Comparator.comparingDouble(TrashCan::getDistance));

        int[] pageAndSize = DistanceAndPagingUtil.calculatePageAndSize(offset, limit, trashCanList.size());

        return trashCanList.subList(pageAndSize[0], pageAndSize[1]);

    }


}
