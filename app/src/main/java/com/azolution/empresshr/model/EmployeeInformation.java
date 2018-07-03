package com.azolution.empresshr.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EmployeeInformation implements Serializable{

    @SerializedName("Name")
    private String employeeName;

    @SerializedName("EmployeeId")
    private String employeeId;

    @SerializedName("IMEI")
    private String imei;

    @SerializedName("Message")
    private String Message;

    public String getEmployeeName() {
        return employeeName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getImei() {
        return imei;
    }

    public String getMessage() {
        return Message;
    }
}
