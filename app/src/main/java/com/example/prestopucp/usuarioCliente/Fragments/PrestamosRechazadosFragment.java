package com.example.prestopucp.usuarioCliente.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prestopucp.R;
import com.example.prestopucp.dto.SolicitudPendiente;
import com.example.prestopucp.usuarioCliente.Adaptadores.AdapterHistorialPrestamos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class PrestamosRechazadosFragment extends Fragment {


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    RecyclerView recyclerViewRechazados;

    public PrestamosRechazadosFragment() {
        // Required empty public constructor
    }


    public static PrestamosRechazadosFragment newInstance(String param1, String param2) {
        PrestamosRechazadosFragment fragment = new PrestamosRechazadosFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        // initialize firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prestamos_rechazados, container, false);
        recyclerViewRechazados = view.findViewById(R.id.recyclerViewRechazados);

        return view;
        // Inflate the layout for this fragment
    }

    @Override
    public void onResume() {
        super.onResume();

        String idUser = mAuth.getCurrentUser().getUid();
        //solicitudes aprobadas
        mDatabase.child("solicitudesRechazadas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ArrayList<SolicitudPendiente> listReservas = new ArrayList<SolicitudPendiente>();
                    for (DataSnapshot reservaSnap : snapshot.getChildren()){
                        if (reservaSnap.exists()){
                            String uidUser = reservaSnap.child("nombre").getValue().toString();

                            if (uidUser.equals(mAuth.getCurrentUser().getUid())){
                                Log.d("msg historial", reservaSnap.getValue().toString());

                                SolicitudPendiente soliPendiente = new SolicitudPendiente();

                                String curso = reservaSnap.child("curso").getValue().toString();
                                soliPendiente.setCurso(curso);

                                String detalles = reservaSnap.child("detalles").getValue().toString();
                                soliPendiente.setDetalles(detalles);

                                String dispositivoId = reservaSnap.child("dispositivoId").getValue().toString();
                                soliPendiente.setDispositivoId(dispositivoId);

                                String dniUrl = reservaSnap.child("dniUrl").getValue().toString();
                                soliPendiente.setDniUrl(dniUrl);

                                String emailUsuario= reservaSnap.child("emailUsuario").getValue().toString();
                                soliPendiente.setEmailUsuario(emailUsuario);

                                String id= reservaSnap.child("id").getValue().toString();
                                soliPendiente.setId(id);

                                String motivo= reservaSnap.child("motivo").getValue().toString();
                                soliPendiente.setMotivo(motivo);

                                String motivoRechazo= reservaSnap.child("motivoRechazo").getValue().toString();
                                soliPendiente.setMotivoRechazo(motivoRechazo);

                                String nombre= reservaSnap.child("nombre").getValue().toString();
                                soliPendiente.setNombre(nombre);

                                String programas= reservaSnap.child("programas").getValue().toString();
                                soliPendiente.setProgramas(programas);

                                String rol= reservaSnap.child("rol").getValue().toString();
                                soliPendiente.setRol(rol);

                                String tiempoReserva= reservaSnap.child("tiempoReserva").getValue().toString();
                                soliPendiente.setTiempoReserva(tiempoReserva);

                                listReservas.add(soliPendiente);
                            }

                        }
                    }

                    SolicitudPendiente[] arrayHistoAprobados = new SolicitudPendiente[listReservas.size()];
                    listReservas.toArray(arrayHistoAprobados);
                    AdapterHistorialPrestamos adapterHistorialPrestamos = new AdapterHistorialPrestamos();
                    adapterHistorialPrestamos.setListHistorialReservas(arrayHistoAprobados);
                    adapterHistorialPrestamos.setContext(getActivity());

                    recyclerViewRechazados.setAdapter(adapterHistorialPrestamos);
                    recyclerViewRechazados.setLayoutManager(new LinearLayoutManager(getActivity()));



                }else{
                    Log.d("msg historial","no existe info");
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}