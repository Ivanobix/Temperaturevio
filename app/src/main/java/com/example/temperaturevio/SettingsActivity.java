package com.example.temperaturevio;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    public static final int RESULT_APLICAR = 1375;
    public static final int RESULT_CANCELAR = 3452;
    private SwitchCompat swAltasTemperaturas, swBajasTemperaturas;
    private SwitchCompat swExposicionFrio, swExposicionCalor;
    private SwitchCompat swPersonalizacion, swHidratacion;
    private RadioButton rbCelsius, rbKelvin, rbFahrenheit;
    private EditText txtAltura, txtPeso, txtEdad;
    private Button btnAplicar, btnCancelar;
    private SharedPreferences misDatos;
    private SharedPreferences.Editor editor;

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

        rbCelsius = (RadioButton) findViewById(R.id.rbCelsius);
        rbKelvin = (RadioButton) findViewById(R.id.rbKelvin);
        rbFahrenheit = (RadioButton) findViewById(R.id.rbFahrenheit);

        misDatos = getSharedPreferences("preferencias", MODE_PRIVATE);
        editor = misDatos.edit();
    }

    private void initHandlers() {
        btnAplicar.setOnClickListener(v -> guardarPreferencias());
        btnCancelar.setOnClickListener(v -> salir());
    }

    private void cargarConfiguracionesActuales() {
        if (misDatos.getString("escala", "celsius").equals("celsius")) rbCelsius.setChecked(true);
        else if (misDatos.getString("escala", "celsius").equals("kelvin"))
            rbKelvin.setChecked(true);
        else rbFahrenheit.setChecked(true);

        if (misDatos.getBoolean("altasTemperaturas", true)) swAltasTemperaturas.setChecked(true);
        if (misDatos.getBoolean("bajasTemperaturas", true)) swBajasTemperaturas.setChecked(true);
        if (misDatos.getBoolean("exposicionCalor", true)) swExposicionCalor.setChecked(true);
        if (misDatos.getBoolean("exposicionFrio", true)) swExposicionFrio.setChecked(true);
        if (misDatos.getBoolean("hidratacion", true)) swHidratacion.setChecked(true);

        if (misDatos.getBoolean("personalizacion", true)) {
            swPersonalizacion.setChecked(true);
            txtAltura.setText(misDatos.getString("altura", ""));
            txtPeso.setText(misDatos.getString("peso", ""));
            txtEdad.setText(misDatos.getString("edad", ""));
        }
    }

    private void guardarPreferencias() {
        if (rbCelsius.isChecked()) editor.putString("escala", "celsius");
        else if (rbKelvin.isChecked()) editor.putString("escala", "kelvin");
        else editor.putString("escala", "fahrenheit");

        editor.putBoolean("altasTemperaturas", swAltasTemperaturas.isChecked());
        editor.putBoolean("bajasTemperaturas", swBajasTemperaturas.isChecked());
        editor.putBoolean("exposicionCalor", swExposicionCalor.isChecked());
        editor.putBoolean("exposicionFrio", swExposicionFrio.isChecked());
        editor.putBoolean("hidratacion", swHidratacion.isChecked());

        editor.putBoolean("personalizacion", swPersonalizacion.isChecked());
        if (swPersonalizacion.isChecked()) {
            editor.putString("altura", txtAltura.getText().toString());
            editor.putString("peso", txtPeso.getText().toString());
            editor.putString("edad", txtEdad.getText().toString());
        } else {
            editor.putString("altura", "");
            editor.putString("peso", "");
            editor.putString("edad", "");
        }
        editor.apply();

        setResult(RESULT_APLICAR);
        finish();
    }

    private void salir() {
        setResult(RESULT_CANCELAR);
        finish();
    }
}