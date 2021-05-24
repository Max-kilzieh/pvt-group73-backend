package com.pvt73.recycling.controller;

import com.pvt73.recycling.model.dao.Address;
import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.service.address.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;

@Tag(name = "Addresses")
@Validated
@RequiredArgsConstructor
@RestController
public class AddressController {
    private final AddressService service;

    @Operation(summary = "Obtain adress from Gps coordinates.")

    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Address.class), mediaType = MediaType.APPLICATION_JSON_VALUE))

    @GetMapping("/addresses")
    Address getAddress(@Parameter(description = "latitude,longitude")
                       @RequestParam @Size(min = 2, max = 2) double[] latlng) {

        return service.getAddress(new LatLng(latlng[0], latlng[1]));
    }

}
