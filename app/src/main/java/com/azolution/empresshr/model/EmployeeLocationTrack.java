package com.azolution.empresshr.model;

import com.google.gson.annotations.SerializedName;

public class EmployeeLocationTrack {

    @SerializedName("EmployeeId")
    private String employeeId;

    @SerializedName("Latitude")
    private String latitude;

    @SerializedName("Longitud")
    private String longitude;

    @SerializedName("LogTime")
    private String time;

    @SerializedName("IMEI")
    private String imei;

    public EmployeeLocationTrack(String employeeId, String latitude, String longitude, String time, String imei) {
        this.employeeId = employeeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.imei = imei;
    }
}
