package com.azolution.empresshr.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class AttendanceAdjustment {
    @SerializedName("EmployeeId")
    private String employeeId;

    @SerializedName("AttendanceDate")
    private Date date;

    @SerializedName("AppliedDate")
    private Date appliedDate;

    @SerializedName("Reason")
    private String reason;

    public AttendanceAdjustment(String employeeId, Date date, Date appliedDate, String reason) {
        this.employeeId = employeeId;
        this.date = date;
        this.appliedDate = appliedDate;
        this.reason = reason;
    }
}
