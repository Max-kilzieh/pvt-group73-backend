package com.pvt73.recycling.controller;

import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.dao.RecycleStation;
import com.pvt73.recycling.model.dao.TrashCan;
import com.pvt73.recycling.model.service.garbage_disposal.GarbageDisposalService;
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
import javax.validation.constraints.Size;
import java.util.List;

@Tag(name = "Garbage disposals")
@Validated
@RequiredArgsConstructor
@RestController
public class GarbageDisposalController {
    private static final int OFFSET_MIN = 0;
    private static final int LIMIT_MIN = 1;
    private static final int TOTAL_TRASH_CANS = 12537;
    private static final int TOTAL_RECYCLE_STATIONS = 245;
    private final GarbageDisposalService service;

    @Operation(summary = "Trash cans nearby, currently 12537 trash cans within Stockholm County.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list containing trash cans returned", content = @Content(schema = @Schema(implementation = TrashCan.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "204", description = "No more trash cans nearby were found.", content = @Content)})

    @GetMapping(value = "/garbage-disposals/trash-cans")
    ResponseEntity<List<TrashCan>> getTrashCansNearby(@Parameter(description = "latitude,longitude")
                                                      @RequestParam @Size(min = 2, max = 2) double[] latlng,
                                                      @Parameter(description = "The index of the first result to return.")
                                                      @RequestParam(defaultValue = "0") @Min(OFFSET_MIN) @Max(TOTAL_TRASH_CANS) int offset,
                                                      @Parameter(description = "Maximum number of results to return. " +
                                                              "Maximum offset (including limit): 12537. " +
                                                              "Use with limit to get the next page of search results.")
                                                      @RequestParam(defaultValue = "10") @Min(LIMIT_MIN) @Max(TOTAL_TRASH_CANS) int limit) {

        List<TrashCan> trashCanList = service.getTrashCansNearby(new LatLng(latlng[0], latlng[1]), offset, limit);

        return trashCanList.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                ResponseEntity.ok(trashCanList);
    }


    @Operation(summary = "Recycle stations nearby, currently 245 Recycle Stations within Stockholm County")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list containing recycle stations returned", content = @Content(schema = @Schema(implementation = RecycleStation.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "204", description = "No more recycle stations nearby were found.", content = @Content)})

    @GetMapping(value = "/garbage-disposals/recycle-stations")
    ResponseEntity<List<RecycleStation>> getRecycleStationNearby(@Parameter(description = "latitude,longitude")
                                                                 @RequestParam @Size(min = 2, max = 2) double[] latlng,
                                                                 @Parameter(description = "The index of the first result to return.")
                                                                 @RequestParam(defaultValue = "0") @Min(OFFSET_MIN) @Max(TOTAL_RECYCLE_STATIONS) int offset,
                                                                 @Parameter(description = "Maximum number of results to return. " +
                                                                         "Maximum offset (including limit): 245. " +
                                                                         "Use with limit to get the next page of search results.")
                                                                 @RequestParam(defaultValue = "10") @Min(LIMIT_MIN) @Max(TOTAL_RECYCLE_STATIONS) int limit) {


        List<RecycleStation> trashCanList = service.getRecycleStationsNearby(new LatLng(latlng[0], latlng[1]), offset, limit);

        return trashCanList.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                ResponseEntity.ok(trashCanList);
    }


}

