package com.example.temperaturevio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.core.content.ContextCompat;

public class AlarmReceiver extends BroadcastReceiver {

    public static final int TYPE_TEMPERATURAS = 998810;
    public static final int TYPE_EXPOSICION = 998820;
    public static final int TYPE_HIDRATACION = 998830;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        if(preferences.getBoolean("iniciado", false)){
            String data = intent.getDataString();
            int tipo;
            if (data != null && data.equals("custom://" + TYPE_TEMPERATURAS)) tipo = TYPE_TEMPERATURAS;
            else if (data != null && data.equals("custom://" + TYPE_EXPOSICION)) tipo = TYPE_EXPOSICION;
            else if (data != null && data.equals("custom://" + TYPE_HIDRATACION)) tipo = TYPE_HIDRATACION;
            else tipo = 0;
            Intent service1 = new Intent(context, NotificationService.class);
            service1.setData((Uri.parse("custom://" + System.currentTimeMillis())));
            service1.putExtra("tipo", tipo);
            ContextCompat.startForegroundService(context, service1);
        }

    }
}