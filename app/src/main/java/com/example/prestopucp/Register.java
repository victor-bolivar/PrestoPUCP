package com.example.prestopucp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prestopucp.dto.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

    public void registroUsuario(View view){
        EditText editText_nombre = findViewById(R.id.register_nombre);
        EditText editText_codigo = findViewById(R.id.register_codigo);
        EditText editText_email = findViewById(R.id.register_email);
        EditText editText_password = findViewById(R.id.register_password);
        Spinner spinner_rol = (Spinner) findViewById(R.id.register_rol);

        String nombre = editText_nombre.getText().toString();
        String codigo = editText_codigo.getText().toString();
        String email = editText_email.getText().toString();
        String password = editText_password.getText().toString();
        String rol = spinner_rol.getSelectedItem().toString();

        // 1. se verifica que no hayan valores vacios
        if (    nombre.isEmpty() ||
                codigo.isEmpty() ||
                email.isEmpty() ||
                password.isEmpty() ||
                rol.isEmpty() ) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_LONG).show();
            return;
        }

        // se verifica que el codigo sea de 8 digitos alfanumerico
        if (    !isAlphaNumeric(codigo) || (codigo.length() != 8) ) {
            Toast.makeText(this, "Ingrese un código válido", Toast.LENGTH_LONG).show();
            return;
        }

        // 2. se crea el usuario https://firebase.google.com/docs/auth/android/password-auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // 3. si se crea satifactoriamente, se guarda la info en la real time database
                            if (task.isSuccessful()){

                                User user = new User(nombre,  codigo,  email,  rol, "Cliente", "");
                                FirebaseDatabase.getInstance().getReference("users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                // 4. una vez se registra el usuario, se envia la confirmacion por correo
                                                mAuth.getCurrentUser().sendEmailVerification();


                                                // 5. se cierra esta actividad y se vuelve al inicia para que se loguee
                                                finish();
                                                return;
                                            }
                                        });

                            }

                        } else {
                            // si falla, se muestra un Toast
                            Toast.makeText(Register.this, "Error en el registro", Toast.LENGTH_LONG).show();

                        }
                    }
                });


    }


    public static boolean isAlphaNumeric(String s) {
        return s != null && s.matches("^[a-zA-Z0-9]*$");
    }


}