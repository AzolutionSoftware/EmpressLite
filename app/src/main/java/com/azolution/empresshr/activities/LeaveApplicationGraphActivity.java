package com.azolution.empresshr.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.azolution.empresshr.R;
import com.azolution.empresshr.adepter.LeaveTypeAdepter;
import com.azolution.empresshr.model.ApiResponseMessage;
import com.azolution.empresshr.model.AttendanceHistory;
import com.azolution.empresshr.model.LeaveApplication;
import com.azolution.empresshr.model.LeaveType;
import com.azolution.empresshr.network.ApiClient;
import com.azolution.empresshr.network.EmployeeApi;
import com.azolution.empresshr.utils.Util;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveApplicationGraphActivity extends AppCompatActivity {

    private BarChart barChart;
    private SharedPreferences userPref;
    private String employeeId,authToken,profileName,profileImageString;

    float barWidth = 0.3f;
    float barSpace = 0f;
    float groupSpace = 0.4f;

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
                int groupCount = 5;

                ArrayList xVals = new ArrayList();

                xVals.add(response.body().get(0).getLeaveTypeName());
                xVals.add(response.body().get(1).getLeaveTypeName());
                xVals.add(response.body().get(2).getLeaveTypeName());
                xVals.add(response.body().get(3).getLeaveTypeName());
                xVals.add(response.body().get(4).getLeaveTypeName());




                ArrayList yVals1 = new ArrayList();
                ArrayList yVals2 = new ArrayList();
                ArrayList yVals3 = new ArrayList();

                yVals1.add(new BarEntry(1, (float) response.body().get(0).getLeaveEnjoyed()));
                yVals2.add(new BarEntry(1, (float) response.body().get(0).getOpeningLeaveBalance()));
                yVals3.add(new BarEntry(1, (float) response.body().get(0).getTotaLeave()));


                yVals1.add(new BarEntry(2, (float) response.body().get(1).getLeaveEnjoyed()));
                yVals2.add(new BarEntry(2, (float) response.body().get(1).getOpeningLeaveBalance()));
                yVals3.add(new BarEntry(2, (float) response.body().get(1).getTotaLeave()));


                yVals1.add(new BarEntry(3, (float) response.body().get(2).getLeaveEnjoyed()));
                yVals2.add(new BarEntry(3, (float) response.body().get(2).getOpeningLeaveBalance()));
                yVals3.add(new BarEntry(3, (float) response.body().get(2).getTotaLeave()));


                yVals1.add(new BarEntry(4, (float) response.body().get(3).getLeaveEnjoyed()));
                yVals2.add(new BarEntry(4, (float) response.body().get(3).getOpeningLeaveBalance()));
                yVals3.add(new BarEntry(4, (float) response.body().get(3).getTotaLeave()));


                yVals1.add(new BarEntry(5, (float) response.body().get(4).getLeaveEnjoyed()));
                yVals2.add(new BarEntry(5, (float) response.body().get(4).getOpeningLeaveBalance()));
                yVals3.add(new BarEntry(5, (float) response.body().get(4).getTotaLeave()));


                BarDataSet set1, set2,set3;
                set1 = new BarDataSet(yVals1, "use");
                set1.setColor(Color.RED);

                set2 = new BarDataSet(yVals2, "available");
                set2.setColor(Color.BLUE);

                set3 = new BarDataSet(yVals3,"total");
                set3.setColor(Color.GREEN);

                BarData data = new BarData(set1, set2,set3);
                data.setValueFormatter(new LargeValueFormatter());
                barChart.setData(data);
                barChart.getBarData().setBarWidth(barWidth);
                barChart.getXAxis().setAxisMinimum(0);
                barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
                barChart.groupBars(0, groupSpace, barSpace);
                barChart.getData().setHighlightEnabled(false);
                barChart.invalidate();

                Legend l = barChart.getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                l.setDrawInside(true);
                l.setYOffset(20f);
                l.setXOffset(0f);
                l.setYEntrySpace(0f);
                l.setTextSize(8f);

                //X-axis
                XAxis xAxis = barChart.getXAxis();
                xAxis.setGranularity(1f);
                xAxis.setGranularityEnabled(true);
                xAxis.setCenterAxisLabels(true);
                xAxis.setDrawGridLines(false);
                xAxis.setAxisMaximum(6);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
//Y-axis
                barChart.getAxisRight().setEnabled(false);
                YAxis leftAxis = barChart.getAxisLeft();
                leftAxis.setValueFormatter(new LargeValueFormatter());
                leftAxis.setDrawGridLines(true);
                leftAxis.setSpaceTop(35f);
                leftAxis.setAxisMinimum(0f);

            }

            @Override
            public void onFailure(@NonNull Call<List<LeaveType>> call, @NonNull Throwable t) {

            }
        });

    }

    private void drawGraph(){





       /* ArrayList<BarEntry> totalLeaveEntries = new ArrayList<>();
        totalLeaveEntries.add(new BarEntry(1,10));
        BarDataSet totalLeaveEntriesDataSet = new BarDataSet(totalLeaveEntries,"total Leave");
        totalLeaveEntriesDataSet.setColors(Color.RED);

        ArrayList<BarEntry> enjoyedLeaveEntries = new ArrayList<>();
        enjoyedLeaveEntries.add(new BarEntry(2,10));
        BarDataSet enjoyedLeaveEntriesDataSet = new BarDataSet(enjoyedLeaveEntries,"Enjoyed Leave");
        enjoyedLeaveEntriesDataSet.setColors(Color.GREEN);

        ArrayList<BarEntry> availableLeaveEntries = new ArrayList<>();
        availableLeaveEntries.add(new BarEntry(3,10));
        BarDataSet availabledLeaveEntriesDataSet = new BarDataSet(availableLeaveEntries,"Available Leave");
        availabledLeaveEntriesDataSet.setColors(Color.BLUE);



        BarData totalLeaveData = new BarData(totalLeaveEntriesDataSet,enjoyedLeaveEntriesDataSet,availabledLeaveEntriesDataSet);
        totalLeaveData.setBarWidth(0.9f);
        totalLeaveData.setValueTextSize(15f);
        totalLeaveData.setValueTextColor(Color.WHITE);
        barChart.setData(totalLeaveData);*/


    }

    public void applyLeaveApplication(View view) {
        startActivity(new Intent(LeaveApplicationGraphActivity.this,LeaveApplicationActivity.class));
    }
}
