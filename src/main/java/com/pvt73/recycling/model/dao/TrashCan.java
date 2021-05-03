package com.pvt73.recycling.model.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TrashCan {

    @Id
    @GeneratedValue
    private Integer id;

    private double latitude;
    private double longitude;

    public TrashCan(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public TrashCan() {
    }

    public Integer getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
