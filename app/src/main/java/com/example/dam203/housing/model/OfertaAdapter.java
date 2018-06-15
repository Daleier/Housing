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

public class OfertaAdapter extends BaseAdapter {

    Activity context;
    ArrayList<OfertaDetallada> oferta;
    private static LayoutInflater inflater = null;

    public OfertaAdapter(Activity context, ArrayList<OfertaDetallada> oferta) {
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
        itemView = (itemView == null) ? inflater.inflate(R.layout.elemento_lista_oferta, null) : itemView;
        TextView titulo = itemView.findViewById(R.id.titulo);
        TextView precio = itemView.findViewById(R.id.precio);
        TextView provincia = itemView.findViewById(R.id.provincia);
        TextView fecha = itemView.findViewById(R.id.fecha);
        TextView num_reseñas = itemView.findViewById(R.id.reseñas);
        TextView puntuacion = itemView.findViewById(R.id.puntuacion);

        OfertaDetallada ofertaDet = oferta.get(position);
        titulo.setText(ofertaDet.getTitulo());
        precio.setText(Float.toString(ofertaDet.getPrecio()) + "€/día");
        provincia.setText(ofertaDet.getProvincia());
        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");
        try{
            String data_inicio = sdfr.format(ofertaDet.getDia_inicio());
            String data_fin = sdfr.format(ofertaDet.getDia_fin());
            fecha.setText(data_inicio+" - "+data_fin);
        }catch (Exception ex ){
            System.out.println(ex);
        }
        num_reseñas.setText(ofertaDet.getNum_total_personas() + " reseñas");
        try{
            puntuacion.setText(ofertaDet.getPuntuacion_total()/ofertaDet.getNum_total_personas() + "*");
        }catch (ArithmeticException ex){
            puntuacion.setText("0*");
        }
        return itemView;
    }
}
