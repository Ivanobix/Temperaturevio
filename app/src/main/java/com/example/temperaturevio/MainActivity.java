package com.example.temperaturevio;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // Valores menores que 60 son solo recomendados para debug
    private static final int UNA_HORA_EN_MINUTOS = 1;

    private final static int REQUEST_PREFERENCIAS = 345;
    private ImageButton btnIniciar, btnDetener, btnConfiguraciones;
    private TextView lblTemperatura;
    private SharedPreferences misDatos;
    private SharedPreferences.Editor editor;

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

        if (misDatos.getBoolean("iniciado", false)) btnIniciar.setEnabled(false);
        else btnDetener.setEnabled(false);

    }

    private void initHandlers() {
        btnIniciar.setOnClickListener(v -> iniciar());
        btnDetener.setOnClickListener(v -> detener());
        btnConfiguraciones.setOnClickListener(v -> irAConfiguraciones());
    }

    private void irAConfiguraciones() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivityForResult(intent, REQUEST_PREFERENCIAS);
    }

    private void iniciar() {
        iniciarAlarmas();

        editor.putBoolean("iniciado", true);
        editor.apply();

        btnIniciar.setEnabled(false);
        btnDetener.setEnabled(true);
    }

    private void detener() {
        editor.putBoolean("iniciado", false);
        editor.apply();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent;

        //Recorrer por orden los 3 tipos de alarmas
        for (int i = 1; i <= 3; i++) {
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, i, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
            alarmManager.cancel(pendingIntent);
        }

        btnIniciar.setEnabled(true);
        btnDetener.setEnabled(false);
    }

    private void iniciarAlarmas() {
        Calendar today = Calendar.getInstance();
        crearAlarmasTemperaturas(today);
        crearAlarmasExposicion(today);
        crearAlarmaHidratacion(today);

    }

    private void crearAlarmasTemperaturas(Calendar today) {
        if (UNA_HORA_EN_MINUTOS == 60) {
            today.set(Calendar.HOUR_OF_DAY, today.get(Calendar.HOUR_OF_DAY) + 1);
            today.set(Calendar.MINUTE, 0);
        } else {
            today.set(Calendar.HOUR_OF_DAY, today.get(Calendar.HOUR_OF_DAY));
            today.set(Calendar.MINUTE, today.get(Calendar.MINUTE) + UNA_HORA_EN_MINUTOS);
        }
        today.set(Calendar.SECOND, 0);

        crearAlarma(AlarmReceiver.TYPE_TEMPERATURAS, today.getTimeInMillis());
    }

    private void crearAlarmasExposicion(Calendar today) {
        if (UNA_HORA_EN_MINUTOS == 60) {
            today.set(Calendar.HOUR_OF_DAY, today.get(Calendar.HOUR_OF_DAY) + 4);
            today.set(Calendar.MINUTE, 0);
        } else {
            int hora = today.get(Calendar.HOUR_OF_DAY);
            int minuto = today.get(Calendar.MINUTE) + UNA_HORA_EN_MINUTOS;
            if (minuto >= 60) {
                hora++;
                minuto = minuto - 60;
            }
            today.set(Calendar.HOUR_OF_DAY, hora);
            today.set(Calendar.MINUTE, minuto);
        }
        today.set(Calendar.SECOND, 0);

        crearAlarma(AlarmReceiver.TYPE_EXPOSICION, today.getTimeInMillis());
    }

    private void crearAlarmaHidratacion(Calendar today) {
        if (UNA_HORA_EN_MINUTOS == 60) {
            today.set(Calendar.HOUR_OF_DAY, today.get(Calendar.HOUR_OF_DAY) + 6);
            today.set(Calendar.MINUTE, 0);
        } else {
            int hora = today.get(Calendar.HOUR_OF_DAY);
            int minuto = today.get(Calendar.MINUTE) + UNA_HORA_EN_MINUTOS;
            if (minuto >= 60) {
                hora++;
                minuto = minuto - 60;
            }
            today.set(Calendar.HOUR_OF_DAY, hora);
            today.set(Calendar.MINUTE, minuto);
        }
        today.set(Calendar.SECOND, 0);

        crearAlarma(AlarmReceiver.TYPE_HIDRATACION, today.getTimeInMillis());
    }

    private void crearAlarma(int id, Long timestamp) {
        AlarmManager alarmManager = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, id, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);
    }

}