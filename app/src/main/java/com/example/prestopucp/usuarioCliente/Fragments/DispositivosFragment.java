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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DispositivosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DispositivosFragment extends Fragment {

    FirebaseAuth mAuth;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference mDatabase;
    private DatabaseReference databaseReference;

    AdapterDispositivos adapterDispositivos;
    RecyclerView recyclerViewDispositivos;
    ArrayList<Dispositivo> listDispositivos;

    //variables para pasar data a otro fragment
    Activity actividad;
    iComunicaFragment interfaceComunicaFragment;

    public DispositivosFragment() {

        // initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        // initialize firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference("dispositivos");
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DispositivosFragment newInstance(String param1, String param2) {
        DispositivosFragment fragment = new DispositivosFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dispositivos, container, false);

        recyclerViewDispositivos = view.findViewById(R.id.recyclerViewDispositivos);
        listDispositivos = new ArrayList<>();
        //cargar la lista
        cargarLista();
        //mostrar datos
        mostrarData();



        return view;
    }

    private void mostrarData() {
        recyclerViewDispositivos.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterDispositivos = new AdapterDispositivos(getContext(),listDispositivos);
        recyclerViewDispositivos.setAdapter(adapterDispositivos);
        adapterDispositivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = listDispositivos.get(recyclerViewDispositivos.getChildAdapterPosition(view)).getMarca();
                Toast.makeText(getContext(), "Selecciono : " + nombre, Toast.LENGTH_SHORT).show();
                //para mandar a otro fragment
                interfaceComunicaFragment.envidarDetalleDispositivo(listDispositivos.get(recyclerViewDispositivos.getChildAdapterPosition(view)));
            }
        });

    }

    private void cargarLista() {

        mAuth = FirebaseAuth.getInstance();


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnaphot : snapshot.getChildren()){
                    Dispositivo dis = (Dispositivo) postSnaphot.getValue(Dispositivo.class);
                    Log.d("msg",dis.getTipo() + " " + dis.getMarca());
                    listDispositivos.add(dis);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("msgError", "loadPost:onCancelled", error.toException());
            }
        });

        System.out.println( listDispositivos);

        ArrayList<String> imagenes = new ArrayList<>();
        imagenes.add("asdasdasd");
        listDispositivos.add(new Dispositivo("laptop",imagenes,",arca_2",10,"caracteristicas","incluyeaasd","-NIcJEpcuQuj0LtXOUGE"));


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.actividad = (Activity) context;
            interfaceComunicaFragment = (iComunicaFragment) this.actividad;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}