package com.pvt73.recycling.model.service.garbage_disposal;

import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.dao.RecycleStation;
import com.pvt73.recycling.model.dao.TrashCan;

import java.util.List;

public interface GarbageDisposalService {

    List<RecycleStation> getRecycleStationsNearby(LatLng coordinates, int offset, int limit);

    List<TrashCan> getTrashCansNearby(LatLng coordinates, int offset, int limit);

}
