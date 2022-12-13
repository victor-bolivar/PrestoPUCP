package com.example.prestopucp.usuarioti;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.prestopucp.R;
import com.example.prestopucp.dto.Dispositivo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Scanner;

public class uti_editardispositivo extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    Button btn_editarDispositivo;
    Button btn_seleccionarImagenes;
    TextView textView_numeroImagenesSeleccionadas;
    ProgressDialog progressDialog;
    ArrayList<Uri> imageList = new ArrayList<Uri>();
    private Uri imageUri;
    private int uploadCount = 0;

    private int numeroImagenesSeleccionadas;
    private ArrayList<String> listaUrlImagenes = new ArrayList<String>();

    private Boolean seModificaronImagenes = false;

    private Dispositivo dispositivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uti_editardispositivo);

        // se cambia el titulo
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Editar dispositivo");
        }

        // se obtienen los datos del dispositivo a editar
        Intent intent = getIntent();
        dispositivo = (Dispositivo) intent.getSerializableExtra("dispositivo");

        EditText editText_marca = findViewById(R.id.uti_editardispositivo_marca);
        EditText editText_caracteristicas = findViewById(R.id.uti_editardispositivo_caracteristicas);
        EditText editText_incluye = findViewById(R.id.uti_editardispositivo_incluye);
        EditText editText_stock = findViewById(R.id.uti_editardispositivo_stock);

        editText_marca.setText(dispositivo.getMarca());
        editText_caracteristicas.setText(dispositivo.getCaracteristicas());
        editText_incluye.setText(dispositivo.getIncluye());
        editText_stock.setText(String.valueOf(dispositivo.getStock()));

        Spinner spinner = findViewById(R.id.uti_editardispositivo_spinnertipo);
        if (dispositivo.getTipo().equals("Laptop")
                || dispositivo.getTipo().equals("Tableta")
                || dispositivo.getTipo().equals("Celular")
                || dispositivo.getTipo().equals("Monitor")){
            // si es uno de los tipos predefinidos
            spinner.setSelection(((ArrayAdapter<String>)spinner.getAdapter()).getPosition(dispositivo.getTipo()));
        } else{
            // sino, se muestra el EditText para que ingrese el tipo
            spinner.setSelection(((ArrayAdapter<String>)spinner.getAdapter()).getPosition("Otro"));

            EditText editText_especificarTipo = findViewById(R.id.uti_editardispositivo_especificartipo);
            final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT); // Width , height
            lparams.setMargins(convertirDpPixel(48), convertirDpPixel(8), convertirDpPixel(48), 0);
            editText_especificarTipo.setLayoutParams(lparams);
            editText_especificarTipo.setText(dispositivo.getTipo());
        }

        // para la funcionalidad de que si el usuario selecciona 'Otro', pueda especificar el tipo de dispositivo
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String item = spinner.getSelectedItem().toString();
                if (item.equals("Otro")) {
                    // si se selecciona "Otro" como opcion, se mostraria un EditText para q el usuario ingrese la opcion
                    EditText editText_especificarTipo = findViewById(R.id.uti_editardispositivo_especificartipo);

                    final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT); // Width , height
                    lparams.setMargins(convertirDpPixel(48), convertirDpPixel(8), convertirDpPixel(48), 0);
                    editText_especificarTipo.setLayoutParams(lparams);
                } else {
                    // caso contrario se mantendria oculto ese editText
                    EditText editText_especificarTipo = findViewById(R.id.uti_editardispositivo_especificartipo);

                    final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, 0); // Width , height
                    editText_especificarTipo.setLayoutParams(lparams);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // mostrar imagenes actuales
        LinearLayout linearLayout = findViewById(R.id.uti_editardispositivo_linearlayout);

        for (String imagenUrl : dispositivo.getImagenes()){
            //ImageView Setup
            ImageView imageView = new ImageView(this);
            //setting image resource
            imageView.setImageResource(R.drawable.imagen_ejemplo_dispositivo);
            //setting image position
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, convertirDpPixel(300)));
            //adding view to layout
            linearLayout.addView(imageView);

            Picasso.with(this)
                    .load(imagenUrl)
                    .resize(0, convertirDpPixel(300))
                    .into(imageView);
        }

        // para la funcionalidad de seleccionar multiples imagenes
        btn_editarDispositivo = findViewById(R.id.uti_editardispositivo_btnEditarDispositivo);
        btn_seleccionarImagenes = findViewById(R.id.uti_editardispositivo_btnseleccionar);
        textView_numeroImagenesSeleccionadas = findViewById(R.id.uti_editardispositivo_numeroimagenes);

        btn_seleccionarImagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        // para finalmente actualizar la info del dispositivo en firebase
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Guardando cambios...");
        btn_editarDispositivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Spinner spinnnerTipo = findViewById(R.id.uti_editardispositivo_spinnertipo);
                EditText editText_especificarTipo = findViewById(R.id.uti_editardispositivo_especificartipo);
                String tipo = spinnnerTipo.getSelectedItem().toString();
                if (tipo.equals("Otro")) {
                    tipo = editText_especificarTipo.getText().toString();
                }

                EditText editText_marca = findViewById(R.id.uti_editardispositivo_marca);
                String marca = editText_marca.getText().toString();

                EditText editText_caracteristicas = findViewById(R.id.uti_editardispositivo_caracteristicas);
                String caracteristicas = editText_caracteristicas.getText().toString();

                EditText editText_incluye = findViewById(R.id.uti_editardispositivo_incluye);
                String incluye = editText_incluye.getText().toString();

                EditText editText_stock = findViewById(R.id.uti_editardispositivo_stock);
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
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(uti_editardispositivo.this);
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

                progressDialog.show();

                if (seModificaronImagenes){
                    if(numeroImagenesSeleccionadas < 3){
                        // alert dialog para indicar al usuario que ingrese 3 imagenes como minimo
                        AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(uti_editardispositivo.this);
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

                    // borrado de las imagenes anteriores en base al URL
                    for(String urlImagenAntigua : dispositivo.getImagenes()){
                        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(urlImagenAntigua);
                        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // File deleted successfully
                                Log.d("msg / eliminarimagen", "onSuccess: deleted file");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Uh-oh, an error occurred!
                                Log.d("msg / eliminarimagen", "onFailure: did not delete file");
                            }
                        });
                    }

                    // guardado de las nuevas imagenes
                    StorageReference imageFolder = FirebaseStorage.getInstance().getReference().child("dispositivos");
                    for (uploadCount = 0; uploadCount < imageList.size(); uploadCount++) {

                        Uri individualImage = imageList.get(uploadCount);
                        StorageReference imageName = imageFolder.child(tipo + "-" + marca + "-" + individualImage.getLastPathSegment()); // nombre a guardar en Fire Storage
                        String finalTipo = tipo; // vainas del compilador
                        String finalKey = dispositivo.getKey(); // vainas del compilador

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

//                                        String key = dispositivo.getKey(); // KEY DEL ACTUAL DISPOSITIVO QUE SE ESTA EDITANDO
                                            Dispositivo dispositivo = new Dispositivo(finalTipo, listaUrlImagenes, marca, stock, caracteristicas, incluye, finalKey);

                                            FirebaseDatabase.getInstance().getReference("dispositivos").child(finalKey).setValue(dispositivo).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                else{
                    // se mantiene las fotos antiguas

                    Dispositivo nuevoDispositivo = new Dispositivo(tipo, dispositivo.getImagenes(), marca, stock, caracteristicas, incluye, dispositivo.getKey());

                    FirebaseDatabase.getInstance().getReference("dispositivos").child(dispositivo.getKey()).setValue(nuevoDispositivo).addOnCompleteListener(new OnCompleteListener<Void>() {
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

        // FUNCIONALIDAD: BORRAR DISPOSITIVO
        Button button_eliminarDispositivo = findViewById(R.id.uti_editardispositivo_btnEliminarDispositivo);
        button_eliminarDispositivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(uti_editardispositivo.this);
                builder1.setMessage("¿Està seguro que desea eliminar este dispositivo? No podrà recuperar esta informaciòn.");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Si",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                // borrado de las imagenes en base al URL
                                for(String urlImagenAntigua : dispositivo.getImagenes()){
                                    StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(urlImagenAntigua);
                                    photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // File deleted successfully
                                            Log.d("msg / eliminarimagen", "onSuccess: deleted file");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Uh-oh, an error occurred!
                                            Log.d("msg / eliminarimagen", "onFailure: did not delete file");
                                        }
                                    });
                                }

                                // borrado de la data en realtime database
                                FirebaseDatabase.getInstance().getReference("dispositivos").child(dispositivo.getKey()).removeValue();

                                // se cierra el activity
                                finish();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();




            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE){

            if(resultCode == RESULT_OK){

                if (data.getClipData() != null){

                    // se oculta el linear layout con las imagenes antiguas
                    View linearLayout = findViewById(R.id.uti_editardispositivo_linearlayout);
                    ((ViewGroup) linearLayout.getParent()).removeView(linearLayout);

                    // se guarda la info de las imagenes seleccionadas
                    int countClipData = data.getClipData().getItemCount();
                    Log.d("msg / n imagenes", String.valueOf(countClipData));
                    numeroImagenesSeleccionadas = countClipData;

                    if (numeroImagenesSeleccionadas == 1){
                        textView_numeroImagenesSeleccionadas.setText("Ha seleccionado 1 nueva imagen (se borraran las anteriores)");
                    } else{
                        textView_numeroImagenesSeleccionadas.setText("Ha seleccionado " +String.valueOf(numeroImagenesSeleccionadas) + " nuevas imagenes (se borraran las anteriores)");
                    }

                    int currentImageSelect = 0;
                    while (currentImageSelect < countClipData){
                        imageUri = data.getClipData().getItemAt(currentImageSelect).getUri();
                        imageList.add(imageUri);
                        currentImageSelect++;
                    }

                    // se indica que se modificaron las imagenes
                    seModificaronImagenes = true;


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