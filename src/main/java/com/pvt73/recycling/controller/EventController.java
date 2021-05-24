package com.pvt73.recycling.controller;

import com.pvt73.recycling.exception.ErrorMessage;
import com.pvt73.recycling.model.dao.Event;
import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.service.event.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Tag(name = "Events")
@Validated
@RequiredArgsConstructor
@RestController
public class EventController {
    private static final int OFFSET_MIN = 0;
    private static final int LIMIT_MIN = 1;
    private final EventService service;


    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New even place Created."),
            @ApiResponse(responseCode = "400", description = "parameter is missing or wrong formatted.", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "409", description = "Event already exist.", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))})


    @PostMapping(value = "/events", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    Event creat(@RequestBody @Valid Event event) {
        return service.creat(event);
    }


    @PutMapping(value = "/events/{event-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Event update(@RequestBody @Valid Event event,
                 @PathVariable("event-id") int eventId) {

        return service.update(event, eventId);

    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content, Event deleted."),
            @ApiResponse(responseCode = "404", description = "Event not found.", content = @Content(schema = @Schema(implementation = ErrorMessage.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})


    @DeleteMapping("/events/{event-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable("event-id") int id) {
        service.delete(id);
    }

    @Operation(summary = "One event, including past event.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event found."),
            @ApiResponse(responseCode = "404", description = "Event not found.", content = @Content(schema = @Schema(implementation = ErrorMessage.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})

    @GetMapping(value = "/events/{event-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Event findById(@PathVariable("event-id") int id,
                   @Parameter(description = "latitude,longitude")
                   @RequestParam @Size(min = 2, max = 2) double[] latlng) {


        return service.findById(id, new LatLng(latlng[0], latlng[1]));

    }

    @Operation(summary = "All events")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list containing all events returned", content = @Content(schema = @Schema(implementation = Event.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "204", description = "No more events nearby were found.", content = @Content)})

    @GetMapping(value = "/events")
    ResponseEntity<List<Event>> findAllNearby(@Parameter(description = "latitude,longitude")
                                              @RequestParam @Size(min = 2, max = 2) double[] latlng,
                                              @Parameter(description = "The index of the first result to return.")
                                              @RequestParam(defaultValue = "0") @Min(OFFSET_MIN) int offset,
                                              @Parameter(description = "Maximum number of results to return. " +
                                                      "Use with limit to get the next page of search results.")
                                              @RequestParam(defaultValue = "10") @Min(LIMIT_MIN) int limit) {


        List<Event> eventList = service.findAllNearby(new LatLng(latlng[0], latlng[1]), offset, limit);

        return eventList.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                ResponseEntity.ok(eventList);
    }

    @Operation(summary = "All events participated by the user, sorted by a most recent event.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list containing all participated events returned", content = @Content(schema = @Schema(implementation = Event.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "204", description = "The user has not participated in any events.", content = @Content)})

    @GetMapping(value = "/events/participants/{user-id}")
    ResponseEntity<List<Event>> allEventsUserParticipated(@PathVariable("user-id") String userId) {


        List<Event> eventList = service.findAllParticipatedByUser(userId);

        return eventList.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                ResponseEntity.ok(eventList);
    }

    @Operation(summary = "All events created by the user, sorted by a most recent event.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list containing all the user created events returned", content = @Content(schema = @Schema(implementation = Event.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "204", description = "The user has not created any events.", content = @Content)})

    @GetMapping(value = "/events/craters/{user-id}")
    ResponseEntity<List<Event>> allEventsCreatedByUser(@PathVariable("user-id") String userId) {


        List<Event> eventList = service.findAllCreatedByUser(userId);

        return eventList.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                ResponseEntity.ok(eventList);
    }


}
