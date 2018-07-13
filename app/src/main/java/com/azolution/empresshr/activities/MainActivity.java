package com.azolution.empresshr.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.azolution.empresshr.R;
import com.azolution.empresshr.backgroundservice.SendLocationService;
import com.azolution.empresshr.dialog.OthersUserAttendanceIdInputDialogFragment;
import com.azolution.empresshr.network.EmployeeApi;
import com.azolution.empresshr.model.EmployeeInformation;
import com.azolution.empresshr.network.ApiClient;
import com.azolution.empresshr.utils.Util;
import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.FileUtils;


import java.io.ByteArrayOutputStream;
import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //-----------xml instance----------------
    private CircleImageView navProfileImage;
    private TextView navUserName, navUserId;
    public String authHeader;

    //----------class instance-------------
    private SharedPreferences loggedUserPref;
    private String employeeId, employeeName;
    private Bitmap profileImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent service = new Intent(getApplicationContext(), SendLocationService.class);
        //startService(service);
        initializeXMLField();
       new initRecAsync().execute();

        //-----------get logged in user id------------------------------
        loggedUserPref = getSharedPreferences(Util.EMPLOYEE_LOGGEDIN_PREF, Context.MODE_PRIVATE);
        employeeId = loggedUserPref.getString(Util.EMPLOYEE_ID, "123456");
        employeeName = loggedUserPref.getString(Util.EMPLOYEE_NAME, "");
        authHeader = loggedUserPref.getString(Util.AUTH_TOKEN, "");
        navUserId.setText(employeeId);

        Log.v("INFOM", employeeId + authHeader);

        getEmployeeInformation();
        getEmployeeProfileImage();
    }

    private void initializeXMLField() {

        //-----------action bar------------
        Toolbar toolbar = findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Workplace");
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        View hView = navigationView.getHeaderView(0);
        navProfileImage = hView.findViewById(R.id.nav_user_profile_image);
        navUserName = hView.findViewById(R.id.nav_user_profile_name);
        navUserId = hView.findViewById(R.id.nav_user_profile_id);

        navProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,EmployeeInformationActivity.class));
            }
        });

    }

    private void getEmployeeInformation() {
        if (!loggedUserPref.getString(Util.EMPLOYEE_NAME, "").equals("")) {
            navUserName.setText(employeeName);
        } else {
            ApiClient.resetApiClient();
            Call<EmployeeInformation> call = ApiClient.getClient(Util.BASE_URL, authHeader).create(EmployeeApi.class).getEmployeeInformation(employeeId);
            call.enqueue(new Callback<EmployeeInformation>() {
                @Override
                public void onResponse(@NonNull Call<EmployeeInformation> call, @NonNull Response<EmployeeInformation> response) {
                    if (response.isSuccessful()) {
                       if (response.body() !=null){
                           employeeName = response.body().getEmployeeName();
                           navUserName.setText(employeeName);
                           SharedPreferences.Editor editor = loggedUserPref.edit();
                           editor.putString(Util.EMPLOYEE_NAME, employeeName);
                           editor.apply();
                       }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<EmployeeInformation> call, @NonNull Throwable t) {
                    Log.v("ERROR", t.getMessage());
                }
            });
        }

    }

    private void getEmployeeProfileImage() {
        if (!loggedUserPref.getString(Util.EMPLOYEE_PROFILE_IMAGE, "").equals("")) {
            String imageString = loggedUserPref.getString(Util.EMPLOYEE_PROFILE_IMAGE, "");
            profileImageBitmap = decodeBase64ToBitmap(imageString);
            navProfileImage.setImageBitmap(profileImageBitmap);
        } else {
            ApiClient.resetApiClient();
            Call<ResponseBody> call = ApiClient.getClient(Util.BASE_URL, authHeader).create(EmployeeApi.class).getUserProfilePicture(employeeId);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        profileImageBitmap = BitmapFactory.decodeStream(response.body().byteStream());
                        navProfileImage.setImageBitmap(profileImageBitmap);

                        String base64Image = convertBase64Image(profileImageBitmap);
                        SharedPreferences.Editor editor = loggedUserPref.edit();
                        editor.putString(Util.EMPLOYEE_PROFILE_IMAGE, base64Image);
                        editor.apply();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.v("ERROR", t.getMessage());
                }
            });
        }

    }

    //-------------convert image into base 64------------------------
    private String convertBase64Image(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private Bitmap decodeBase64ToBitmap(String imageString) {
        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            showLogOutDialog();
        }else if (id == R.id.action_attendance){
            showEmployeeAttendanceType();
        }else if (id == R.id.action_attendance_history){
            startActivity(new Intent(this, AttendenceHistoryActivity.class));
        }else if (id == R.id.action_attendance_adjustment){
            startActivity(new Intent(MainActivity.this,AttendanceAdjustmentActivity.class));
        }else if (id == R.id.action_leave_application){
            startActivity(new Intent(MainActivity.this, LeaveApplicationGraphActivity.class));
        }else if (id == R.id.action_employee){
            startActivity(new Intent(MainActivity.this, EmployeeInformationActivity.class));
        }else if (id == R.id.action_rootPlan){
            startActivity(new Intent(MainActivity.this,RoutePlanActivity.class));
        }




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Logout!")
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
              /* Intent service = new Intent(getApplicationContext(), SendLocationService.class);
               stopService(service);*/
                        loggedUserPref.edit().clear().apply();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    /**
     * ------------employee attendance-------------
     **/
    public void employeeAttendance(View view) {
        showEmployeeAttendanceType();
    }

    private void showEmployeeAttendanceType(){
        AlertDialog.Builder attendanceDialog = new AlertDialog.Builder(MainActivity.this)
                .setMessage("Please select Attendance option")
                .setPositiveButton("Others", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        DialogFragment dialogFragment = new OthersUserAttendanceIdInputDialogFragment();
                        dialogFragment.show(getSupportFragmentManager(), MainActivity.class.getSimpleName());
                    }
                })
                .setNegativeButton("My Attendance", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (employeeName != null) {
                            if (Util.isGPSEnabled(MainActivity.this)) {
                                startActivity(new Intent(MainActivity.this, FaceRecognitionActivity.class)
                                        .putExtra("employeeName", employeeName)
                                        .putExtra("employeeId", employeeId)
                                        .putExtra("employeeProfileImage", convertBase64Image(profileImageBitmap))

                                );
                            } else {
                                Toast.makeText(getApplicationContext(), "please enable your gps", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });

        Dialog dialog = attendanceDialog.create();
        dialog.show();
    }

    public void attendanceHistory(View view) {
        startActivity(new Intent(this, AttendenceHistoryActivity.class));
    }

    //---------leave application------------
    public void gotoLeaveApplicationActivity(View view) {
        startActivity(new Intent(MainActivity.this, LeaveApplicationGraphActivity.class));
    }

    public void viewEmployeeProfile(View view) {
        startActivity(new Intent(MainActivity.this, EmployeeInformationActivity.class));
    }

    public void attendanceAdjustment(View view) {
        startActivity(new Intent(MainActivity.this,AttendanceAdjustmentActivity.class));
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void loadEmpressPage(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("http://www.empresshr.com/"));
        startActivity(i);
    }




    @SuppressLint("StaticFieldLeak")
    private class initRecAsync extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Initializing...");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        protected Void doInBackground(Void... args) {
            File folder = new File(Constants.getDLibDirectoryPath());
            boolean success = false;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            if (success) {
                File image_folder = new File(Constants.getDLibImageDirectoryPath());
                image_folder.mkdirs();
                if (!new File(Constants.getFaceShapeModelPath()).exists()) {
                    FileUtils.copyFileFromRawToOthers(MainActivity.this, R.raw.shape_predictor_5_face_landmarks, Constants.getFaceShapeModelPath());
                }
                if (!new File(Constants.getFaceDescriptorModelPath()).exists()) {
                    FileUtils.copyFileFromRawToOthers(MainActivity.this, R.raw.dlib_face_recognition_resnet_model_v1, Constants.getFaceDescriptorModelPath());
                }
            } else {
                Log.d("error", "some error occur");
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }

    public void routePlan(View view) {
        startActivity(new Intent(this, RoutePlanActivity.class));
    }
}