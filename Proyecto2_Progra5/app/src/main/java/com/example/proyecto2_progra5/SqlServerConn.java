package com.example.proyecto2_progra5;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;


public class SqlServerConn {
    @SuppressLint("NewApi")
    public Connection conexionSql(){
        Connection con = null;
        String ip = "192.168.0.28", port = "56791", username = "Danny", password = "12345678", databasename = "SALON_COMUNALProgra5";
        StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(tp);

        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connectionUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databasename=" + databasename + ";User=" + username + ";password=" + password + ";";
            con = DriverManager.getConnection(connectionUrl);
        }
        catch (Exception exception){
            Log.e("Error", exception.getMessage());
        }
        return con;
    }

}
