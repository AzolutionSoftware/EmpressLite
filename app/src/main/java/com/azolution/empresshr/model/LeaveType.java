package com.azolution.empresshr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveType {
    @SerializedName("LeaveTypeId")
    @Expose
    private Integer leaveTypeId;
    @SerializedName("LeaveTypeName")
    @Expose
    private String leaveTypeName;

    @SerializedName("Totalleave")
    private int totaLeave;

    @SerializedName("LeaveEnjoied")
    private int leaveEnjoyed;

    @SerializedName("OpeningLeaveBalance")
    private int openingLeaveBalance;

    @SerializedName("ClosingLeaveBalance")
    private int closingLeaveBalance;

    public int getTotaLeave() {
        return totaLeave;
    }

    public int getLeaveEnjoyed() {
        return leaveEnjoyed;
    }

    public int getOpeningLeaveBalance() {
        return openingLeaveBalance;
    }

    public Integer getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(Integer leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public String getLeaveTypeName() {
        return leaveTypeName;
    }

    public void setLeaveTypeName(String leaveTypeName) {
        this.leaveTypeName = leaveTypeName;
    }
    public int getClosingLeaveBalance() {
        return closingLeaveBalance;
    }
}
