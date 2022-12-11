package com.example.prestopucp.usuarioAdmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.prestopucp.Constantes.Constante;
import com.example.prestopucp.R;
import com.example.prestopucp.UsuarioTI;
import com.example.prestopucp.dto.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsuarioTiAgregarActivity extends AppCompatActivity {

    Button btn_add_user,btn_add_imagenUser;
    EditText editText_nombre,editText_correo,editText_codigo;

    LinearLayout linearLayout_imagen_btn;

    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;

    StorageReference storageReference;
    String storege_path = "imagen_usuario";

    DatabaseReference databaseReference;

    private static final int COD_SEL_STORAGE=200;
    private static final int COD_SEL_IMAGE=300;

    private Uri image_url;
    String photo = "photo";
    String nombreUser,codigoUser,correoUser,imagenUrlUser;

    ProgressDialog progressDialog;
    User usuarioTI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_ti_agregar);
        mAuth = FirebaseAuth.getInstance();

        //obtener el id del user
        Intent intent = getIntent();
        usuarioTI = (User) intent.getSerializableExtra("usuarioTi");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        String id ="id_imagen_dni";

        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //campos de editText
        editText_nombre = findViewById(R.id.admin_agregarUsuario_nombre);
        editText_codigo = findViewById(R.id.admin_agregarUsuario_codigo);
        editText_correo = findViewById(R.id.admin_agregarUsuario_correo);

        System.out.println(usuarioTI);
//        Log.d("msg useeer", usuarioTI.getLlave());
        btn_add_user = findViewById(R.id.admin_agregarUsuario_btnCrear);
        btn_add_imagenUser = findViewById(R.id.admin_agregarUsuario_btnImagen);

        if (usuarioTI == null){
            this.setTitle("Agregar Usuario TI");
            //botones
            btn_add_imagenUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadPhoto();

                }
            });

            btn_add_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("msg","url de imagen : " + imagenUrlUser);



                    nombreUser = editText_nombre.getText().toString();
                    usuarioTI.setNombre(nombreUser);

                    codigoUser = editText_codigo.getText().toString();
                    Log.d("msg codigo usuario",codigoUser);
                    usuarioTI.setCodigo(codigoUser);

                    correoUser = editText_correo.getText().toString();
                    usuarioTI.setEmail(codigoUser);

                    if (nombreUser.isEmpty() || codigoUser.isEmpty() || correoUser.isEmpty()){
                        Toast.makeText(UsuarioTiAgregarActivity.this,"Por favor de ingresar sus datos",Toast.LENGTH_SHORT).show();
                    }else{
                        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

                        String email = "info@programacionextrema.com";
                        Matcher mather = pattern.matcher(correoUser);

                        if (mather.find() == true) {
                            progressDialog.setMessage("Guardando Usuario");

                            Log.d("msg","Guardando al usuario con su url de imagen");
                            String contra = "asdasd";

                            mAuth.createUserWithEmailAndPassword(correoUser,contra)
                                    .addOnCompleteListener(UsuarioTiAgregarActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()){
                                                task.getResult();
                                                //usuarioTI.setLlave();
                                            }
                                        }
                                    });

                            //nombre, String codigo, String email, String rol, String privilegio, String imagenUrl
                            FirebaseDatabase.getInstance().getReference("users")
                                    .push().setValue(new User(nombreUser,codigoUser,correoUser,"UsuarioTI","Alumno",imagenUrlUser))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d("msg","guardadro exitoso del usuario con urlimagen");
                                            Toast.makeText(UsuarioTiAgregarActivity.this, "Se guardo exitosamente",Toast.LENGTH_LONG );
                                        }
                                    });
                            progressDialog.dismiss();
                        }else {
                            Toast.makeText(UsuarioTiAgregarActivity.this,"Ingresar un correo valido",Toast.LENGTH_SHORT).show();
                        }

                    }


                }
            });

        }else{

            this.setTitle("Editar Usuario TI");

            btn_add_user.setText("Actualizar usuario Ti");

            obtenerUsuarioTi(usuarioTI.getLlave());

            btn_add_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String correoEdi = editText_correo.getText().toString().trim();
                    String nombreEdi = editText_nombre.getText().toString().trim();
                    String codigoEdi = editText_codigo.getText().toString().trim();
                    usuarioTI.setEmail(correoEdi);
                    usuarioTI.setNombre(nombreEdi);
                    usuarioTI.setCodigo(codigoEdi);
                    actualizarUsuarioTi(usuarioTI);
                    progressDialog.dismiss();
                    finish();
                }
            });

        }







    }

    private void actualizarUsuarioTi(User usuarioTI) {

        FirebaseDatabase.getInstance().getReference().child(Constante.DB_USERS)
                .child(usuarioTI.getLlave())
                .setValue(usuarioTI).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UsuarioTiAgregarActivity.this,"Se actualizo el usuario",Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void obtenerUsuarioTi(String llave) {

        FirebaseDatabase.getInstance()
                .getReference().child(Constante.DB_USERS).child(llave)
                .get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        User userData = (User) dataSnapshot.getValue(User.class);
                        editText_correo.setText(userData.getEmail());
                        editText_codigo.setText(userData.getCodigo());
                        editText_nombre.setText(userData.getNombre());

                        Log.d("msg usuario ti : ", userData.getCodigo());
                        Log.d("msg usuario ti: ", userData.getEmail());
                        Log.d("msg usuario ti : ", userData.getNombre());
                        Log.d("msg usuario ti : ", userData.getLlave());

                    }
                });

    }

    private void uploadPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i,COD_SEL_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK){
            if (requestCode == COD_SEL_IMAGE){
                image_url = data.getData();
                subirPhoto(image_url);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void subirPhoto(Uri image_url) {
        progressDialog.setMessage("agregando foto");
        progressDialog.show();
        StorageReference store_photo = FirebaseStorage.getInstance().getReference().child(storege_path);
        String rute_storage_photo = "usuarioAgregado -"+mAuth.getUid()+image_url.getLastPathSegment();
        StorageReference file_name=store_photo.child(rute_storage_photo);

        file_name.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                file_name.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imagenUrlUser = uri.toString();
                        usuarioTI.setImagenUrl(imagenUrlUser);
                        Log.d("msg","url de imagen usuario : " + usuarioTI.getImagenUrl());
                        //System.out.println(usuarioTI);
                        progressDialog.dismiss();
                        btn_add_imagenUser = findViewById(R.id.admin_agregarUsuario_btnImagen);
                        btn_add_imagenUser.setEnabled(false);
                        Toast.makeText(UsuarioTiAgregarActivity.this, "Se agrego dni del usuario", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}