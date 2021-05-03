package com.pvt73.recycling.controller;

import com.pvt73.recycling.model.dao.TrashCan;
import com.pvt73.recycling.model.service.TrashCanService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Trash Cans")
public class TrashCanController {

    private final TrashCanService service;

    public TrashCanController(TrashCanService service) {
        this.service = service;
    }

    @Operation(summary = "Trash cans within a distance.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list containing trash cans returned",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrashCan.class))}),
            @ApiResponse(responseCode = "204",
                    description = "No trash cans were found within the given distance.",
                    content = @Content)})
    @GetMapping("/trash-cans")
    public ResponseEntity<List<TrashCan>> getNearestTrashCansWithinDistance(@Parameter(description = "latitude")
                                                                            @RequestParam("lat") double latitude,
                                                                            @Parameter(description = "longitude")
                                                                            @RequestParam("lng") double longitude,
                                                                            @RequestParam(required = false, defaultValue = "100") int distance) {

        List<TrashCan> trashCanList = service.getNearestTrashCansWithinDistance(latitude, longitude, distance);
        if (trashCanList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(trashCanList);
    }
}

