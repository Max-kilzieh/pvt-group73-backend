package com.pvt73.recycling.model.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Image {

    @Id
    private String id;
    private int userId;


    private String url;

    private boolean isClean;
    private double latitude;
    private double longitude;

    public Image(int userId, boolean isClean, double latitude, double longitude, String id, String url) {
        this.userId = userId;
        this.isClean = isClean;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.url = url;
    }

    public Image() {
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isClean() {
        return isClean;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
