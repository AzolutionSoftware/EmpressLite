package com.azolution.empresshr.model;

import com.google.gson.annotations.SerializedName;

public class AttendanceHistory {

    @SerializedName("AttendanceDate")
    private String attendanceDate;

    @SerializedName("InTime")
    private String intime;

    @SerializedName("OutTime")
    private String outTime;

    @SerializedName("WorkingHour")
    private String workingHour;

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public String getIntime() {
        return intime;
    }

    public String getOutTime() {
        return outTime;
    }

    public String getWorkingHour() {
        return workingHour;
    }
}
