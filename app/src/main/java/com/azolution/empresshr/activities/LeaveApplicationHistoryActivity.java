package com.azolution.empresshr.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.azolution.empresshr.R;
import com.azolution.empresshr.adepter.LeaveHistoryAdepter;
import com.azolution.empresshr.model.LeaveApplicationHistory;
import com.azolution.empresshr.network.ApiClient;
import com.azolution.empresshr.network.EmployeeApi;
import com.azolution.empresshr.utils.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveApplicationHistoryActivity extends AppCompatActivity {

    //---------xml instance---------------
    private RecyclerView recyclerView;

    //-------------class instance--------------
    private SharedPreferences userInfoPref;
    private String authToken,employeeId,employeeName;
    private LeaveHistoryAdepter adepter;
    private ArrayList<LeaveApplicationHistory> historiesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application_history);

        userInfoPref = getSharedPreferences(Util.EMPLOYEE_LOGGEDIN_PREF,0);
        authToken = userInfoPref.getString(Util.AUTH_TOKEN,"");
        employeeId = userInfoPref.getString(Util.EMPLOYEE_ID,"");
        employeeName = userInfoPref.getString(Util.EMPLOYEE_NAME,"");



        //----------action bar--------------
        Toolbar toolbar = findViewById(R.id.leave_application_history_activity_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Leave Application History");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.leave_application_history_activity_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApiClient.resetApiClient();
        Call<List<LeaveApplicationHistory>> leaveApplicationHistoryCall = ApiClient.getClient(Util.BASE_URL,authToken).create(EmployeeApi.class).getEmployeeLeaveApplicationHistory(employeeId);
        leaveApplicationHistoryCall.enqueue(new Callback<List<LeaveApplicationHistory>>() {
            @Override
            public void onResponse(@NonNull Call<List<LeaveApplicationHistory>> call, @NonNull Response<List<LeaveApplicationHistory>> response) {
                if (response.isSuccessful()){
                    historiesList.addAll(response.body());
                    adepter = new LeaveHistoryAdepter(historiesList,getApplicationContext(),employeeName);
                    recyclerView.setAdapter(adepter);
                    adepter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<LeaveApplicationHistory>> call, @NonNull Throwable t) {

            }
        });



    }

    public void loadEmpressPage(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("http://www.empresshr.com/"));
        startActivity(i);
    }
}
