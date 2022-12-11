package com.example.prestopucp.usuarioCliente.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prestopucp.Interface.iComunicaFragment;
import com.example.prestopucp.dto.Dispositivo;
import com.example.prestopucp.R;
import com.example.prestopucp.usuarioCliente.Fragments.DispositivosFragment;
import com.example.prestopucp.usuarioCliente.Fragments.ReservarDisFragment;
import com.example.prestopucp.usuarioCliente.ReservaDispositivoActivity;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdapterDispositivos extends RecyclerView.Adapter<AdapterDispositivos.DispositivoViewHolder>  {



    //nuevos parametros///

    // tiene que guardar la data
    private Dispositivo[] listaDispositivos;
    // para guardar el contexto de la actividad donde se va a pintar el RecyclerView
    private Context context;


    public class DispositivoViewHolder extends RecyclerView.ViewHolder{

        Dispositivo d;

        public DispositivoViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }


    @NonNull
    @Override
    public DispositivoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_productos,parent,false);
        return new DispositivoViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull DispositivoViewHolder holder, int position) {

        Dispositivo dispositivo = listaDispositivos[position];
        holder.d = dispositivo;

        //colocar datos
        TextView textView_tipo = holder.itemView.findViewById(R.id.id_tipo_dispositivo);
        textView_tipo.setText(dispositivo.getTipo());

        TextView textView_marca = holder.itemView.findViewById(R.id.id_marca_dispositivo);
        textView_marca.setText(dispositivo.getMarca());

        TextView textView_stock = holder.itemView.findViewById(R.id.id_stock_dispositivo);
        textView_stock.setText("Stock : " + String.valueOf(dispositivo.getStock()));

        ImageView imageView = holder.itemView.findViewById(R.id.imagen_cv_producto);

        String urlImagen = dispositivo.getImagenes().get(0);
        if (urlImagen.equals("")){
            imageView.setBackgroundResource(R.drawable.imagen_ejemplo_dispositivo);
            Log.d("msg", "imagen predefinida dispositivo");
        } else {
            // se coloca la imagen con Picasso
            Picasso.with(context)
                    .load(urlImagen)
                    .resize(120, 120)
                    .into(imageView);
        }


        Button btn_reserva = holder.itemView.findViewById(R.id.id_btn_reservar_dispositivo);

        btn_reserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReservaDispositivoActivity.class);
                intent.putExtra("dispositivoReservar",dispositivo);
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
