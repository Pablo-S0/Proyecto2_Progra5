package com.example.proyecto2_progra5;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogoAdapter {
    Context context;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    public List<Map<String, String>> getlist() {
        List<Map<String, String>> data = null;
        data = new ArrayList<Map<String, String>>();
        try {
            SqlServerConn connection = new SqlServerConn();
            Connection conn = connection.conexionSql();
            if (conn != null) {
                String query = "SELECT ID, Detalle, Mesas, Sillas, Catering, Comparsa, DiscoMovil, CentrosMesa, Monto, Foto FROM Catalogo";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    Map<String,String> dtname = new HashMap<String,String>();
                    dtname.put("listID","ID: "+rs.getString("ID"));
                    dtname.put("listDetalle","Detalle: "+rs.getString("Detalle"));
                    dtname.put("listMesas","Cantidad de mesas: "+rs.getString("Mesas"));
                    dtname.put("listSillas","Cantidad de sillas: "+rs.getString("Sillas"));
                    dtname.put("listCatering","Catering: "+rs.getString("Catering"));
                    dtname.put("listComparsa","Comparsa: "+rs.getString("Comparsa"));
                    dtname.put("listDiscoMovil","Disco movil: "+rs.getString("DiscoMovil"));
                    dtname.put("listCentros","Centros de mesa: "+rs.getString("CentrosMesa"));
                    dtname.put("listMonto","Precio: "+rs.getString("Monto"));
                    dtname.put("listimagen", rs.getString("Foto"));
                    data.add(dtname);
                }
                ConnectionResult = "Success";
                isSuccess=true;
                conn.close();
            }
            else{
                ConnectionResult= "Failed";
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return data;
    }

}
