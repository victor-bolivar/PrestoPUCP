package com.example.prestopucp.usuarioti;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.prestopucp.R;
import com.example.prestopucp.dto.Dispositivo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class uti_gestiondispositivos extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    RecyclerView recyclerView;

    public uti_gestiondispositivos() {
        super(R.layout.fragment_uti_gestiondispositivos);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        // initialize firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // recycler view binding
        recyclerView = view.findViewById(R.id.uti_dispositivos_recyclerView);

        // listener para el boton de agregar dispositivos
        FloatingActionButton btn_agregardispositivo = view.findViewById(R.id.uti_gestiondispositivos_btnAdd);
        btn_agregardispositivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), uti_agegardispositivo.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // se obtienen los datos

        mDatabase.child("dispositivos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    // se define la lista de dispositivos a llenar
                    ArrayList<Dispositivo> arraylistDispositivos = new ArrayList<Dispositivo>();

                    // se obtiene la data
                    for (DataSnapshot dispositivoSnapshot: snapshot.getChildren()) {
                        if (dispositivoSnapshot.exists()){ // para evitar encontrar un null
                            // info de cada dispositivo

                            Log.d("msg / data dispositivo", dispositivoSnapshot.getValue().toString());
                            String marca = dispositivoSnapshot.child("marca").getValue().toString();
                            String caracteristicas = dispositivoSnapshot.child("caracteristicas").getValue().toString();
                            String incluye = dispositivoSnapshot.child("incluye").getValue().toString();
                            int stock = Integer.parseInt(dispositivoSnapshot.child("stock").getValue().toString());
                            String tipo = dispositivoSnapshot.child("tipo").getValue().toString();
                            String key = dispositivoSnapshot.child("key").getValue().toString();

                            ArrayList<String> listaImagenes = new ArrayList<String>();
                            for (DataSnapshot imagenSnapshot : dispositivoSnapshot.child("imagenes").getChildren()){
                                // se itera sobre cada imagen
                                String imagen = imagenSnapshot.getValue().toString();
                                listaImagenes.add(imagen);
                            }

                            // se agrega el nuevo dispositivo
                            arraylistDispositivos.add(new Dispositivo(tipo, listaImagenes, marca, stock, caracteristicas, incluye, key));

                        }
                    }

                    // se convierte a java array pq asi lo requiere el adapter
                    Dispositivo[] listaDispositivos = new Dispositivo[ arraylistDispositivos.size() ];
                    arraylistDispositivos.toArray( listaDispositivos );

                    // se crea el adapter
                    ListaDispositivosAdapter adapter = new ListaDispositivosAdapter();
                    adapter.setListaDispositivos(listaDispositivos);
                    adapter.setContext(getActivity());

                    // se asigna el adapter al recyclerview
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