package com.example.dam203.housing.controler;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dam203.housing.R;
import com.example.dam203.housing.model.FirebaseReferences;
import com.example.dam203.housing.model.OfertaAdapter;
import com.example.dam203.housing.model.OfertaDetallada;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ListAceptOffersScreen extends AppCompatActivity {

    FirebaseAuth mAuth;
    Date fecha;
    ArrayList<OfertaDetallada> list;
    ListView listView;
    OfertaAdapter adapter;
    TextView sin_ofertas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_offers_screen);
        listView = findViewById(R.id.litview);
        list = new ArrayList<>();
        sin_ofertas = findViewById(R.id.sin_ofertas);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.todas_ofertas));

        final int personas = getIntent().getIntExtra("personas", 0);
        final String provincia = getIntent().getStringExtra("provincia");
        final String str_fecha = getIntent().getStringExtra("fecha");
        if(str_fecha != null) {
            try{
                DateFormat format = new SimpleDateFormat("dd/MM/yyy");
                fecha = format.parse(str_fecha);
            }catch(ParseException ex){
                ex.getStackTrace();
            }
        }
        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference mDatabaseOff = FirebaseDatabase.getInstance().getReference(FirebaseReferences.OFERTA_REFERENCE);

        mDatabaseOff.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final OfertaDetallada oferta = dataSnapshot.getValue(OfertaDetallada.class);
                if(!mAuth.getCurrentUser().getUid().equalsIgnoreCase(oferta.getId_usuario())) {
                    if(Calendar.getInstance().getTime().before(oferta.getDia_fin())){
                        if (oferta.getId_cliente().isEmpty() || oferta.getId_cliente().equalsIgnoreCase("")) { // ofertas sin cliente
                            if (str_fecha != null) {
                                if (!str_fecha.isEmpty()) { // con filtro de fecha
                                    if (oferta.getDia_inicio().before(fecha) || oferta.getDia_inicio().compareTo(fecha) == 0) {
                                        if (oferta.getDia_fin().after(fecha) || oferta.getDia_fin().compareTo(fecha) == 0) {
                                            if (provincia != null) { // con provincia
                                                if (!provincia.isEmpty() && provincia.equalsIgnoreCase(oferta.getProvincia())) {
                                                    if (personas >= oferta.getNum_total_personas()) { // con filtro de personas
                                                        list.add(oferta);
                                                    } else { // sin filtro de personas
                                                        list.add(oferta);
                                                    }
                                                }
                                            } else { // sin provincia
                                                if (personas >= oferta.getNum_total_personas()) { // con filtro de personas
                                                    list.add(oferta);
                                                } else { // sin filtro de personas
                                                    list.add(oferta);
                                                }
                                            }
                                        }
                                    }
                                }
                            } else { // sin filtro de fecha
                                if (provincia != null) { // con provincia
                                    if (!provincia.isEmpty() && provincia.equalsIgnoreCase(oferta.getProvincia())) {
                                        if (personas >= oferta.getNum_total_personas()) { // con filtro de personas
                                            list.add(oferta);
                                        } else { // sin filtro de personas
                                            list.add(oferta);
                                        }
                                    }
                                } else { // sin provincia
                                    if (personas >= oferta.getNum_total_personas()) { // con filtro de personas
                                        list.add(oferta);
                                    } else { // sin filtro de personas
                                        list.add(oferta);
                                    }
                                }
                            }
                            adapter = new OfertaAdapter(ListAceptOffersScreen.this, list);
                            listView.setAdapter(adapter);
                            if (list.size() < 1) {
                                sin_ofertas.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        gestionarEventos();
    }

    public void gestionarEventos() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OfertaDetallada item = adapter.getItemAtPosition(position);
                Intent intent = new Intent(ListAceptOffersScreen.this, DetailAceptOfferScreen.class);
                intent.putExtra("oferta", item);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(ListAceptOffersScreen.this, MainScreen.class);
            startActivity(intent);
            finish();
        }
        return true;
    }


}
