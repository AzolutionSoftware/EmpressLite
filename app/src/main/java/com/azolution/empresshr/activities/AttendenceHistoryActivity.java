package com.azolution.empresshr.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import com.azolution.empresshr.R;
import com.azolution.empresshr.adepter.AttendanceHistoryAdepter;
import com.azolution.empresshr.model.AttendanceHistory;
import com.azolution.empresshr.model.EmployeeAttendanceGraph;
import com.azolution.empresshr.network.ApiClient;
import com.azolution.empresshr.network.EmployeeApi;
import com.azolution.empresshr.utils.Util;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendenceHistoryActivity extends AppCompatActivity {

    //----------xml instance---------------
    private RecyclerView recyclerView;
    private TextView monthSelectorText;
    private PieChart pieChart;



    //---------class insatnce----------
    private SharedPreferences userInfoPref;
    private String authToken,employeeId,employeeName;
    private ArrayList<AttendanceHistory> attendanceHistorieList = new ArrayList<>();
    private AttendanceHistoryAdepter adepter;
    private int mYear, mMonth, mDay;
    private SimpleDateFormat readableFormat,dateFormat;

    final int[] MY_COLORS = {
            Color.rgb(66, 134, 244),
            Color.rgb(255,0,0),
            Color.rgb(255,192,0),
            Color.rgb(127,127,127),
            Color.rgb(146,208,80),
            Color.rgb(0,176,80)};
    ArrayList<Integer> colors = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_history);
        readableFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

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
        pieChart = findViewById(R.id.attendance_activity_piecart);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adepter = new AttendanceHistoryAdepter(attendanceHistorieList,getApplicationContext());
        recyclerView.setAdapter(adepter);

        getAttendanceHistory(Util.getCurrentYear(),Util.getCurrentMonth());
        showAttendanceGraph(Integer.parseInt(Util.getCurrentMonth()),Integer.parseInt(Util.getCurrentYear()));


        try {
            Date currentReadbleDate = dateFormat.parse(Util.getCurrentDateForServer());
            String formatDate = readableFormat.format(currentReadbleDate);
            monthSelectorText.setText(formatDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        monthSelectorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AttendenceHistoryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                getAttendanceHistory(String.valueOf(year),String.valueOf(monthOfYear+1));
                                showAttendanceGraph((monthOfYear+1),year);
                                try {
                                    String pickerDate = String.valueOf((monthOfYear+1)+"/"+dayOfMonth+"/"+year);
                                    Date currentReadbleDate = dateFormat.parse(pickerDate);
                                    String formatDate = readableFormat.format(currentReadbleDate);
                                    monthSelectorText.setText(formatDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }



                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });

        /**
         *------------------pi chart------------
         **/

    }


    private void getAttendanceHistory(String year, String month){
        ApiClient.resetApiClient();
        Call<List<AttendanceHistory>> getAttendanceHistoryCall = ApiClient.getClient(Util.BASE_URL,authToken).create(EmployeeApi.class).getAttendanceHistory(employeeId,Integer.parseInt(year),Integer.parseInt(month));
        getAttendanceHistoryCall.enqueue(new Callback<List<AttendanceHistory>>() {
            @Override
            public void onResponse(@NonNull Call<List<AttendanceHistory>> call, @NonNull Response<List<AttendanceHistory>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        attendanceHistorieList.clear();
                        attendanceHistorieList.addAll(response.body());
                        adepter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(getApplicationContext(),"Failed to fetch data from server",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Something went wrong. Please try again",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<AttendanceHistory>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void showAttendanceGraph(int month, int year){
        ApiClient.resetApiClient();
        Call<List<EmployeeAttendanceGraph>> call = ApiClient.getClient(Util.BASE_URL,authToken).create(EmployeeApi.class).getAttendanceGraph(employeeId,year,month);
        call.enqueue(new Callback<List<EmployeeAttendanceGraph>>() {
            @Override
            public void onResponse(@NonNull Call<List<EmployeeAttendanceGraph>> call, @NonNull Response<List<EmployeeAttendanceGraph>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        pieChart.setUsePercentValues(true);
                       /* ArrayList<Entry> yvalues = new ArrayList<>();
                        yvalues.add(new Entry(response.body().getNoOfPresent(), 0));
                        yvalues.add(new Entry(response.body().getNoOfOnsite(), 1));
                        yvalues.add(new Entry(response.body().getNoOfAbsent(), 2));
                        yvalues.add(new Entry(response.body().getNoOfLeave(), 3));
                        yvalues.add(new Entry(response.body().getNoOfDayOff(), 4));
                        yvalues.add(new Entry(response.body().getNoOfTraining(), 5));*/

                        ArrayList<Entry> yvalues = new ArrayList<>();
                        ArrayList<String> xVals = new ArrayList<String>();
                       for (int i = 0 ; i<response.body().size() ; i++){

                           if (response.body().get(i).getDate() > 0){
                               xVals.add(response.body().get(i).getLabel());
                               yvalues.add(new Entry(response.body().get(i).getDate(),i));
                           }

                       }


                        PieDataSet dataSet = new PieDataSet(yvalues, "");
                        for(int c: MY_COLORS) colors.add(c);
                        dataSet.setColors(colors);
                        //dataSet.setColors(ColorTemplate.JOYFUL_COLORS);


                     /*   ArrayList<String> xVals = new ArrayList<String>();

                        xVals.add(response.body().getNoOfPresentLebel());
                        xVals.add(response.body().getNoOfOnsiteLebel());
                        xVals.add(response.body().getNoOfAbsentLebel());
                        xVals.add(response.body().getNoOfLeaveLebel());
                        xVals.add(response.body().getNoOfDayOffLebel());
                        xVals.add(response.body().getNoOfTrainingLebel());*/


                        PieData data = new PieData(xVals, dataSet);
                        data.setValueFormatter(new PercentFormatter());
                        pieChart.setData(data);
                        pieChart.animateY(4000);

                    }else {
                        Toast.makeText(getApplicationContext(),"Failed to fetch data from server",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Something went wrong. Please try again",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<EmployeeAttendanceGraph>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void loadEmpressPage(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("http://www.empresshr.com/"));
        startActivity(i);
    }
}

