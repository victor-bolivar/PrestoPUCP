package com.example.prestopucp.usuarioCliente.Adaptadores;

import android.content.Context;
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

import java.util.ArrayList;

public class AdapterDispositivos extends RecyclerView.Adapter<AdapterDispositivos.ViewHolder> implements View.OnClickListener {

    ArrayList<Dispositivo> model;
    LayoutInflater inflater;
    iComunicaFragment comunicaFragment;
    //listener
    private View.OnClickListener listener;

    ReservarDisFragment reservarDisFragment;
    DispositivosFragment dispositivosFragment;

    public AdapterDispositivos(Context context, ArrayList<Dispositivo> listDispositivos){
        this.inflater = LayoutInflater.from(context);
        this.model=listDispositivos;
    }

    @Override
    public void onClick(View view) {
        if (listener!=null){
            listener.onClick(view);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_productos,parent,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String marca = model.get(position).getMarca();
        int stock = model.get(position).getStock();
        String tipo = model.get(position).getTipo();

        holder.stockDis.setText(String.valueOf(stock));
        holder.marcaDis.setText(marca);
        holder.tipoDis.setText(tipo);
        holder.reservadis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //comunicaFragment.envidarDetalleDispositivo(model.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tipoDis,marcaDis,stockDis;
        Button reservadis;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tipoDis = itemView.findViewById(R.id.id_tipo_dispositivo);
            marcaDis = itemView.findViewById(R.id.id_marca_dispositivo);
            stockDis = itemView.findViewById(R.id.id_stock_dispositivo);
            reservadis = itemView.findViewById(R.id.id_btn_reservar_dispositivo);
        }
    }
}
