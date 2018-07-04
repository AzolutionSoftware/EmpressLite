package com.azolution.empresshr.backgroundservice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.azolution.empresshr.model.EmployeeLocationTrack;
import com.azolution.empresshr.network.ApiClient;
import com.azolution.empresshr.network.EmployeeApi;
import com.azolution.empresshr.utils.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendLocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private GoogleApiClient googleApiClient;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timer mTimer = new Timer();
        mTimer.schedule(timerTask, 6000, 6 * 1000);

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
                            if (Util.isGPSEnabled(SendLocationService.this.getApplicationContext())) {
                                buildGoogleApiClient();
                            }
                        }
                    });
                }
            });

        }
    };

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(SendLocationService.this.getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        googleApiClient.connect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);

        }else {
            Log.v("PERMISSION","Permission problem");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {
        googleApiClient.disconnect();
        if (location != null){
            String latitude = String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());
            String imei = Util.getIMEI(SendLocationService.this.getApplicationContext());
            String id = "";
            String authToken = "";
            String date = Util.getCurrentDate();
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
}
