package com.example.prestopucp.usuarioCliente.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prestopucp.R;
import com.example.prestopucp.dto.SolicitudPendiente;
import com.example.prestopucp.usuarioCliente.Adaptadores.AdapterHistorialPrestamos;
import com.example.prestopucp.usuarioCliente.Adaptadores.AdapterSolicitudPrestamo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SolicitudPrestamoFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    RecyclerView recyclerView;

    public SolicitudPrestamoFragment() {
        // Required empty public constructor
    }


    public static SolicitudPrestamoFragment newInstance(String param1, String param2) {
        SolicitudPrestamoFragment fragment = new SolicitudPrestamoFragment();

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
        View view = inflater.inflate(R.layout.fragment_solicitud_prestamo, container, false);
        recyclerView = view.findViewById(R.id.cli_ReservaDis_recyclerView);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mDatabase.child("solicitudesPendientes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ArrayList<SolicitudPendiente> listSoli = new ArrayList<>();

                    for (DataSnapshot soliSnap : snapshot.getChildren()){
                        if (soliSnap.exists()){

                            String uidUser= soliSnap.child("nombre").getValue().toString();
                            if (uidUser.equals(mAuth.getCurrentUser().getUid())){
                                SolicitudPendiente soliPendiente = new SolicitudPendiente();

                                String curso = soliSnap.child("curso").getValue().toString();
                                soliPendiente.setCurso(curso);

                                String detalles = soliSnap.child("detalles").getValue().toString();
                                soliPendiente.setDetalles(detalles);

                                String dispositivoId = soliSnap.child("dispositivoId").getValue().toString();
                                soliPendiente.setDispositivoId(dispositivoId);

                                String dniUrl = soliSnap.child("dniUrl").getValue().toString();
                                soliPendiente.setDniUrl(dniUrl);

                                String emailUsuario= soliSnap.child("emailUsuario").getValue().toString();
                                soliPendiente.setEmailUsuario(emailUsuario);

                                String id= soliSnap.child("id").getValue().toString();
                                soliPendiente.setId(id);

                                String motivo= soliSnap.child("motivo").getValue().toString();
                                soliPendiente.setMotivo(motivo);

                                String nombre= soliSnap.child("nombre").getValue().toString();
                                soliPendiente.setNombre(nombre);

                                String programas= soliSnap.child("programas").getValue().toString();
                                soliPendiente.setProgramas(programas);

                                String rol= soliSnap.child("rol").getValue().toString();
                                soliPendiente.setRol(rol);

                                String tiempoReserva= soliSnap.child("tiempoReserva").getValue().toString();
                                soliPendiente.setTiempoReserva(tiempoReserva);

                                listSoli.add(soliPendiente);
                            }

                        }
                    }

                    SolicitudPendiente[] arraySolicitudPendientes = new SolicitudPendiente[listSoli.size()];
                    listSoli.toArray(arraySolicitudPendientes);
                    System.out.println("probando data de historial prestamo");
                    System.out.println(listSoli);


                    AdapterSolicitudPrestamo adapter = new AdapterSolicitudPrestamo();
                    adapter.setListReservasDis(arraySolicitudPendientes);
                    adapter.setContext(getActivity());



                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}