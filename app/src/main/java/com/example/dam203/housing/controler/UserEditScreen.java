package com.example.dam203.housing.controler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dam203.housing.R;
import com.example.dam203.housing.model.FirebaseReferences;
import com.example.dam203.housing.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class UserEditScreen extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private Usuario usuario;
    EditText email, nombre, apellidos, contraseña, contraseña_antigua;
    Button cambiar_nombre, cambiar_email, cambiar_contraseña;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_screen);
        mAuth = FirebaseAuth.getInstance();
        usuario = (Usuario) getIntent().getSerializableExtra("user");
        email = findViewById(R.id.email);
        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        contraseña = findViewById(R.id.contraseña);
        contraseña_antigua = findViewById(R.id.contraseña_antigua);
        email.setText(mAuth.getCurrentUser().getEmail());
        nombre.setText(usuario.getNombre());
        apellidos.setText(usuario.getApellidos());
        cambiar_nombre = findViewById(R.id.cambiar_nombre);
        cambiar_email = findViewById(R.id.cambiar_email);
        cambiar_contraseña = findViewById(R.id.cambiar_contraseña);
        gestionarEventos();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.editar_perfil);
    }

    public void gestionarEventos() {
        cambiar_nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error = false;
                if(nombre.getText().toString().isEmpty()){
                    nombre.setError(getString(R.string.nombre_vacio));
                    return;
                }
                if(apellidos.getText().toString().isEmpty()){
                    apellidos.setError(getString(R.string.apellidos_vacio));
                    error = true;
                }
                if(email.getText().toString().isEmpty()){
                    apellidos.setError(getString(R.string.email_vacio));
                    error = true;
                }
                if(error){
                    return;
                }

                if(usuario.getNombre() != nombre.getText().toString()){
                    usuario.setNombre(nombre.getText().toString());
                }
                if(usuario.getApellidos() != apellidos.getText().toString()){
                    usuario.setApellidos(apellidos.getText().toString());
                }
                FirebaseDatabase.getInstance().getReference(FirebaseReferences.USUARIOS_REFERENCE).child(mAuth.getCurrentUser().getUid()).setValue(usuario);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("user", usuario);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        cambiar_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mAuth.getCurrentUser().getEmail().equals(email.getText().toString())) {
                    mAuth.getCurrentUser().updateEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                usuario.setEmail(email.getText().toString());
                                Log.d("UserEditScreen", "User email address updated.");
                                FirebaseDatabase.getInstance().getReference(FirebaseReferences.USUARIOS_REFERENCE).child(mAuth.getCurrentUser().getUid()).setValue(usuario);
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("user", usuario);
                                setResult(Activity.RESULT_OK,returnIntent);
                                finish();
                            }else{
                                email.setError(getString(R.string.email_invalido));
                                return;
                            }
                        }
                    });
                }
            }
        });

        cambiar_contraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contraseña.getText().toString().isEmpty() || contraseña.getText().toString().length() < 6) {
                    contraseña.setError(getString(R.string.contraseña_invalida));
                }else{
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), contraseña_antigua.getText().toString() );
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(contraseña.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("Edit", "Contraseña actualizada");
                                                    FirebaseDatabase.getInstance().getReference(FirebaseReferences.USUARIOS_REFERENCE).child(mAuth.getCurrentUser().getUid()).setValue(usuario);
                                                    Intent returnIntent = new Intent();
                                                    setResult(Activity.RESULT_CANCELED,returnIntent);
                                                    finish();
                                                } else {
                                                    Log.d("Edit", "Contraseña no actualizada");
                                                    contraseña.setError(getString(R.string.contraseña_invalida));
                                                }
                                            }
                                        });
                                    } else {
                                        contraseña_antigua.setError(getString(R.string.contraseña_incorrecta));
                                        Log.d("Edit", "Autenticacion fallida");
                                    }
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
            setResult(Activity.RESULT_CANCELED,returnIntent);
            finish();
        }
        return true;
    }
}
