package com.pvt73.recycling.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class RecycleStation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    private String area;

    private Double latitude;
    private Double longitude;


    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> sorting = new ArrayList<>();


    public RecycleStation() {
    }

    public RecycleStation(String name, Double latitude, Double longitude, String area, List<String> sorting) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.area = area;
        this.sorting = sorting;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String locationName) {
        this.name = locationName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double lat) {
        this.latitude = lat;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double lng) {
        this.longitude = lng;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<String> getSorting() {
        return sorting;
    }

    public void setSorting(List<String> sorting) {
        this.sorting = sorting;
    }

}