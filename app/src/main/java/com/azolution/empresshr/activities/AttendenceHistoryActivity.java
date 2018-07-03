package com.azolution.empresshr.activities;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.azolution.empresshr.R;
import com.azolution.empresshr.adepter.AttendanceHistoryAdepter;
import com.azolution.empresshr.model.AttendanceHistory;
import com.azolution.empresshr.network.ApiClient;
import com.azolution.empresshr.network.EmployeeApi;
import com.azolution.empresshr.utils.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendenceHistoryActivity extends AppCompatActivity {

    //----------xml instance---------------
    private RecyclerView recyclerView;
    private TextView monthSelectorText;

    //---------class insatnce----------
    private SharedPreferences userInfoPref;
    private String authToken,employeeId,employeeName;
    private ArrayList<AttendanceHistory> attendanceHistorieList = new ArrayList<>();
    private AttendanceHistoryAdepter adepter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_history);

        userInfoPref = getSharedPreferences(Util.EMPLOYEE_LOGGEDIN_PREF,0);
        authToken = userInfoPref.getString(Util.AUTH_TOKEN,"");
        employeeId = userInfoPref.getString(Util.EMPLOYEE_ID,"");
        employeeName = userInfoPref.getString(Util.EMPLOYEE_NAME,"");

        Toolbar toolbar = findViewById(R.id.attendance_history_activity_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Attendance History");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.attendance_history_activity_recyclerView);
        monthSelectorText = findViewById(R.id.attendance_history_monthSelectorText);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adepter = new AttendanceHistoryAdepter(attendanceHistorieList,getApplicationContext());
        recyclerView.setAdapter(adepter);
        getAttendanceHistory(Util.getCurrentYear(),Util.getCurrentMonth());
        monthSelectorText.setText(Util.getCurrentMonth() + "/"+Util.getCurrentYear());


        monthSelectorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void getAttendanceHistory(String year, String month){
        ApiClient.resetApiClient();
        Call<List<AttendanceHistory>> getAttendanceHistoryCall = ApiClient.getClient(Util.BASE_URL,authToken).create(EmployeeApi.class).getAttendanceHistory(employeeId,Integer.parseInt(year),Integer.parseInt(month));
        getAttendanceHistoryCall.enqueue(new Callback<List<AttendanceHistory>>() {
            @Override
            public void onResponse(@NonNull Call<List<AttendanceHistory>> call, @NonNull Response<List<AttendanceHistory>> response) {
                if (response.isSuccessful()){
                    attendanceHistorieList.clear();
                    attendanceHistorieList.addAll(response.body());

                    adepter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<AttendanceHistory>> call, @NonNull Throwable t) {

            }
        });
    }


}

