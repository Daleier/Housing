package com.example.dam203.housing.controler;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dam203.housing.R;
import com.example.dam203.housing.dialog.DatePickerFragment;
import com.example.dam203.housing.model.FirebaseReferences;
import com.example.dam203.housing.model.Oferta;
import com.example.dam203.housing.model.OfertaDetallada;
import com.example.dam203.housing.model.Residencia;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNewOfferScreen extends AppCompatActivity {

    EditText titulo, descripcion, cp, direccion, maximo_personas, precio, fecha_inicio, fecha_fin;
    Spinner spinner_provincia;
    Button crear_oferta;
    FirebaseAuth mauth;
    FirebaseDatabase mDatabase;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Button btn_elgirImg;
    private ImageView imageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer_screen);
        mauth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        titulo = findViewById(R.id.titulo);
        descripcion = findViewById(R.id.descripcion);
        cp = findViewById(R.id.cp);
        direccion = findViewById(R.id.direccion);
        maximo_personas = findViewById(R.id.maximo_personas);
        precio = findViewById(R.id.precio);
        spinner_provincia = findViewById(R.id.provincia);
        fecha_inicio = findViewById(R.id.data_inicio);
        fecha_fin =  findViewById(R.id.data_fin);
        crear_oferta = findViewById(R.id.crear_oferta);

        btn_elgirImg = findViewById(R.id.btn_elegirIm);
        imageView = findViewById(R.id.imgView);

        gestionarEventos();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.nueva_oferta));
    }

    public void gestionarEventos() {
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
                boolean error = validarEntradas();
                if(!error){
                    String provincia = spinner_provincia.getSelectedItem().toString();
                    Date dia_inicio = new Date();
                    Date dia_fin = new Date();
                    try {
                        DateFormat format = new SimpleDateFormat("dd/MM/yyy");
                        dia_inicio = format.parse(fecha_inicio.getText().toString());
                        dia_fin = format.parse(fecha_fin.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Residencia residencia = new Residencia(mauth.getCurrentUser().getUid(), titulo.getText().toString(), descripcion.getText().toString(),
                            provincia, cp.getText().toString(), direccion.getText().toString() ,Float.parseFloat(precio.getText().toString()), Integer.parseInt(maximo_personas.getText().toString()),
                            0, 0);
                    DatabaseReference residencia_ref = mDatabase.getReference(FirebaseReferences.RESIDENCIA_REFERENCE);
                    String res_key = residencia_ref.push().getKey();
                    residencia_ref.child(res_key).setValue(residencia);
                    Oferta oferta = new Oferta(res_key, mauth.getCurrentUser().getUid(), "",
                            dia_inicio, dia_fin, 0, "");
                    mDatabase.getReference(FirebaseReferences.OFERTA_REFERENCE).push().setValue(new OfertaDetallada(oferta, residencia));
                    if(imageView.getDrawable() != null){
                        if(filePath != null)
                        {
                            StorageReference ref = storageReference.child("images/"+ res_key + "/imagen");
                            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddNewOfferScreen.this, "Fallo al subir la imagen", Toast.LENGTH_SHORT).show();
                                        Log.e("Fallo al subir imagen", e.getMessage());
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                                .getTotalByteCount());
                                    }
                                });
                        }
                    }
                    Intent intent = new Intent(AddNewOfferScreen.this, MainScreen.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btn_elgirImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.elegir_img)), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            Intent intent = new Intent(AddNewOfferScreen.this, MainScreen.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    private boolean validarEntradas() {
        boolean error = false;
        if(titulo.getText().toString().isEmpty()){
            titulo.setError(getString(R.string.campo_vacio));
            error = true;
        }
        if(descripcion.getText().toString().isEmpty()){
            descripcion.setError(getString(R.string.campo_vacio));
            error = true;
        }
        if(cp.getText().toString().isEmpty()){
            cp.setError(getString(R.string.campo_vacio));
            error = true;
        }
        if(direccion.getText().toString().isEmpty()){
            direccion.setError(getString(R.string.campo_vacio));
            error = true;
        }
        if(maximo_personas.getText().toString().isEmpty()){
            maximo_personas.setError(getString(R.string.campo_vacio));
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
        if(Integer.parseInt(maximo_personas.getText().toString()) < 1) {
            maximo_personas.setError(getString(R.string.no_negativo));
            error = true;
        }
        if(precio.getText().toString().isEmpty()){
            precio.setError(getString(R.string.campo_vacio));
            error = true;
        }else if(Integer.parseInt(precio.getText().toString()) < 1){
            precio.setError(getString(R.string.no_negativo));
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

}
