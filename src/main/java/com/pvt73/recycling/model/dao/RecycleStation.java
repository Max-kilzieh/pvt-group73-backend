package com.pvt73.recycling.model.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Getter
@NoArgsConstructor
@Entity
public class RecycleStation {

    @EmbeddedId
    private LatLng coordinates;

    public RecycleStation(LatLng coordinates) {
        this.coordinates = coordinates;
    }
}