package com.example.dam203.housing.controler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.example.dam203.housing.R;
import com.example.dam203.housing.model.FirebaseReferences;
import com.example.dam203.housing.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainScreen extends AppCompatActivity {

    Usuario usuario;
    private FirebaseAuth mAuth;
    Button añadir_oferta, mis_ofertas, ver_ofertas, ofertas_aceptadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(FirebaseReferences.USUARIOS_REFERENCE);
        mDatabase.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("USUARIO", dataSnapshot.toString());
                usuario = dataSnapshot.getValue(Usuario.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", databaseError.getMessage());
            }
        });

        añadir_oferta = findViewById(R.id.añadir_oferta);
        mis_ofertas = findViewById(R.id.mis_ofertas);
        ver_ofertas = findViewById(R.id.ver_oferta);
        ofertas_aceptadas = findViewById(R.id.ofertas_aceptadas);
        gestionEventos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.salir:
                mAuth.signOut();
                Intent intent = new Intent(MainScreen.this, UserLoginScreen.class);
                startActivity(intent);
                finish();
                break;
            case R.id.ajustes:
                Intent settIntent = new Intent(MainScreen.this, UserEditScreen.class);
                settIntent.putExtra("user", usuario);
                startActivityForResult(settIntent, 1);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                usuario = (Usuario) data.getSerializableExtra("user");
            }else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("Main", "Datos no devueltos desde UserEditScreen");
            }
        }
    }

    public void gestionEventos() {
        añadir_oferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this, AddNewOfferScreen.class);
                startActivity(intent);
            }
        });

        mis_ofertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this, ListMyOffersScreen.class);
                startActivity(intent);
            }
        });

        ver_ofertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this, SearchOfferScreen.class);
                startActivity(intent);
            }
        });

        ofertas_aceptadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this, ListReservations.class);
                startActivity(intent);
            }
        });


    }


}
