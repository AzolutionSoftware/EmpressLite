package com.azolution.empresshr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeAttendanceGraph {

    @SerializedName("label")
    private String label;

    @SerializedName("data")
    private int data;

    @SerializedName("color")
    private String color;

    public String getLabel() {
        return label;
    }

    public int getDate() {
        return data;
    }

    public String getColor() {
        return color;
    }

    /*  @SerializedName("NoOfPresent")
    @Expose
    private Integer noOfPresent;
    @SerializedName("NoOfPresentLebel")
    @Expose
    private String noOfPresentLebel;
    @SerializedName("NoOfOnsite")
    @Expose
    private Integer noOfOnsite;
    @SerializedName("NoOfOnsiteLebel")
    @Expose
    private String noOfOnsiteLebel;
    @SerializedName("NoOfAbsent")
    @Expose
    private Integer noOfAbsent;
    @SerializedName("NoOfAbsentLebel")
    @Expose
    private String noOfAbsentLebel;
    @SerializedName("NoOfLeave")
    @Expose
    private Integer noOfLeave;
    @SerializedName("NoOfLeaveLebel")
    @Expose
    private String noOfLeaveLebel;
    @SerializedName("NoOfDayOff")
    @Expose
    private Integer noOfDayOff;
    @SerializedName("NoOfDayOffLebel")
    @Expose
    private String noOfDayOffLebel;
    @SerializedName("NoOfTraining")
    @Expose
    private Integer noOfTraining;
    @SerializedName("NoOfTrainingLebel")
    @Expose
    private String noOfTrainingLebel;

    public Integer getNoOfPresent() {
        return noOfPresent;
    }

    public void setNoOfPresent(Integer noOfPresent) {
        this.noOfPresent = noOfPresent;
    }

    public String getNoOfPresentLebel() {
        return noOfPresentLebel;
    }

    public void setNoOfPresentLebel(String noOfPresentLebel) {
        this.noOfPresentLebel = noOfPresentLebel;
    }

    public Integer getNoOfOnsite() {
        return noOfOnsite;
    }

    public void setNoOfOnsite(Integer noOfOnsite) {
        this.noOfOnsite = noOfOnsite;
    }

    public String getNoOfOnsiteLebel() {
        return noOfOnsiteLebel;
    }

    public void setNoOfOnsiteLebel(String noOfOnsiteLebel) {
        this.noOfOnsiteLebel = noOfOnsiteLebel;
    }

    public Integer getNoOfAbsent() {
        return noOfAbsent;
    }

    public void setNoOfAbsent(Integer noOfAbsent) {
        this.noOfAbsent = noOfAbsent;
    }

    public String getNoOfAbsentLebel() {
        return noOfAbsentLebel;
    }

    public void setNoOfAbsentLebel(String noOfAbsentLebel) {
        this.noOfAbsentLebel = noOfAbsentLebel;
    }

    public Integer getNoOfLeave() {
        return noOfLeave;
    }

    public void setNoOfLeave(Integer noOfLeave) {
        this.noOfLeave = noOfLeave;
    }

    public String getNoOfLeaveLebel() {
        return noOfLeaveLebel;
    }

    public void setNoOfLeaveLebel(String noOfLeaveLebel) {
        this.noOfLeaveLebel = noOfLeaveLebel;
    }

    public Integer getNoOfDayOff() {
        return noOfDayOff;
    }

    public void setNoOfDayOff(Integer noOfDayOff) {
        this.noOfDayOff = noOfDayOff;
    }

    public String getNoOfDayOffLebel() {
        return noOfDayOffLebel;
    }

    public void setNoOfDayOffLebel(String noOfDayOffLebel) {
        this.noOfDayOffLebel = noOfDayOffLebel;
    }

    public Integer getNoOfTraining() {
        return noOfTraining;
    }

    public void setNoOfTraining(Integer noOfTraining) {
        this.noOfTraining = noOfTraining;
    }

    public String getNoOfTrainingLebel() {
        return noOfTrainingLebel;
    }

    public void setNoOfTrainingLebel(String noOfTrainingLebel) {
        this.noOfTrainingLebel = noOfTrainingLebel;
    }*/
}
