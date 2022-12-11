package com.example.prestopucp.usuarioCliente.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.prestopucp.Interface.iComunicaFragment;
import com.example.prestopucp.R;
import com.example.prestopucp.dto.Dispositivo;
import com.example.prestopucp.usuarioCliente.Adaptadores.AdapterDispositivos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class DispositivosFragment extends Fragment {


    ArrayList<Dispositivo> listDispositivos;

    ///nuevos parametros

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    RecyclerView recyclerViewDispositivos;

    public DispositivosFragment() {
        super(R.layout.fragment_dispositivos);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        // initialize firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dispositivos, container, false);

        recyclerViewDispositivos = view.findViewById(R.id.recyclerViewDispositivos);
        listDispositivos = new ArrayList<>();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mDatabase.child("dispositivos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ArrayList<Dispositivo> listDispositivos = new ArrayList<Dispositivo>();

                    for (DataSnapshot disSnap : snapshot.getChildren()){
                        if (disSnap.exists()){
                            Log.d("msg dispositivos",disSnap.getValue().toString());

                            Dispositivo dis = (Dispositivo) disSnap.getValue(Dispositivo.class);
                            Log.d("msg",dis.getTipo() + " " + dis.getMarca());
                            listDispositivos.add(dis);

                        }
                    }

                    Dispositivo[] arrayDispositivos = new Dispositivo[listDispositivos.size()];
                    listDispositivos.toArray(arrayDispositivos);
                    AdapterDispositivos adapterDispositivos = new AdapterDispositivos();
                    adapterDispositivos.setListaDispositivos(arrayDispositivos);
                    adapterDispositivos.setContext(getActivity());

                    recyclerViewDispositivos.setAdapter(adapterDispositivos);
                    recyclerViewDispositivos.setLayoutManager(new LinearLayoutManager(getActivity()));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




}