package com.azolution.empresshr.model;

import com.google.gson.annotations.SerializedName;

public class RegisterUser {
    private String EmployeeId;
    private String Latitude;
    private String Longitud;
    private String IMEI;
    private String Password;
    private String ImageString;

    public RegisterUser(String employeeId, String latitude, String longitud, String IMEI, String password, String imageString) {
        EmployeeId = employeeId;
        Latitude = latitude;
        Longitud = longitud;
        this.IMEI = IMEI;
        Password = password;
        ImageString = imageString;
    }
}
