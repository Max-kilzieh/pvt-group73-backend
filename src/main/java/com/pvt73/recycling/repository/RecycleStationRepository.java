package com.pvt73.recycling.repository;

import com.pvt73.recycling.model.dao.RecycleStation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecycleStationRepository extends CrudRepository<RecycleStation, Integer> {

    List<RecycleStation> findAll();

}
