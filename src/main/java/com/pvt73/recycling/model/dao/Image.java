package com.pvt73.recycling.model.dao;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Image {

    @Id
    private String id;
    private Integer userId;
    private String url;
    private boolean clean;
    private String description;
    private double latitude;
    private double longitude;

    public Image(int userId, boolean clean, double latitude, double longitude, String description, String id, String url) {
        this.userId = userId;
        this.clean = clean;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.id = id;
        this.url = url;
    }


    public Image() {
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isClean() {
        return clean;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
