package com.pvt73.recycling.repository;

import com.pvt73.recycling.model.dao.Event;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends CrudRepository<Event, Integer> {

    List<Event> findAllByEventDateTimeAfter(LocalDateTime localDateTime);

    List<Event> findAllByUserIdEquals(String userId);
}
