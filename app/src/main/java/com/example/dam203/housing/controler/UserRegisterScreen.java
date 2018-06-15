package com.example.dam203.housing.controler;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.dam203.housing.R;
import com.example.dam203.housing.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRegisterScreen extends AppCompatActivity {

    private EditText inputUser, inputPassword, inputNombre, inputApellidos;
    private Button btnSignUp;
    private FirebaseAuth auth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.nuevo_usuario));

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        inputUser = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.contraseña);
        inputNombre = (EditText) findViewById(R.id.nombre);
        inputApellidos = (EditText) findViewById(R.id.apellidos);
        btnSignUp = (Button) findViewById(R.id.registrar);
        iniciarListeners();
    }

    private void iniciarListeners(){
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputUser.getText().toString().trim();
                String pass = inputPassword.getText().toString().trim();
                final String nombre = inputNombre.getText().toString().trim();
                final String apellidos = inputApellidos.getText().toString().trim();

                if (email.isEmpty()) {
                    inputUser.setError(getString(R.string.email_vacio));
                    return;
                }

                if (pass.isEmpty() || pass.length() < 6) {
                    inputPassword.setError(getString(R.string.contraseña_invalida));
                    return;
                }

                if(nombre.isEmpty()) {
                    inputNombre.setError(getString(R.string.nombre_vacio));
                    return;
                }

                if(apellidos.isEmpty()){
                    inputApellidos.setError(getString(R.string.apellidos_vacio));
                }

                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(UserRegisterScreen.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("UserRegisterScreen",task.getException().toString());
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthUserCollisionException e) {
                                inputUser.setError(getString(R.string.usuario_existe));
                                inputUser.requestFocus();
                            } catch(Exception e) {
                                Log.e("UserRegisterScreen", e.getMessage());
                            }
                        } else {
                            crearNuevoUsuario(auth.getCurrentUser().getUid(), nombre, apellidos, auth.getCurrentUser().getEmail());
                            finish();
                        }
                    }
                });
            }
        });
        inputApellidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSignUp.callOnClick();
            }
        });
    }

    private void crearNuevoUsuario(String userId, String nombre, String apellidos, String email){
        Usuario usuario = new Usuario(nombre, apellidos, email);
        database.child("usuarios").child(userId).setValue(usuario);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(UserRegisterScreen.this, UserLoginScreen.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

}
