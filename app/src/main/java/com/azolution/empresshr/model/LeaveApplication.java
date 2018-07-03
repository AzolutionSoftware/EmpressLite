package com.azolution.empresshr.model;

import com.google.gson.annotations.SerializedName;

public class LeaveApplication {
    @SerializedName("EmployeeId")
    private String employeeId;

    @SerializedName("LeaveTypeId")
    private Integer leaveTypeId;

    @SerializedName("FromDate")
    private String startDate;

    @SerializedName("ToDate")
    private String endDate;

    @SerializedName("LeaveReason")
    private String leaveReason;

    public LeaveApplication(String employeeId, Integer leaveTypeId, String startDate, String endDate, String leaveReason) {
        this.employeeId = employeeId;
        this.leaveTypeId = leaveTypeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leaveReason = leaveReason;
    }
}
