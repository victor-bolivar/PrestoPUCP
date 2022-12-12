package com.example.prestopucp.usuarioti;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prestopucp.R;
import com.example.prestopucp.dto.Dispositivo;
import com.example.prestopucp.dto.SolicitudPendiente;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class uti_solicitudespendientes extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private TextView textView_solicitudID;
    private TextView textView_nombre;
    private TextView textView_curso;
    private TextView textView_motivo;
    private TextView textView_tiempo;
    private TextView textView_programas;
    private TextView textView_detalles;
    private TextView textView_dispositivo;
    private ImageView imageView_dni;
    private ImageView imageView_dispositivo;
    private FloatingActionButton floatingActionButton_aceptar;
    private FloatingActionButton floatingActionButton_rechazar;

    private int REQUEST_CODE_RECHAZAR_SOLICITUD = 1;
    private int REQUEST_CODE_ACEPTAR_SOLICITUD = 2;

    private List<SolicitudPendiente> solicitudesPendientes;
    private SolicitudPendiente solicitudActual;
    private Dispositivo dispositivoSolicitado;




    public uti_solicitudespendientes() {
        super(R.layout.fragment_uti_solicitudespendientes);

        // initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        // initialize firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        // binding de elementos UI
        textView_solicitudID = view.findViewById(R.id.uti_solicitud_id);
        textView_nombre = view.findViewById(R.id.uti_solicitudes_nombre);
        textView_curso = view.findViewById(R.id.uti_solicitudes_curso);
        textView_motivo = view.findViewById(R.id.uti_solicitudes_motivo);
        textView_tiempo = view.findViewById(R.id.uti_solicitudes_tiempo);
        textView_programas = view.findViewById(R.id.uti_solicitudes_programas);
        textView_detalles = view.findViewById(R.id.uti_solicitudes_detalles);
        textView_dispositivo = view.findViewById(R.id.uti_solicitudes_dispositivo);

        imageView_dispositivo = view.findViewById(R.id.uti_solicitudes_imageView_dispositivo);
        imageView_dni = view.findViewById(R.id.uti_solicitudes_imageView_dni);

        floatingActionButton_aceptar = view.findViewById(R.id.uti_solicitud_aceptar);
        floatingActionButton_rechazar = view.findViewById(R.id.uti_solicitud_rechazar);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // se obtienen los datos
        DatabaseReference usersRef = mDatabase.child("solicitudesPendientes");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                solicitudesPendientes = new ArrayList<SolicitudPendiente>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    // String uid = ds.getKey();
                    solicitudesPendientes.add(new SolicitudPendiente(
                            ds.child("curso").getValue().toString(),
                            ds.child("detalles").getValue().toString(),
                            ds.child("dispositivoId").getValue().toString(),
                            ds.child("dniUrl").getValue().toString(),
                            ds.child("motivo").getValue().toString(),
                            ds.child("nombre").getValue().toString(),
                            ds.child("programas").getValue().toString(),
                            ds.child("rol").getValue().toString(),
                            ds.child("tiempoReserva").getValue().toString(),
                            ds.getKey(),
                            ds.child("emailUsuario").getValue().toString()

                    ));
                }

                if (solicitudesPendientes.size() == 0){
                    // si no hay solicitudes restantes
                    dialogNoHayMasSolicitudesPendientes();

                    // se limpianl los datos
                    solicitudActual = null;
                    textView_solicitudID.setText("No hay mas solicitudes");

                    textView_solicitudID.setText("No hay mas solicitudes");
                    textView_nombre.setText("");
                    textView_curso.setText("");
                    textView_motivo.setText("");
                    textView_tiempo.setText("");
                    textView_programas.setText("");
                    textView_detalles.setText("");
                    Picasso.with(getActivity())
                            .load(R.drawable.ic_computer)
                            .resize(convertirDpPixel(280), convertirDpPixel(180))
                            .into(imageView_dni);
                    textView_dispositivo.setText("");
                    Picasso.with(getActivity())
                            .load(R.drawable.ic_computer)
                            .resize(convertirDpPixel(280), convertirDpPixel(280))
                            .into(imageView_dispositivo);

                } else{
                    // se pinta el primer elemento
                    solicitudActual = solicitudesPendientes.get(0);

                    textView_solicitudID.setText("Solicitud #"+ solicitudActual.getId());
                    textView_nombre.setText(solicitudActual.getNombre());
                    textView_curso.setText(solicitudActual.getCurso());
                    textView_motivo.setText(solicitudActual.getNombre());
                    textView_tiempo.setText(solicitudActual.getTiempoReserva());
                    textView_programas.setText(solicitudActual.getProgramas());
                    textView_detalles.setText(solicitudActual.getDetalles());
                    Picasso.with(getActivity())
                            .load(solicitudActual.getDniUrl())
                            .resize(convertirDpPixel(280), convertirDpPixel(180))
                            .into(imageView_dni);

                    // obtener info del dispositivo
                    mDatabase.child("dispositivos").child(solicitudActual.getDispositivoId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            }
                            else {
                                HashMap<String, Object> data = (HashMap<String, Object>) task.getResult().getValue();


                                textView_dispositivo.setText(
                                        data.get("tipo") + " " + data.get("marca") + " (Stock: "+ String.valueOf(data.get("stock")) + ")"
                                );
                                // se llena la informacion del dispositivo (se usara para luego actualizar el stock si se aprueba)
                                dispositivoSolicitado = new Dispositivo(
                                        (String)data.get("tipo"),
                                        (ArrayList<String>) data.get("imagenes"),
                                        (String) data.get("marca"),
                                        Math.toIntExact((Long) data.get("stock")),
                                        (String) data.get("caracteristicas"),
                                        (String) data.get("incluye"),
                                        (String) data.get("key"));

                                ArrayList<String> urlImagenes = (ArrayList<String>) data.get("imagenes");
                                Picasso.with(getActivity())
                                        .load(urlImagenes.get(0))
                                        .resize(convertirDpPixel(280), convertirDpPixel(280))
                                        .into(imageView_dispositivo);

                            }
                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("msg / solicitudesPendie", databaseError.getMessage()); //Don't ignore errors!
            }
        };
        usersRef.addListenerForSingleValueEvent(valueEventListener);

        //  aceptar solicitud
        floatingActionButton_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), uti_aceptarsolicitud.class);
                startActivityForResult(intent, REQUEST_CODE_ACEPTAR_SOLICITUD);
            }
        });


        //  rechazar solicitud
        floatingActionButton_rechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), uti_rechazarsolicitud.class);
                startActivityForResult(intent, REQUEST_CODE_RECHAZAR_SOLICITUD);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode== Activity.RESULT_OK){

            if(requestCode==REQUEST_CODE_RECHAZAR_SOLICITUD){

                String motivo = data.getStringExtra("motivo");
                solicitudActual.setMotivoRechazo(motivo);

                // se guarda en solicitudes rechazadas
                mDatabase.child("solicitudesRechazadas").child(solicitudActual.getId()).setValue(solicitudActual);

                // se borra de solicitudes pendientes
                mDatabase.child("solicitudesPendientes").child(solicitudActual.getId()).removeValue();
                
                // se manda la notificacion (correo + notificacion app) al cliente, indicando que se rechazo su solicitud
                notificarCliente("rechazo");

            } else if (requestCode == REQUEST_CODE_ACEPTAR_SOLICITUD){
                String longitud = data.getStringExtra("longitud");
                String latitud = data.getStringExtra("latitud");
                Log.d("msg", longitud + " "+ latitud);

                solicitudActual.setLatitud(latitud);
                solicitudActual.setLongitud(longitud);

                // se guarda en solicitudes aprobadas
                mDatabase.child("solicitudesAprobadas").child(solicitudActual.getId()).setValue(solicitudActual);

                // se borra de solicitudes pendientes
                mDatabase.child("solicitudesPendientes").child(solicitudActual.getId()).removeValue();

                // se actualiza el stock del dispositivo
                dispositivoSolicitado.setStock( dispositivoSolicitado.getStock() - 1 );
                FirebaseDatabase.getInstance().getReference("dispositivos").child(dispositivoSolicitado.getKey()).setValue(dispositivoSolicitado).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("msg / realtimedatabase", "se actualizo la info del dispositivo con la URL");

                    }
                });

                // se manda la notificacion (correo + notificacion app) al cliente, indicando que se acepto su solicitud
                notificarCliente("aceptado");


            }

        }

    }

    public void notificarCliente(String resultado){
        // TODO se enviara la notificacion a la app del cliente
        // TODO se enviara el correo al usuario usando el backend microservicios de la app

        if (resultado.equals("aceptado")){

        } else if (resultado.equals("rechazo")){

        }
    }

    public int convertirDpPixel(int dp){
        Resources r = this.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );

        return px;
    }

    public void dialogNoHayMasSolicitudesPendientes(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage("Buen trabajo! No hay mas solicitudes pendientes por revisar");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}