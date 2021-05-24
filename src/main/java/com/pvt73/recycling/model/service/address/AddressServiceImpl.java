package com.pvt73.recycling.model.service.address;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.pvt73.recycling.model.dao.Address;
import com.pvt73.recycling.model.dao.LatLng;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {
    private final GeoApiContext context;

    @Override
    public Address getAddress(LatLng coordinates) {

        GeocodingResult[] results = new GeocodingResult[0];

        try {

            results = GeocodingApi.newRequest(context)
                    .latlng(new com.google.maps.model.LatLng(coordinates.getLatitude(), coordinates.getLongitude()))
                    .await();

        } catch (ApiException | InterruptedException | IOException e) {
            log.error("Google maps geocoding api", e);
        }

        String[] addresses = results[0].formattedAddress.split(",");


        StringBuilder toSave = new StringBuilder();

        for (int i = 0; i < addresses.length - 1; i++)
            toSave.append(addresses[i]);


        return new Address(coordinates, toSave.toString());
    }
}
