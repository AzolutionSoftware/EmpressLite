package com.azolution.empresshr.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class EmployeeProfileInformation {
    @SerializedName("EmployeeId")
    private String employeeId;

    @SerializedName("EmployeeName")
    private String employeeName;

    @SerializedName("Designation")
    private String designation;

    @SerializedName("CompanyName")
    private String companyName;

    @SerializedName("BranchName")
    private String branchName;

    @SerializedName("DepartmentName")
    private String departmentName;

    @SerializedName("DevisionName")
    private String devisionName;

    @SerializedName("RSMRegionName")
    private String RSMRegionName;

    @SerializedName("RSMManager")
    private String RSMManager;

    @SerializedName("SalesManager")
    private String salesManager;

    @SerializedName("PSOLocationName")
    private String PSOLocationName;

    @SerializedName("SectionName")
    private String sectionName;

    @SerializedName("FacilityName")
    private String facilityName;

   /* @SerializedName("EmploymentDate")
    private Date employmentDate;*/

 /*   @SerializedName("DateOfBirth")
    private Date dateOfBirth;*/

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getDesignation() {
        return designation;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getDevisionName() {
        return devisionName;
    }

    public String getRSMRegionName() {
        return RSMRegionName;
    }

    public String getRSMManager() {
        return RSMManager;
    }

    public String getSalesManager() {
        return salesManager;
    }

    public String getPSOLocationName() {
        return PSOLocationName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getFacilityName() {
        return facilityName;
    }

   /* public Date getEmploymentDate() {
        return employmentDate;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }*/
}
