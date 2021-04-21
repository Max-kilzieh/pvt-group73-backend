package com.pvt73.recycling.controller;

import com.pvt73.recycling.model.dao.WasteBin;
import com.pvt73.recycling.model.service.WasteBinService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WasteBinController {

    private final WasteBinService wasteBinService;

    public WasteBinController(WasteBinService wasteBinService) {
        this.wasteBinService = wasteBinService;
    }

    @GetMapping("/waste_bins/nearest_waste_bins")
    public List<WasteBin> getNearestWasteBinsWithinDistance(@RequestParam double latitude,
                                                            @RequestParam double longitude,
                                                            @RequestParam(required = false, defaultValue = "100") int distance) {

        return wasteBinService.getNearestWasteBinsWithinDistance(latitude, longitude, distance);
    }
}
