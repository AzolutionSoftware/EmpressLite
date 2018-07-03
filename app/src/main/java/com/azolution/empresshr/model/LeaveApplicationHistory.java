package com.azolution.empresshr.model;

import com.google.gson.annotations.SerializedName;

public class LeaveApplicationHistory {
    @SerializedName("LeaveId")
    private String leaveId;

    @SerializedName("EmployeeId")
    private String employeeId;

    @SerializedName("LeaveTypeName")
    private String leaveTypeName;

    @SerializedName("LeaveReason")
    private String leaveReason;

    @SerializedName("Status")
    private String status;

    public String getLeaveId() {
        return leaveId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getLeaveTypeName() {
        return leaveTypeName;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public String getStatus() {
        return status;
    }
}
