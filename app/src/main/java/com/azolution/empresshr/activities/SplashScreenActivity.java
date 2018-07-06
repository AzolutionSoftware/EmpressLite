package com.azolution.empresshr.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.azolution.empresshr.R;
import com.azolution.empresshr.utils.Util;

public class SplashScreenActivity extends AppCompatActivity {

    private SharedPreferences userRegistrationPref;
    private static final int REQUEST_PERMISSION = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        requestSelfPermission();


      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                userRegistrationPref = getSharedPreferences(Util.EMPLOYEE_REGISTRATION_PREF, Context.MODE_PRIVATE);
                if (userRegistrationPref != null){
                    String status = userRegistrationPref.getString(Util.EMPLOYEE_REGISTRATION_STATUS,"No");
                    if (status.equals("No")){
                        startActivity(new Intent(SplashScreenActivity.this,RegistrationActivity.class));
                        finish();
                    }else if (status.equals("Yes")){
                        startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
                        finish();
                    }
                }

            }
        },SPLASH_DURATION);*/
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

                    userRegistrationPref = getSharedPreferences(Util.EMPLOYEE_REGISTRATION_PREF, Context.MODE_PRIVATE);
                    if (userRegistrationPref != null){

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                String status = userRegistrationPref.getString(Util.EMPLOYEE_REGISTRATION_STATUS,"No");
                                if (status.equals("No")){
                                    startActivity(new Intent(SplashScreenActivity.this,RegistrationActivity.class));
                                    finish();
                                }else if (status.equals("Yes")){
                                    startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
                                    finish();
                                }
                            }
                        },2000);


                    }

                } else {
                    requestSelfPermission();
                }

            }
            break;
        }
    }

    private void requestSelfPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
    }
}
