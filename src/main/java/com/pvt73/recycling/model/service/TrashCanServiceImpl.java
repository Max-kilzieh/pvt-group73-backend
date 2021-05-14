package com.pvt73.recycling.model.service;

import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.dao.TrashCan;
import com.pvt73.recycling.repository.TrashCanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TrashCanServiceImpl implements TrashCanService {

    private final TrashCanRepository repository;

    public List<TrashCan> getNearby(LatLng coordinates, int page, int size, Integer distance) {


        return distance != null && distance > 0 ?
                getWithinDistance(coordinates, distance) :
                getPagedAndSorted(coordinates, page, size);
    }

    private List<TrashCan> getPagedAndSorted(LatLng coordinates, int page, int size) {


        List<TrashCan> trashCanList = repository.findAll();

        trashCanList.sort(Comparator.comparingDouble(
                trashCan -> getDistanceBetweenGpsCoordinates(
                        coordinates, trashCan.getCoordinates())));

        int[] pageAndSize = controlPageAndSize(page, size);

        return trashCanList.subList(pageAndSize[0], pageAndSize[1]);

    }

    private int[] controlPageAndSize(int page, int size) {
        if (size < 1 || page < 0)
            throw new IllegalArgumentException();

        int from = page * size;
        int to = (page + 1) * size;

        int maxSize = (int) repository.count();

        if (from > maxSize)
            from = maxSize;

        if (to > maxSize)
            to = maxSize;

        return new int[]{from, to};
    }


    /**
     * The calculations are based on the Haversine formula.
     * Giving great-circle distances between two points on a sphere from their longitudes and latitudes.
     * It is a special case of a more general formula in spherical trigonometry, the law of haversine,
     * relating the sides and angles of spherical "triangles".
     *
     * @param origin      origin coordinates
     * @param destination destination coordinates
     * @return distance in meters
     * @see <a href="https://en.wikipedia.org/wiki/Haversine_formula">Harversine formula</a>
     * @see <a href="https://www.movable-type.co.uk/scripts/latlong.html">Harversine formula implimentation</a>
     */
    private double getDistanceBetweenGpsCoordinates(LatLng origin, LatLng destination) {
        final double R = 6378.137; // In kilometers, matching Google Maps API V3 ‘spherical’

        double dLat = Math.toRadians(destination.getLatitude() - origin.getLatitude());
        double dLon = Math.toRadians(destination.getLongitude() - origin.getLongitude());
        double originLat = Math.toRadians(origin.getLatitude());
        double destinationLat = Math.toRadians(destination.getLatitude());

        // a is the square of half the chord length between the points.
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(originLat) * Math.cos(destinationLat);

        // c is the angular distance in radians
        double c = 2 * Math.asin(Math.sqrt(a));

        return R * c * 1000.0;
    }

    private List<TrashCan> getWithinDistance(LatLng coordinates, int distansInMeter) {
        List<TrashCan> withinDistance = new ArrayList<>();
        for (TrashCan trashCan : repository.findAll()) {

            double current = getDistanceBetweenGpsCoordinates(coordinates, trashCan.getCoordinates());
            if (current < distansInMeter)
                withinDistance.add(trashCan);

        }
        return withinDistance;
    }


}
