package com.pvt73.recycling.repository;

import com.pvt73.recycling.model.dao.Image;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface ImageRepository extends CrudRepository<Image, Integer> {


    @Transactional
    void deleteImageById(String id);


}
