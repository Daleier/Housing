package com.example.dam203.housing.controler;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.dam203.housing.R;
import com.example.dam203.housing.dialog.DatePickerFragment;
import com.example.dam203.housing.model.FirebaseReferences;
import com.example.dam203.housing.model.OfertaDetallada;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddDetaildOfferScreen extends AppCompatActivity {

    EditText fecha_inicio, fecha_fin, precio;
    Button crear_oferta;
    OfertaDetallada oferta;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_detailed_offer);
        oferta =  (OfertaDetallada) getIntent().getSerializableExtra("oferta");

        fecha_inicio = findViewById(R.id.data_inicio);
        fecha_fin = findViewById(R.id.data_fin);
        precio = findViewById(R.id.precio);
        crear_oferta = findViewById(R.id.crear_oferta);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.nueva_oferta_detalle));
        gestionarEventos();
    }

    public void gestionarEventos(){
        fecha_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        fecha_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        crear_oferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error = validar();
                if(!error){
                    Date dia_inicio = new Date();
                    Date dia_fin = new Date();
                    try {
                        DateFormat format = new SimpleDateFormat("dd/MM/yyy");
                        dia_inicio = format.parse(fecha_inicio.getText().toString());
                        dia_fin = format.parse(fecha_fin.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    oferta.setDia_inicio(dia_inicio);
                    oferta.setDia_fin(dia_fin);
                    oferta.setPrecio(Float.parseFloat(precio.getText().toString()));
                    oferta.setId_cliente("");
                    final DatabaseReference mDatabaseOff = FirebaseDatabase.getInstance().getReference(FirebaseReferences.OFERTA_REFERENCE);
                    String key = mDatabaseOff.push().getKey();
                    mDatabaseOff.child(key).setValue(oferta);
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }

    private boolean validar(){
        boolean error = false;
        if(precio.getText().toString().isEmpty()){
            precio.setError(getString(R.string.campo_vacio));
            error = true;
        }else if(Integer.parseInt(precio.getText().toString()) < 1){
            precio.setError(getString(R.string.no_negativo));
            error = true;
        }
        if(fecha_inicio.getText().toString().isEmpty()){
            fecha_inicio.setError(getString(R.string.campo_vacio));
            error = true;
        }
        if(fecha_fin.getText().toString().isEmpty()){
            fecha_fin.setError(getString(R.string.campo_vacio));
            error = true;
        }
        DateFormat format = new SimpleDateFormat("dd/MM/yyy");
        if(!fecha_inicio.getText().toString().isEmpty() && !fecha_fin.getText().toString().isEmpty()){
            try {
                Date dia_inicio = format.parse(fecha_inicio.getText().toString());
                Date dia_fin = format.parse(fecha_fin.getText().toString());
                if(dia_fin.before(dia_inicio)){
                    fecha_fin.setError(getString(R.string.fecha_no_valida));
                    error = true;
                    Log.d("error", String.valueOf(error));
                }
            } catch (ParseException e) {
                e.printStackTrace();
                fecha_inicio.setError(getString(R.string.fecha_no_valida));
                fecha_fin.setError(getString(R.string.fecha_no_valida));
                error = true;
            }
        }
        return error;
    }

    private void showDatePickerDialog(final View view) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String cero_dia = "", cero_mes = "";
                if(day < 10){
                    cero_dia = "0";
                }
                if(month+1 < 10){
                    cero_mes = "0";
                }
                final String selectedDate = cero_dia + "" + day + "/" + cero_mes + "" + (month+1) + "/" + year;
                EditText texto = findViewById(view.getId());
                texto.setText(selectedDate);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
        return true;
    }
}
