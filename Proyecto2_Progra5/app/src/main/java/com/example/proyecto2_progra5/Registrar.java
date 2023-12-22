package com.example.proyecto2_progra5;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Registrar extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        Button btnGoLogin = findViewById(R.id.btnGoLogin);

        btnGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registrar.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void guardeUsuarioSqlServer(View view){
        SqlServerConn connection = new SqlServerConn();
        Connection conn = connection.conexionSql();

        EditText txtRCedula, txtRNombre, txtREmail, txtRClave, txtRTelefono;

        txtRCedula = findViewById(R.id.txtRCedula);
        txtRNombre = findViewById(R.id.txtRNombre);
        txtREmail = findViewById(R.id.txtREmail);
        txtRClave = findViewById(R.id.txtRClave);
        txtRTelefono = findViewById(R.id.txtRTelefono);

        String cedula = txtRCedula.getText().toString();
        String nombre = txtRNombre.getText().toString();
        String correo = txtREmail.getText().toString();
        String clave = txtRClave.getText().toString();
        String telefono = txtRTelefono.getText().toString();
        String rolId = "1";

        try{
            if(cedula.isEmpty() || nombre.isEmpty() || correo.isEmpty() || clave.isEmpty() || telefono.isEmpty()){
                Toast.makeText(Registrar.this, "Faltan datos por ingresar", Toast.LENGTH_SHORT).show();
                return;
            } else if(conn != null) {
                String query = "INSERT INTO Usuarios(Nombre, Correo, Clave, Cedula, Telefono, RolId) VALUES('" + nombre + "', '" + correo + "', '" + clave + "', '" + cedula + "', '" + telefono + "', " + rolId + ")";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
            }
        }catch (Exception exception){
            Log.e("Error al agregar usuario", exception.getMessage());
        }

        Toast.makeText(Registrar.this, "Usuario agregado", Toast.LENGTH_SHORT).show();
        txtRCedula.setText("");
        txtRNombre.setText("");
        txtREmail.setText("");
        txtRClave.setText("");
        txtRTelefono.setText("");
        Log.i("info", "Usuario agregado " + nombre);

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
    public void guardeUsuarioSqlServer2(View view){
        SqliteConn sqliteConn = new SqliteConn(getApplicationContext());

        EditText txtRCedula, txtRNombre, txtREmail, txtRClave, txtRTelefono;

        txtRCedula = findViewById(R.id.txtRCedula);
        txtRNombre = findViewById(R.id.txtRNombre);
        txtREmail = findViewById(R.id.txtREmail);
        txtRClave = findViewById(R.id.txtRClave);
        txtRTelefono = findViewById(R.id.txtRTelefono);

        String cedula = txtRCedula.getText().toString();
        String nombre = txtRNombre.getText().toString();
        String correo = txtREmail.getText().toString();
        String clave = txtRClave.getText().toString();
        String telefono = txtRTelefono.getText().toString();
        String rol = "1";

        try {
            if (cedula.isEmpty() || nombre.isEmpty() || correo.isEmpty() || clave.isEmpty() || telefono.isEmpty()) {
                Toast.makeText(Registrar.this, "Faltan datos por ingresar", Toast.LENGTH_SHORT).show();
                return;
            } else {
                // Utilizar el método de la clase SqliteConn para agregar el usuario localmente
                sqliteConn.agregarPersona(nombre, correo, clave, cedula, telefono, rol);
            }
        } catch (Exception exception) {
            Log.e("Error al agregar usuario", exception.getMessage());
            Toast.makeText(Registrar.this, "Error al agregar usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(Registrar.this, "Usuario agregado localmente", Toast.LENGTH_SHORT).show();
        txtRCedula.setText("");
        txtRNombre.setText("");
        txtREmail.setText("");
        txtRClave.setText("");
        txtRTelefono.setText("");
        Log.i("info", "Usuario agregado " + nombre);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

}