package com.example.dam203.housing.controler;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.dam203.housing.R;
import com.example.dam203.housing.dialog.DatePickerFragment;

public class SearchOfferScreen extends AppCompatActivity {

    Spinner provincia;
    EditText num_personas, fecha;
    CheckBox check_provincia, check_personas, check_fecha;
    Button buscar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_offer);

        provincia = findViewById(R.id.provincia);
        num_personas = findViewById(R.id.num_total_personas);
        fecha = findViewById(R.id.fecha);

        check_provincia = findViewById(R.id.check_provincia);
        check_personas = findViewById(R.id.check_personas);
        check_fecha = findViewById(R.id.check_fecha);

        buscar = findViewById(R.id.buscar);
        gestionarEventos();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.buscar));
    }

    public void gestionarEventos() {
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchOfferScreen.this, ListAceptOffersScreen.class);
                if(check_fecha.isChecked() && !fecha.getText().toString().isEmpty()){
                    intent.putExtra("fecha", fecha.getText().toString());
                }
                if(check_personas.isChecked() && !num_personas.getText().toString().isEmpty()){
                    intent.putExtra("personas", Integer.parseInt(num_personas.getText().toString()));
                }
                if(check_provincia.isChecked()){
                    intent.putExtra("provincia", provincia.getSelectedItem().toString());
                }
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(SearchOfferScreen.this, MainScreen.class);
            startActivity(intent);
            finish();
        }
        return true;
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
}
