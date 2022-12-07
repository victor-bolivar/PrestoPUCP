package com.example.prestopucp.usuarioCliente.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.prestopucp.Interface.iComunicaFragment;
import com.example.prestopucp.R;
import com.example.prestopucp.dto.Dispositivo;


public class DispositivoDetalleFragment extends Fragment {


    TextView tipoDispo,marcaDispo,caracteristicaDispo,stockDispo;
    Button reservarDis;

    //variables para cargar el fragment principal
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    ReservarDisFragment reservarDisFragment;
    DispositivosFragment dispositivosFragment;
    Dispositivo dispositivo;

    public DispositivoDetalleFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DispositivoDetalleFragment newInstance(String param1, String param2) {
        DispositivoDetalleFragment fragment = new DispositivoDetalleFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dispositivo_detalle, container, false);
        tipoDispo = view.findViewById(R.id.tipo_dispositivo_detalle);
        marcaDispo = view.findViewById(R.id.marca_dispositivo_detalle);
        caracteristicaDispo = view.findViewById(R.id.caracteristica_dispositivo_detalle);
        stockDispo = view.findViewById(R.id.stock_dispositivo_detalle);
        reservarDis = view.findViewById(R.id.id_btn_reservar_dispositivo);

        Bundle objetoDispositivo = getArguments();
        System.out.println(objetoDispositivo);
        dispositivo = null;
        if (objetoDispositivo!=null){
            dispositivo = (Dispositivo) objetoDispositivo.getSerializable("dispositivo");
            Log.d("msg","dispositivo marca "+ dispositivo.getMarca() );
            tipoDispo.setText(dispositivo.getTipo());
            marcaDispo.setText(dispositivo.getMarca());
            caracteristicaDispo.setText(dispositivo.getCaracteristicas());
            stockDispo.setText(String.valueOf(dispositivo.getStock()));
        }else{
            Log.d("msg", "objetDis es nulo");
        }
        // Inflate the layout for this fragment

        return view;
    }

    public void reservaDispositivo(View view){

        reservarDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reservarDisFragment = new ReservarDisFragment();
                Bundle bundleReserva = new Bundle();
                bundleReserva.putSerializable("reserva",dispositivo);
                reservarDisFragment.setArguments(bundleReserva);
                //getParentFragmentManager().setFragmentResult("dispositivo",bundleReserva);
                fragmentManager = getParentFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerCliente,reservarDisFragment);
                fragmentTransaction.commit();
            }
        });
    }


}