package com.example.prestopucp.usuarioAdmin.Fragmentos;

import android.content.Intent;
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
import com.example.prestopucp.dto.User;
import com.example.prestopucp.usuarioAdmin.UsuarioTiAgregarActivity;
import com.example.prestopucp.usuarioAdmin.ListAdapterUsuariosTi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class UsuarioTiFragment extends Fragment {


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    RecyclerView recyclerView;
    User usuarioDti;

    public UsuarioTiFragment() {
        super(R.layout.fragment_usuario_ti);
        // Required empty public constructor
    }


    public static UsuarioTiFragment newInstance(String param1, String param2) {
        UsuarioTiFragment fragment = new UsuarioTiFragment();

        return fragment;
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
        //View view = inflater.inflate(R.layout.fragment_usuario_ti, container, false);
        // Inflate the layout for this fragment
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // recycler view binding
        recyclerView = view.findViewById(R.id.admin_usuariosTi_recyclerView);

        // listener para el boton de agregar dispositivos
        FloatingActionButton btn_agregardispositivo = view.findViewById(R.id.admin_UsuariosTi_btnAdd);
        btn_agregardispositivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UsuarioTiAgregarActivity.class);
                intent.putExtra("usuarioTi", usuarioDti);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ArrayList<User> listUsuarios = new ArrayList<>();
                    for (DataSnapshot usuariosSnap : snapshot.getChildren()){
                        if (usuariosSnap.exists()){
                            Log.d("msgUsuario", usuariosSnap.getValue().toString());
                            //listUsuarios.add((User) usuariosSnap.getValue());
                            String nombre = usuariosSnap.child("nombre").getValue().toString();
                            String codigo = usuariosSnap.child("codigo").getValue().toString();
                            String email =usuariosSnap.child("email").getValue().toString();
                            String imagenUrl =usuariosSnap.child("imagenUrl").getValue().toString();
                            String privilegio =usuariosSnap.child("privilegio").getValue().toString();
                            String rol =usuariosSnap.child("rol").getValue().toString();
                            String llave = usuariosSnap.child("llave").getValue().toString();
                            Log.d("msg llave", llave);
                            //nombre, String codigo, String email, String rol, String privilegio
                            User usuariAdd = (User) usuariosSnap.getValue(User.class);
                            listUsuarios.add(usuariAdd);
                        }
                    }

                    User[] arraysUsuarios = new User[listUsuarios.size()];
                    listUsuarios.toArray(arraysUsuarios);

                    ListAdapterUsuariosTi adapterUsuarios = new ListAdapterUsuariosTi();
                    adapterUsuarios.setListaUsuarios(arraysUsuarios);
                    adapterUsuarios.setContext(getActivity());

                    recyclerView.setAdapter(adapterUsuarios);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}