package com.example.temperaturevio;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_PREFERENCIAS = 345;
    private ImageButton btnIniciar, btnDetener, btnConfiguraciones;
    private TextView lblTemperatura;
    private SharedPreferences misDatos;
    private SharedPreferences.Editor editor;
    private ArrayList<PendingIntent> alarmas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        initComponents();
        initHandlers();

    }

    private void initComponents() {
        btnIniciar = (ImageButton) findViewById(R.id.btnIniciar);
        btnDetener = (ImageButton) findViewById(R.id.btnDetener);
        btnConfiguraciones = (ImageButton) findViewById(R.id.btnConfiguraciones);

        lblTemperatura = (TextView) findViewById(R.id.lblTemperatura);

        misDatos = getSharedPreferences("preferencias", MODE_PRIVATE);
        editor = misDatos.edit();

        alarmas = new ArrayList<>();
    }

    private void initHandlers() {
        btnIniciar.setOnClickListener(v -> iniciar());
        btnDetener.setOnClickListener(v -> detener());
        btnConfiguraciones.setOnClickListener(v -> irAConfiguraciones());
    }

    private void iniciar() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(MainActivity.this, (timePicker, selectedHour, selectedMinute) -> {
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, selectedHour);
            today.set(Calendar.MINUTE, selectedMinute);
            today.set(Calendar.SECOND, 0);

            crearAlarma(1, today.getTimeInMillis(), MainActivity.this);
        }, hour, minute, true);
        mTimePicker.setTitle(getString(R.string.select_time));
        mTimePicker.show();

        editor.putBoolean("iniciado", true);
        editor.apply();

        btnIniciar.setEnabled(false);
    }

    private void detener() {
        editor.putBoolean("iniciado", false);
        editor.apply();

        btnDetener.setEnabled(false);
    }

    private void irAConfiguraciones() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivityForResult(intent, REQUEST_PREFERENCIAS);
    }

    private void crearAlarma(int i, Long timestamp, Context ctx) {
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(ctx, i, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);
    }

}