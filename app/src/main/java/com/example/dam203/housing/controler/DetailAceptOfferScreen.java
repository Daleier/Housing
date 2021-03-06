package com.example.dam203.housing.controler;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dam203.housing.R;
import com.example.dam203.housing.model.FirebaseReferences;
import com.example.dam203.housing.model.OfertaAdapter;
import com.example.dam203.housing.model.OfertaDetallada;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class DetailAceptOfferScreen extends AppCompatActivity {

    TextView titulo, descripcion, provincia, cp, direccion, fecha_inicio, fecha_fin, precio, maximo_personas, puntuaciones, estado;
    Button reservar, nueva_oferta, eliminar_oferta, nueva_reseña;
    TextView text_reseñas;
    ImageView imgView;
    FirebaseStorage storage;
    StorageReference storageReference;
    ArrayList<OfertaDetallada> list;
    ListView listView;
    OfertaAdapter adapter;
    OfertaDetallada oferta;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);
        listView = findViewById(R.id.listView);
        list = new ArrayList<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.detalles));

        oferta = (OfertaDetallada) getIntent().getSerializableExtra("oferta");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        imgView = findViewById(R.id.imgView);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        StorageReference imagePath = storageReference.child("images/"+oferta.getId_residencia()+"/imagen");
        imagePath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                try{
                    Log.d("URL IMAGEN", task.getResult().toString());
                    Picasso.with(DetailAceptOfferScreen.this).load(task.getResult().toString()).into(imgView);
                }catch (RuntimeExecutionException ex){
                    Log.e("Excepcion task", task.getException().getMessage());
                }
            }
        });

        titulo = findViewById(R.id.titulo);
        descripcion = findViewById(R.id.descripcion);
        provincia = findViewById(R.id.provincia);
        cp = findViewById(R.id.cp);
        direccion = findViewById(R.id.direccion);
        fecha_inicio = findViewById(R.id.fecha_inicio);
        fecha_fin = findViewById(R.id.fecha_fin);
        precio = findViewById(R.id.precio);
        maximo_personas = findViewById(R.id.maximo_personas);
        puntuaciones = findViewById(R.id.puntuaciones);
        reservar = findViewById(R.id.reservar);
        nueva_oferta = findViewById(R.id.nueva_oferta);
        eliminar_oferta = findViewById(R.id.eliminar_oferta);
        reservar.setVisibility(View.VISIBLE);
        nueva_oferta.setVisibility(View.INVISIBLE);
        eliminar_oferta.setVisibility(View.INVISIBLE);
        nueva_reseña = findViewById(R.id.nueva_reseña);
        nueva_reseña.setVisibility(View.INVISIBLE);
        text_reseñas = findViewById(R.id.text_reseñas);

        titulo.setText(oferta.getTitulo());
        descripcion.setText(oferta.getDescripcion());
        provincia.setText(oferta.getProvincia());
        cp.setText(oferta.getCp());
        direccion.setText(oferta.getDireccion());
        DateFormat format = new SimpleDateFormat("dd/MM/yyy");
        String str_fecha_inicio = format.format(oferta.getDia_inicio());
        String str_fecha_fin = format.format(oferta.getDia_fin());
        fecha_inicio.setText(str_fecha_inicio);
        fecha_fin.setText(str_fecha_fin);
        precio.setText(Float.toString(oferta.getPrecio()) + "€");
        maximo_personas.setText(Integer.toString(oferta.getMaximo_personas()));
        int puntuacion_media;
        try{
            puntuacion_media =  oferta.getPuntuacion_total()/oferta.getNum_total_personas();
        }catch (ArithmeticException ex){
            puntuacion_media = 0;
        }
        puntuaciones.setText(Integer.toString(oferta.getNum_total_personas()) + " reseñas con un total de " +
                puntuacion_media + " estrellas");

        estado = findViewById(R.id.estado);
        if(Calendar.getInstance().getTime().after(oferta.getDia_fin())){
            if(oferta.getId_cliente().equalsIgnoreCase("")){
                estado.setText(getText(R.string.fuera_plazo));
            }else {
                estado.setText(getText(R.string.finalizado));
            }
            estado.setTextColor(getResources().getColor(R.color.red));
        } else {
            if(oferta.getId_cliente().equalsIgnoreCase("")){
                estado.setText(getText(R.string.disponible));
                estado.setTextColor(getResources().getColor(R.color.green));
            }else {
                estado.setText(getText(R.string.reservado));
                estado.setTextColor(getResources().getColor(R.color.red));
            }
        }

        final DatabaseReference mDatabaseOff = FirebaseDatabase.getInstance().getReference(FirebaseReferences.OFERTA_REFERENCE);

        mDatabaseOff.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final OfertaDetallada ofertaFire = dataSnapshot.getValue(OfertaDetallada.class);
                if(oferta.getId_residencia().equalsIgnoreCase(ofertaFire.getId_residencia())) {
                    if (ofertaFire.getComentario() != null) {
                        if (!oferta.getComentario().equalsIgnoreCase("")) {
                            list.add(ofertaFire);
                            adapter = new OfertaAdapter(DetailAceptOfferScreen.this, list);
                            listView.setAdapter(adapter);
                        }
                    }
                    if (list.size() < 1) {
                        text_reseñas.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.INVISIBLE);
                    } else {
                        text_reseñas.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.VISIBLE);
                    }
                    Log.d("tamaño lista", "list.size() " + list.size());
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
        reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference mDatabaseOff = FirebaseDatabase.getInstance().getReference(FirebaseReferences.OFERTA_REFERENCE);
                mDatabaseOff.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final OfertaDetallada ofertaFirebase = dataSnapshot.getValue(OfertaDetallada.class);
                        final String key = dataSnapshot.getKey();
                        if(ofertaFirebase.getId_cliente() != null){
                            if(ofertaFirebase.getId_cliente().equalsIgnoreCase("")){
                                if(oferta.getId_residencia().equalsIgnoreCase(ofertaFirebase.getId_residencia()) && oferta.getId_usuario().equalsIgnoreCase(ofertaFirebase.getId_usuario())
                                        && oferta.getDia_inicio().equals(ofertaFirebase.getDia_inicio()) && oferta.getDia_fin().equals(ofertaFirebase.getDia_fin())) { // comprueba que las ofertas sean iguales
                                    ofertaFirebase.setId_cliente(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    mDatabaseOff.child(key).setValue(ofertaFirebase);
                                    Toast.makeText(DetailAceptOfferScreen.this, R.string.reserva_realizada, Toast.LENGTH_LONG );
                                    Intent intent = new Intent(DetailAceptOfferScreen.this, MainScreen.class );
                                    startActivity(intent);
                                    finish();
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
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
