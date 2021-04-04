package com.pvt73.recycling.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class RecycleStation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Integer id;

    private String locationName;
    private Double lat;
    private Double lng;
    private String area;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> sorting = new ArrayList<>();


    public RecycleStation() {
    }

    public RecycleStation(String locationName, Double lat, Double lng, String area, List<String> sorting) {
        this.locationName = locationName;
        this.lat = lat;
        this.lng = lng;
        this.area = area;
        this.sorting = sorting;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
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