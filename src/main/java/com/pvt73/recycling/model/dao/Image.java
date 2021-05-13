package com.pvt73.recycling.model.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class Image {

    @Id
    private String imageId;
    private LatLng coordinates;
    private Integer userId;
    private String url;
    private boolean clean;
    private String description;


    public Image(int userId, boolean clean, LatLng coordinates, String description, String imageId, String url) {
        this.userId = userId;
        this.clean = clean;
        this.coordinates = coordinates;
        this.description = description;
        this.imageId = imageId;
        this.url = url;
    }

}
