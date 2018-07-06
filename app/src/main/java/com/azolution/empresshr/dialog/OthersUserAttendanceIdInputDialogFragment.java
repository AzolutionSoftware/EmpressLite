package com.azolution.empresshr.dialog;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.azolution.empresshr.R;
import com.azolution.empresshr.activities.FaceRecognitionActivity;
import com.azolution.empresshr.model.EmployeeInformation;
import com.azolution.empresshr.network.ApiClient;
import com.azolution.empresshr.network.EmployeeApi;
import com.azolution.empresshr.utils.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OthersUserAttendanceIdInputDialogFragment extends DialogFragment {

    private  EditText userIdText;
    public OthersUserAttendanceIdInputDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressLint("InflateParams") final View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_others_user_attendance_id_input_dialog, null);
        assert getActivity()!=null;

        userIdText = view.findViewById(R.id.other_user_attendance_dialog_employeeId);
        Button verifyButton = view.findViewById(R.id.other_user_attendance_dialog_verifyButton);

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboardInAndroidFragment(view);
                String userId = userIdText.getText().toString();
                if (!userId.equals("")){
                    getEmployeeInformation(userId);
                }else {
                    userIdText.setError("enter an employee id");
                }
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setCancelable(true)
                .show();
    }

    private void getEmployeeInformation(String employeeId){
        SharedPreferences preferences = getActivity().getSharedPreferences(Util.EMPLOYEE_LOGGEDIN_PREF,0);
        String token = preferences.getString(Util.AUTH_TOKEN,"");
        ApiClient.resetApiClient();
        Call<EmployeeInformation> call = ApiClient.getClient(Util.BASE_URL,token).create(EmployeeApi.class).getEmployeeInformation(employeeId);
        call.enqueue(new Callback<EmployeeInformation>() {
            @Override
            public void onResponse(@NonNull Call<EmployeeInformation> call, @NonNull Response<EmployeeInformation> response) {
                if (response.isSuccessful()){

                    if (response.body().getEmployeeName() != null){
                        dismiss();
                        String employeeName = response.body().getEmployeeName();
                        String employeeId = response.body().getEmployeeId();
                        assert getActivity() != null;
                        if (Util.isGPSEnabled(getActivity())){
                            getActivity().startActivity(new Intent(getActivity(), FaceRecognitionActivity.class)
                                    .putExtra("employeeName",employeeName)
                                    .putExtra("employeeId",employeeId)
                                    .putExtra("employeeProfileImage","")


                            );
                        }else {
                            Toast.makeText(getActivity(),"please enable your gps",Toast.LENGTH_SHORT).show();

                        }
                    }else {
                        Toast.makeText(getActivity(),"employee not found",Toast.LENGTH_SHORT).show();
                    }

                }
            }
            @Override
            public void onFailure(@NonNull Call<EmployeeInformation> call, @NonNull Throwable t) {
                Log.v("ERROR",t.getMessage());
            }
        });
    }

    public static void hideKeyboardInAndroidFragment(View view){
        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
