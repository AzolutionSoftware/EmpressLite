package com.azolution.empresshr.model;

import com.google.gson.annotations.SerializedName;

public class EmployeeAttendance {
    @SerializedName("EmployeeId")
    private String employeeId;

    @SerializedName("IMEI")
    private String imei;

    @SerializedName("Latitude")
    private String latitude;

    @SerializedName("Longitud")
    private String longitude;

    @SerializedName("ImageString")
    private String image;

    @SerializedName("LogTime")
    private String date;

    public EmployeeAttendance(String employeeId, String imei, String latitude, String longitude, String image, String date) {
        this.employeeId = employeeId;
        this.imei = imei;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.date = date;
    }
}
