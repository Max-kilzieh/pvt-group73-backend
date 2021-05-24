package com.pvt73.recycling.model.util;

import com.pvt73.recycling.model.dao.LatLng;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DistanceAndPagingUtil {


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
    public double calculateDistanceBetweenGpsCoordinates(@NonNull LatLng origin, @NonNull LatLng destination) {
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


    public int[] calculatePageAndSize(int offset, int limit, int maxOffset) {

        if (offset < 0 || limit < 1 || maxOffset < 1)
            throw new IllegalArgumentException();

        int from = offset * limit;
        int to = (offset + 1) * limit;


        if (from > maxOffset)
            from = maxOffset;

        if (to > maxOffset)
            to = maxOffset;

        return new int[]{from, to};
    }


}
