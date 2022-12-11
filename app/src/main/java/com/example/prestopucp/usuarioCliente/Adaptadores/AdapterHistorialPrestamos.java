package com.example.prestopucp.usuarioCliente.Adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prestopucp.R;
import com.example.prestopucp.dto.ReservaDispositivo;
import com.squareup.picasso.Picasso;

public class AdapterHistorialPrestamos extends RecyclerView.Adapter<AdapterHistorialPrestamos.ReservaHistorialViewHolder> {


    private ReservaDispositivo[] listHistorialReservas;
    private Context context;

    public class ReservaHistorialViewHolder extends RecyclerView.ViewHolder {
        ReservaDispositivo d;
        public ReservaHistorialViewHolder(@NonNull View itemView){super(itemView);}
    }

    @NonNull
    @Override
    public ReservaHistorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_historial_reserva,parent,false);
        return new ReservaHistorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaHistorialViewHolder holder, int position) {
        ReservaDispositivo historialDis = listHistorialReservas[position];
        holder.d = historialDis;


        //colocar datos

    }

    @Override
    public int getItemCount() {
        return listHistorialReservas.length;
    }

    public ReservaDispositivo[] getListHistorialReservas() {
        return listHistorialReservas;
    }

    public void setListHistorialReservas(ReservaDispositivo[] listHistorialReservas) {
        this.listHistorialReservas = listHistorialReservas;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
