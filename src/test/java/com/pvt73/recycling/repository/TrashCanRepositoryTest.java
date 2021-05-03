package com.pvt73.recycling.repository;

import com.pvt73.recycling.model.dao.TrashCan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TrashCanRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TrashCanRepository wasteBins;

    @Test
    void findWasteBinWithinDistance() {

        //waste bin within 15 meter from Kista train station.
        TrashCan trashCan = new TrashCan(59.40321616, 17.94232856);
        entityManager.persist(trashCan);

        List<TrashCan> wasteBinsNearby = getWasteBins(59.40332696500667, 17.942350268367566, 15);

        assertThat(wasteBinsNearby).extracting(TrashCan::getLatitude).containsOnly(trashCan.getLatitude());
        assertThat(wasteBinsNearby).extracting(TrashCan::getLongitude).containsOnly(trashCan.getLongitude());
    }

    private List<TrashCan> getWasteBins(double latitude, double longitude, int distansInMeter) {
        double ONE_DEGREE_IN_METER = 111319.5;
        double ONE_METER_IN_DEGREE = 1 / ONE_DEGREE_IN_METER;

        double latitudeGreaterThan = latitude - (ONE_METER_IN_DEGREE * distansInMeter);
        double latitudeLessThan = latitude + (ONE_METER_IN_DEGREE * distansInMeter);

        double longitudeGreaterThan = longitude - (ONE_METER_IN_DEGREE * distansInMeter);
        double longitudeLessThan = longitude + (ONE_METER_IN_DEGREE * distansInMeter);

        return wasteBins.findAllByLatitudeGreaterThanAndLatitudeLessThanAndLongitudeGreaterThanAndLongitudeLessThan(latitudeGreaterThan, latitudeLessThan, longitudeGreaterThan, longitudeLessThan);
    }

}