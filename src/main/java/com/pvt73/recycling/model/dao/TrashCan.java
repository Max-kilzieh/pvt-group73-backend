package com.pvt73.recycling.model.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Getter
@NoArgsConstructor
@Entity
public class TrashCan {

    @EmbeddedId
    private LatLng coordinates;

    public TrashCan(LatLng coordinates) {
        this.coordinates = coordinates;
    }

}
