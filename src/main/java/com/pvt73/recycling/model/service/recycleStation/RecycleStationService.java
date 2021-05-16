package com.pvt73.recycling.model.service.recycleStation;

import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.dao.RecycleStation;

import java.util.List;

public interface RecycleStationService {
    List<RecycleStation> getNearby(LatLng coordinates, int page, int size, Integer distance);

}
