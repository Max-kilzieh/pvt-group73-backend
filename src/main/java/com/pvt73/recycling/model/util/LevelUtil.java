package com.pvt73.recycling.model.util;

import lombok.experimental.UtilityClass;

/**
 * Level 1 - Nystädare
 * <p>
 * Level 2 - Aktiv städare
 * <p>
 * Level 3 - Städhjälte
 * <p>
 * Level 4 - Elitstädare
 * <p>
 * Level 5 - Städpro
 * <p>
 * Level 6 - Städveteran
 * <p>
 * Level 7 - Städexpert
 * <p>
 * Level 8 - Städgeni
 * <p>
 * Level 9 - Städmästare
 * <p>
 * Level 10 - Städlegend
 * <p>
 * Ex:
 * <p>    100 xp / level
 * <p>      5 xp / reported place
 * <p>     25 xp / städad plats
 * <p>     50 xp / städevent
 */
@UtilityClass
public class LevelUtil {

    private final int POINT_PER_LITTERED_PLACE_CLEANED = 25;
    private final int POINT_PER_LITTERED_PLACE_REPORTED = 5;
    private final int POINT_PER_EVENT_PARTICIPATED = 50;

    private final int POINT_PER_LEVEL = 100;
    private final int HIGHEST_LEVEL = 10;


    public int getLevel(int placeCleaned, int eventParticipated, int litteredPlacesReported) {

        int level = (POINT_PER_LEVEL
                + (placeCleaned * POINT_PER_LITTERED_PLACE_CLEANED
                + litteredPlacesReported * POINT_PER_LITTERED_PLACE_REPORTED
                + eventParticipated * POINT_PER_EVENT_PARTICIPATED))
                / POINT_PER_LEVEL;


        return Math.min(level, HIGHEST_LEVEL);
    }

    public int getProgressPoints(int placeCleaned, int eventParticipated, int litteredPlacesReported) {


        return (POINT_PER_LEVEL
                + (placeCleaned * POINT_PER_LITTERED_PLACE_CLEANED
                + litteredPlacesReported * POINT_PER_LITTERED_PLACE_REPORTED
                + eventParticipated * POINT_PER_EVENT_PARTICIPATED))
                % POINT_PER_LEVEL;
    }


}
