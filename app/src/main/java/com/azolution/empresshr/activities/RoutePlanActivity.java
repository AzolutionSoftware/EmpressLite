package com.azolution.empresshr.activities;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.azolution.empresshr.R;
import com.azolution.empresshr.adepter.RoutePlanAdepter;
import com.azolution.empresshr.model.RoutePlan;
import com.azolution.empresshr.network.ApiClient;
import com.azolution.empresshr.network.EmployeeApi;
import com.azolution.empresshr.utils.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutePlanActivity extends AppCompatActivity {

    private TextView selectDateText;
    private RecyclerView recyclerView;

    private ArrayList<RoutePlan> routePlanList = new ArrayList<>();
    private RoutePlanAdepter adepter;
    private SharedPreferences sharedPreferences;
    private String employeeId,authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_plan);

        sharedPreferences = getSharedPreferences(Util.EMPLOYEE_LOGGEDIN_PREF,0);
        employeeId = sharedPreferences.getString(Util.EMPLOYEE_ID,"");
        authToken = sharedPreferences.getString(Util.AUTH_TOKEN,"");

        Toolbar toolbar = findViewById(R.id.route_plan_activity_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Route Plan");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.route_plan_activity_recyclerView);
        selectDateText = findViewById(R.id.route_plan_activity_dateText);
        selectDateText.setText(Util.getCurrentDate());


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adepter = new RoutePlanAdepter(routePlanList,getApplicationContext());
        recyclerView.setAdapter(adepter);

        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = df.format(date);

        Call<List<RoutePlan>> routePlanCall = ApiClient.getClient(Util.BASE_URL,authToken).create(EmployeeApi.class).getRoutePlan("521229",formattedDate);
        routePlanCall.enqueue(new Callback<List<RoutePlan>>() {
            @Override
            public void onResponse(@NonNull Call<List<RoutePlan>> call, @NonNull Response<List<RoutePlan>> response) {
                if (response.isSuccessful()){
                    routePlanList.clear();
                    routePlanList.addAll(response.body());
                    adepter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RoutePlan>> call, @NonNull Throwable t) {

            }
        });
    }
}
