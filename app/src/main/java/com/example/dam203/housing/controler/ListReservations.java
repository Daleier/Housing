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

import java.util.ArrayList;


public class ListReservations extends AppCompatActivity {

    ArrayList<OfertaDetallada> list;
    ListView listView;
    OfertaAdapter adapter;
    FirebaseAuth mAuth;
    TextView sin_ofertas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_offers_screen);

        listView = findViewById(R.id.litview);
        list = new ArrayList<>();
        sin_ofertas = findViewById(R.id.sin_ofertas);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.mis_reservas));

        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference mDatabaseOff = FirebaseDatabase.getInstance().getReference(FirebaseReferences.OFERTA_REFERENCE);

            mDatabaseOff.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final OfertaDetallada oferta = dataSnapshot.getValue(OfertaDetallada.class);
                if(oferta.getId_cliente().equalsIgnoreCase(mAuth.getCurrentUser().getUid())){
                    list.add(oferta);
                }
                adapter = new OfertaAdapter(ListReservations.this, list);
                listView.setAdapter(adapter);
                if(list.size() < 1) {
                    sin_ofertas.setVisibility(View.INVISIBLE);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(ListReservations.this, MainScreen.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    public void gestionarEventos() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OfertaDetallada item = adapter.getItemAtPosition(position);
                Intent intent = new Intent(ListReservations.this, DetailReservationScreen.class);
                intent.putExtra("oferta", item);
                startActivity(intent);
            }
        });
    }
}

