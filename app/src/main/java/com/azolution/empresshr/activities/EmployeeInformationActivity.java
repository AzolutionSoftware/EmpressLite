package com.azolution.empresshr.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.azolution.empresshr.R;
import com.azolution.empresshr.model.EmployeeProfileInformation;
import com.azolution.empresshr.network.ApiClient;
import com.azolution.empresshr.network.EmployeeApi;
import com.azolution.empresshr.utils.Util;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeInformationActivity extends AppCompatActivity {

    //-----------xml instance----------------
    private CircleImageView employeeProfileImage;
    private TextView employeeIdText,employeeDesText,employeeNameText;
    private TextView companyName,branchName,departmentName,devisionName,RSMRegionName,RSMManager,salesManager,PSOLocationName;
    private TextView sectionName,facilityName,employmentDate,dateOfBirth;

    //--------------class instance-------------
    private SharedPreferences userInfoPref;
    private String authToken,employeeId,profileImageString,employeeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_information);

        initializeXMLField();

        userInfoPref = getSharedPreferences(Util.EMPLOYEE_LOGGEDIN_PREF,0);
        authToken = userInfoPref.getString(Util.AUTH_TOKEN,"");
        employeeId = userInfoPref.getString(Util.EMPLOYEE_ID,"");
        profileImageString = userInfoPref.getString(Util.EMPLOYEE_PROFILE_IMAGE,"");
        employeeName = userInfoPref.getString(Util.EMPLOYEE_NAME,"");

        //-------------action bar-------------
        Toolbar toolbar = findViewById(R.id.employee_information_activity_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("My Profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //---------show user basic info------------
        employeeProfileImage.setImageBitmap(Util.decodeBase64ToBitmap(profileImageString));
        employeeNameText.setText(employeeName);
        employeeIdText.setText(employeeId);




        getEmployeeProfileInformation();
    }

    private void initializeXMLField() {
        employeeProfileImage = findViewById(R.id.employee_information_activity_employeeImage);
        employeeIdText = findViewById(R.id.employee_information_activity_employeeId);
        employeeDesText = findViewById(R.id.employee_information_activity_employeeDes);
        employeeNameText = findViewById(R.id.employee_information_activity_employeeName);

        companyName = findViewById(R.id.employee_information_activity_companyName);
        branchName = findViewById(R.id.employee_information_activity_branchName);
        departmentName = findViewById(R.id.employee_information_activity_departmentName);
        devisionName = findViewById(R.id.employee_information_activity_divisionName);
        RSMRegionName = findViewById(R.id.employee_information_activity_PSMRegionName);
        RSMManager = findViewById(R.id.employee_information_activity_RSMManager);
        salesManager = findViewById(R.id.employee_information_activity_salesManager);
        employmentDate = findViewById(R.id.employee_information_activity_dateOfJoining);
        dateOfBirth = findViewById(R.id.employee_information_activity_dob);
        PSOLocationName = findViewById(R.id.employee_information_activity_PSOlocationName);
       /* PSOLocationName = findViewById(R.id.employee_information_activity_PSOlocationName);
        sectionName = findViewById(R.id.employee_information_activity_sectionName);
        facilityName = findViewById(R.id.employee_information_activity_facilityName);
        employmentDate = findViewById(R.id.employee_information_activity_employeeDateOfBirth);
        dateOfBirth = findViewById(R.id.employee_information_activity_employementDate);*/
    }

    private void getEmployeeProfileInformation() {
        ApiClient.resetApiClient();
        Call<EmployeeProfileInformation> infoCall = ApiClient.getClient(Util.BASE_URL,authToken).create(EmployeeApi.class).getEmployeeProfileInformation(employeeId);
        infoCall.enqueue(new Callback<EmployeeProfileInformation>() {
            @Override
            public void onResponse(@NonNull Call<EmployeeProfileInformation> call, @NonNull Response<EmployeeProfileInformation> response) {
              if (response.isSuccessful()){
                if (response.body() != null){
                    employeeDesText.setText(response.body().getDesignation());
                    companyName.setText(": "+response.body().getCompanyName());
                    branchName.setText(": "+response.body().getBranchName());
                    departmentName.setText(": "+response.body().getDepartmentName());
                    devisionName.setText(": "+response.body().getDevisionName());
                    RSMRegionName.setText(": "+response.body().getRSMRegionName());
                    RSMManager.setText(": "+response.body().getRSMManager());
                    salesManager.setText(": "+response.body().getSalesManager());
                    PSOLocationName.setText(": "+response.body().getPSOLocationName());
                    //sectionName.setText("Section Name : "+response.body().getSectionName());
                    //facilityName.setText("Facility Name : "+response.body().getFacilityName());
                    employmentDate.setText(": "+response.body().getEmploymentDate());
                    dateOfBirth.setText(": "+response.body().getDateOfBirth());
                }else {
                    Toast.makeText(getApplicationContext(),"Failed to fetch data from server",Toast.LENGTH_SHORT).show();
                }

              }else {
                  Toast.makeText(getApplicationContext(),"Something went wrong. Please try again",Toast.LENGTH_SHORT).show();
              }
            }

            @Override
            public void onFailure(@NonNull Call<EmployeeProfileInformation> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void loadEmpressPage(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("http://www.empresshr.com/"));
        startActivity(i);
    }
}
