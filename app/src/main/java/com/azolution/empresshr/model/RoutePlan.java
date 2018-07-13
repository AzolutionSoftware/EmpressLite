package com.azolution.empresshr.model;

import com.google.gson.annotations.SerializedName;

public class RoutePlan {
    @SerializedName("OutletName")
    private String outletName;

    @SerializedName("GeoLocation")
    private String geoLocation;

    @SerializedName("VisitStartDateTime")
    private String visitDateTime;


    public String getOutletName() {
        return outletName;
    }

    public String getGeoLocation() {
        return geoLocation;
    }

    public String getVisitDateTime() {
        return visitDateTime;
    }
}
