package com.example.prestopucp.usuarioti;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.TintableCheckedTextView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prestopucp.Inicio;
import com.example.prestopucp.R;
import com.example.prestopucp.UsuarioTI;
import com.example.prestopucp.dto.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class uti_micuenta<FirebaseFirestore> extends Fragment {

    public uti_micuenta() {
        super(R.layout.fragment_uti_micuenta);
    }

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    // PARA SUBIR LA IMAGEN A FIRESTORAGE
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private String storagePath = "imagen_usuario/";
    private static final int COD_SEL_STORAGE = 300;
    private static final int COD_SEL_IMAGE = 200;
    private Uri imagenUrl;
    ProgressDialog progressDialog;

    // info del usuario
    private String privilegio;
    private String codigo;
    private String email;
    private String nombre;
    private String urlImagen;

    // elementos de UI
    TextView textView_nombre;
    TextView textView_codigo;
    TextView textView_email;
    TextView textView_privilegio;
    ImageView imageView;

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

        // 1. Para mostrar los datos
        textView_nombre = view.findViewById(R.id.uti_micuenta_nombre);
        textView_codigo = view.findViewById(R.id.uti_micuenta_codigo);
        textView_email = view.findViewById(R.id.uti_micuenta_email);
        textView_privilegio = view.findViewById(R.id.uti_micuenta_privilegio);
        imageView = view.findViewById(R.id.uti_micuenta_imagen);
        cargarDatos();

        // 2. para subir la imagen

        ImageView imagen = view.findViewById(R.id.uti_micuenta_imagen);
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, COD_SEL_IMAGE);
            }
        });


        // 3. para LOGOUT
        Button btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("msg", "logout");
                FirebaseAuth.getInstance().signOut();

                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();

        if(resultCode == Activity.RESULT_OK){

            if(requestCode == COD_SEL_IMAGE){
                Log.d("msg / img", "se recibio la imagen en el fragment");
                imagenUrl = data.getData();


                // se sube la imagen
                String routeStoragePhoto = storagePath + mAuth.getCurrentUser().getUid();
                StorageReference reference = storageReference.child(routeStoragePhoto);
                reference.putFile(imagenUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl(); // uri que se va a asignar a la imagen
                        while (!uriTask.isSuccessful()){
                            // hasta que se almacene la imagen
                            // PARA ESTO, SE USA EL UID del usuario como nombre de la foto
                            if (uriTask.isSuccessful()){
                                uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        Log.d("msg / uri imagen", uri.toString());
                                        urlImagen = uri.toString();
                                        Toast.makeText(getActivity(), "Imagen subida correctamente", Toast.LENGTH_LONG).show();

                                        // se actualiza la data en RealTimeDatabase con la URL de la imagen
                                        User user = new User(nombre,  codigo,  email,  "UsuarioTI", "UsuarioTI", urlImagen);
                                        FirebaseDatabase.getInstance().getReference("users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Log.d("msg / realtimedatabase", "se actualizo la info del usuario con la URL");

                                                        // funcion refrescar datos
                                                        cargarDatos();
                                                    }
                                                });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(getActivity(), "Error al subir imagen", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void cargarDatos(){

        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("msg / firebase", "Error getting data", task.getException());
            }
            else {
                Log.d("msg / firebase", String.valueOf(task.getResult().getValue()));
                HashMap<String, String> data = (HashMap<String, String>) task.getResult().getValue();

                privilegio = data.get("privilegio");
                codigo = data.get("codigo");
                email = data.get("email");
                nombre = data.get("nombre");

                textView_nombre.setText(nombre);
                textView_codigo.setText(codigo);
                textView_email.setText(email);
                textView_privilegio.setText(privilegio);

                // se verifica si existe una imagen (por convencion)
                urlImagen = data.get("imagenUrl");
                if (urlImagen.equals("")){
                    imageView.setBackgroundResource(R.drawable.imagen_usuario_predefinida);
                    Log.d("msg", "imagen predefinida");
                } else {
                    // se coloca la imagen con Picasso
                    Picasso.with(getActivity())
                            .load(urlImagen)
                            .resize(180, 180)
                            .into(imageView);
                }


            }
        });

    }
}