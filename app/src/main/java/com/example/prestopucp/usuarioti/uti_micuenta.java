package com.example.prestopucp.usuarioti;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prestopucp.Inicio;
import com.example.prestopucp.R;
import com.example.prestopucp.UsuarioTI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class uti_micuenta extends Fragment {

    public uti_micuenta() {
        super(R.layout.fragment_uti_micuenta);
    }

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        // initialize firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        TextView textView_nombre = view.findViewById(R.id.uti_micuenta_nombre);
        TextView textView_codigo = view.findViewById(R.id.uti_micuenta_codigo);
        TextView textView_email = view.findViewById(R.id.uti_micuenta_email);
        TextView textView_privilegio = view.findViewById(R.id.uti_micuenta_privilegio);

        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("msg / firebase", "Error getting data", task.getException());
            }
            else {
                Log.d("msg / firebase", String.valueOf(task.getResult().getValue()));
                HashMap<String, String> data = (HashMap<String, String>) task.getResult().getValue();

                String privilegio = data.get("privilegio");
                String codigo = data.get("codigo");
                String email = data.get("email");
                String nombre = data.get("nombre");

                textView_nombre.setText(nombre);
                textView_codigo.setText(codigo);
                textView_email.setText(email);
                textView_privilegio.setText(privilegio);

            }
        });


        return view;
    }
}