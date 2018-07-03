package com.azolution.empresshr.backgroundservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.azolution.empresshr.activities.LoginActivity;

public class ReceiverCall extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intentone = new Intent(context.getApplicationContext(), LoginActivity.class);
            intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentone);
        } else {
            Intent service = new Intent(context, SendLocationService.class);
            context.startService(service);
        }


    }
}
