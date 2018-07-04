package com.azolution.empresshr.network;

import com.azolution.empresshr.model.AttendanceHistory;
import com.azolution.empresshr.model.EmployeeProfileInformation;
import com.azolution.empresshr.model.LeaveApplication;
import com.azolution.empresshr.model.LeaveApplicationHistory;
import com.azolution.empresshr.model.LeaveType;
import com.azolution.empresshr.model.RoutePlan;
import com.azolution.empresshr.model.TokenResponse;
import com.azolution.empresshr.model.ApiResponseMessage;
import com.azolution.empresshr.model.EmployeeAttendance;
import com.azolution.empresshr.model.EmployeeInformation;
import com.azolution.empresshr.model.EmployeeLocationTrack;
import com.azolution.empresshr.model.RegisterUser;

import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EmployeeApi {
    @Headers("Content-Type: application/json")
    @POST("/api/UserAuthorization/FaceRagistration")
    Call<ApiResponseMessage> registerUser(@Body RegisterUser users);

   @FormUrlEncoded
    @POST("/token")
    Call<TokenResponse> requestForUserLoggedIn(@Field("grant_type") String type, @Field("username") String name, @Field("password") String pass);

    @Headers("Content-Type: application/json")
    @GET("/api/UserAuthorization/GetUser")
    Call<EmployeeInformation> getEmployeeInformation(@Query("empId") String empId);

    @Headers({"Content-Type: application/json"})
    @GET("/api/UserAuthorization/GetPhoto")
    Call<ResponseBody> getUserProfilePicture(@Query("empId") String empId);


    @Headers("Content-Type: application/json")
    @POST("/api/Attendance/Punch")
    Call<ApiResponseMessage> sendEmployeeAttendanceData(@Body EmployeeAttendance attendance);

    @Headers("Content-Type: application/json")
    @POST("/api/LocationTracker/SendLocation")
    Call<Void> sendEmployeeTrackData(@Body EmployeeLocationTrack locationTrack);


    @GET("/api/Leave/GetLeaveType")
    Call<List<LeaveType>> getLeaveTypeData(@Query("empId") String empId);

    @POST("/api/Leave/Apply")
    Call<ApiResponseMessage> sendLeaveApplication(@Body LeaveApplication application);

    @Headers("Content-Type: application/json")
    @GET("/api/UserAuthorization/GetEmployeeProfile")
    Call<EmployeeProfileInformation> getEmployeeProfileInformation(@Query("empId") String empId);

    @Headers("Content-Type: application/json")
    @GET("/api/Leave/GetLeaveApplications")
    Call<List<LeaveApplicationHistory>> getEmployeeLeaveApplicationHistory(@Query("empId") String empId);

    @GET("/api/Attendance/GetAttendanceSummary")
    Call<List<AttendanceHistory>> getAttendanceHistory(@Query("empId") String empId, @Query("year") int year, @Query("month") int month);

    @GET("/api/Attendance/GetRoutePlanByEmployee")
    Call<List<RoutePlan>> getRoutePlan(@Query("empId") String empId, @Query("date") String date);

}
