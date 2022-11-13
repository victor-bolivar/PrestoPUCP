package com.example.prestopucp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsuarioTI_SolicitudesPendientes extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_ti_solicitudes_pendientes);

        // initialize firebase auth
        mAuth = FirebaseAuth.getInstance();

        // se verifica que el usuario este logeado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Intent intent = new Intent(this, Inicio.class);
            startActivity(intent);
            finish();
        }

    }

    public void logout(View view){
        Log.d("msg", "logout");
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, Inicio.class);
        startActivity(intent);
        finish();
    }
}