package com.pvt73.recycling.model.service;

import com.pvt73.recycling.model.dao.TrashCan;
import com.pvt73.recycling.repository.TrashCanRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TrashCanService {

    private final TrashCanRepository repository;

    public TrashCanService(TrashCanRepository repository) {
        this.repository = repository;
    }

    public List<TrashCan> getTrashCansPagedAndSorted(double latitude, double longitude, int page, int size) {


        List<TrashCan> trashCanList = repository.findAll();

        trashCanList.sort(Comparator.comparingDouble(
                trashCan -> distanceBetweenGpsCoordinates(
                        latitude, longitude, trashCan.getLatitude(), trashCan.getLongitude())));

        int[] pageAndSize = controlPageAndSize(page, size);

        return trashCanList.subList(pageAndSize[0], pageAndSize[1]);

    }

    private int[] controlPageAndSize(int page, int size) {
        if (size == 0)
            size = 1;

        // exception!

        int from = page * size;
        int to = (page + 1) * size;

        if (from < 0)
            from = 0;

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
     * @param lat1 origin latitude
     * @param lon1 origin longitude
     * @param lat2 destination latitude
     * @param lon2 destination longitude
     * @return distance in meters
     * @see <a href="https://en.wikipedia.org/wiki/Haversine_formula">Harversine formula</a>
     * @see <a href="https://www.movable-type.co.uk/scripts/latlong.html">Harversine formula implimentation</a>
     */
    private double distanceBetweenGpsCoordinates(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6378.137; // In kilometers, matching Google Maps API V3 ‘spherical’

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // a is the square of half the chord length between the points.
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);

        // c is the angular distance in radians
        double c = 2 * Math.asin(Math.sqrt(a));

        return R * c * 1000.0;
    }

    public List<TrashCan> getTrashCansWithinDistance(double latitude, double longitude, int distansInMeter) {
        if (distansInMeter < 0)
            distansInMeter = 1;

        List<TrashCan> withinDistance = new ArrayList<>();
        for (TrashCan trashCan : repository.findAll()) {

            double current = distanceBetweenGpsCoordinates(latitude, longitude, trashCan.getLatitude(), trashCan.getLongitude());
            if (current < distansInMeter)
                withinDistance.add(trashCan);

        }
        return withinDistance;
    }


}
