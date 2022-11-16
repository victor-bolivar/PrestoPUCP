package com.example.prestopucp.usuarioti;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class uti_editardispositivo extends AppCompatActivity {

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


        // TODO copiar y pegar funcionalidad para seleecionar nuevas imagenes
        // TODO borrar imagenes actuales de firestorage en base a su url / https://stackoverflow.com/questions/42930619/how-to-delete-image-from-firebase-storage

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
}