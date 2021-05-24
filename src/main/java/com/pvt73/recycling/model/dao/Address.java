package com.pvt73.recycling.model.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class Address {
    private final LatLng coordinates;
    private final String adress;
}
