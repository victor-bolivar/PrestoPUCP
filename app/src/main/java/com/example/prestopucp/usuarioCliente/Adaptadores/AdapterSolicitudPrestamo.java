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

import com.example.prestopucp.Constantes.Constante;
import com.example.prestopucp.R;
import com.example.prestopucp.dto.Dispositivo;
import com.example.prestopucp.dto.SolicitudPendiente;
import com.example.prestopucp.usuarioCliente.ReservaDispositivoActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdapterSolicitudPrestamo  extends RecyclerView.Adapter<AdapterSolicitudPrestamo.SolicitudPrestamoViewHolder> {

    private SolicitudPendiente[] listReservasDis;
    private Context context;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @NonNull
    @Override
    public AdapterSolicitudPrestamo.SolicitudPrestamoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_historial_reserva,parent,false);
        mAuth = FirebaseAuth.getInstance();
        // initialize firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        return new SolicitudPrestamoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSolicitudPrestamo.SolicitudPrestamoViewHolder holder, int position) {
        SolicitudPendiente reservasDis = listReservasDis[position];
        holder.d = reservasDis;



        ImageView photo = holder.itemView.findViewById(R.id.id_imagen_histRese);
        TextView textView_tipo = holder.itemView.findViewById(R.id.id_text_tipo_hr);
        TextView textView_curso = holder.itemView.findViewById(R.id.id_text_curso_hr);
        TextView textView_fecha = holder.itemView.findViewById(R.id.id_text_fecha_hr);
        TextView textView_marca = holder.itemView.findViewById(R.id.id_text_estado_hr);
        Log.d("id disposi",reservasDis.getDispositivoId());

        mDatabase.child(Constante.DB_DISPOSITIVOS).child(reservasDis.getDispositivoId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Dispositivo dispositivo = snapshot.getValue(Dispositivo.class);
                    textView_tipo.setText(dispositivo.getTipo());
                    textView_marca.setText(dispositivo.getMarca());

                    String urlImagen = dispositivo.getImagenes().get(0);

                    if (urlImagen.equals("")){
                        photo.setBackgroundResource(R.drawable.imagen_ejemplo_dispositivo);
                        Log.d("msg", "imagen predefinida dispositivo");
                    } else {
                        // se coloca la imagen con Picasso
                        Picasso.with(context)
                                .load(urlImagen)
                                .resize(120, 120)
                                .into(photo);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        textView_curso.setText("Curso : " + reservasDis.getCurso());
        textView_fecha.setText("Fecha Max : "+ reservasDis.getTiempoReserva());


    }

    @Override
    public int getItemCount() {
        return listReservasDis.length;
    }

    public class SolicitudPrestamoViewHolder extends RecyclerView.ViewHolder {
        SolicitudPendiente d;
        public SolicitudPrestamoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setListReservasDis(SolicitudPendiente[] listReservasDis) {
        this.listReservasDis = listReservasDis;
    }

    public SolicitudPendiente[] getListReservasDis() {
        return listReservasDis;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
