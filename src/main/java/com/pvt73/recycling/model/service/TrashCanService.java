package com.pvt73.recycling.model.service;

import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.dao.TrashCan;

import java.util.List;

public interface TrashCanService {
    List<TrashCan> getNearbyTrashCans(LatLng coordinates, int page, int size, Integer distance);
}
