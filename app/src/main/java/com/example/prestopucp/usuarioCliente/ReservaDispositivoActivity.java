package com.example.prestopucp.usuarioCliente;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prestopucp.Constantes.Constante;
import com.example.prestopucp.R;
import com.example.prestopucp.dto.Dispositivo;
import com.example.prestopucp.dto.ReservaDispositivo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ReservaDispositivoActivity extends AppCompatActivity {

    private Dispositivo dispositivo;
    Button btn_reservar;
    TextView dispositivoCli;
    EditText cursoCli,motivoCli,diasCli,programasCli,otrosCli;
    ImageView fotoDispositivoCli,add_user;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Uri imagenUrl;
    String dniUrl;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private ReservaDispositivo reservaDispositivo;
    private String storagePath = "imagen_dni/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_dispositivo);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Reservar dispositivo dispositivo");
        }

        Intent intent = getIntent();
        dispositivo = (Dispositivo) intent.getSerializableExtra("dispositivoReservar");
        dispositivoCli = findViewById(R.id.ucliente_solicitudes_dispositivo);
        dispositivoCli.setText(dispositivo.getTipo() + " " + dispositivo.getMarca());

        fotoDispositivoCli = findViewById(R.id.ucliente_solicitudes_imageView_Dispositivo);

        String urlImagen = dispositivo.getImagenes().get(0);
        Log.d("msg",urlImagen);
        if (urlImagen.equals("")){
            fotoDispositivoCli.setBackgroundResource(R.drawable.imagen_ejemplo_dispositivo);
            Log.d("msg", "imagen predefinida dispositivo");
        } else {
            // se coloca la imagen con Picasso
            Picasso.with(ReservaDispositivoActivity.this)
                    .load(urlImagen)
                    .resize(120, 120)
                    .into(fotoDispositivoCli);
        }

        //ucliente_solicitudes_imageView_dni

        btn_reservar = findViewById(R.id.ucliente_solicitudes_btnReservar);

        cursoCli = findViewById(R.id.ucliente_solicitudes_curso);
        motivoCli = findViewById(R.id.ucliente_solicitudes_motivo);
        diasCli = findViewById(R.id.ucliente_solicitudes_dias);
        programasCli = findViewById(R.id.ucliente_solicitudes_programas);
        otrosCli = findViewById(R.id.ucliente_solicitudes_otros);


        //add_user = findViewById(R.id.ucliente_solicitudes_imageView_dni);


        ImageView dniView = findViewById(R.id.ucliente_solicitudes_imageView_dni);
        dniView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1,200);
            }
        });

        Log.d("msg dni url aaa", dniUrl.toString().trim());

        btn_reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String curso = cursoCli.getText().toString().trim();
                String motivo = motivoCli.getText().toString().trim();
                String dias = motivoCli.getText().toString().trim();
                String programas = motivoCli.getText().toString().trim();
                String otros = motivoCli.getText().toString().trim();


                reservaDispositivo = new ReservaDispositivo();
                reservaDispositivo.setIdDispositivo(dispositivo.getKey());
                reservaDispositivo.setIdUsuario(mAuth.getCurrentUser().getUid());
                reservaDispositivo.setCurso(curso);
                reservaDispositivo.setMotivo(motivo);

                reservaDispositivo.setTiempoReserva(dias);

                reservaDispositivo.setProgramasNecesarios(programas);
                reservaDispositivo.setOtros(otros);
                reservaDispositivo.setEstadoReserva("Pendiente");


                reservaDispositivo.setDniUrl(dniUrl);


                String llaveReserva =  FirebaseDatabase.getInstance()
                        .getReference(Constante.DB_USERS)
                        .child(mAuth.getCurrentUser().getUid())
                        .child(Constante.DB_HISTORIAL_RESERVAS).push().getKey();

                reservaDispositivo.setIdReserva(llaveReserva);

                FirebaseDatabase.getInstance()
                        .getReference(Constante.DB_USERS).child(mAuth.getCurrentUser().getUid())
                        .child(Constante.DB_HISTORIAL_RESERVAS).child(llaveReserva)
                        .setValue(reservaDispositivo);

            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){

            if (requestCode==200){
                Log.d("msg dniurl", "se recibio la imagen en la activity");
                imagenUrl = data.getData();

                String routeStoragePhoto = storagePath + mAuth.getCurrentUser().getUid();
                StorageReference reference = storageReference.child(routeStoragePhoto);
                reference.putFile(imagenUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()){
                            btn_reservar.setEnabled(false);
                            if (uriTask.isSuccessful()){
                                uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Log.d("msg uri", uri.toString());
                                        Toast.makeText(ReservaDispositivoActivity.this, "Imagen subida correctamente", Toast.LENGTH_LONG).show();

                                        dniUrl = uri.toString();

                                    }
                                });
                            }
                        }
                    }
                });



            }
        }
    }
}