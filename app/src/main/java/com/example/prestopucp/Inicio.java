package com.example.prestopucp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.prestopucp.dto.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;

public class Inicio extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        // to hide title bar (la vaina que esta arriba que dice el nombre de la app "PrestoPucp")
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        // initialize firebase auth
        mAuth = FirebaseAuth.getInstance();

        // initialize firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // SE VERIFICA QUE SI ESTA LOGUEADO -> actividad solicitudes pendientes
        if (mAuth.getCurrentUser() != null){
            FirebaseUser currentUser = mAuth.getCurrentUser();
            Log.d("msg / i usuario logeado", mAuth.getCurrentUser().getUid()
            );

            mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("msg / firebase", "Error getting data", task.getException());
                        Toast.makeText(Inicio.this, "Error en obtener información del usuario", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Log.d("msg / firebase", String.valueOf(task.getResult().getValue()));
                        HashMap<String, String> data = (HashMap<String, String>) task.getResult().getValue();
                        String privilegio = data.get("privilegio");

                        switch(privilegio){
                            case "UsuarioTI":
                                Intent intent = new Intent(Inicio.this, UsuarioTI.class);
                                startActivity( intent );
                                break;

                            case "Cliente":
                                // TODO
                                Log.d("msg", "ir a pagina principal de cliente");
                                mAuth.signOut();
                                break;
                            case "Admin":
                                // TODO
                                Log.d("msg", "ir a pagina principal de Admin");
                                mAuth.signOut();
                                break;
                        }


                    }
                }
            });

        }
    }

    public void irRegistro(View view){
        Intent intent = new Intent(this, Register.class);
        startActivity( intent );
    }

    public void irLogin(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity( intent );
    }


//    public void onClickIniciarSesion(View view){
//        Intent signInIntent = AuthUI.getInstance()
//                .createSignInIntentBuilder()
//                .setAvailableProviders(Arrays.asList(
//                        new AuthUI.IdpConfig.EmailBuilder().build()
//                ))
//                .setLogo(R.drawable.logo_prestopucp)      // Set logo drawable
//                .setTheme(R.style.Theme_PrestoPUCP)      // Set theme
//                .setIsSmartLockEnabled(false)
//
//                .build();
//        signInLauncher.launch(signInIntent);
//    }
//
//    // Firebase UI (para inicio de sesion)
//    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
//            new FirebaseAuthUIActivityResultContract(), result -> {
//               onSignInResult(result);
//            }
//
//    );
//    private void onSignInResult(FirebaseAuthUIAuthenticationResult result){
//        IdpResponse response = result.getIdpResponse();
//        if (result.getResultCode() == RESULT_OK){
//            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//            Log.d("msg", "Firebase uid: "+user.getUid()); // TODO usar el UID para identificar a los usuarios admin, usuario TI y mandarlos a distintos flujos
//        } else {
//            Log.d("msg", "Se cancelo log in");
//        }
//    }


}