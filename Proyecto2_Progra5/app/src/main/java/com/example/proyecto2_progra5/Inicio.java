package com.example.proyecto2_progra5;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.proyecto2_progra5.databinding.ActivityInicioBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;



public class Inicio extends AppCompatActivity {
    private SqliteConn myDbContext;
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageView;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityInicioBinding binding;
    private Uri imageUri;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInicioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }

        setSupportActionBar(binding.appBarInicio.toolbar);

        binding.appBarInicio.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_catalogo)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_inicio);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        cargarDatos();
        myDbContext = new SqliteConn(Inicio.this);

    }



    //Cargar datos del usuario
    public void cargarDatos(){
        final View header = binding.navView.getHeaderView(0);

        TextView nUsu, cUsu;
        nUsu = header.findViewById(R.id.txtNombreUsuario);
        cUsu = header.findViewById(R.id.txtCorreoUsuario);

        if(nUsu!=null){
            Intent intent = getIntent();
            if(intent!=null){
                String nombreUsu = "";
                String correoUsu = "";
                Bundle extras = getIntent().getExtras();
                if(extras!=null){
                    nombreUsu = extras.getString("nombreUsu");
                    correoUsu = extras.getString("correoUsu");
                }
                nUsu.setText(nombreUsu);
                cUsu.setText(correoUsu);
            }
        }
    }

    //CRUD Usuarios
    public void agregaUsuarioSqlServer(View view){
        SqlServerConn connection = new SqlServerConn();
        Connection conn = connection.conexionSql();

        EditText eTCedula, eTNombre, eTCorreo, eTClave, eTTelefono;

        eTCedula = findViewById(R.id.eTCedula);
        eTNombre = findViewById(R.id.eTNombre);
        eTCorreo = findViewById(R.id.eTCorreo);
        eTClave = findViewById(R.id.eTClave);
        eTTelefono = findViewById(R.id.eTTelefono);

        String cedula = eTCedula.getText().toString();
        String nombre = eTNombre.getText().toString();
        String correo = eTCorreo.getText().toString();
        String clave = eTClave.getText().toString();
        String telefono = eTTelefono.getText().toString();
        String rolId = "1";

        try{
            if(cedula.isEmpty() || nombre.isEmpty() || correo.isEmpty() || clave.isEmpty() || telefono.isEmpty()){
                Toast.makeText(Inicio.this, "Faltan datos por ingresar", Toast.LENGTH_SHORT).show();
                return;
            } else if(conn != null) {
                String query = "INSERT INTO Usuarios(Nombre, Correo, Clave, Cedula, Telefono, RolId) VALUES('" + nombre + "', '" + correo + "', '" + clave + "', '" + cedula + "', '" + telefono + "', " + rolId + ")";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
            }
        }catch (Exception exception){
            Log.e("Error al agregar usuario", exception.getMessage());
        }

        Toast.makeText(Inicio.this, "Usuario agregado", Toast.LENGTH_SHORT).show();
        eTCedula.setText("");
        eTNombre.setText("");
        eTCorreo.setText("");
        eTClave.setText("");
        eTTelefono.setText("");
        Log.i("info", "Usuario agregado " + nombre);
    }

    public void actualiceUsuarioSqlServer(View view){
        SqlServerConn connection = new SqlServerConn();
        Connection conn = connection.conexionSql();

        EditText eTCedula, eTNombre, eTCorreo, eTClave, eTTelefono;

        eTCedula = findViewById(R.id.eTCedula);
        eTNombre = findViewById(R.id.eTNombre);
        eTCorreo = findViewById(R.id.eTCorreo);
        eTClave = findViewById(R.id.eTClave);
        eTTelefono = findViewById(R.id.eTTelefono);

        String cedula = eTCedula.getText().toString();
        String nombre = eTNombre.getText().toString();
        String correo = eTCorreo.getText().toString();
        String clave = eTClave.getText().toString();
        String telefono = eTTelefono.getText().toString();

        try{
            if(cedula.isEmpty() || nombre.isEmpty() || correo.isEmpty() || clave.isEmpty() || telefono.isEmpty()){
                Toast.makeText(Inicio.this, "Faltan datos por ingresar", Toast.LENGTH_SHORT).show();
                return;
            } else if(conn != null) {
                //Actualizar a usuario en Sqlite
                Cursor c = myDbContext.buscarNombre(cedula);
                if (c.getCount() > 0){
                    myDbContext.actualizarPersona(cedula, nombre, correo, clave, telefono);
                    Log.i("info", "Actualizando usuario en Sqlite");
                }
                else{
                    Log.i("info", "Este usuario aun no existe en Sqlite");
                }
                //------------------------------

                String query = "UPDATE Usuarios SET Nombre = '" + nombre + "', Correo = '" + correo + "', Clave = '" + clave + "', Telefono = '" + telefono + "' WHERE Cedula = '" + cedula + "'";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
            }
        }catch (Exception exception){
            Log.e("Error", exception.getMessage());
        }

        Toast.makeText(Inicio.this, "Usuario actualizado", Toast.LENGTH_SHORT).show();
        eTCedula.setText("");
        eTNombre.setText("");
        eTCorreo.setText("");
        eTClave.setText("");
        eTTelefono.setText("");
        Log.i("info", "Usuario actualizado " + cedula);
    }

    public void borreUsuarioSqlServer(View view){
        SqlServerConn connection = new SqlServerConn();
        Connection conn = connection.conexionSql();

        EditText eTCedula, eTNombre, eTCorreo, eTClave, eTTelefono;

        eTCedula = findViewById(R.id.eTCedula);
        eTNombre = findViewById(R.id.eTNombre);
        eTCorreo = findViewById(R.id.eTCorreo);
        eTClave = findViewById(R.id.eTClave);
        eTTelefono = findViewById(R.id.eTTelefono);

        String cedula = eTCedula.getText().toString();

        try{
            if(cedula.isEmpty()){
                Toast.makeText(Inicio.this, "Ingresar cedula a borrar", Toast.LENGTH_SHORT).show();
                return;
            } else if(conn != null) {
                //Borrar a usuario en Sqlite
                Cursor c = myDbContext.buscarNombre(cedula);
                if (c.getCount() > 0){
                    myDbContext.borrarPersona(cedula);
                    Log.i("info", "Borrando usuario en Sqlite");
                }
                else{
                    Log.i("info", "Este usuario aun no existe en Sqlite");
                }
                //------------------------------

                String query = "DELETE Usuarios WHERE Cedula = '" + cedula + "'";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
            }
        }catch (Exception exception){
            Log.e("Error", exception.getMessage());
        }

        Toast.makeText(Inicio.this, "Usuario borrado", Toast.LENGTH_SHORT).show();
        eTCedula.setText("");
        eTNombre.setText("");
        eTCorreo.setText("");
        eTClave.setText("");
        eTTelefono.setText("");
        Log.i("info", "Usuario borrado " + cedula);
    }

    public void muestreUsuarioSqlServer(View view){
        SqlServerConn connection = new SqlServerConn();
        Connection conn = connection.conexionSql();

        EditText eTCedula, eTNombre, eTCorreo, eTClave, eTTelefono;

        eTCedula = findViewById(R.id.eTCedula);
        eTNombre = findViewById(R.id.eTNombre);
        eTCorreo = findViewById(R.id.eTCorreo);
        eTClave = findViewById(R.id.eTClave);
        eTTelefono = findViewById(R.id.eTTelefono);

        String cedula = eTCedula.getText().toString();

        try{
            if(cedula.isEmpty()){
                Toast.makeText(Inicio.this, "Ingresar cedula a mostrar", Toast.LENGTH_SHORT).show();
                return;
            } else if(conn != null) {
                String query = "SELECT * FROM Usuarios WHERE Cedula = '" + cedula + "'";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);

                while (rs.next()){
                    eTNombre.setText(rs.getString(2));
                    eTCorreo.setText(rs.getString(3));
                    eTClave.setText(rs.getString(4));
                    eTTelefono.setText(rs.getString(6));
                }
            }
        }catch (Exception exception){
            Log.e("Error", exception.getMessage());
        }
        Toast.makeText(Inicio.this, "Usuario mostrado", Toast.LENGTH_SHORT).show();
        Log.i("info", "Usuario mostrado " + cedula);
    }

    public void listaUsuarioSqlServer(View view){
        SqlServerConn connection = new SqlServerConn();
        Connection conn = connection.conexionSql();

        TextView txtLista;

        txtLista = findViewById(R.id.txtLista);

        try{
            if(conn != null) {
                String lista = "";
                String query = "SELECT * FROM Usuarios";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()){
                    lista += (rs.getString(1) + " " + rs.getString(2)+ " " + rs.getString(3)+ " " + rs.getString(5)+ " " + rs.getString(6) + " \n");
                }
                txtLista.setText(lista);
                if (lista == ""){
                    txtLista.setText("No hay personas");
                }
            }
        }catch (Exception exception){
            Log.e("Error", exception.getMessage());
        }

        Toast.makeText(Inicio.this, "Lista mostrada", Toast.LENGTH_SHORT).show();
        Log.i("info", "Lista mostrada");
    }
//--------------------------------------------------Reservaciones--------------------------------------------------
    //---------Genera o actualiza todas las reservaciones ----------------------------------------------
    public void buttonActualizar(View view) {
        SqlServerConn connection = new SqlServerConn();
        Connection conn = connection.conexionSql();

        TextView TextVistaReservas;

        TextVistaReservas = findViewById(R.id.TextVistaReservas);

        try {
            if (conn != null) {
                String reserva = "";
                String query = "SELECT r.Id, CONVERT(VARCHAR(10), r.FechaReserva, 103) AS FechaReserva, " +
                        "CONVERT(VARCHAR(10), r.FechaFinalPago, 103) AS FechaFinalPago, r.Monto, u.Nombre " +
                        "FROM Reservas r " +
                        "INNER JOIN Usuarios u ON r.UsuariosId = u.Id";

                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);

                StringBuilder reservasStringBuilder = new StringBuilder();
                reservasStringBuilder.append("ID | Fecha Reserva | Fecha Final Pago | Monto | Nombre\n");

                while (rs.next()) {
                    reservasStringBuilder.append(rs.getString(1))
                            .append(" | ")
                            .append(rs.getString(2))
                            .append(" | ")
                            .append(rs.getString(3))
                            .append(" | ")
                            .append(rs.getString(4))
                            .append(" | ")
                            .append(rs.getString(5))
                            .append("\n");
                }

                reserva = reservasStringBuilder.toString();

                if (reserva.isEmpty()) {
                    reserva = "No hay reservas registradas";
                }

                TextVistaReservas.setText(reserva);
            }
        } catch (Exception exception) {
            Log.e("Error", exception.getMessage());
        } finally {

        }
    }
    //---------Buscar   Reservacion por usuario----------------------------------------------
    public void buttonBuscar(View view) {
        SqlServerConn connection = new SqlServerConn();
        Connection conn = connection.conexionSql();

        TextView TextVistaReservas, TextBuscar;
        TextBuscar = findViewById(R.id.TextBuscar);
        TextVistaReservas = findViewById(R.id.TextVistaReservas);

        try {
            if (conn != null) {
                String buscarText = TextBuscar.getText().toString();
                String reserva = "";
                String query = "SELECT r.Id, CONVERT(VARCHAR(10), r.FechaReserva, 103) AS FechaReserva, " +
                        "CONVERT(VARCHAR(10), r.FechaFinalPago, 103) AS FechaFinalPago, r.Monto, u.Nombre " +
                        "FROM Reservas r " +
                        "INNER JOIN Usuarios u ON r.UsuariosId = u.Id WHERE u.Nombre = '" + buscarText + "'";

                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);

                StringBuilder reservasStringBuilder = new StringBuilder();
                reservasStringBuilder.append("ID | Fecha Reserva | Fecha Final Pago | Monto | Nombre\n");

                while (rs.next()) {
                    reservasStringBuilder.append(rs.getString(1))
                            .append(" | ")
                            .append(rs.getString(2))
                            .append(" | ")
                            .append(rs.getString(3))
                            .append(" | ")
                            .append(rs.getString(4))
                            .append(" | ")
                            .append(rs.getString(5))
                            .append("\n");
                }

                reserva = reservasStringBuilder.toString();

                if (reserva.isEmpty()) {
                    reserva = "No hay reservas registradas";
                }

                TextVistaReservas.setText(reserva);
            }
        } catch (Exception exception) {
            Log.e("Error", exception.getMessage());
        } finally {

        }
    }
    //--------Buscar Reservacion de usuario por el id ya para poder editar o eliminar----------------------------------------------
    public void buttonBuscar2(View view){
        SqlServerConn connection = new SqlServerConn();
        Connection conn = connection.conexionSql();

        EditText TextId, TextFechaReserva, TextbtnFechaFinal, TextMonto, TextNombre;

        TextId = findViewById(R.id.TextID);
        TextFechaReserva = findViewById(R.id.TextFechaReserva);
        TextbtnFechaFinal = findViewById(R.id.TextFechaFinalPago);
        TextMonto = findViewById(R.id.TextMonto);
        TextNombre = findViewById(R.id.TextUsuario);

        String ID = TextId.getText().toString();

        try{
            if(ID.isEmpty()){
                Toast.makeText(Inicio.this, "Ingresar ID de la reserva", Toast.LENGTH_SHORT).show();
                return;
            } else if(conn != null) {
                String query = "SELECT r.FechaReserva, r.FechaFinalPago, r.Monto, u.Nombre " +
                        "FROM Reservas r " +
                        "INNER JOIN Usuarios u ON r.UsuariosId = u.Id WHERE r.Id = '" + ID+ "'";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);

                if (rs.next()){
                    TextFechaReserva.setText(rs.getString(1));
                    TextbtnFechaFinal.setText(rs.getString(2));
                    TextMonto.setText(rs.getString(3));
                    TextNombre.setText(rs.getString(4));
                } else {
                    Toast.makeText(Inicio.this, "No se encontró la reserva con ID: " + ID, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        } catch (Exception exception){
            Log.e("Error", exception.getMessage());
        }
        Toast.makeText(Inicio.this, "Reserva mostrada", Toast.LENGTH_SHORT).show();
        Log.i("info", "Reserva mostrada " + ID);
    }
    //---------Editar reservacion del usuario encontrado por medio del id----------------------------------------------
    public void buttonEditar(View view){
        SqlServerConn connection = new SqlServerConn();
        Connection conn = connection.conexionSql();

        EditText TextId, TextFechaReserva, TextFechaFinalPago, TextMonto, TextNombre;

        TextId = findViewById(R.id.TextID);
        TextFechaReserva = findViewById(R.id.TextFechaReserva);
        TextFechaFinalPago = findViewById(R.id.TextFechaFinalPago);
        TextMonto = findViewById(R.id.TextMonto);
        TextNombre = findViewById(R.id.TextUsuario);

        String ID = TextId.getText().toString();
        String FechaReserva = TextFechaReserva.getText().toString();
        String FechaFinal = TextFechaFinalPago.getText().toString();
        String Monto = TextMonto.getText().toString();
        String Nombre = TextNombre.getText().toString();

        try{
            if(ID.isEmpty() || FechaReserva.isEmpty() || FechaFinal.isEmpty() || Monto.isEmpty() || Nombre.isEmpty()){
                Toast.makeText(Inicio.this, "Faltan datos por ingresar", Toast.LENGTH_SHORT).show();
                return;
            } else if(conn != null) {

                String Query = "SELECT Id FROM Usuarios WHERE Nombre = '" + Nombre + "'";
                Statement stUsuarioId = conn.createStatement();
                ResultSet rsUsuarioId = stUsuarioId.executeQuery(Query);

                if (rsUsuarioId.next()) {
                    int usuarioId = rsUsuarioId.getInt(1);
                    String query = "UPDATE Reservas SET FechaReserva = '" + FechaReserva + "', FechaFinalPago = '" + FechaFinal + "', Monto = '" + Monto + "', UsuariosId = '" + usuarioId + "' WHERE Id = '" + ID + "'";
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    int editar = st.executeUpdate(query);


                    if (editar > 0) {
                        Toast.makeText(Inicio.this, "Reserva actualizada", Toast.LENGTH_SHORT).show();
                        TextId.setText("");
                        TextFechaReserva.setText("");
                        TextFechaFinalPago.setText("");
                        TextMonto.setText("");
                        TextNombre.setText("");
                    } else {
                        Toast.makeText(Inicio.this, "No se encontró la reserva con ID: " + ID, Toast.LENGTH_SHORT).show();
                    }

                }

            }

        }catch (Exception exception){
            Log.e("Error", exception.getMessage());
        }


    }
    //---------Eliminar reservacion por medio del id----------------------------------------------
    public void buttonEliminar(View view){
        SqlServerConn connection = new SqlServerConn();
        Connection conn = connection.conexionSql();

        EditText TextId, TextFechaReserva, TextbtnFechaFinal, TextMonto, TextNombre;

        TextId = findViewById(R.id.TextID);
        TextFechaReserva = findViewById(R.id.TextFechaReserva);
        TextbtnFechaFinal = findViewById(R.id.TextFechaFinalPago);
        TextMonto = findViewById(R.id.TextMonto);
        TextNombre = findViewById(R.id.TextUsuario);

        String ID = TextId.getText().toString();
        String FechaReserva = TextFechaReserva.getText().toString();
        String FechaFinal = TextbtnFechaFinal.getText().toString();
        String Monto = TextMonto.getText().toString();
        String Nombre = TextNombre.getText().toString();

        try{
            if(ID.isEmpty() || FechaReserva.isEmpty() || FechaFinal.isEmpty() || Monto.isEmpty() || Nombre.isEmpty()){
                Toast.makeText(Inicio.this, "Faltan datos por ingresar", Toast.LENGTH_SHORT).show();
                return;
            } else if(conn != null) {
                String query = "DELETE Reservas WHERE Id = '" + ID + "'";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
            }
        }catch (Exception exception){
            Log.e("Error", exception.getMessage());
        }

        Toast.makeText(Inicio.this, "Reserva Eliminada", Toast.LENGTH_SHORT).show();
        TextId.setText("");
        TextFechaReserva.setText("");
        TextbtnFechaFinal.setText("");
        TextMonto.setText("");
        TextNombre.setText("");
        Log.i("info", "Reserva Eliminada " + ID);
    }
    //---------Crear reservacion solo con un usuario registrado----------------------------------------------
    public void buttonCrear(View view){
        SqlServerConn connection = new SqlServerConn();
        Connection conn = connection.conexionSql();

        EditText TextFechaReserva, TextbtnFechaFinal, TextMonto, TextNombre;

        TextFechaReserva = findViewById(R.id.TextFechaReserva);
        TextbtnFechaFinal = findViewById(R.id.TextFechaFinalPago);
        TextMonto = findViewById(R.id.TextMonto);
        TextNombre = findViewById(R.id.TextUsuario);

        String FechaReserva = TextFechaReserva.getText().toString();
        String FechaFinal = TextbtnFechaFinal.getText().toString();
        String Monto = TextMonto.getText().toString();
        String Nombre = TextNombre.getText().toString();

        try{
            if (FechaReserva.isEmpty() || FechaFinal.isEmpty() || Monto.isEmpty() || Nombre.isEmpty()) {
                Toast.makeText(Inicio.this, "Faltan datos por ingresar", Toast.LENGTH_SHORT).show();
                return;
            } else if(conn != null) {

                String Query = "SELECT Id FROM Usuarios WHERE Nombre = '" + Nombre + "'";
                Statement stUsuarioId = conn.createStatement();
                ResultSet rsUsuarioId = stUsuarioId.executeQuery(Query);

                if (rsUsuarioId.next()) {

                    int usuarioId = rsUsuarioId.getInt(1);
                    String Reserva = "INSERT INTO Reservas(FechaReserva, FechaFinalPago, Monto, UsuariosId)VALUES('" + FechaReserva + "', '" + FechaFinal + "', '" + Monto + "', " + usuarioId + ")";
                    Statement stReserva = conn.createStatement();
                    ResultSet rs = stReserva.executeQuery(Reserva);
                    Toast.makeText(Inicio.this, "Reserva Registrada", Toast.LENGTH_SHORT).show();
                    TextFechaReserva.setText("");
                    TextbtnFechaFinal.setText("");
                    TextMonto.setText("");
                    TextNombre.setText("");
                } else {
                    Toast.makeText(Inicio.this, "Usuario no encontrado: ", Toast.LENGTH_SHORT).show();
                }

            }
        }catch (Exception exception){
            Log.e("Error al agregar reserva", exception.getMessage());
        }
    }
 //-------------------------------------------Catalogo--------------------------------------------------------
    //--------------------Imagen hace el llamado para seleccioner la imagen y mostrarla en el ImageView--------------------------------------------------------


    public void btnIngresarImagen(View view) {
        imageView = findViewById(R.id.imageView2);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            loadSelectedImage(imageUri);
        }
    }

    private void loadSelectedImage(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(this, "Error al cargar la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            ex.printStackTrace();

            Toast.makeText(this, "Error inesperado al cargar la imagen", Toast.LENGTH_SHORT).show();
        }
    }
    //-------------------------------------------Crear el Catalogo--------------------------------------------------------

    private String convertirImagen(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void btnCrearCatalogo(View view) {
        SqlServerConn connection = new SqlServerConn();
        Connection conn = connection.conexionSql();

        EditText textDetalle, textSillas, textMesas, textCatering, textComparsa, textCentros, textDiscoMovil, textPrecio;

        textDetalle = findViewById(R.id.textDetalle);
        textSillas = findViewById(R.id.textSillas);
        textMesas = findViewById(R.id.textMesas);
        textCatering = findViewById(R.id.textCatering);
        textComparsa = findViewById(R.id.textComparsa);
        textCentros = findViewById(R.id.textCentros);
        textDiscoMovil = findViewById(R.id.textDiscoMovil);
        textPrecio = findViewById(R.id.textPrecio);

        String Detalle = textDetalle.getText().toString();
        String Sillas = textSillas.getText().toString();
        String Mesas = textMesas.getText().toString();
        String Catering = textCatering.getText().toString();
        String Comparsa = textComparsa.getText().toString();
        String Centros = textCentros.getText().toString();
        String DiscoMovil = textDiscoMovil.getText().toString();
        String Precio = textPrecio.getText().toString();

        try {
            if (Detalle.isEmpty() || Sillas.isEmpty() || Mesas.isEmpty() || Catering.isEmpty() || Comparsa.isEmpty() || Centros.isEmpty() || DiscoMovil.isEmpty() || Precio.isEmpty()) {
                Toast.makeText(Inicio.this, "Faltan datos por ingresar", Toast.LENGTH_SHORT).show();
                return;
            } else if (conn != null) {
                // Obtener la cadena base64 de la imagen
                String fotoBase64 = convertirImagen(imageUri);
                Log.i("Longitud de la cadena Base64", String.valueOf(fotoBase64.length()));
                if (fotoBase64.length() <= 36000) {
                    String Catalogo = "INSERT INTO Catalogo(Detalle, Mesas, Sillas, Catering, Comparsa, DiscoMovil, CentrosMesa, Monto, Foto) VALUES('" + Detalle + "', " + Sillas + ", " + Mesas + ", '" + Catering + "', '" + Comparsa + "', '" + Centros + "', '" + DiscoMovil + "', '" + Precio + "','" + fotoBase64 + "')";
                    Statement stCatalogo = conn.createStatement();
                    int affectedRows = stCatalogo.executeUpdate(Catalogo);
                    if (affectedRows > 0) {
                        Toast.makeText(Inicio.this, "Catálogo registrado", Toast.LENGTH_SHORT).show();
                        textDetalle.setText("");
                        textSillas.setText("");
                        textMesas.setText("");
                        textCatering.setText("");
                        textComparsa.setText("");
                        textCentros.setText("");
                        textDiscoMovil.setText("");
                        textPrecio.setText("");
                } else {
                    Toast.makeText(Inicio.this, "La imagen es demasiado grande", Toast.LENGTH_SHORT).show();
                }



                } else {
                    Toast.makeText(Inicio.this, "No se pudo registrar el catálogo", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception exception) {
            Log.e("Error al agregar catálogo", exception.getMessage());
        }
    }


    //-------------------------------------------Elimina el Catalogo por el id--------------------------------------------------------

    public void btnBorrarCatalogo(View view) {
        SqlServerConn connection = new SqlServerConn();
        Connection conn = connection.conexionSql();

        EditText textId;
        textId = findViewById(R.id.textID);

        String id = textId.getText().toString();

        try {
            if (id.isEmpty()) {
                Toast.makeText(Inicio.this, "Ingrese el ID que desea eliminar", Toast.LENGTH_SHORT).show();
                return;
            } else if (conn != null) {
                String query = "DELETE FROM Catalogo WHERE ID = '" + id + "'";
                Statement st = conn.createStatement();
                int affectedRows = st.executeUpdate(query);

                if (affectedRows > 0) {
                    Toast.makeText(Inicio.this, "Catálogo eliminado", Toast.LENGTH_SHORT).show();
                    textId.setText("");
                } else {
                    Toast.makeText(Inicio.this, "No se pudo eliminar el catálogo", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception exception) {
            Log.e("Error", exception.getMessage());
        }
    }

    //-------------------------------------------Busca el Catalogo por el id--------------------------------------------------------
    public void btnBuscarCatalogo(View view) {
        SqlServerConn connection = new SqlServerConn();
        String ID = ((EditText) findViewById(R.id.textID)).getText().toString();

        try (Connection conn = connection.conexionSql();
             Statement st = conn.createStatement()) {

            if (ID.isEmpty()) {
                Toast.makeText(Inicio.this, "Ingresar ID del catálogo que desea buscar", Toast.LENGTH_SHORT).show();
                return;
            }

            String query = "SELECT Detalle, Mesas, Sillas, Catering, Comparsa, DiscoMovil, CentrosMesa, Monto, Foto FROM Catalogo WHERE ID = '" + ID + "'";
            try (ResultSet rs = st.executeQuery(query)) {

                if (rs.next()) {

                    String detalle = rs.getString(1);
                    String sillas = rs.getString(2);
                    String mesas = rs.getString(3);
                    String catering = rs.getString(4);
                    String comparsa = rs.getString(5);
                    String discoMovil = rs.getString(6);
                    String centrosMesa = rs.getString(7);
                    String precio = rs.getString(8);
                    String imagen= rs.getString(9);


                    ((EditText) findViewById(R.id.textDetalle)).setText(detalle);
                    ((EditText) findViewById(R.id.textSillas)).setText(sillas);
                    ((EditText) findViewById(R.id.textMesas)).setText(mesas);
                    ((EditText) findViewById(R.id.textCatering)).setText(catering);
                    ((EditText) findViewById(R.id.textComparsa)).setText(comparsa);
                    ((EditText) findViewById(R.id.textDiscoMovil)).setText(discoMovil);
                    ((EditText) findViewById(R.id.textCentros)).setText(centrosMesa);
                    ((EditText) findViewById(R.id.textPrecio)).setText(precio);


                    Log.e("Cadena Base64", imagen);
                    Bitmap bitmap = descomponer(imagen);
                    ((ImageView) findViewById(R.id.imageView2)).setImageBitmap(bitmap);


                    Toast.makeText(Inicio.this, "Catálogo mostrado", Toast.LENGTH_SHORT).show();
                    Log.i("INFO", "Catálogo mostrado " + ID);
                } else {
                    Toast.makeText(Inicio.this, "No se encontró el catálogo con ID: " + ID, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception exception) {
            Log.e("Error", exception.getMessage());
        }
    }


    private Bitmap descomponer(String des) {
        Log.e("Lo que tomo por parametro", des);
        try {
            byte[] decodedBytes = Base64.decode(des, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            Log.e("Error decoding des", e.getMessage());
            e.printStackTrace(); // Agrega esta línea para obtener más detalles del error en Logcat.
            return null;
        }
    }



    //---------Editar catalogo  encontrado por medio del id----------------------------------------------
    public void btnActualizarCatalogo(View view) {
        SqlServerConn connection = new SqlServerConn();
        Connection conn = connection.conexionSql();

        EditText textID, textDetalle, textSillas, textMesas, textCatering, textComparsa, textCentros, textDiscoMovil, textPrecio;

        textID = findViewById(R.id.textID);
        textDetalle = findViewById(R.id.textDetalle);
        textSillas = findViewById(R.id.textSillas);
        textMesas = findViewById(R.id.textMesas);
        textCatering = findViewById(R.id.textCatering);
        textComparsa = findViewById(R.id.textComparsa);
        textCentros = findViewById(R.id.textCentros);
        textDiscoMovil = findViewById(R.id.textDiscoMovil);
        textPrecio = findViewById(R.id.textPrecio);

        String ID = textID.getText().toString();
        String Detalle = textDetalle.getText().toString();
        String Sillas = textSillas.getText().toString();
        String Mesas = textMesas.getText().toString();
        String Catering = textCatering.getText().toString();
        String Comparsa = textComparsa.getText().toString();
        String Centros = textCentros.getText().toString();
        String DiscoMovil = textDiscoMovil.getText().toString();
        String Precio = textPrecio.getText().toString();

        try {
            if (ID.isEmpty() || Detalle.isEmpty() || Sillas.isEmpty() || Mesas.isEmpty() || Catering.isEmpty() || Comparsa.isEmpty() || Centros.isEmpty() || DiscoMovil.isEmpty() || Precio.isEmpty()) {
                Toast.makeText(Inicio.this, "Faltan datos por ingresar", Toast.LENGTH_SHORT).show();
                return;
            } else if (conn != null) {

                String fotoBase64 = convertirImagen(imageUri);
                Log.i("Longitud de la cadena Base64", String.valueOf(fotoBase64.length()));
                if (fotoBase64.length() <= 36000) {
                    String query = "UPDATE Catalogo SET Detalle = '" + Detalle + "', Mesas = '" + Mesas + "', Sillas = '" + Sillas + "', Catering = '" + Catering + "', Comparsa = '" + Comparsa + "', DiscoMovil = '" + DiscoMovil + "', CentrosMesa = '" + Centros + "', Monto = '" + Precio + "', Foto = '" + fotoBase64 + "' WHERE ID = '" + ID + "'";
                Statement st = conn.createStatement();
                int affectedRows = st.executeUpdate(query);

                if (affectedRows > 0) {
                    Toast.makeText(Inicio.this, "Catálogo actualizado", Toast.LENGTH_SHORT).show();
                    textID.setText("");
                    textDetalle.setText("");
                    textSillas.setText("");
                    textMesas.setText("");
                    textCatering.setText("");
                    textComparsa.setText("");
                    textCentros.setText("");
                    textDiscoMovil.setText("");
                    textPrecio.setText("");
                } else {
                    Toast.makeText(Inicio.this, "La imagen es demasiado grande", Toast.LENGTH_SHORT).show();
                }
                } else {
                    Toast.makeText(Inicio.this, "No se pudo actualizar el catálogo", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception exception) {
            Log.e("Error", exception.getMessage());
        }
    }

    //-------------------------------------------Inicio mostrar catalogo--------------------------------------------------------------------


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.CerrarSesion){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_inicio);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}