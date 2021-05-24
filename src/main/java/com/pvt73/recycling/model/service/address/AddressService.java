package com.pvt73.recycling.model.service.address;

import com.pvt73.recycling.model.dao.Address;
import com.pvt73.recycling.model.dao.LatLng;

public interface AddressService {

    Address getAddress(LatLng coordinates);
}
