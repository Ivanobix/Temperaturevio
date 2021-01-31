package com.example.temperaturevio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.content.ContextCompat;

public class AlarmReceiver extends BroadcastReceiver {

    public static final int TYPE_TEMPERATURAS = 1;
    public static final int TYPE_EXPOSICION = 2;
    public static final int TYPE_HIDRATACION = 3;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service1 = new Intent(context, NotificationService.class);
        service1.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        ContextCompat.startForegroundService(context, service1);

    }
}