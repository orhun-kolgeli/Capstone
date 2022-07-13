package com.orhunkolgeli.capstone;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.parse.ParseGeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomGeoCoder {
    public static final int MAX_RESULTS = 1;
    private final Geocoder geocoder;

    public CustomGeoCoder(Context context) {
        this.geocoder = new Geocoder(context, Locale.getDefault());
    }

    // Return a list of addresses matching the given string
    public List<Address> getAddressListFromText(String text, int maxResults) {
        try {
            return geocoder.getFromLocationName(text, maxResults);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Return a ParseGeoPoint matching the given address string
    public ParseGeoPoint getGeoPointFromText(String text) {
        List<Address> addressList = getAddressListFromText(text, MAX_RESULTS);
        if (addressList.isEmpty()) {
            return null;
        }
        Address address = addressList.get(0);
        return new ParseGeoPoint(address.getLatitude(), address.getLongitude());
    }
}
