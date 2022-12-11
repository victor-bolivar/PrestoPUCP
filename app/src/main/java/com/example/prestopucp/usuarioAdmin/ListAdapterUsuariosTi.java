package com.example.prestopucp.usuarioAdmin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prestopucp.R;
import com.example.prestopucp.dto.User;
import com.example.prestopucp.usuarioti.uti_editardispositivo;
import com.squareup.picasso.Picasso;

public class ListAdapterUsuariosTi extends RecyclerView.Adapter<ListAdapterUsuariosTi.UsuariosViewHolder> {

    // tiene que guardar la data
    private User[] listaUsuarios;
    // para guardar el contexto de la actividad donde se va a pintar el RecyclerView
    private Context context;
    TextView nombre,codigo,email;
    ImageView fotoDni;

    // viewholder / el que gestionara la interfaz a nivel de codigo
    public class UsuariosViewHolder extends RecyclerView.ViewHolder{

        User d;

        public UsuariosViewHolder(@NonNull View itemView){

            // TODO aca gestionar si, cuando se haga clic sobre un elemento se querra editarlo
            // ya que esta clase interna representa un solo elemento

            super(itemView);
        }
    }

    @NonNull
    @Override
    public UsuariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView =  LayoutInflater.from(context).inflate(R.layout.admin_usuariosti, parent, false);
        return new UsuariosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosViewHolder holder, int position) {
        User usuariosTi = listaUsuarios[position];
        holder.d = usuariosTi;

        nombre = holder.itemView.findViewById(R.id.admin_usuarioTi_nombre);
        email = holder.itemView.findViewById(R.id.admin_usuarioTi_email);
        codigo = holder.itemView.findViewById(R.id.admin_usuarioTi_codigo);
        fotoDni = holder.itemView.findViewById(R.id.admin_usuarioTi_imagen);

        nombre.setText(usuariosTi.getNombre());
        email.setText(usuariosTi.getEmail());
        codigo.setText(usuariosTi.getCodigo());
        String imagenDni = usuariosTi.getImagenUrl();
        if (imagenDni.equals("")){
            fotoDni.setBackgroundResource(R.drawable.dni_ejemplo);
        }else{
            Picasso.with(context).load(imagenDni).resize(120,120).into(fotoDni);
        }

        // 3. onClick para ir a editar
        ConstraintLayout constraintLayout = holder.itemView.findViewById(R.id.admin_usuariosTi_fondo);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UsuarioTiAgregarActivity.class);
                intent.putExtra("usuarioTi", usuariosTi);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaUsuarios.length;
    }

    public User[] getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(User[] listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
