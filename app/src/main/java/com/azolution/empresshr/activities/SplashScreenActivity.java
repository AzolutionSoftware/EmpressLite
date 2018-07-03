package com.azolution.empresshr.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.azolution.empresshr.R;
import com.azolution.empresshr.utils.Util;

public class SplashScreenActivity extends AppCompatActivity {
    private  static  final int SPLASH_DURATION = 2000;
    private SharedPreferences userRegistrationPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
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
        },SPLASH_DURATION);
    }
}
