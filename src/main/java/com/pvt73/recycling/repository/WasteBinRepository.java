package com.pvt73.recycling.repository;

import com.pvt73.recycling.model.dao.WasteBin;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WasteBinRepository extends CrudRepository<WasteBin, Integer> {

    List<WasteBin> findAllByLatitudeGreaterThanAndLatitudeLessThanAndLongitudeGreaterThanAndLongitudeLessThan(double latitudeGreaterThan,
                                                                                                              double latitudeLessThan,
                                                                                                              double longitudeGreaterThan,
                                                                                                              double longitudeLessThan);
}
