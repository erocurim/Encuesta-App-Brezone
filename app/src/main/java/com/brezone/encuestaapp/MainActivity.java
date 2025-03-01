package com.brezone.encuestaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;  // Asegúrate de que usa AppCompatActivity

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnIrEncuesta = findViewById(R.id.btn_ir_encuesta);
        btnIrEncuesta.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EncuestaActivity.class);
            startActivity(intent);
        });
    }
}
