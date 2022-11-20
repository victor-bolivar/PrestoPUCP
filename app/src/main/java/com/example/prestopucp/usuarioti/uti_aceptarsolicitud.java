package com.example.prestopucp.usuarioti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.prestopucp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class uti_aceptarsolicitud extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    String longitud, latitud;
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uti_aceptarsolicitud);

        // se cambia el titulo a 'Solicitudes pendientes' ya que es el Fragment por defecto
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Aceptar solicitud");
        }

        // maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.uti_aceptarsolicitud_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // se coloca los listeners respecto al mapa

        mMap = googleMap;
        this.mMap.setOnMapClickListener(this); // cuando se hace click
        this.mMap.setOnMapLongClickListener(this); // cuando se mantiene presionado

        // posicion inicial del mapa
        LatLng pucp = new LatLng(-12.0701382,-77.0791665);
        mMap.addMarker(new MarkerOptions().position(pucp).title("PUCP"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pucp));

    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        latitud = String.valueOf(latLng.latitude);
        longitud = String.valueOf(latLng.longitude);

        // se actualiza el marcador del mapa
        mMap.clear();
        LatLng nuevaPosicion = new LatLng(latLng.latitude,latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(nuevaPosicion).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(nuevaPosicion));

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        latitud = String.valueOf(latLng.latitude);
        longitud = String.valueOf(latLng.longitude);

        // se actualiza el marcador del mapa
        mMap.clear();
        LatLng nuevaPosicion = new LatLng(latLng.latitude,latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(nuevaPosicion).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(nuevaPosicion));
    }

    public void aceptarSolicitud(View view){
        // se crea un nuevo intent
        Intent intent = new Intent();

        // se regresa
        intent.putExtra("latitud", latitud);
        intent.putExtra("longitud", longitud);
        setResult(RESULT_OK, intent);
        finish();
    }
}