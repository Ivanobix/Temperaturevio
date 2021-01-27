package com.example.temperaturevio;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnIniciar, btnDetener, btnConfiguraciones;
    private TextView lblTemperatura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        initComponents();
        initHandlers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void initComponents() {
        btnIniciar = (ImageButton) findViewById(R.id.btnIniciar);
        btnDetener = (ImageButton) findViewById(R.id.btnDetener);
        btnConfiguraciones = (ImageButton) findViewById(R.id.btnConfiguraciones);
        lblTemperatura = (TextView) findViewById(R.id.lblTemperatura);
    }

    private void initHandlers() {
        btnIniciar.setOnClickListener(v -> iniciar());
        btnDetener.setOnClickListener(v -> detener());
        btnConfiguraciones.setOnClickListener(v -> irAConfiguraciones());
    }

    private void iniciar() {

    }

    private void detener() {

    }

    private void irAConfiguraciones() {

    }
}