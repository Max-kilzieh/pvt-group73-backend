package com.pvt73.recycling.repository;

import com.pvt73.recycling.model.dao.CleaningStatus;
import com.pvt73.recycling.model.dao.LitteredPlace;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LitteredPlaceRepository extends CrudRepository<LitteredPlace, Integer> {

    List<LitteredPlace> findAllByEventFalseAndCleaningStatus(CleaningStatus status);

    int countAllByCleanedByEquals(String userId);

    int countAllByUserIdEquals(String userId);

}
