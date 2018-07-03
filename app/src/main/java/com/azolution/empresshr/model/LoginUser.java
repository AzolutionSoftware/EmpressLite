package com.azolution.empresshr.model;

import com.google.gson.annotations.SerializedName;

public class LoginUser {
    @SerializedName("username")
    private String employeeID;

    @SerializedName("password")
    private String employeePassword;

    @SerializedName("grand_type")
    private String type;

    public LoginUser(String employeeID, String employeePassword) {
        this.employeeID = employeeID;
        this.employeePassword = employeePassword;
        type = "password";
    }
}
