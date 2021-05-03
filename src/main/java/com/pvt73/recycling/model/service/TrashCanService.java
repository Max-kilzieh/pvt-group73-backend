package com.pvt73.recycling.model.service;

import com.pvt73.recycling.model.dao.TrashCan;
import com.pvt73.recycling.repository.TrashCanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrashCanService {

    private final TrashCanRepository repository;

    public TrashCanService(TrashCanRepository repository) {
        this.repository = repository;
    }

    public List<TrashCan> getNearestTrashCansWithinDistance(double latitude, double longitude, int distansInMeter) {
        final int ALLOWED_DISTANCE = 500;
        if (distansInMeter > ALLOWED_DISTANCE)
            distansInMeter = ALLOWED_DISTANCE;

        return getTrashCans(latitude, longitude, distansInMeter);
    }


    //A tiny error margin is expected as the calculation doesn't consider that earth is rounded!

    private List<TrashCan> getTrashCans(double latitude, double longitude, int distansInMeter) {
        double ONE_DEGREE_IN_METER = 111319.5;
        double ONE_METER_IN_DEGREE = 1 / ONE_DEGREE_IN_METER;

        double latitudeGreaterThan = latitude - (ONE_METER_IN_DEGREE * distansInMeter);
        double latitudeLessThan = latitude + (ONE_METER_IN_DEGREE * distansInMeter);

        double longitudeGreaterThan = longitude - (ONE_METER_IN_DEGREE * distansInMeter);
        double longitudeLessThan = longitude + (ONE_METER_IN_DEGREE * distansInMeter);

        return repository.findAllByLatitudeGreaterThanAndLatitudeLessThanAndLongitudeGreaterThanAndLongitudeLessThan(latitudeGreaterThan, latitudeLessThan, longitudeGreaterThan, longitudeLessThan);
    }
}
