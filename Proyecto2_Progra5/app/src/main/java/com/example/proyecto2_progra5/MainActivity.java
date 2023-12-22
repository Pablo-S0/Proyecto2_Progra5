package com.example.proyecto2_progra5;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    private SqliteConn myDbContext;
    ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDbContext = new SqliteConn(this);
        Button btnGoRegistrar = findViewById(R.id.btnGoRegistrar);

        btnGoRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Registrar.class);
                startActivity(intent);
            }
        });
        myDbContext = new SqliteConn(MainActivity.this);
    }

    public void LoginUsuarioSqlServer(View view){
        SqlServerConn connection = new SqlServerConn();
        Connection conn = connection.conexionSql();

        EditText txtLEmail, txtLClave;

        txtLEmail = findViewById(R.id.txtLEmail);
        txtLClave = findViewById(R.id.txtLClave);

        String correo = txtLEmail.getText().toString();
        String clave = txtLClave.getText().toString();

        String nombreUsu = "";
        String correoUsu = "";
        String claveUsu = "";
        String cedulaUsu = "";
        String telefonoUsu = "";
        String rolUsu = "";

        try{
            if(correo.isEmpty() || clave.isEmpty()){
                Toast.makeText(MainActivity.this, "Faltan datos por ingresar", Toast.LENGTH_SHORT).show();
                return;
            } else if(conn != null) {
                String query = "SELECT * FROM Usuarios WHERE Correo = '" + correo + "' AND Clave = '" + clave + "'";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);

                while(rs.next()){
                    nombreUsu = rs.getString(2);
                    correoUsu = rs.getString(3);
                    claveUsu = rs.getString(4);
                    cedulaUsu = rs.getString(5);
                    telefonoUsu = rs.getString(6);
                    rolUsu = rs.getString(7);
                }

                //Agregar a Sqlite nuevo usuario que ingrese
                Cursor c = myDbContext.buscarNombre(cedulaUsu);
                if (c.getCount() > 0){
                    Log.i("info", "Este usuario ya existe en Sqlite");
                }
                else{
                    myDbContext.agregarPersona(nombreUsu, correoUsu, claveUsu, cedulaUsu, telefonoUsu, rolUsu);
                    Log.i("info", "Agregando nuevo usuario a Sqlite");
                }
                //-------------------------------------------

                if(nombreUsu == ""){
                    Toast.makeText(MainActivity.this, "Verificar correo y clave", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Toast.makeText(MainActivity.this, "Ingresando", Toast.LENGTH_SHORT).show();
                    txtLEmail.setText("");
                    txtLClave.setText("");
                    Log.i("info", "Usuario ingresando " + nombreUsu);

                    Intent intent = new Intent(getApplicationContext(),Inicio.class);
                    intent.putExtra("nombreUsu", nombreUsu);
                    intent.putExtra("correoUsu", correoUsu);
                    startActivity(intent);
                }
            }
        }catch (Exception exception){
            Log.e("Error", exception.getMessage());
        }
    }

    public void LoginUsuarioSqlServer2(View view) {
        EditText txtLEmail = findViewById(R.id.txtLEmail);
        EditText txtLClave = findViewById(R.id.txtLClave);

        String correo = txtLEmail.getText().toString();
        String clave = txtLClave.getText().toString();

        if (correo.isEmpty() || clave.isEmpty()) {
            Toast.makeText(MainActivity.this, "Faltan datos por ingresar", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buscar usuario en la base de datos SQLite
        Cursor cursor = myDbContext.buscarUsuario(correo, clave);

        if (cursor.moveToFirst()) {
            // Usuario encontrado
            String nombreUsu = cursor.getString(cursor.getColumnIndex(myDbContext.NAME_COL));
            String correoUsu = cursor.getString(cursor.getColumnIndex(myDbContext.CORREO_COL));

            Toast.makeText(MainActivity.this, "Ingresando", Toast.LENGTH_SHORT).show();
            txtLEmail.setText("");
            txtLClave.setText("");
            Log.i("info", "Usuario ingresando " + nombreUsu);

            Intent intent = new Intent(getApplicationContext(), Inicio.class);
            intent.putExtra("nombreUsu", nombreUsu);
            intent.putExtra("correoUsu", correoUsu);
            startActivity(intent);
        } else {
            // Usuario no encontrado
            Toast.makeText(MainActivity.this, "Verificar correo y clave", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }


}