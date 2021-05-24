package com.pvt73.recycling.model.service.event;

import com.pvt73.recycling.exception.ResourceNotFoundException;
import com.pvt73.recycling.model.dao.Event;
import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.util.DistanceAndPagingUtil;
import com.pvt73.recycling.repository.EventRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository repository;

    @Override
    public Event creat(@NonNull Event event) {
        return repository.save(event);
    }

    @Override
    public Event findById(int id, @NonNull LatLng coordinates) {

        Event event = repository.findById(id)
                .map(eve -> {
                    eve.setNumberOfParticipant(eve.getParticipantSet().size());
                    return eve;
                })
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("id", id, "event not found.");
                });

        event.setDistance(DistanceAndPagingUtil
                .calculateDistanceBetweenGpsCoordinates(coordinates, event.getCoordinates()));

        return event;
    }

    @Override
    public Event update(@NonNull Event event, int eventId) {
        return repository.findById(eventId)
                .map(eve -> {
                    eve.setDescription(event.getDescription());
                    eve.setParticipantSet(event.getParticipantSet());
                    return repository.save(eve);
                })
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("id", eventId, "event not found.");
                });
    }

    @Override
    public List<Event> findAllNearby(@NonNull LatLng coordinates, int offset, int limit) {

        List<Event> eventList = repository.findAllByEventDateTimeAfter(LocalDateTime.now());

        eventList.forEach(event -> event.setNumberOfParticipant(event.getParticipantSet().size()));

        eventList.forEach(event -> {
            event.setDistance(
                    DistanceAndPagingUtil.calculateDistanceBetweenGpsCoordinates(
                            coordinates, event.getCoordinates()));

            event.setNumberOfParticipant(event.getParticipantSet().size());
        });

        eventList.sort(Comparator.comparingDouble(Event::getDistance));

        int[] pageAndSize = DistanceAndPagingUtil.calculatePageAndSize(offset, limit, eventList.size());

        return eventList.subList(pageAndSize[0], pageAndSize[1]);
    }

    @Override
    public void delete(int eventId) {
        repository.deleteById(eventId);
    }

    @Override
    public int countByEventParticipated(String userId) {
        int participated = 0;
        for (Event event : repository.findAll()) {
            if (event.getParticipantSet().contains(userId))
                participated++;
        }

        return participated;
    }

    @Override
    public List<Event> findAllParticipatedByUser(String userId) {

        List<Event> participated = new ArrayList<>();
        for (Event event : repository.findAll()) {
            if (event.getParticipantSet().contains(userId))
                participated.add(event);
        }
        participated.sort(Comparator.comparing(Event::getEventDateTime).reversed());

        return participated;
    }

    @Override
    public List<Event> findAllCreatedByUser(String userId) {
        return repository.findAllByUserIdEquals(userId);
    }
}
