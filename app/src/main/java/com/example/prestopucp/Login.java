package com.example.prestopucp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // to hide title bar (la vaina que esta arriba que dice el nombre de la app "PrestoPucp")
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        // initialize firebase auth
        mAuth = FirebaseAuth.getInstance();

        // SE VERIFICA QUE SI ESTA LOGUEADO, SE CIERRA ESTA ACTIVIDAD
        if (mAuth.getCurrentUser() != null){
            Log.d("msg / usuario logeado", mAuth.getCurrentUser().getEmail());
            finish();
            return;
        }
    }

    public void iniciarSesion(View view){


        EditText editText_email = findViewById(R.id.login_email);
        EditText editText_password = findViewById(R.id.login_password);

        String email = editText_email.getText().toString();
        String password = editText_password.getText().toString();

        // se verifica que no hayan valores vacios
        if (    email.isEmpty() ||
                password.isEmpty() ) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_LONG).show();
            return;
        }

        // sino, se autentica al usuario
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // se regresa a la actrividad principal
                            finish();
                            return;

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d( "msg / InicioSesion", String.valueOf(task.getException()));

                        }
                    }
                });

    }

    public void olvideMiContraseña(View view){

        EditText editText_email = findViewById(R.id.login_email);

        String email = editText_email.getText().toString();

        // se verifica que haya ingresado el correo
        if (    email.isEmpty() ) {
            Toast.makeText(this, "Por favor, ingrese su email para enviar el correo de restauracion de contraseña", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Login.this, "Correo enviado enviado para la restauracion de contraseña", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Login.this, "Error en el proceso. Por favor, intente de nuevo", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}