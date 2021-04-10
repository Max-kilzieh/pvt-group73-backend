package com.pvt73.recycling.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class RecycleStation {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String area;
    private double latitude;
    private double longitude;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> sorting = new ArrayList<>();


    public RecycleStation() {
    }

    public RecycleStation(String name, double latitude, double longitude, String area, List<String> sorting) {

        this.name = name;
        this.area = area;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sorting = sorting;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArea() {
        return area;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public List<String> getSorting() {
        return sorting;
    }
}