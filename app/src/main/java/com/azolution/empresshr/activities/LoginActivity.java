package com.azolution.empresshr.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.azolution.empresshr.R;
import com.azolution.empresshr.model.TokenResponse;
import com.azolution.empresshr.network.ApiClient;
import com.azolution.empresshr.network.EmployeeApi;
import com.azolution.empresshr.utils.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 10;
    //------------xml instance--------------
    private TextView switchRegisterActivityHintText;
    private EditText employeeIdET,passwordET;

    //-------------class instance-------------------
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        requestSelfPermission();


        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("verifying...");




        //--------------check ser already logged in or not?-------------
        checkUserLoggedInStatus();

        //-------------initialize all xml field--------------------
        initializeXMLField();

        //--------------set different color----------------------
        setSwitchRegisterActivityHintTextColor();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                //----------phone state permission result---------------
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED
                        && grantResults[4] == PackageManager.PERMISSION_GRANTED) {



                } else {
                    requestSelfPermission();
                }

            }
            break;
        }
    }

    private void requestSelfPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
    }

    private void checkUserLoggedInStatus() {
        SharedPreferences userLoggedInPref = getSharedPreferences(Util.EMPLOYEE_LOGGEDIN_PREF,Context.MODE_PRIVATE);
        String status = userLoggedInPref.getString(Util.EMPLOYEE_LOGGEDIN_STATUS,"No");
        if (status.equals("Yes")){
            launchMainActivity();
        }
    }

    private void launchMainActivity() {
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        finish();
    }

    private void initializeXMLField() {
        switchRegisterActivityHintText = findViewById(R.id.login_activity_switchLogin_activity_text);
        employeeIdET = findViewById(R.id.login_activity_employee_id);
        passwordET = findViewById(R.id.login_activity_employee_account_password);
    }

    private void setSwitchRegisterActivityHintTextColor() {
        Spannable hint1 = new SpannableString("Don't have account? ");
        hint1.setSpan(new ForegroundColorSpan(Color.GRAY), 0, hint1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        switchRegisterActivityHintText.setText(hint1);

        Spannable hint2 = new SpannableString("Register Now");
        hint2.setSpan(new ForegroundColorSpan(Color.GREEN), 0, hint2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        switchRegisterActivityHintText.append(hint2);

    }

    //-----------launch register activity----------------------
    public void gotoRegisterActivity(View view) {
        startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
    }

    //------------login the user----------------
    public void loginNow(View view) {
        if (employeeIdET.getText().toString().isEmpty()){
            employeeIdET.setError("please enter you employee id");
            return;
        }
        
        if (passwordET.getText().toString().isEmpty()){
            passwordET.setError("please enter you password");
            return;
        }

        checkUserCredential();
    }

    private void checkUserCredential() {
        // todo call api
        progressDialog.show();

        ApiClient.resetApiClient();
        Call<TokenResponse> call = ApiClient.getClient(Util.BASE_URL,"").create(EmployeeApi.class).requestForUserLoggedIn("password",employeeIdET.getText().toString(),passwordET.getText().toString());
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<TokenResponse> call, @NonNull Response<TokenResponse> response) {
                if (response.isSuccessful()){
                    SharedPreferences.Editor editor = getSharedPreferences(Util.EMPLOYEE_LOGGEDIN_PREF,Context.MODE_PRIVATE).edit();
                    editor.putString(Util.EMPLOYEE_LOGGEDIN_STATUS,"Yes");
                    editor.putString(Util.EMPLOYEE_ID,employeeIdET.getText().toString());
                    editor.putString(Util.AUTH_TOKEN,response.body().getToken());
                    editor.apply();

                    SharedPreferences.Editor editor1 = getSharedPreferences(Util.EMPLOYEE_REGISTRATION_PREF,Context.MODE_PRIVATE).edit();
                    editor1.putString(Util.EMPLOYEE_REGISTRATION_STATUS,"Yes");
                    editor1.apply();

                    progressDialog.dismiss();
                    employeeIdET.setText("");
                    passwordET.setText("");
                    launchMainActivity();
                    Toast.makeText(getApplicationContext(),"Login successfully done",Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Face Identity registration is not found",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TokenResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }
}
