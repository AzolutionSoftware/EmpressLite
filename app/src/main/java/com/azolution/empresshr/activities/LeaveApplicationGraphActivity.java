package com.azolution.empresshr.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.azolution.empresshr.R;
import com.azolution.empresshr.model.LeaveType;
import com.azolution.empresshr.network.ApiClient;
import com.azolution.empresshr.network.EmployeeApi;
import com.azolution.empresshr.utils.Util;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveApplicationGraphActivity extends AppCompatActivity {

    private BarChart barChart;
    private SharedPreferences userPref;
    private String employeeId,authToken,profileName,profileImageString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application_graph);

        userPref = getSharedPreferences(Util.EMPLOYEE_LOGGEDIN_PREF,0);
        authToken = userPref.getString(Util.AUTH_TOKEN,"");
        employeeId = userPref.getString(Util.EMPLOYEE_ID,"");

        //-----------action bar------------
        Toolbar toolbar = findViewById(R.id.leave_application_graph_activity_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Leave Application Graph");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        barChart = findViewById(R.id.leave_application_graph_activity_lineGraph);
        barChart.setDescription(null);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);




        ApiClient.resetApiClient();
        Call<List<LeaveType>> leaveTypeCall = ApiClient.getClient(Util.BASE_URL,authToken).create(EmployeeApi.class).getLeaveTypeData(employeeId);
        leaveTypeCall.enqueue(new Callback<List<LeaveType>>() {
            @Override
            public void onResponse(@NonNull Call<List<LeaveType>> call, @NonNull Response<List<LeaveType>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        ArrayList<String> labels = new ArrayList<String>();
                        ArrayList<IBarDataSet> dataSets = new ArrayList<>();




                        for (int i = 0 ; i<response.body().size() ; i++){
                            labels.add(response.body().get(i).getLeaveTypeName());

                            ArrayList<BarEntry> bargroup1 = new ArrayList<>();
                            bargroup1.add(new BarEntry(response.body().get(i).getOpeningLeaveBalance()+1, 0));

                            ArrayList<BarEntry> bargroup2 = new ArrayList<>();
                            bargroup2.add(new BarEntry(response.body().get(i).getLeaveEnjoyed()+10, 1));

                            ArrayList<BarEntry> bargroup3 = new ArrayList<>();
                            bargroup3.add(new BarEntry(response.body().get(i).getTotaLeave()+10, 2));

                            BarDataSet barDataSet1 = new BarDataSet(bargroup1, "Opening");
                            barDataSet1.setColor(Color.RED);
                            dataSets.add(barDataSet1);
                            // creating dataset for Bar Group 2
                            BarDataSet barDataSet2 = new BarDataSet(bargroup2, "Availed");
                            barDataSet2.setColor(Color.GREEN);

                            // creating dataset for Bar Group 3
                            BarDataSet barDataSet3 = new BarDataSet(bargroup3, "Closing");
                            barDataSet3.setColor(Color.BLUE);

                            dataSets.add(barDataSet1);
                            dataSets.add(barDataSet2);
                            dataSets.add(barDataSet3);

                        }




                        BarData data = new BarData(labels,dataSets);
                        barChart.setData(data);
                        barChart.animateX(4000);
                        barChart.animateY(4000);

                        // create BarEntry for Bar Group 1
                      /*  ArrayList<BarEntry> bargroup1 = new ArrayList<>();
                        bargroup1.add(new BarEntry(8f, 0));
                        bargroup1.add(new BarEntry(2f, 1));
                        bargroup1.add(new BarEntry(5f, 2));
                        bargroup1.add(new BarEntry(20f, 3));
                        bargroup1.add(new BarEntry(15f, 4));
                        bargroup1.add(new BarEntry(19f, 5));

                        // create BarEntry for Bar Group 2
                        ArrayList<BarEntry> bargroup2 = new ArrayList<>();
                        bargroup2.add(new BarEntry(6f, 0));
                        bargroup2.add(new BarEntry(10f, 1));
                        bargroup2.add(new BarEntry(5f, 2));
                        bargroup2.add(new BarEntry(25f, 3));
                        bargroup2.add(new BarEntry(4f, 4));
                        bargroup2.add(new BarEntry(17f, 5));

                        // create BarEntry for Bar Group 3
                        ArrayList<BarEntry> bargroup3 = new ArrayList<>();
                        bargroup3.add(new BarEntry(4f, 0));
                        bargroup3.add(new BarEntry(7f, 1));
                        bargroup3.add(new BarEntry(15f, 2));
                        bargroup3.add(new BarEntry(20f, 3));
                        bargroup3.add(new BarEntry(8f, 4));
                        bargroup3.add(new BarEntry(12f, 5));

                        // creating dataset for Bar Group1
                        BarDataSet barDataSet1 = new BarDataSet(bargroup1, "Bar Group 1");
                        //barDataSet1.setColor(Color.rgb(0, 155, 0));
                        barDataSet1.setColor(Color.RED);

                        // creating dataset for Bar Group 2
                        BarDataSet barDataSet2 = new BarDataSet(bargroup2, "Bar Group 2");
                        barDataSet2.setColor(Color.GREEN);

                        // creating dataset for Bar Group 3
                        BarDataSet barDataSet3 = new BarDataSet(bargroup3, "Bar Group 3");
                        barDataSet3.setColor(Color.BLUE);

                        ArrayList<String> labels = new ArrayList<String>();
                        labels.add("2016");
                        labels.add("2015");
                        labels.add("2014");
                        labels.add("2013");
                        labels.add("2012");
                        labels.add("2011");

                        ArrayList<IBarDataSet> dataSets = new ArrayList<>();  // combined all dataset into an arraylist
                        dataSets.add(barDataSet1);
                        dataSets.add(barDataSet2);
                        dataSets.add(barDataSet3);

                        BarData data = new BarData(labels,dataSets);
                        barChart.setData(data);
                        barChart.animateX(4000);
                        barChart.animateY(4000);*/

                    }else {
                        Toast.makeText(getApplicationContext(),"Failed to fetch data from server",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Something went wrong. Please try again",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<LeaveType>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void applyLeaveApplication(View view) {
        startActivity(new Intent(LeaveApplicationGraphActivity.this,LeaveApplicationActivity.class));
    }

    public void loadEmpressPage(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("http://www.empresshr.com/"));
        startActivity(i);
    }
}
