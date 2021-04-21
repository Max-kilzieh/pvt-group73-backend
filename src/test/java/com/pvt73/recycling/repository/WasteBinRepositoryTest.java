package com.pvt73.recycling.repository;

import com.pvt73.recycling.model.dao.WasteBin;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
@DataJpaTest
class WasteBinRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private WasteBinRepository wasteBins;

    @Test
     void findWasteBinWithinDistance() {

        //waste bin within 15 meter from Kista train station.
        WasteBin wasteBin = new WasteBin(59.40321616, 17.94232856);
        entityManager.persist(wasteBin);

        List<WasteBin> wasteBinsNearby = getWasteBins(59.40332696500667,17.942350268367566,15);

        assertThat(wasteBinsNearby).extracting(WasteBin::getLatitude).containsOnly(wasteBin.getLatitude());
        assertThat(wasteBinsNearby).extracting(WasteBin::getLongitude).containsOnly(wasteBin.getLongitude());
    }

    private List<WasteBin> getWasteBins(double latitude, double longitude, int distansInMeter) {
        double ONE_DEGREE_IN_METER = 111319.5;
        double ONE_METER_IN_DEGREE = 1 / ONE_DEGREE_IN_METER;

        double latitudeGreaterThan = latitude - (ONE_METER_IN_DEGREE * distansInMeter);
        double latitudeLessThan = latitude + (ONE_METER_IN_DEGREE * distansInMeter);

        double longitudeGreaterThan = longitude - (ONE_METER_IN_DEGREE * distansInMeter);
        double longitudeLessThan = longitude + (ONE_METER_IN_DEGREE * distansInMeter);

        return wasteBins.findAllByLatitudeGreaterThanAndLatitudeLessThanAndLongitudeGreaterThanAndLongitudeLessThan(latitudeGreaterThan, latitudeLessThan, longitudeGreaterThan, longitudeLessThan);
    }

}