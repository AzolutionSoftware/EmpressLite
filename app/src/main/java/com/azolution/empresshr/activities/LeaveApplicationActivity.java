package com.azolution.empresshr.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azolution.empresshr.R;
import com.azolution.empresshr.adepter.LeaveTypeAdepter;
import com.azolution.empresshr.model.ApiResponseMessage;
import com.azolution.empresshr.model.LeaveApplication;
import com.azolution.empresshr.model.LeaveType;
import com.azolution.empresshr.network.ApiClient;
import com.azolution.empresshr.network.EmployeeApi;
import com.azolution.empresshr.utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveApplicationActivity extends AppCompatActivity implements View.OnClickListener{

    //----------xml instance------------------
    private EditText leaveStartDateText,leaveEndDateText,leaveReasonText;
    private TextView employeeLeaveTypeText;
    private RelativeLayout rootLayout;
    private RecyclerView popUpRecyclerView;

    //-------------class instance------------
    private SharedPreferences userPref;
    private String employeeId,authToken,profileName,profileImageString;
    private LeaveTypeAdepter adepter;
    private CircleImageView profileImageView;
    private TextView nameText,idText,currentDateText;

    private String leaveType;
    private Integer leaveTypeId;
    private int mYear, mMonth, mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        userPref = getSharedPreferences(Util.EMPLOYEE_LOGGEDIN_PREF,0);
        employeeId = userPref.getString(Util.EMPLOYEE_ID,"");
        authToken = userPref.getString(Util.AUTH_TOKEN,"");
        profileName = userPref.getString(Util.EMPLOYEE_NAME,"");
        profileImageString = userPref.getString(Util.EMPLOYEE_PROFILE_IMAGE,"");

        //----------action bar-------------
        Toolbar toolbar = findViewById(R.id.leave_application_activity_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Leave Application");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initializeXMLField();


        //----------start date----------
        leaveStartDateText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showDatePicker(v.getId());
            }
        });

        leaveStartDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v.getId());
            }
        });

        //---------end date---------------
        leaveEndDateText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showDatePicker(v.getId());
            }
        });

        leaveEndDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v.getId());
            }
        });

        //----------- set user info------------
        nameText.setText(profileName);
        idText.setText(employeeId);
        currentDateText.setText(Util.getCurrentDate());
        profileImageView.setImageBitmap(Util.decodeBase64ToBitmap(profileImageString));
    }

    private void initializeXMLField() {
        leaveStartDateText = findViewById(R.id.leave_application_activity_leaveStartDate);
        leaveEndDateText = findViewById(R.id.leave_application_activity_leaveEndDate);
        leaveReasonText = findViewById(R.id.leave_application_activity_leaveReason);
        employeeLeaveTypeText = findViewById(R.id.leave_application_activity_leaveTypeText);
        rootLayout = findViewById(R.id.leave_application_activity_rootLayout);
        idText = findViewById(R.id.leave_application_activity_profileId);
        nameText = findViewById(R.id.leave_application_activity_profileName);
        currentDateText = findViewById(R.id.leave_application_activity_currentDate);
        profileImageView = findViewById(R.id.leave_application_activity_profileImage);
    }

    private void showDatePicker(final int id){
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (id == R.id.leave_application_activity_leaveStartDate){
                            leaveStartDateText.setText((monthOfYear +1) + "/"+ dayOfMonth +"/" +year);
                        }else if (id == R.id.leave_application_activity_leaveEndDate){
                            leaveEndDateText.setText((monthOfYear +1) + "/"+ dayOfMonth +"/" +year);
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }


    public void apply(View view) {
        String leaveStartDate = leaveStartDateText.getText().toString();
        String leaveEndDate = leaveEndDateText.getText().toString();
        String leaveReason = leaveReasonText.getText().toString();
        String employeeLeaveType = employeeLeaveTypeText.getText().toString();
        if (employeeLeaveType.equals("Select Leave Type")){
            Toast.makeText(getApplicationContext(),"Please select leave type",Toast.LENGTH_SHORT).show();
            return;
        }

        if (leaveStartDate.isEmpty()){
            leaveStartDateText.setError("require start date");
            return;
        }

        if (leaveEndDate.isEmpty()){
            leaveEndDateText.setError("require end date");
            return;
        }

        if (leaveReason.isEmpty()){
            leaveReasonText.setError("require leave reason");
            return;
        }

        ApiClient.resetApiClient();
        Call<ApiResponseMessage> leaveApplicationCall = ApiClient.getClient(Util.BASE_URL,authToken).create(EmployeeApi.class).sendLeaveApplication(new LeaveApplication(employeeId,leaveTypeId,leaveStartDate,leaveEndDate,leaveReason));
        leaveApplicationCall.enqueue(new Callback<ApiResponseMessage>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponseMessage> call, @NonNull Response<ApiResponseMessage> response) {
                if (response.isSuccessful()){
                    leaveStartDateText.setText("");
                    leaveEndDateText.setText("");
                    leaveReasonText.setText("");
                    employeeLeaveTypeText.setText("Select Leave Type");
                    Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();

                }else {
                    Log.v("ERRORMESSAGE",response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponseMessage> call, @NonNull Throwable t) {

            }
        });


    }

    public void showLeaveType(View view) {
        showLeaveTypePopupOptionMenu();
    }

    private void showLeaveTypePopupOptionMenu() {
        @SuppressLint("InflateParams") final View popupView = getLayoutInflater().inflate(R.layout.leave_type_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);

        popUpRecyclerView = popupView.findViewById(R.id.leave_type_popup_recyclerView);
        popUpRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        Call<List<LeaveType>> leaveTypeCall = ApiClient.getClient(Util.BASE_URL,authToken).create(EmployeeApi.class).getLeaveTypeData(employeeId);
        leaveTypeCall.enqueue(new Callback<List<LeaveType>>() {
            @Override
            public void onResponse(@NonNull Call<List<LeaveType>> call, @NonNull Response<List<LeaveType>> response) {
                if (response.isSuccessful()){
                    adepter = new LeaveTypeAdepter(response.body(),getApplicationContext());
                    popUpRecyclerView.setAdapter(adepter);
                    adepter.notifyDataSetChanged();

                    adepter.setOnItemClickListener(new LeaveTypeAdepter.onRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClickListener(String type, Integer id) {
                            leaveType = type;
                            leaveTypeId = id;
                            employeeLeaveTypeText.setText(leaveType);
                            popupWindow.dismiss();
                        }
                    });

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<LeaveType>> call, @NonNull Throwable t) {

            }
        });


        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        // Get the View's(the one that was clicked in the Fragment) location
        int location[] = new int[2];
        rootLayout.setBackgroundColor(Color.parseColor("#CC000000"));
        employeeLeaveTypeText.getLocationOnScreen(location);
        // Using location, the PopupWindow will be displayed right under anchorVie+
        popupWindow.showAtLocation(employeeLeaveTypeText, Gravity.NO_GRAVITY, location[0] + 130, location[1] + 60);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rootLayout.setBackgroundResource(R.drawable.bg_image);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_leave_application,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_menu_leave_application_history){
            startActivity(new Intent(LeaveApplicationActivity.this,LeaveApplicationHistoryActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }


}
