package com.example.prestopucp.usuarioti;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prestopucp.R;
import com.example.prestopucp.dto.Dispositivo;
import com.example.prestopucp.dto.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class uti_agegardispositivo extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    Button btn_agregarDispositivo;
    Button btn_seleccionarImagenes;
    TextView textView_numeroImagenesSeleccionadas;
    ProgressDialog progressDialog;
    ArrayList<Uri> imageList = new ArrayList<Uri>();
    private Uri imageUri;
    private int uploadCount = 0;

    private int numeroImagenesSeleccionadas;
    private ArrayList<String> listaUrlImagenes = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uti_agegardispositivo);

        // se cambia el titulo
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Agregar dispositivo");
        }

        // para la funcionalidad de que si el usuario selecciona 'Otro', pueda especificar el tipo de dispositivo
        Spinner spinner = findViewById(R.id.uti_agregardispsitivo_spinnertipo);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String item = spinner.getSelectedItem().toString();
                if (item.equals("Otro")) {
                    // si se selecciona "Otro" como opcion, se mostraria un EditText para q el usuario ingrese la opcion
                    EditText editText_especificarTipo = findViewById(R.id.uti_agregardispositivo_especificartipo);

                    final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT); // Width , height
                    lparams.setMargins(convertirDpPixel(48), convertirDpPixel(8), convertirDpPixel(48), 0);
                    editText_especificarTipo.setLayoutParams(lparams);
                } else {
                    // caso contrario se mantendria oculto ese editText
                    EditText editText_especificarTipo = findViewById(R.id.uti_agregardispositivo_especificartipo);

                    final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, 0); // Width , height
                    editText_especificarTipo.setLayoutParams(lparams);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // para la funcionalidad de seleccionar multiples imagenes
        btn_agregarDispositivo = findViewById(R.id.uti_agregardispositivo_btnCrearDispositivo);
        btn_seleccionarImagenes = findViewById(R.id.uti_agregardispositivo_btnseleccionar);
        textView_numeroImagenesSeleccionadas = findViewById(R.id.uti_agregardispositivo_numeroimagenes);

        btn_seleccionarImagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        // para finalmente guardar el dispositivo en firebase
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Guardando...");
        btn_agregarDispositivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(numeroImagenesSeleccionadas < 3){
                    // alert dialog para indicar al usuario que ingrese 3 imagenes como minimo
                    AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(uti_agegardispositivo.this);
                    alertdialogBuilder.setMessage("Por favor, seleccionar 3 imagenes como minimo.");
                    alertdialogBuilder.setCancelable(true);
                    alertdialogBuilder.setPositiveButton(
                            "Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = alertdialogBuilder.create();
                    alert11.show();

                    return;
                }


                Spinner spinnnerTipo = findViewById(R.id.uti_agregardispsitivo_spinnertipo);
                EditText editText_especificarTipo = findViewById(R.id.uti_agregardispositivo_especificartipo);
                String tipo = spinnnerTipo.getSelectedItem().toString();
                if (tipo.equals("Otro")) {
                    tipo = editText_especificarTipo.getText().toString();
                }

                EditText editText_marca = findViewById(R.id.uti_agregardispositivo_marca);
                String marca = editText_marca.getText().toString();

                EditText editText_caracteristicas = findViewById(R.id.uti_agregardispositivo_caracteristicas);
                String caracteristicas = editText_caracteristicas.getText().toString();

                EditText editText_incluye = findViewById(R.id.uti_agregardispositivo_incluye);
                String incluye = editText_incluye.getText().toString();

                EditText editText_stock = findViewById(R.id.uti_agregardispositivo_stock);
                String stockString = editText_stock.getText().toString();
                int stock;
                if (stockString.equals("") || !isInteger(stockString)){
                    stock = -1;
                } else {
                    stock = Integer.parseInt(stockString);
                }

                // validacion campos  vacios
                if (tipo.equals("") ||
                    marca.equals("") ||
                    caracteristicas.equals("") ||
                    incluye.equals("") ||
                    stock<0){

                    // alert dialog
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(uti_agegardispositivo.this);
                    builder2.setMessage("Por favor, ingrese valores validos.");
                    builder2.setCancelable(true);

                    builder2.setPositiveButton(
                            "Cancelar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert12 = builder2.create();
                    alert12.show();
                    return;

                }

                // 1. guardado de imagenes
                progressDialog.show();
                StorageReference imageFolder = FirebaseStorage.getInstance().getReference().child("dispositivos");
                for (uploadCount = 0; uploadCount < imageList.size(); uploadCount++) {

                    Uri individualImage = imageList.get(uploadCount);
                    StorageReference imageName = imageFolder.child(tipo + "-" + marca + "-" + individualImage.getLastPathSegment()); // nombre a guardar en Fire Storage
                    String finalTipo = tipo; // vainas del compilador

                    imageName.putFile(individualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    listaUrlImagenes.add(downloadUrl);

                                    if (listaUrlImagenes.size() == imageList.size()){
                                                // si es la ultima imagen, se guarda la info en RelTime Database

                                                String key = FirebaseDatabase.getInstance().getReference("dispositivos")
                                                        .push().getKey(); // para guardar un ID nuevo
                                                Dispositivo dispositivo = new Dispositivo(finalTipo, listaUrlImagenes, marca, stock, caracteristicas, incluye, key);

                                                FirebaseDatabase.getInstance().getReference("dispositivos").child(key).setValue(dispositivo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Log.d("msg / realtimedatabase", "se actualizo la info del dispositivo con la URL");

                                                            }
                                                        });

                                                progressDialog.dismiss();
                                                finish();

                                    }
                                }
                            });

                        }
                    });
                }
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE){

            if(resultCode == RESULT_OK){

                if (data.getClipData() != null){
                    int countClipData = data.getClipData().getItemCount();
                    Log.d("msg / n imagenes", String.valueOf(countClipData));
                    numeroImagenesSeleccionadas = countClipData;

                    if (numeroImagenesSeleccionadas == 1){
                        textView_numeroImagenesSeleccionadas.setText("Ha seleccionado 1 imagen");
                    } else{
                        textView_numeroImagenesSeleccionadas.setText("Ha seleccionado " +String.valueOf(numeroImagenesSeleccionadas) + " imagenes");
                    }

                    int currentImageSelect = 0;
                    while (currentImageSelect < countClipData){
                        imageUri = data.getClipData().getItemAt(currentImageSelect).getUri();
                        imageList.add(imageUri);
                        currentImageSelect++;
                    }


                }

            }
        }
    }

    public int convertirDpPixel(int dp){
        Resources r = this.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );

        return px;
    }

    public static boolean isInteger(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}