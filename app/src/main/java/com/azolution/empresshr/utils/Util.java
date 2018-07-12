package com.azolution.empresshr.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Util {

    public static final String BASE_URL = "http://192.168.1.210:5000/";

    public static final String EMPLOYEE_REGISTRATION_PREF = "EMPLOYEE_REGISTRATION_PREF";
    public static final String EMPLOYEE_REGISTRATION_STATUS = "EMPLOYEE_REGISTRATION_STATUS";

    public static final String EMPLOYEE_LOGGEDIN_PREF = "EMPLOYEE_LOGGEDIN_PREF";
    public static final String EMPLOYEE_LOGGEDIN_STATUS = "EMPLOYEE_LOGGEDIN_STATUS";
    public static final String EMPLOYEE_ID = "EMPLOYEE_ID";
    public static final String EMPLOYEE_NAME = "EMPLOYEE_NAME";
    public static final String AUTH_TOKEN = "AUTH_TOKEN";
    public static final String EMPLOYEE_PROFILE_IMAGE = "EMPLOYEE_PROFILE_IMAGE";


    //----------check the mobile have internet connection or not?----------------
    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    //------------check the user mobile has gps enable or not?--------------
    public static boolean isGPSEnabled (Context mContext){
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    //-------------convert image into base 64------------------------
    public static String convertBase64Image(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    //-----------get deveive IMEI--------------------
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getIMEI(Context activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity
                .getSystemService(Context.TELEPHONY_SERVICE);
        assert telephonyManager != null;
        return telephonyManager.getDeviceId();
    }

    //-----------get current date-------------
    public static String getCurrentDate(){
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        return df.format(date);
    }

    public static String getCurrentDateForServer(){
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        return df.format(date);
    }

    public static String getAttendanceDateForServer(){
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        return df.format(date);
    }

    public static String getCurrentMonth(){
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("M", Locale.getDefault());
        return df.format(date);
    }

    public static String getCurrentYear(){
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy", Locale.getDefault());
        return df.format(date);
    }

    public static Bitmap decodeBase64ToBitmap(String imageString){
        byte[]imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }



}
