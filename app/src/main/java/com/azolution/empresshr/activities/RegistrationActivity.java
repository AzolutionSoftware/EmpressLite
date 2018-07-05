package com.azolution.empresshr.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.azolution.empresshr.R;
import com.azolution.empresshr.model.ApiResponseMessage;
import com.azolution.empresshr.model.RegisterUser;
import com.azolution.empresshr.network.ApiClient;
import com.azolution.empresshr.network.EmployeeApi;
import com.azolution.empresshr.utils.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //------------xml instance----------
    private ScrollView scrollView;
    private EditText employeeIdET, employeePasswordET;
    private CircleImageView profileImage;
    private TextView switchLoginActivityHintText;

    //----------class instance--------------
    private GoogleApiClient googleApiClient;
    private String deviceIMEINumber;
    private Bitmap profileImageBitmap;

    private ProgressDialog progressDialog;

    private final static int REQUEST_PERMISSION = 10;
    private final static int REQUEST_CAMERA_PERMISSION = 30;
    private final int PICK_IMAGE_CAMERA = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        progressDialog = new ProgressDialog(RegistrationActivity.this);
        progressDialog.setMessage("registering...");

        requestSelfPermission();
        //----------initialize xml fields-------------
        initializeXMLField();

        //------------hide scrollview scroll bar---------------
        hideScrollViewScrollBar();
        //--------------set different color----------------------
        setSwitchLoginActivityHintTextColor();


    }

    //----------set profile image from gallery-------------------
    public void addProfileImage(View view) {
        selectImage();
    }


    //----------when user click register button--------------------------
    @SuppressLint("HardwareIds")
    public void createNewEmployee(View view) {
        //-----------check  permission granted or not?------------------
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
           if (getIMEI(RegistrationActivity.this).equals("")){
               deviceIMEINumber= "secureIMEI";
           }else{
                deviceIMEINumber = getIMEI(RegistrationActivity.this);
            }

            //-----------check internet connection------------------
            if (Util.haveNetworkConnection(getApplicationContext())){
                //----------check gps connection----------------
                if (Util.isGPSEnabled(getApplicationContext())){
                    if(profileImageBitmap == null){
                        showErrorDialog(RegistrationActivity.this,"please add profile image","profileImage");
                        return;
                    }

                   if (employeeIdET.getText().toString().isEmpty()){
                       employeeIdET.setError("please enter your employee id");
                       return;
                   }

                   if(employeePasswordET.getText().toString().isEmpty()){
                       employeePasswordET.setError("please enter your password");
                       return;
                   }

                   buildGoogleApiClient();

                }else {
                    showErrorDialog(RegistrationActivity.this,"Please enable your GPS","gps");
                }
            }else {
                showErrorDialog(RegistrationActivity.this,"Please make sure your internet connection","network");
            }

        }else {
            requestSelfPermission();
        }
    }

    private void initializeXMLField() {
        profileImage = findViewById(R.id.register_activity_profile_image);
        scrollView = findViewById(R.id.register_activity_scrollview);
        employeeIdET = findViewById(R.id.register_activity_employee_id);
        employeePasswordET = findViewById(R.id.register_activity_employee_account_password);
        switchLoginActivityHintText = findViewById(R.id.register_activity_switchLogin_activity_text);
    }

    private void requestSelfPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION);
    }

    private void hideScrollViewScrollBar() {
        scrollView.setVerticalScrollBarEnabled(false);
    }

    private void setSwitchLoginActivityHintTextColor() {
        Spannable hint1 = new SpannableString("Already have account? ");
        hint1.setSpan(new ForegroundColorSpan(Color.GRAY), 0, hint1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        switchLoginActivityHintText.setText(hint1);

        Spannable hint2 = new SpannableString("Login Now");
        hint2.setSpan(new ForegroundColorSpan(Color.GREEN), 0, hint2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        switchLoginActivityHintText.append(hint2);
    }


    private void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        googleApiClient.connect();
    }
    // Select image from camera and gallery
    private void selectImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, PICK_IMAGE_CAMERA);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {
            profileImageBitmap = (Bitmap) data.getExtras().get("data");
            profileImage.setImageBitmap(profileImageBitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                //----------phone state permission result---------------
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("PERMISSION","PERMISSION GRANTED");
                } else {
                    requestSelfPermission();
                }
                //-----------location permission result------------
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("PERMISSION","PERMISSION GRANTED");
                } else {
                    requestSelfPermission();
                }
            }
            break;

            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   selectImage();
                }
                break;

                default:
                    break;
        }
    }
    //-----------get deveive IMEI--------------------
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public String getIMEI(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity
                .getSystemService(Context.TELEPHONY_SERVICE);
        assert telephonyManager != null;
        return telephonyManager.getDeviceId();
    }

    //-------------show dialog--------------
    private void showErrorDialog(Context context, String message, final String errorType){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (errorType.equals("network")){
                            dialog.dismiss();
                        }else if (errorType.equals("gps")){
                            Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(gpsOptionsIntent);
                            dialog.dismiss();
                        }else if (errorType.equals("profileImage")){
                            dialog.dismiss();
                        }
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
               if (location != null){
                   registerNewEmployee(location.getLatitude(), location.getLongitude());
               }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("error",e.getLocalizedMessage());
            }
        });
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
    }

    //-------------finally register the user---------------------
    private void registerNewEmployee(double lat, double lng) {
        progressDialog.show();
        ApiClient.resetApiClient();
        String imageData = Util.convertBase64Image(profileImageBitmap);
        Log.v("IMAGEDATA",imageData);
        if(imageData != null){
            Call<ApiResponseMessage> call = ApiClient.getClient(Util.BASE_URL,"").create(EmployeeApi.class).registerUser(new RegisterUser(employeeIdET.getText().toString(),String.valueOf(lat),String.valueOf(lng),deviceIMEINumber,employeePasswordET.getText().toString(),imageData));
            call.enqueue(new Callback<ApiResponseMessage>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponseMessage> call, @NonNull Response<ApiResponseMessage> response) {

                    if(response.isSuccessful()){
                        if (response.body().getMessage().equals("Success")){
                            SharedPreferences.Editor editor = getSharedPreferences(Util.EMPLOYEE_REGISTRATION_PREF,Context.MODE_PRIVATE).edit();
                            editor.putString(Util.EMPLOYEE_REGISTRATION_STATUS,"Yes");
                            editor.apply();

                            progressDialog.dismiss();
                            employeePasswordET.setText("");
                            employeeIdET.setText("");

                            profileImage.setImageResource(R.drawable.default_camera);
                            profileImageBitmap = null;
                            startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                            finish();
                            Toast.makeText(getApplicationContext(),"Registration successfully completed",Toast.LENGTH_SHORT).show();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponseMessage> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void gotoLoginActivity(View view) {
        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
        finish();
    }
}
