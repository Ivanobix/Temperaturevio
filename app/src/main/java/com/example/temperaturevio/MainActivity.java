package com.example.temperaturevio;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // (Debug: 1 // Normal: 60)
    private static final int UNA_HORA_EN_MINUTOS = 1;

    private final static int REQUEST_PREFERENCIAS = 345;
    private Button btnIniciar, btnDetener, btnConfiguraciones;
    private TextView lblTemperatura;
    private SharedPreferences misDatos;
    private SharedPreferences.Editor editor;
    private SensorManager sensorManager;
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        initComponents();
        initHandlers();

    }

    private void initComponents() {
        btnIniciar = (Button) findViewById(R.id.btnIniciar);
        btnDetener = (Button) findViewById(R.id.btnDetener);
        btnConfiguraciones = (Button) findViewById(R.id.btnConfiguraciones);

        lblTemperatura = (TextView) findViewById(R.id.lblTemperatura);

        misDatos = getSharedPreferences("preferencias", MODE_PRIVATE);
        editor = misDatos.edit();

        if (misDatos.getBoolean("iniciado", false)) btnIniciar.setEnabled(false);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (sensor == null)
            Toast.makeText(this.getApplicationContext(),
                    "Este dispositivo no tiene sensor de temperatura ambiental.", Toast.LENGTH_SHORT).show();
        else
            iniciarSensorDeTemperatura();

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
        iniciarSensorDeTemperatura();

        editor.putBoolean("iniciado", true);
        editor.apply();

        btnIniciar.setEnabled(false);
        btnDetener.setEnabled(true);
    }

    private void detener() {
        detenerSensorDeTemperatura();

        editor.putBoolean("iniciado", false);
        editor.apply();

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
        long intervaloDeRepeticion;
        if (UNA_HORA_EN_MINUTOS == 60) {
            today.set(Calendar.HOUR_OF_DAY, today.get(Calendar.HOUR_OF_DAY) + 1);
            today.set(Calendar.MINUTE, 0);
            intervaloDeRepeticion = AlarmManager.INTERVAL_HOUR;
        } else {
            int hora = today.get(Calendar.HOUR_OF_DAY);
            int minuto = today.get(Calendar.MINUTE) + 1;
            if (minuto >= 60) {
                minuto -= 60;
                hora++;
            }
            today.set(Calendar.HOUR_OF_DAY, hora);
            today.set(Calendar.MINUTE, minuto);
            intervaloDeRepeticion = 60 * 1000;
        }
        today.set(Calendar.SECOND, 0);

        crearAlarma(AlarmReceiver.TYPE_TEMPERATURAS, today.getTimeInMillis(), intervaloDeRepeticion);
    }

    private void crearAlarmasExposicion(Calendar today) {
        long intervaloDeRepeticion;
        if (UNA_HORA_EN_MINUTOS == 60) {
            today.set(Calendar.HOUR_OF_DAY, today.get(Calendar.HOUR_OF_DAY) + 4);
            today.set(Calendar.MINUTE, 0);
            intervaloDeRepeticion = AlarmManager.INTERVAL_HOUR * 4;
        } else {
            int hora = today.get(Calendar.HOUR_OF_DAY);
            int minuto = today.get(Calendar.MINUTE) + 2;
            while (minuto >= 60) {
                minuto -= 60;
                hora++;
            }
            today.set(Calendar.HOUR_OF_DAY, hora);
            today.set(Calendar.MINUTE, minuto);
            intervaloDeRepeticion = 2 * 60 * 1000;
        }
        today.set(Calendar.SECOND, 10);

        crearAlarma(AlarmReceiver.TYPE_EXPOSICION, today.getTimeInMillis(), intervaloDeRepeticion);
    }

    private void crearAlarmaHidratacion(Calendar today) {
        long intervaloDeRepeticion;
        if (UNA_HORA_EN_MINUTOS == 60) {
            today.set(Calendar.HOUR_OF_DAY, today.get(Calendar.HOUR_OF_DAY) + 6);
            today.set(Calendar.MINUTE, 0);
            intervaloDeRepeticion = AlarmManager.INTERVAL_HOUR * 6;
        } else {
            int hora = today.get(Calendar.HOUR_OF_DAY);
            int minuto = today.get(Calendar.MINUTE) + 3;
            while (minuto >= 60) {
                minuto -= 60;
                hora++;
            }
            today.set(Calendar.HOUR_OF_DAY, hora);
            today.set(Calendar.MINUTE, minuto);
            intervaloDeRepeticion = 3 * 60 * 1000;
        }
        today.set(Calendar.SECOND, 20);

        crearAlarma(AlarmReceiver.TYPE_HIDRATACION, today.getTimeInMillis(), intervaloDeRepeticion);
    }

    private void crearAlarma(int id, Long timestamp, long intervaloRepeticion) {
        AlarmManager alarmManager = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        //alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        alarmIntent.setData(Uri.parse("custom://" + id));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, id, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timestamp, intervaloRepeticion, pendingIntent);
    }

    private void iniciarSensorDeTemperatura() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void detenerSensorDeTemperatura() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float temperaturaActual = event.values[0];

        lblTemperatura.setText(String.valueOf(temperaturaActual));

        editor.putFloat("temperaturaActual", temperaturaActual);
        editor.apply();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}