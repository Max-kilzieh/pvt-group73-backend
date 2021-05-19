package com.pvt73.recycling.model.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class RecycleStation {

    @EmbeddedId
    private LatLng coordinates;

    @Transient
    private double distance;

    public RecycleStation(LatLng coordinates) {
        this.coordinates = coordinates;
    }
}