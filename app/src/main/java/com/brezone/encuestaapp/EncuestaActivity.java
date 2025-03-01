package com.brezone.encuestaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EncuestaActivity extends AppCompatActivity {
    private EditText inputAsesor, inputCliente, inputClavePropiedad, inputColonia, inputComentario;
    private Spinner spinnerEstado, spinnerUbicacion, spinnerProyecto, spinnerPrecio, spinnerLimpieza, spinnerElegible;
    private Button btnGuardar, btnVerEncuestas;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);

        databaseHelper = new DatabaseHelper(this);

        btnVerEncuestas = findViewById(R.id.btn_ver_encuestas);
        btnVerEncuestas.setOnClickListener(v -> {
            Intent intent = new Intent(EncuestaActivity.this, VerEncuestasActivity.class);
            startActivity(intent);
        });

        // Enlazar vistas
        inputAsesor = findViewById(R.id.input_asesor);
        inputCliente = findViewById(R.id.input_cliente);
        inputClavePropiedad = findViewById(R.id.input_clave_propiedad); // NUEVO
        inputColonia = findViewById(R.id.input_colonia);
        inputComentario = findViewById(R.id.input_comentario);

        spinnerEstado = findViewById(R.id.spinner_estado);
        spinnerUbicacion = findViewById(R.id.spinner_ubicacion);
        spinnerProyecto = findViewById(R.id.spinner_proyecto);
        spinnerPrecio = findViewById(R.id.spinner_precio);
        spinnerLimpieza = findViewById(R.id.spinner_limpieza);
        spinnerElegible = findViewById(R.id.spinner_elegible);

        btnGuardar = findViewById(R.id.btn_guardar);

        btnGuardar.setOnClickListener(v -> {
            String asesor = inputAsesor.getText().toString();
            String cliente = inputCliente.getText().toString();
            String clavePropiedad = inputClavePropiedad.getText().toString(); // NUEVO
            String colonia = inputColonia.getText().toString();
            String estado = spinnerEstado.getSelectedItem().toString();
            String ubicacion = spinnerUbicacion.getSelectedItem().toString();
            String proyecto = spinnerProyecto.getSelectedItem().toString();
            String precio = spinnerPrecio.getSelectedItem().toString();
            String limpieza = spinnerLimpieza.getSelectedItem().toString();
            String elegible = spinnerElegible.getSelectedItem().toString();
            String comentario = inputComentario.getText().toString();
            String fechaHora = getCurrentDateTime(); // Obtener fecha y hora actuales

            boolean insertado = databaseHelper.insertarEncuesta(
                    asesor, cliente, clavePropiedad, colonia, estado, ubicacion,
                    proyecto, precio, limpieza, elegible, comentario, fechaHora
            );

            if (insertado) {
                Toast.makeText(this, "Encuesta guardada exitosamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al guardar la encuesta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para obtener la fecha y hora actual en formato "YYYY-MM-DD HH:MM:SS"
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
