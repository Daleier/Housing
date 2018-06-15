package com.example.dam203.housing.controler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.dam203.housing.R;
import com.example.dam203.housing.model.FirebaseReferences;
import com.example.dam203.housing.model.Oferta;
import com.example.dam203.housing.model.OfertaDetallada;
import com.example.dam203.housing.model.Residencia;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCommentScreen extends AppCompatActivity {

    OfertaDetallada oferta;
    EditText reseña;
    Spinner spinner_puntuacion;
    Button aceptar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_offer);

        oferta = (OfertaDetallada) getIntent().getSerializableExtra("oferta");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.nueva_reseña));

        reseña = findViewById(R.id.reseña);
        spinner_puntuacion = findViewById(R.id.spinner_puntuacion);
        aceptar = findViewById(R.id.btn_aceptar);

        gestionarEventos();
    }

    public void gestionarEventos() {
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reseña.getText().toString().trim().isEmpty()){
                    reseña.setError(getString(R.string.campo_vacio));
                }else{
                    int puntuacion_nueva = Integer.parseInt(spinner_puntuacion.getSelectedItem().toString().substring(0,1));
                    oferta.setNum_total_personas(oferta.getNum_total_personas() + 1);
                    oferta.setPuntuacion_total(oferta.getPuntuacion_total() + puntuacion_nueva);
                    final DatabaseReference mDatabaseOff = FirebaseDatabase.getInstance().getReference(FirebaseReferences.OFERTA_REFERENCE);
                    mDatabaseOff.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            final OfertaDetallada ofertaFirebase = dataSnapshot.getValue(OfertaDetallada.class);
                            String key = dataSnapshot.getKey();
                            if(ofertaFirebase.getId_residencia().equals(oferta.getId_residencia())) {
                                if(ofertaFirebase.getDia_inicio().equals(oferta.getDia_inicio()) && ofertaFirebase.getDia_fin().equals(oferta.getDia_fin())){
                                    int puntuacion_nueva = Integer.parseInt(spinner_puntuacion.getSelectedItem().toString().substring(0,1));
                                    oferta.setComentario(reseña.getText().toString());
                                    oferta.setPuntuacion(puntuacion_nueva);
                                    mDatabaseOff.child(key).setValue(oferta);
                                }else{
                                    mDatabaseOff.child(key).setValue(oferta);
                                }
                                final DatabaseReference mDatabaseRes = FirebaseDatabase.getInstance().getReference(FirebaseReferences.RESIDENCIA_REFERENCE);
                                mDatabaseRes.child(ofertaFirebase.getId_residencia()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        final Residencia residencia = dataSnapshot.getValue(Residencia.class);
                                        residencia.setPuntuacion(oferta.getPuntuacion_total());
                                        residencia.setNum_personas(oferta.getNum_total_personas());
                                        mDatabaseRes.child(ofertaFirebase.getId_residencia()).setValue(residencia);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
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
            }
        });
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
