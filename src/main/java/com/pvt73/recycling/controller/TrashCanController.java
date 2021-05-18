package com.pvt73.recycling.controller;

import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.dao.TrashCan;
import com.pvt73.recycling.model.service.TrashCan.TrashCanService;
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

import javax.validation.constraints.Max;
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
            @ApiResponse(responseCode = "200", description = "A list containing trash cans returned", content = @Content(schema = @Schema(implementation = TrashCan.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "204", description = "No more trash cans nearby were found.", content = @Content)})

    @GetMapping(value = "/trash-cans")
    ResponseEntity<List<TrashCan>> getNearby(@Parameter(description = "latitude")
                                             @RequestParam("lat") double latitude,
                                             @Parameter(description = "longitude")
                                             @RequestParam("lng") double longitude,
                                             @Parameter(description = "The index of the first result to return.")
                                             @RequestParam(defaultValue = "0") @Min(0) @Max(12537) int offset,
                                             @Parameter(description = "Maximum number of results to return. " +
                                                     "Maximum offset (including limit): 12537. " +
                                                     "Use with limit to get the next page of search results.")
                                             @RequestParam(defaultValue = "10") @Min(1) @Max(12537) int limit) {

        List<TrashCan> trashCanList = service.getNearby(new LatLng(latitude, longitude), offset, limit);

        return trashCanList.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                ResponseEntity.ok(trashCanList);

    }
}

