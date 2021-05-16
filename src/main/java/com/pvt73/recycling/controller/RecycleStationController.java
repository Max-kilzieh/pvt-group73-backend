package com.pvt73.recycling.controller;


import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.dao.RecycleStation;
import com.pvt73.recycling.model.dao.TrashCan;
import com.pvt73.recycling.model.service.recycleStation.RecycleStationService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

@Tag(name = "Recycle Stations", description = "Currently 245 Recycle Stations within Stockholm County")
@Validated
@RequiredArgsConstructor
@RestController
public class RecycleStationController {


    private final RecycleStationService service;

    @Operation(summary = "Recycle stations nearby")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list containing recycle stations returned", content = @Content(schema = @Schema(implementation = TrashCan.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "204", description = "No recycle stations nearby were found.", content = @Content)})

    @GetMapping(value = "/recycle-stations")
    ResponseEntity<List<RecycleStation>> getNearby(@Parameter(description = "latitude")
                                                   @RequestParam("lat") double latitude,
                                                   @Parameter(description = "longitude")
                                                   @RequestParam("lng") double longitude,
                                                   @Parameter(description = "Recycle stations to Skip from the result (page * size)")
                                                   @RequestParam(defaultValue = "0") @Min(0) int page,
                                                   @Parameter(description = " Quantity of Recycle stations to return")
                                                   @RequestParam(defaultValue = "10") @Min(1) int size,
                                                   @Parameter(description = "In meters; if provided, page and size will be ignored.")
                                                   @RequestParam(required = false) Integer distance) {

        List<RecycleStation> trashCanList = service.getNearby(new LatLng(latitude, longitude), page, size, distance);

        return trashCanList.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                ResponseEntity.ok(trashCanList);

    }

}
