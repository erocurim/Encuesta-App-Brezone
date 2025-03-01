package com.brezone.encuestaapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.OutputStream;

public class VerEncuestasActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private ListView listView;
    private Button btnExportar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_encuestas);

        databaseHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listView);
        btnExportar = findViewById(R.id.btn_exportar);

        cargarEncuestas();

        // Evento del botón para exportar Excel
        btnExportar.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                exportarExcel();
            } else {
                solicitarPermisos();
            }
        });
    }

    private void cargarEncuestas() {
        Cursor cursor = databaseHelper.obtenerEncuestas();

        if (cursor == null) {
            Toast.makeText(this, "Error al cargar encuestas", Toast.LENGTH_SHORT).show();
            Log.e("VerEncuestasActivity", "Error: Cursor es null");
            return;
        }

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No hay encuestas guardadas", Toast.LENGTH_SHORT).show();
            Log.w("VerEncuestasActivity", "No hay encuestas en la base de datos");
            return;
        }

        Log.d("VerEncuestasActivity", "Se encontraron " + cursor.getCount() + " encuestas");

        String[] from = {"asesor", "cliente", "clave_propiedad", "colonia", "estado", "ubicacion", "proyecto", "precio", "limpieza", "elegible", "comentario", "fecha_hora"};
        int[] to = {R.id.txtAsesor, R.id.txtCliente, R.id.txtClavePropiedad, R.id.txtColonia, R.id.txtEstado, R.id.txtUbicacion, R.id.txtProyecto, R.id.txtPrecio, R.id.txtLimpieza, R.id.txtElegible, R.id.txtComentario, R.id.txtFechaHora};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this, R.layout.item_encuesta, cursor, from, to, 0);
        listView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void exportarExcel() {
        String fileName = "Encuestas.xlsx";

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

        ContentResolver resolver = getContentResolver();
        Uri uri = resolver.insert(MediaStore.Files.getContentUri("external"), values);

        if (uri == null) {
            Toast.makeText(this, "Error al crear el archivo en Descargas", Toast.LENGTH_SHORT).show();
            return;
        }

        try (OutputStream outputStream = resolver.openOutputStream(uri)) {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Encuestas");

            // Crear encabezados
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Asesor", "Cliente", "Clave Propiedad", "Colonia", "Estado", "Ubicación", "Proyecto", "Precio", "Limpieza", "¿Elegible?", "Comentario", "Fecha y Hora"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Obtener encuestas desde la base de datos
            Cursor cursor = databaseHelper.obtenerEncuestas();
            int rowIndex = 1;

            if (cursor.moveToFirst()) {
                do {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
                    row.createCell(1).setCellValue(cursor.getString(cursor.getColumnIndexOrThrow("asesor")));
                    row.createCell(2).setCellValue(cursor.getString(cursor.getColumnIndexOrThrow("cliente")));
                    row.createCell(3).setCellValue(cursor.getString(cursor.getColumnIndexOrThrow("clave_propiedad")));
                    row.createCell(4).setCellValue(cursor.getString(cursor.getColumnIndexOrThrow("colonia")));
                    row.createCell(5).setCellValue(cursor.getString(cursor.getColumnIndexOrThrow("estado")));
                    row.createCell(6).setCellValue(cursor.getString(cursor.getColumnIndexOrThrow("ubicacion")));
                    row.createCell(7).setCellValue(cursor.getString(cursor.getColumnIndexOrThrow("proyecto")));
                    row.createCell(8).setCellValue(cursor.getString(cursor.getColumnIndexOrThrow("precio")));
                    row.createCell(9).setCellValue(cursor.getString(cursor.getColumnIndexOrThrow("limpieza")));
                    row.createCell(10).setCellValue(cursor.getString(cursor.getColumnIndexOrThrow("elegible")));
                    row.createCell(11).setCellValue(cursor.getString(cursor.getColumnIndexOrThrow("comentario")));
                    row.createCell(12).setCellValue(cursor.getString(cursor.getColumnIndexOrThrow("fecha_hora")));
                } while (cursor.moveToNext());
            }
            cursor.close();

            workbook.write(outputStream);
            workbook.close();

            Toast.makeText(this, "📂 Archivo guardado en Descargas como " + fileName, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "❌ Error al guardar el archivo Excel", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void solicitarPermisos() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    exportarExcel();
                }
            }
        }
    }
}
