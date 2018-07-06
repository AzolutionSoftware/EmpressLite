package com.azolution.empresshr.activities;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.azolution.empresshr.R;
import com.azolution.empresshr.adepter.AttendanceHistoryAdepter;
import com.azolution.empresshr.model.AttendanceHistory;
import com.azolution.empresshr.network.ApiClient;
import com.azolution.empresshr.network.EmployeeApi;
import com.azolution.empresshr.utils.Util;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
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

    PieChart pieChart ;
    ArrayList<PieEntry> entries ;
    ArrayList<String> PieEntryLabels ;
    PieDataSet pieDataSet ;
    PieData pieData ;

    //---------class insatnce----------
    private SharedPreferences userInfoPref;
    private String authToken,employeeId,employeeName;
    private ArrayList<AttendanceHistory> attendanceHistorieList = new ArrayList<>();
    private AttendanceHistoryAdepter adepter;

    private int mYear, mMonth, mDay;



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
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AttendenceHistoryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                getAttendanceHistory(String.valueOf(year),String.valueOf(monthOfYear+1));
                                monthSelectorText.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });

        pieChart = findViewById(R.id.attendance_activity_piecart);
        entries = new ArrayList<>();
        PieEntryLabels = new ArrayList<>();

        AddValuesToPIEENTRY();

        AddValuesToPieEntryLabels();

        pieDataSet = new PieDataSet(entries, "");
        pieData = new PieData (pieDataSet);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        pieChart.setData(pieData);

        pieChart.animateY(3000);



    }

    public void AddValuesToPIEENTRY(){

        entries.add(new PieEntry(2f, 0));
        entries.add(new PieEntry(4f, 1));
        entries.add(new PieEntry(6f, 2));
        entries.add(new PieEntry(8f, 3));
        entries.add(new PieEntry(7f, 4));
        entries.add(new PieEntry(3f, 5));

    }

    public void AddValuesToPieEntryLabels(){

        PieEntryLabels.add("January");
        PieEntryLabels.add("February");
        PieEntryLabels.add("March");
        PieEntryLabels.add("April");
        PieEntryLabels.add("May");
        PieEntryLabels.add("June");

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

