package com.example.prestopucp.usuarioti;

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
import androidx.core.widget.TintableCheckedTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prestopucp.R;
import com.example.prestopucp.dto.Dispositivo;
import com.squareup.picasso.Picasso;

public class ListaDispositivosAdapter extends RecyclerView.Adapter<ListaDispositivosAdapter.DispositivoViewHolder> {

    // tiene que guardar la data
    private Dispositivo[] listaDispositivos;
    // para guardar el contexto de la actividad donde se va a pintar el RecyclerView
    private Context context;

    // viewholder / el que gestionara la interfaz a nivel de codigo
    public class DispositivoViewHolder extends RecyclerView.ViewHolder{

        Dispositivo d;

        public DispositivoViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }

    @NonNull
    @Override
    public DispositivoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView =  LayoutInflater.from(context).inflate(R.layout.uti_itemdispositivo, parent, false);
        return new DispositivoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DispositivoViewHolder holder, int position) {
        Dispositivo dispositivo = listaDispositivos[position];
        holder.d = dispositivo;

        // 1. se colocan los datos
        TextView textView_tipo = holder.itemView.findViewById(R.id.uti_dispositivo_tipo);
        TextView textView_marca = holder.itemView.findViewById(R.id.uti_dispositivo_marca);
        TextView textView_stock = holder.itemView.findViewById(R.id.uti_dispositivo_stock);
        TextView textView_caracteristicas = holder.itemView.findViewById(R.id.uti_dispositivo_caracteristicas);
        TextView textView_incluye = holder.itemView.findViewById(R.id.uti_dispositivo_incluye);

        textView_tipo.setText(dispositivo.getTipo());
        textView_marca.setText(dispositivo.getMarca());
        textView_stock.setText(String.valueOf(dispositivo.getStock()));
        textView_caracteristicas.setText(dispositivo.getCaracteristicas());
        textView_incluye.setText(dispositivo.getIncluye());

        // 2. se obtiene la imagen
        ImageView imageView = holder.itemView.findViewById(R.id.uti_dispositivo_imagen);

        // se obtiene la url de la primera imagenes para mostrarla en el listado de dispositivos
        String primeraImagenUrl = dispositivo.getImagenes().get(0);
        if (primeraImagenUrl.equals("")){
            imageView.setBackgroundResource(R.drawable.imagen_ejemplo_dispositivo);
            Log.d("msg", "imagen predefinida dispositivo");
        } else {
            // se coloca la imagen con Picasso
            Picasso.with(context)
                    .load(primeraImagenUrl)
                    .resize(120, 120)
                    .into(imageView);
        }

        // 3. onClick para ir a editar
        ConstraintLayout constraintLayout = holder.itemView.findViewById(R.id.uti_dispositivo_fondo);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, uti_editardispositivo.class);
                intent.putExtra("dispositivo", dispositivo);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaDispositivos.length;
    }

    public Dispositivo[] getListaDispositivos() {
        return listaDispositivos;
    }

    public void setListaDispositivos(Dispositivo[] listaDispositivos) {
        this.listaDispositivos = listaDispositivos;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
