package com.pvt73.recycling.model.service.user;

import org.springframework.stereotype.Component;

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

@Component
public class Level {

    private final int POINT_PER_LEVEL = 100;


    public int getLevel(int placeCleaned, int eventParticipated, int litteredPlacesReported) {

        int level = (POINT_PER_LEVEL
                + getTotalPoints(placeCleaned, eventParticipated, litteredPlacesReported))
                / POINT_PER_LEVEL;

        int HIGHEST_LEVEL = 10;
        return Math.min(level, HIGHEST_LEVEL);
    }

    public int getProgressPoints(int placeCleaned, int eventParticipated, int litteredPlacesReported) {

        int level = getLevel(placeCleaned, eventParticipated, litteredPlacesReported);
        if (level == 10)
            return 0;

        return (getTotalPoints(placeCleaned, eventParticipated, litteredPlacesReported))
                % POINT_PER_LEVEL;
    }

    private int getTotalPoints(int placeCleaned, int eventParticipated, int litteredPlacesReported) {
        int POINT_PER_LITTERED_PLACE_CLEANED = 25;
        int POINT_PER_LITTERED_PLACE_REPORTED = 5;
        int POINT_PER_EVENT_PARTICIPATED = 50;
        return (placeCleaned * POINT_PER_LITTERED_PLACE_CLEANED
                + litteredPlacesReported * POINT_PER_LITTERED_PLACE_REPORTED
                + eventParticipated * POINT_PER_EVENT_PARTICIPATED);
    }

}
