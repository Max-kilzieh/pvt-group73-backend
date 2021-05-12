package com.pvt73.recycling.model.service;

import com.pvt73.recycling.model.dao.TrashCan;

import java.util.List;

public interface TrashCanService {
    List<TrashCan> getTrashCansPagedAndSorted(double latitude, double longitude, int page, int size);

    List<TrashCan> getTrashCansWithinDistance(double latitude, double longitude, int distansInMeter);
}
