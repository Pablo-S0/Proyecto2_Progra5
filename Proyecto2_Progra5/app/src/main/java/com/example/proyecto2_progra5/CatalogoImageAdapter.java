package com.example.proyecto2_progra5;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class CatalogoImageAdapter extends BaseAdapter {
    private List<Map<String, String>> data;
    private Context context;

    public CatalogoImageAdapter(Context context, List<Map<String, String>> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_list, parent, false);
            holder = new ViewHolder();
            holder.textID = convertView.findViewById(R.id.listID);
            holder.textDetalle = convertView.findViewById(R.id.listDetalle);
            holder.textMesas = convertView.findViewById(R.id.listMesas);
            holder.textSillas = convertView.findViewById(R.id.listSillas);
            holder.textCatering = convertView.findViewById(R.id.listCatering);
            holder.textComparsa = convertView.findViewById(R.id.listComparsa);
            holder.textDisco = convertView.findViewById(R.id.listDiscoMovil);
            holder.textCentrosMesa = convertView.findViewById(R.id.listCentros);
            holder.textMonto = convertView.findViewById(R.id.listMonto);
            holder.imageView = convertView.findViewById(R.id.listimagen);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        String base64Image = data.get(position).get("listimagen");
        byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);


        holder.textID.setText(data.get(position).get("listID"));
        holder.textDetalle.setText(data.get(position).get("listDetalle"));
        holder.textMesas.setText(data.get(position).get("listMesas"));
        holder.textSillas.setText(data.get(position).get("listSillas"));
        holder.textCatering.setText(data.get(position).get("listCatering"));
        holder.textComparsa.setText(data.get(position).get("listComparsa"));
        holder.textDisco.setText(data.get(position).get("listDiscoMovil"));
        holder.textCentrosMesa.setText(data.get(position).get("listCentros"));
        holder.textMonto.setText(data.get(position).get("listMonto"));
        holder.imageView.setImageBitmap(bitmap);


        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textID;
        TextView textDetalle;
        TextView textMesas;
        TextView textSillas;
        TextView textCatering;
        TextView textComparsa;
        TextView textDisco;
        TextView textCentrosMesa;
        TextView textMonto;

    }
}