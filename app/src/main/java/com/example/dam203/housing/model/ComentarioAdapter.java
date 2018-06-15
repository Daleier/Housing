package com.example.dam203.housing.model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dam203.housing.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class ComentarioAdapter extends BaseAdapter {

    Activity context;
    ArrayList<OfertaDetallada> oferta;
    private static LayoutInflater inflater = null;

    public ComentarioAdapter(Activity context, ArrayList<OfertaDetallada> oferta) {
        this.context = context;
        this.oferta = oferta;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return oferta.size();
    }

    @Override
    public OfertaDetallada getItem(int position) {
        return oferta.get(position);
    }

    public OfertaDetallada getItemAtPosition(int position) {
        return oferta.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.elemento_lista_comentario, null) : itemView;
        OfertaDetallada ofertaDet = oferta.get(position);
        TextView comentario = itemView.findViewById(R.id.comentario);
        TextView puntuacion = itemView.findViewById(R.id.puntuacion);

        comentario.setText(ofertaDet.getComentario());
        puntuacion.setText("Puntuación: " + ofertaDet.getPuntuacion());

        return itemView;
    }

}
