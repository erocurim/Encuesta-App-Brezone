package com.brezone.encuestaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "encuestas.db";
    private static final int DATABASE_VERSION = 4; // 📌 Se incrementa la versión para forzar onUpgrade
    private static final String TABLE_ENCUESTAS = "encuestas";

    // Columnas
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_ASESOR = "asesor";
    private static final String COLUMN_CLIENTE = "cliente";
    private static final String COLUMN_CLAVE_PROPIEDAD = "clave_propiedad";
    private static final String COLUMN_COLONIA = "colonia";
    private static final String COLUMN_ESTADO = "estado";
    private static final String COLUMN_UBICACION = "ubicacion";
    private static final String COLUMN_PROYECTO = "proyecto";
    private static final String COLUMN_PRECIO = "precio";
    private static final String COLUMN_LIMPIEZA = "limpieza";
    private static final String COLUMN_ELEGIBLE = "elegible";
    private static final String COLUMN_COMENTARIO = "comentario";
    private static final String COLUMN_FECHA_HORA = "fecha_hora";

    private static final String CREATE_TABLE_ENCUESTAS =
            "CREATE TABLE " + TABLE_ENCUESTAS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ASESOR + " TEXT, " +
                    COLUMN_CLIENTE + " TEXT, " +
                    COLUMN_CLAVE_PROPIEDAD + " TEXT, " +
                    COLUMN_COLONIA + " TEXT, " +
                    COLUMN_ESTADO + " TEXT, " +
                    COLUMN_UBICACION + " TEXT, " +
                    COLUMN_PROYECTO + " TEXT, " +
                    COLUMN_PRECIO + " TEXT, " +
                    COLUMN_LIMPIEZA + " TEXT, " +
                    COLUMN_ELEGIBLE + " TEXT, " +
                    COLUMN_COMENTARIO + " TEXT, " +
                    COLUMN_FECHA_HORA + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ENCUESTAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabaseHelper", "Actualizando base de datos de versión " + oldVersion + " a " + newVersion);

        // 📌 En lugar de borrar la base de datos, aseguramos que las columnas necesarias existen
        try {
            db.execSQL("ALTER TABLE " + TABLE_ENCUESTAS + " ADD COLUMN " + COLUMN_ESTADO + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_ENCUESTAS + " ADD COLUMN " + COLUMN_UBICACION + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_ENCUESTAS + " ADD COLUMN " + COLUMN_PROYECTO + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_ENCUESTAS + " ADD COLUMN " + COLUMN_PRECIO + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_ENCUESTAS + " ADD COLUMN " + COLUMN_LIMPIEZA + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_ENCUESTAS + " ADD COLUMN " + COLUMN_ELEGIBLE + " TEXT");
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error en onUpgrade: " + e.getMessage());
        }
    }

    public boolean insertarEncuesta(String asesor, String cliente, String clavePropiedad, String colonia, String estado, String ubicacion, String proyecto, String precio, String limpieza, String elegible, String comentario, String fechaHora) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ASESOR, asesor);
        values.put(COLUMN_CLIENTE, cliente);
        values.put(COLUMN_CLAVE_PROPIEDAD, clavePropiedad);
        values.put(COLUMN_COLONIA, colonia);
        values.put(COLUMN_ESTADO, estado);
        values.put(COLUMN_UBICACION, ubicacion);
        values.put(COLUMN_PROYECTO, proyecto);
        values.put(COLUMN_PRECIO, precio);
        values.put(COLUMN_LIMPIEZA, limpieza);
        values.put(COLUMN_ELEGIBLE, elegible);
        values.put(COLUMN_COMENTARIO, comentario);
        values.put(COLUMN_FECHA_HORA, fechaHora);

        long result = db.insert(TABLE_ENCUESTAS, null, values);
        return result != -1;
    }

    public Cursor obtenerEncuestas() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ENCUESTAS, null);
    }
}
