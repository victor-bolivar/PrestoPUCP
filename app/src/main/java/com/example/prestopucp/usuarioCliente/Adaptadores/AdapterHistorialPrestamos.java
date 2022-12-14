package com.example.prestopucp.usuarioCliente.Adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prestopucp.Constantes.Constante;
import com.example.prestopucp.R;
import com.example.prestopucp.dto.Dispositivo;
import com.example.prestopucp.dto.ReservaDispositivo;
import com.example.prestopucp.dto.SolicitudPendiente;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdapterHistorialPrestamos extends RecyclerView.Adapter<AdapterHistorialPrestamos.ReservaHistorialViewHolder> {


    private SolicitudPendiente[] listHistorialReservas;
    private Context context;

    EditText tipo, estado,curso,fecha;
    ImageView imageView;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public class ReservaHistorialViewHolder extends RecyclerView.ViewHolder {
        SolicitudPendiente d;
        public ReservaHistorialViewHolder(@NonNull View itemView){super(itemView);}
    }

    @NonNull
    @Override
    public ReservaHistorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_historial_reserva,parent,false);
        mAuth = FirebaseAuth.getInstance();
        // initialize firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        return new ReservaHistorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaHistorialViewHolder holder, int position) {
        SolicitudPendiente historialDis = listHistorialReservas[position];
        holder.d = historialDis;

        ImageView photo = holder.itemView.findViewById(R.id.id_imagen_histRese);
        TextView textView_tipo = holder.itemView.findViewById(R.id.id_text_tipo_hr);
        TextView textView_curso = holder.itemView.findViewById(R.id.id_text_curso_hr);
        TextView textView_fecha = holder.itemView.findViewById(R.id.id_text_fecha_hr);
        TextView textView_marca = holder.itemView.findViewById(R.id.id_text_estado_hr);
        Log.d("id disposi",historialDis.getDispositivoId());



        mDatabase.child(Constante.DB_DISPOSITIVOS).child(historialDis.getDispositivoId()).addValueEventListener(new ValueEventListener() {
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

        if (historialDis.getMotivoRechazo() != null){
            textView_fecha.setText("Motivo : "+ historialDis.getMotivoRechazo());
        }else{
            textView_fecha.setText("Fecha Max : "+ historialDis.getTiempoReserva());
        }
        textView_curso.setText("Curso : " + historialDis.getCurso());



        //colocar datos






    }

    @Override
    public int getItemCount() {
        return listHistorialReservas.length;
    }

    public SolicitudPendiente[] getListHistorialReservas() {
        return listHistorialReservas;
    }

    public void setListHistorialReservas(SolicitudPendiente[] listHistorialReservas) {
        this.listHistorialReservas = listHistorialReservas;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
