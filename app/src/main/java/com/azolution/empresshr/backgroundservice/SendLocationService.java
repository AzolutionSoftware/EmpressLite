package com.azolution.empresshr.backgroundservice;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;
import com.azolution.empresshr.model.EmployeeLocationTrack;
import com.azolution.empresshr.network.ApiClient;
import com.azolution.empresshr.network.EmployeeApi;
import com.azolution.empresshr.utils.Util;
import java.util.Timer;
import java.util.TimerTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendLocationService extends Service{
    private static final int REQUEST_LOCATION = 10;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timer mTimer = new Timer();
        mTimer.schedule(timerTask, 3000, 3 * 1000);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stopForeground(false);
        return super.onStartCommand(intent, flags, startId);
    }

    TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            if (Util.haveNetworkConnection(SendLocationService.this.getApplicationContext())){
                                if (Util.isGPSEnabled(SendLocationService.this.getApplicationContext())) {

                                    SharedPreferences sharedPreferences = getSharedPreferences(Util.EMPLOYEE_LOGGEDIN_PREF,0);
                                    if (!sharedPreferences.getString(Util.EMPLOYEE_ID,"").isEmpty()){
                                        getLocation();
                                    }
                                }else {
                                    Toast.makeText(SendLocationService.this.getApplicationContext(),"Please enable your gps",Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(SendLocationService.this.getApplicationContext(),"Please enable your internet connection",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            });

        }
    };


    private void getLocation() {
        LocationManager locationManager = (LocationManager) SendLocationService.this.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(SendLocationService.this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (SendLocationService.this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) SendLocationService.this.getApplicationContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            if (locationManager != null){
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

                if (location != null) {
                    double latti = location.getLatitude();
                    double longi = location.getLongitude();
                    sendLocationDataOn(String.valueOf(latti),String.valueOf(longi));


                } else  if (location1 != null) {
                    double latti = location1.getLatitude();
                    double longi = location1.getLongitude();
                    sendLocationDataOn(String.valueOf(latti),String.valueOf(longi));

                } else  if (location2 != null) {
                    double latti = location2.getLatitude();
                    double longi = location2.getLongitude();
                    sendLocationDataOn(String.valueOf(latti),String.valueOf(longi));

                }else {
                    Toast.makeText(SendLocationService.this.getApplicationContext(),"location not found. Please reboot your device",Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    private void sendLocationDataOn(final String latitude, final String longitude){
        String imei;
        if (Util.getIMEI(SendLocationService.this.getApplicationContext()).equals("")){
            imei = "secureIMEI";
        }else {
            imei = Util.getIMEI(SendLocationService.this.getApplicationContext());
        }
        String id = "";
        String authToken = "";
        String date = Util.getCurrentDateForServer();
        SharedPreferences sharedPreferences = getSharedPreferences(Util.EMPLOYEE_LOGGEDIN_PREF,0);
        if (sharedPreferences != null){
            id = sharedPreferences.getString(Util.EMPLOYEE_ID,"");
            authToken = sharedPreferences.getString(Util.AUTH_TOKEN,"");

        }
        Call<Void> locationSendCall = ApiClient.getClient(Util.BASE_URL,authToken).create(EmployeeApi.class).sendEmployeeTrackData(new EmployeeLocationTrack(id,latitude,longitude,date,imei));
        locationSendCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
            }
        });
    }


}
