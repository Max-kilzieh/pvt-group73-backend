package com.pvt73.recycling.model.service;

import com.pvt73.recycling.model.dao.WasteBin;
import com.pvt73.recycling.repository.WasteBinRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WasteBinService {

    private final WasteBinRepository wasteBinRepository;

    public WasteBinService(WasteBinRepository wasteBinRepository) {
        this.wasteBinRepository = wasteBinRepository;
    }

    public List<WasteBin> getNearestWasteBinsWithinDistance(double latitude, double longitude, int distansInMeter) {
        final int ALLOWED_DISTANCE = 500;
        if (distansInMeter > ALLOWED_DISTANCE)
            distansInMeter = ALLOWED_DISTANCE;

        return getWasteBins(latitude, longitude, distansInMeter);
    }

    private List<WasteBin> getWasteBins(double latitude, double longitude, int distansInMeter) {
        double ONE_DEGREE_IN_METER = 111319.5;
        double ONE_METER_IN_DEGREE = 1 / ONE_DEGREE_IN_METER;

        double latitudeGreaterThan = latitude - (ONE_METER_IN_DEGREE * distansInMeter);
        double latitudeLessThan = latitude + (ONE_METER_IN_DEGREE * distansInMeter);

        double longitudeGreaterThan = longitude - (ONE_METER_IN_DEGREE * distansInMeter);
        double longitudeLessThan = longitude + (ONE_METER_IN_DEGREE * distansInMeter);

        return wasteBinRepository.findAllByLatitudeGreaterThanAndLatitudeLessThanAndLongitudeGreaterThanAndLongitudeLessThan(latitudeGreaterThan, latitudeLessThan, longitudeGreaterThan, longitudeLessThan);
    }
}
