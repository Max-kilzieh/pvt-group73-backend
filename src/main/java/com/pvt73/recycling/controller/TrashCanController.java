package com.pvt73.recycling.controller;

import com.pvt73.recycling.model.dao.TrashCan;
import com.pvt73.recycling.model.service.TrashCanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Trash Cans",
        description = "Currently 12538 trash cans within Stockholm County")
public class TrashCanController {

    private final TrashCanService service;

    public TrashCanController(TrashCanService service) {
        this.service = service;
    }


    // ------------------------Documentation----------------------------------------------------------------------------
    @Operation(summary = "Trash cans nearby")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list containing trash cans returned",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TrashCan.class))}),
            @ApiResponse(responseCode = "204",
                    description = "No trash cans nearby were found.",
                    content = @Content)})
    //------------------------------------------------------------------------------------------------------------------

    @GetMapping(value = "/trash-cans")
    public ResponseEntity<List<TrashCan>> getNearbyTrashCans(@Parameter(description = "latitude")
                                                             @RequestParam("lat") double latitude,
                                                             @Parameter(description = "longitude")
                                                             @RequestParam("lng") double longitude,
                                                             @Parameter(description = "Trash cans to Skip from the result (page * size)")
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @Parameter(description = " Quantity of Trash Cans to return")
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @Parameter(description = "In meters; if provided, page and size will be ignored.")
                                                             @RequestParam(required = false) Integer distance) {

        List<TrashCan> trashCanList = distance != null && distance > 0 ?
                service.getTrashCansWithinDistance(latitude, longitude, distance) :
                service.getTrashCansPagedAndSorted(latitude, longitude, page, size);

        return trashCanList.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                ResponseEntity.ok(trashCanList);

    }


}

