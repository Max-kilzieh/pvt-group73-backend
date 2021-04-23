package com.pvt73.recycling.repository;

import com.pvt73.recycling.model.dao.Image;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image,Integer> {

    Image findByName(String name);
}
