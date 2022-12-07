package com.example.prestopucp.usuarioCliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.prestopucp.Interface.iComunicaFragment;
import com.example.prestopucp.R;
import com.example.prestopucp.dto.Dispositivo;
import com.example.prestopucp.usuarioCliente.Fragments.DispositivoDetalleFragment;
import com.example.prestopucp.usuarioCliente.Fragments.DispositivosFragment;
import com.example.prestopucp.usuarioCliente.Fragments.HistorialPrestamoFragment;
import com.example.prestopucp.usuarioCliente.Fragments.SolicitudPrestamoFragment;
import com.google.android.material.navigation.NavigationView;

public class UsuarioClienteActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener, iComunicaFragment {


    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    TextView textViewHeader;

    //variables para cargar el fragment principal
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    DispositivoDetalleFragment dispositivoDetalleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_cliente);


        drawerLayout = findViewById(R.id.drawerCliente);
        textViewHeader = (TextView) findViewById(R.id.textNameNavUserCliente);

        //toolbar = findViewById(R.id.toolbar);

        //setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.navigationViewCliente);
        //establecer evento onclick al navigationView
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();


        //cargar fragment principal
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.containerCliente,new DispositivosFragment());
        fragmentTransaction.commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if (item.getItemId() == R.id.dispositivos_disponibles){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerCliente,new DispositivosFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        if (item.getItemId() == R.id.solicitud_prestamo){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerCliente,new SolicitudPrestamoFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        if (item.getItemId() == R.id.historial_prestamo){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerCliente,new HistorialPrestamoFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        return false;
    }

    @Override
    public void envidarDetalleDispositivo(Dispositivo dispositivo) {

        dispositivoDetalleFragment = new DispositivoDetalleFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("dispositivo",dispositivo);
        dispositivoDetalleFragment.setArguments(bundle);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerCliente,dispositivoDetalleFragment);
        fragmentTransaction.commit();

    }

}