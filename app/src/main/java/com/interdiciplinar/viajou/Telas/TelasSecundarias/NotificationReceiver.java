package com.interdiciplinar.viajou.Telas.TelasSecundarias;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Handle the notificatiom click here
        Log.e("NOTIFICATION", "Notificação enviada");
    }
}

