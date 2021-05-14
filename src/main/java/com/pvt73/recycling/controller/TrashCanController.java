package com.pvt73.recycling.controller;

import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.dao.TrashCan;
import com.pvt73.recycling.model.service.TrashCanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

@Tag(name = "Trash Cans", description = "Currently 12537 trash cans within Stockholm County")
@Validated
@RequiredArgsConstructor
@RestController
public class TrashCanController {
    private final TrashCanService service;

    @Operation(summary = "Trash cans nearby")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list containing trash cans returned", content = @Content(schema = @Schema(implementation = TrashCan.class))),
            @ApiResponse(responseCode = "204", description = "No trash cans nearby were found.", content = @Content)})

    @GetMapping(value = "/trash-cans")
    ResponseEntity<List<TrashCan>> getNearby(@Parameter(description = "latitude")
                                             @RequestParam("lat") double latitude,
                                             @Parameter(description = "longitude")
                                             @RequestParam("lng") double longitude,
                                             @Parameter(description = "Trash cans to Skip from the result (page * size)")
                                             @RequestParam(defaultValue = "0") @Min(0) int page,
                                             @Parameter(description = " Quantity of Trash Cans to return")
                                             @RequestParam(defaultValue = "10") @Min(1) int size,
                                             @Parameter(description = "In meters; if provided, page and size will be ignored.")
                                             @RequestParam(required = false) Integer distance) {

        List<TrashCan> trashCanList = service.getNearby(new LatLng(latitude, longitude), page, size, distance);

        return trashCanList.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                ResponseEntity.ok(trashCanList);

    }
}

