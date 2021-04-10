package com.pvt73.recycling.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class WasteBin {

    @Id
    @GeneratedValue
    private Integer id;

    private double latitude;
    private double longitude;

    public WasteBin(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public WasteBin() {
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
