package com.pvt73.recycling.model.service.TrashCan;

import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.dao.TrashCan;
import com.pvt73.recycling.model.util.DistanceAndPagingUtil;
import com.pvt73.recycling.repository.TrashCanRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TrashCanServiceImpl implements TrashCanService {
    private final TrashCanRepository repository;

    @Override
    public List<TrashCan> getNearby(@NonNull LatLng coordinates, int offset, int limit) {
        List<TrashCan> trashCanList = repository.findAll();

        trashCanList.sort(Comparator.comparingDouble(
                trashCan -> DistanceAndPagingUtil.
                        calculateDistanceBetweenGpsCoordinates(
                                coordinates, trashCan.getCoordinates())));

        int[] pageAndSize = DistanceAndPagingUtil.calculatePageAndSize(offset, limit, trashCanList.size());

        return trashCanList.subList(pageAndSize[0], pageAndSize[1]);

    }

}
