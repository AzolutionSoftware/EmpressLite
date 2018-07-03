package com.azolution.empresshr.model;

import com.google.gson.annotations.SerializedName;

public class AttendanceHistory {
    @SerializedName("InTime")
    private String intime;

    @SerializedName("OutTime")
    private String outTime;

    @SerializedName("WorkingHour")
    private String workingHour;

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
