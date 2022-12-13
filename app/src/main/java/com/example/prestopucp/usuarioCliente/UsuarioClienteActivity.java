package com.example.prestopucp.usuarioCliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.prestopucp.R;

import com.example.prestopucp.usuarioCliente.Fragments.DispositivosFragment;
import com.example.prestopucp.usuarioCliente.Fragments.HistorialPrestamoFragment;
import com.example.prestopucp.usuarioCliente.Fragments.PrestamosRechazadosFragment;
import com.example.prestopucp.usuarioCliente.Fragments.SolicitudPrestamoFragment;

import com.example.prestopucp.usuarioti.uti_micuenta;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class UsuarioClienteActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_cliente);

        mAuth = FirebaseAuth.getInstance();

        drawerLayout = findViewById(R.id.drawerCliente);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Usuarios TI");
        }

        NavigationView nav  = (NavigationView)findViewById(R.id.navigationViewCliente);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                String titulo = getString(R.string.app_name);

                switch (item.getItemId()){
                    case R.id.nav_perfil_usuarioti:
                        fragment = new uti_micuenta();
                        titulo  = "Mi cuenta";
                        break;
                    case R.id.nav_dispositivos_disponibles:
                        fragment = new DispositivosFragment();
                        titulo  = "Dispositivos Disponibles";
                        break;
                    case R.id.nav_solicitud_prestamo:
                        fragment = new SolicitudPrestamoFragment();
                        titulo  = "Solicitudes de prestamo";
                        break;
                    case R.id.nav_prestamo_aprobado:
                        fragment = new HistorialPrestamoFragment();
                        titulo  = "Prestamos Aprobados";
                        break;
                        case R.id.nav_prestamo_rechazado:
                        fragment = new PrestamosRechazadosFragment();
                        titulo  = "Prestamos Rechazados";
                        break;
                }
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentContainerViewCliente, fragment);
                ft.commit();

                // set the toolbar title
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(titulo);
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerCliente);
                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });
    }

}