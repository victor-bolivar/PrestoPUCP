package com.example.prestopucp.usuarioCliente.Fragments;

import android.os.Bundle;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prestopucp.Constantes.Constante;
import com.example.prestopucp.R;
import com.example.prestopucp.dto.Dispositivo;
import com.example.prestopucp.dto.ReservaDispositivo;
import com.example.prestopucp.dto.SolicitudPendiente;
import com.example.prestopucp.usuarioCliente.Adaptadores.AdapterHistorialPrestamos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;


public class HistorialPrestamoFragment extends Fragment {

    ArrayList<ReservaDispositivo> listReservaDis;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    RecyclerView recyclerViewHistorial;

    public HistorialPrestamoFragment() {
        super(R.layout.fragment_historial_prestamo);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        // initialize firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historial_prestamo, container, false);
        recyclerViewHistorial = view.findViewById(R.id.recyclerViewHistorialPrestamos);
        //listReservaDis = new ArrayList<>();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        String idUser = mAuth.getCurrentUser().getUid();
        //solicitudes aprobadas
        mDatabase.child("solicitudesAprobadas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ArrayList<SolicitudPendiente> listReservas = new ArrayList<SolicitudPendiente>();
                    for (DataSnapshot reservaSnap : snapshot.getChildren()){
                        if (reservaSnap.exists()){
                            Log.d("msg historial", reservaSnap.getValue().toString());

                            SolicitudPendiente ga = new SolicitudPendiente();

                            //SolicitudPendiente reseDis = (SolicitudPendiente) reservaSnap.getValue(SolicitudPendiente.class);
                            if (reservaSnap.child("nombre").getValue().toString().equals(idUser)){
                                //ga.setCurso();
                                // listReservas.add(reseDis);
                            }
                            // String marca = dispositivoSnapshot.child("marca").getValue().toString();
                            //og.d("msg",reseDis.getTipo() + " " + reseDis.getIdDispositivo() + " " + reseDis.getEstadoReserva());

                        }
                    }

                    SolicitudPendiente[] arrayHistoAprobados = new SolicitudPendiente[listReservas.size()];
                    listReservas.toArray(arrayHistoAprobados);
                    AdapterHistorialPrestamos adapterHistorialPrestamos = new AdapterHistorialPrestamos();
                    adapterHistorialPrestamos.setListHistorialReservas(arrayHistoAprobados);
                    adapterHistorialPrestamos.setContext(getActivity());

                    recyclerViewHistorial.setAdapter(adapterHistorialPrestamos);
                    recyclerViewHistorial.setLayoutManager(new LinearLayoutManager(getActivity()));


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