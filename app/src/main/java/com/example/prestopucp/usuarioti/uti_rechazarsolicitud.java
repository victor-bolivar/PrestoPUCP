package com.example.prestopucp.usuarioti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.prestopucp.R;

public class uti_rechazarsolicitud extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uti_rechazarsolicitud);

        // se cambia el titulo a 'Solicitudes pendientes' ya que es el Fragment por defecto
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Rechazar solicitud");
        }
    }

    public void mandarMotivo(View view){
        // se crea un nuevo intent
        Intent intent = new Intent();

        // se obtiene el motivo
        EditText editText = findViewById(R.id.uti_rechazarsolicitud_editTextTextMultiLine);
        String text = editText.getText().toString();

        // se regresa
        intent.putExtra("motivo", text);
        setResult(RESULT_OK, intent);
        finish();
    }
}