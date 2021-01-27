package com.example.temperaturevio;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat swAltasTemperaturas, swBajasTemperaturas;
    private SwitchCompat swExposicionFrio, swExposicionCalor;
    private SwitchCompat swPersonalizacion, swHidratacion;
    private EditText txtAltura, txtPeso, txtEdad;
    private Button btnAplicar, btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initComponents();
        cargarConfiguracionesActuales();
        initHandlers();


    }

    private void initComponents() {
        swAltasTemperaturas = (SwitchCompat) findViewById(R.id.swAltasTemperaturas);
        swBajasTemperaturas = (SwitchCompat) findViewById(R.id.swBajasTemperaturas);
        swExposicionFrio = (SwitchCompat) findViewById(R.id.swExposicionFrio);
        swExposicionCalor = (SwitchCompat) findViewById(R.id.swExposicionCalor);
        swPersonalizacion = (SwitchCompat) findViewById(R.id.swPersonalizacion);
        swHidratacion = (SwitchCompat) findViewById(R.id.swHidratacion);

        txtAltura = (EditText) findViewById(R.id.txtAltura);
        txtPeso = (EditText) findViewById(R.id.txtPeso);
        txtEdad = (EditText) findViewById(R.id.txtEdad);

        btnAplicar = (Button) findViewById(R.id.btnAplicar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
    }

    private void initHandlers() {
        btnAplicar.setOnClickListener(v -> guardarPreferencias());
        btnCancelar.setOnClickListener(v -> salir());
    }

    private void cargarConfiguracionesActuales() {

    }

    private void guardarPreferencias() {

    }

    private void salir() {

    }
}