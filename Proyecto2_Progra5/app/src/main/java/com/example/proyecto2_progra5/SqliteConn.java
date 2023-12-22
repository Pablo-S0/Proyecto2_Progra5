package com.example.proyecto2_progra5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteConn extends SQLiteOpenHelper {
    public static final String DB_NAME = "SalonComunalProgra5";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "Usuarios";
    public static final String ID_COL = "id";
    public static final String NAME_COL = "nombre";
    public static final String CORREO_COL = "correo";
    public static final String CLAVE_COL = "clave";
    public static final String CEDULA_COL = "cedula";
    public static final String TELEFONO_COL = "telefono";
    public static final String ROL_COL = "rol";

    public SqliteConn(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME_COL + " TEXT, " +
                CORREO_COL + " TEXT, " +
                CLAVE_COL + " TEXT, " +
                CEDULA_COL + " TEXT, " +
                TELEFONO_COL + " TEXT, " +
                ROL_COL + " TEXT);";
        db.execSQL(query);
    }

    public void agregarPersona(String nombre, String correo, String clave, String cedula, String telefono, String rol){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL, nombre);
        values.put(CORREO_COL, correo);
        values.put(CLAVE_COL, clave);
        values.put(CEDULA_COL, cedula);
        values.put(TELEFONO_COL, telefono);
        values.put(ROL_COL, rol);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Cursor buscarUsuario(String correo, String clave) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + CORREO_COL + " = ? AND " + CLAVE_COL + " = ?";
        String[] selectionArgs = {correo, clave};
        return db.rawQuery(query, selectionArgs);
    }
    public Cursor buscarNombre(String cedula){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " +
                ID_COL + ", " +
                NAME_COL + ", " +
                CORREO_COL + ", " +
                CLAVE_COL + ", " +
                TELEFONO_COL + ", " +
                ROL_COL +
                " from " +
                TABLE_NAME + " WHERE " +
                CEDULA_COL + " = '" + cedula + "';";

        Log.i("searchName() = ", query);

        Cursor c = db.rawQuery(query, null);
        return c;
    }

    public void borrarPersona(String cedula){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME +
                " WHERE " + CEDULA_COL +
                " = '" + cedula + "';";

        Log.i("delete() = ", query);
        db.execSQL(query);
    }

    public void actualizarPersona(String cedula, String nombre, String correo, String clave, String telefono){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME +
                " SET " + NAME_COL + " = '" + nombre + "', "
                + CORREO_COL + " = '" + correo + "', "
                + CLAVE_COL + " = '" + clave + "', "
                + TELEFONO_COL + " = '" + telefono + "'"
                + " WHERE " + CEDULA_COL +
                " = '" + cedula + "';";

        Log.i("update() = ", query);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
