package com.example.prestopucp.usuarioti;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

        if (text.equals("")){
            // alert dialog
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Ingrese un motivo.");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Cancelar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

        // se regresa
        intent.putExtra("motivo", text);
        setResult(RESULT_OK, intent);
        finish();
    }
}