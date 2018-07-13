package com.azolution.empresshr.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.azolution.empresshr.R;
import com.azolution.empresshr.model.AttendanceAdjustment;
import com.azolution.empresshr.network.ApiClient;
import com.azolution.empresshr.network.EmployeeApi;
import com.azolution.empresshr.utils.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceAdjustmentActivity extends AppCompatActivity {

    //-----------xnl instance-----------
    private EditText adjustmwntReasonText,adjustmentDateText;
    private Spinner spinner;
    private CircleImageView profileImageView;
    private TextView nameText,idText,currentDateText;



    //-------------class instance------------
    private SharedPreferences userPref;
    private String employeeId,authToken,profileName,profileImageString;
    private Date appliedDate, adjustmentDate;
    private SimpleDateFormat simpleDateFormat;
    private int mYear, mMonth, mDay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_adjustment);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        userPref = getSharedPreferences(Util.EMPLOYEE_LOGGEDIN_PREF,0);
        employeeId = userPref.getString(Util.EMPLOYEE_ID,"");
        authToken = userPref.getString(Util.AUTH_TOKEN,"");
        profileName = userPref.getString(Util.EMPLOYEE_NAME,"");
        profileImageString = userPref.getString(Util.EMPLOYEE_PROFILE_IMAGE,"");

        initializeXMLField();

        //----------action bar-------------
        Toolbar toolbar = findViewById(R.id.attendance_adjustment_activity_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initializeXMLField();

        setSpinner();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        adjustmentDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AttendanceAdjustmentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String showDate = String.valueOf(dayOfMonth + "/"+ (monthOfYear + 1) + "/"+year);
                                adjustmentDateText.setText(showDate);
                                String serverDate = String.valueOf((monthOfYear + 1) + "/"+ dayOfMonth + "/"+year);
                                SharedPreferences.Editor editor = userPref.edit();
                                editor.putString("date",serverDate);
                                editor.apply();



                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    private void setSpinner() {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("In\n\n");
        categories.add("Out\n\n");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    private void initializeXMLField() {
        adjustmwntReasonText = findViewById(R.id.attendance_adjustment_activity_reason);
        adjustmentDateText = findViewById(R.id.attendance_adjustment_activity_adjustmentDate);
        spinner = findViewById(R.id.attendance_adjustment_activity_spinner);
        profileImageView = findViewById(R.id.attendance_adjustment_activity_profileImage);
        nameText = findViewById(R.id.attendance_adjustment_profileName);
        idText = findViewById(R.id.attendance_adjustment_activity_profileId);
        currentDateText = findViewById(R.id.attendance_adjustment_activity_currentDate);

        currentDateText.setText(Util.getCurrentDate());
        idText.setText(employeeId);
        nameText.setText(profileName);
        profileImageView.setImageBitmap(Util.decodeBase64ToBitmap(profileImageString));

    }

    public void sendAttendanceAdjustment(View view) throws ParseException {
        TextView textView = (TextView)spinner.getSelectedView();
        String result = textView.getText().toString();
        if (result.isEmpty()){
            Toast.makeText(getApplicationContext(),"Select attendance adjustment type",Toast.LENGTH_SHORT).show();
            return;
        }

        if (adjustmentDateText.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Please select attendance adjustment date",Toast.LENGTH_SHORT).show();
            return;
        }

        if (adjustmwntReasonText.getText().toString().isEmpty()){
            adjustmwntReasonText.setError("need reason");
            return;
        }

        simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        appliedDate = simpleDateFormat.parse(Util.getCurrentDateForServer());

        simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        adjustmentDate = simpleDateFormat.parse(userPref.getString("date",Util.getCurrentDateForServer()));

        ApiClient.resetApiClient();
        Call<Void> call = ApiClient.getClient(Util.BASE_URL,authToken).create(EmployeeApi.class).requestForAttendanceAdjust(new AttendanceAdjustment(employeeId,adjustmentDate,appliedDate,adjustmwntReasonText.getText().toString()));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Successfully send",Toast.LENGTH_SHORT).show();
                    adjustmwntReasonText.setText("");
                    adjustmentDateText.setText("");

                }else {
                    Toast.makeText(getApplicationContext(),"Something went wrong. Please try again",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
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
