package com.example.prestopucp.usuarioAdmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.prestopucp.R;
import com.example.prestopucp.UsuarioTI;
import com.example.prestopucp.dto.User;

public class EditarUsuarioTiActivity extends AppCompatActivity {

    private User usuarioDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario_ti);

        // se cambia el titulo
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Editar Usuario TI");
        }

        Intent intent = getIntent();
        usuarioDto = (User) intent.getSerializableExtra("usuarioTi");

        EditText editText_usuario = findViewById(R.id.ucliente_editarUserCli_usuario);
        EditText editText_correo  = findViewById(R.id.ucliente_editarUserCli_correo);
        EditText editText_codigo  = findViewById(R.id.ucliente_editarUserCli_codigo);

        editText_usuario.setText(usuarioDto.getNombre());
        editText_correo.setText(usuarioDto.getEmail());
        editText_codigo.setText(usuarioDto.getCodigo());

        ImageView dniFoto = findViewById(R.id.uCliente_dnifoto);
        //dniFoto.set






    }
}