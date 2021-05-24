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
 * <p>          100 xp / level
 * <p>       25 xp / städad plats
 * <p>    50 xp / städevent
 */
@UtilityClass
public class LevelUtil {

    private final int pointPerPlaceCleaned = 25;
    private final int pointPerEventParticipated = 50;

    private final int pointPerLevel = 100;
    private final int highestLevel = 10;


    public int getLevel(int placeCleaned, int eventParticipated) {

        int level = (pointPerLevel
                + (placeCleaned * pointPerPlaceCleaned
                + eventParticipated * pointPerEventParticipated))
                / pointPerLevel;


        return Math.min(level, highestLevel);
    }


}
