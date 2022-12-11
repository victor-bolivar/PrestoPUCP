package com.example.prestopucp.usuarioAdmin;

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

import com.example.prestopucp.Inicio;
import com.example.prestopucp.R;
import com.example.prestopucp.usuarioAdmin.Fragmentos.ReportesFragment;
import com.example.prestopucp.usuarioAdmin.Fragmentos.UsuarioTiFragment;
import com.example.prestopucp.usuarioAdmin.Fragmentos.userPerfilAdminFragment;
import com.example.prestopucp.usuarioti.uti_micuenta;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsuarioAdminActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayoutAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_admin);
        mAuth = FirebaseAuth.getInstance();

        // 1. creacion del drawer layout

        drawerLayoutAdmin = (DrawerLayout) findViewById(R.id.drawerLayoutAdmin);
        toggle = new ActionBarDrawerToggle(this, drawerLayoutAdmin, R.string.open_drawer, R.string.close_drawer);

        drawerLayoutAdmin.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Usuarios ti probanding");
        }

        NavigationView nav  = (NavigationView)findViewById(R.id.nav_view_usuarioAdmin);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                String titulo = getString(R.string.app_name);

                switch (item.getItemId()){
                    case R.id.id_perfil_admin:
                        fragment = new uti_micuenta();
                        titulo = "Perfil Usuario";
                        break;
                    case R.id.id_usuarioTi_admin:
                        fragment = new UsuarioTiFragment();
                        titulo = "Usuario TI";
                        break;
                    case R.id.id_reportes_admin:
                        fragment = new ReportesFragment();
                        titulo = "Reportes";
                        break;
                }
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentContainerViewAdmin,fragment);
                ft.commit();

                // set the toolbar title
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(titulo);
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutAdmin);
                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });

    }

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