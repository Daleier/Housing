package com.example.dam203.housing.controler;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dam203.housing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class UserLoginScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText inputUser, inputPass;
    private Button btnSignIn, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        getSupportActionBar().setTitle(getString(R.string.iniciar_sesion));
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(UserLoginScreen.this, MainScreen.class);
            startActivity(intent);
            finish();
        }

        inputUser = (EditText) findViewById(R.id.login);
        inputPass = (EditText) findViewById(R.id.password);
        btnSignIn = (Button) findViewById(R.id.login_button);
        btnSignUp = (Button) findViewById(R.id.registrar_button);
        iniciarListeners();

    }

    private void iniciarListeners(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("UserLoginScreen", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d("UserLoginScreen", "onAuthStateChanged:signed_out");
                }
            }
        };
        btnSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String user = inputUser.getText().toString().trim();
                String pass = inputPass.getText().toString().trim();
                try {
                    Log.i("LOGIN", "Login in with " + user);
                    mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(UserLoginScreen.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            String user = inputUser.getText().toString().trim();
                            String password = inputPass.getText().toString().trim();
                            if (user.isEmpty() || !user.contains("@") || !user.contains(".")) {
                                Toast.makeText(getApplicationContext(), "Introduce correo!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (password.isEmpty() || password.length() < 6) {
                                inputPass.setError(getString(R.string.contraseña_invalida));
                                return;
                            }
                            if (!task.isSuccessful()) {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    inputUser.setError(getString(R.string.email_incorrecto));
                                    inputUser.requestFocus();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    inputPass.setError(getString(R.string.contraseña_incorrecta));
                                    inputPass.requestFocus();
                                } catch (Exception e) {
                                    Log.e("UserLoginScreen", e.getMessage() + " - " + e.getClass().toString());
                                }
                            } else {
                                Log.i("UserLoginScreen",
                                        "Display Name: " + mAuth.getCurrentUser().getDisplayName() +
                                                "\nEmail: " + mAuth.getCurrentUser().getEmail() +
                                                "\nUID: " + mAuth.getCurrentUser().getUid()
                                );
                                Intent intent = new Intent(UserLoginScreen.this, MainScreen.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }catch (IllegalArgumentException ex){
                    Toast.makeText(getApplicationContext(), "Introduce correo y contraseña!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserLoginScreen.this, UserRegisterScreen.class));
            }
        });

        inputPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSignIn.callOnClick();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
