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

    @SerializedName("FromDate")
    private String startDate;

    @SerializedName("ToDate")
    private String endDate;

    @SerializedName("AppliedDate")
    private String appaliedDate;

    @SerializedName("ApprovedDate")
    private SerializedName approveDate;

    @SerializedName("TotalDays")
    private String totalDays;


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

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getAppaliedDate() {
        return appaliedDate;
    }

    public SerializedName getApproveDate() {
        return approveDate;
    }

    public String getTotalDays() {
        return totalDays;
    }
}
