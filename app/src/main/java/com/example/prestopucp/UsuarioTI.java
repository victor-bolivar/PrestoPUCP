package com.example.prestopucp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.prestopucp.usuarioti.uti_gestiondispositivos;
import com.example.prestopucp.usuarioti.uti_micuenta;
import com.example.prestopucp.usuarioti.uti_solicitudespendientes;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsuarioTI extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_ti);

        mAuth = FirebaseAuth.getInstance();

        // 1. creacion del drawer layout

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 2. se cambia el titulo a 'Solicitudes pendientes' ya que es el Fragment por defecto
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Solicitudes pendientes");
        }

        // 3. listener para cada uno de los items en en menu de navegation
        NavigationView nav  = (NavigationView)findViewById(R.id.nav_view_usuarioti);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment fragment = null;
                String title = getString(R.string.app_name);

                switch (item.getItemId()){
                    case R.id.nav_micuenta:
                        fragment = new uti_micuenta();
                        title  = "Mi cuenta";
                        break;
                    case R.id.nav_solicitudespendientes:
                        fragment = new uti_solicitudespendientes();
                        title  = "Solicitudes pendientes";
                        break;
                    case R.id.nav_gestiondispositivos:
                        fragment = new uti_gestiondispositivos();
                        title  = "Gestion de dispositivos";
                        break;

                }

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentContainerView, fragment);
                ft.commit();

                // set the toolbar title
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(title);
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });

    }

    //


    @Override
    protected void onResume() {
        super.onResume();

        // 1. se verifica que el usuario este logeado, sino se regresa al activity Inicio
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Intent intent = new Intent(this, Inicio.class);
            startActivity(intent);
            finish();
        }
    }

    // NavigationDrawer
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}