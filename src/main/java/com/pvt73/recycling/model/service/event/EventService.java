package com.pvt73.recycling.model.service.event;

import com.pvt73.recycling.model.dao.Event;
import com.pvt73.recycling.model.dao.LatLng;

import java.util.List;

public interface EventService {

    Event creat(Event event);

    Event update(Event event, int eventId);

    List<Event> findAllNearby(LatLng coordinates, int offset, int limit);

    Event findById(int id, LatLng currentCoordinates);


    void delete(int eventId);

    int countByEventParticipated(String userId);

    List<Event> findAllParticipatedByUser(String userId);

    List<Event> findAllCreatedByUser(String userId);

}
