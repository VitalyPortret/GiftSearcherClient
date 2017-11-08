package com.giftsearcher.giftsearcherclient.entity;

public class Address {

    private String address;

    private GeoData geoData;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public GeoData getGeoData() {
        return geoData;
    }

    public void setGeoData(GeoData geoData) {
        this.geoData = geoData;
    }
}

