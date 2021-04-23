package com.pvt73.recycling.model.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Image {

    @Id
    @GeneratedValue
    private Integer id;

    private boolean isClean;

    private double latitude;
    private double longitude;

    private String name;
    private String imageType;
    @Lob
    private byte[] data;

    public Image(boolean isClean, double latitude, double longitude, String imageType, byte[] data, String name) {
        this.isClean = isClean;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageType = imageType;
        this.data = data;
        this.name = name;
    }

    public Image() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isClean() {
        return isClean;
    }

    public void setClean(boolean clean) {
        isClean = clean;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] content) {
        this.data = content;
    }
}
