package com.example.prestopucp.dto;

import android.provider.SearchRecentSuggestions;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    public String nombre;
    public String codigo;
    public String email;
    public String rol;
    public String privilegio;
    public String imagenUrl;
    //public ArrayList<ReservaDispositivo> listaReservas;
    public String llave;

    public User(){
    }

    public User(String nombre, String codigo, String email, String rol, String privilegio) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.email = email;
        this.rol = rol;
        this.privilegio = privilegio;

    }

    public User(String nombre, String codigo, String email, String rol, String privilegio, String imagenUrl) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.email = email;
        this.rol = rol;
        this.privilegio = privilegio;
        this.imagenUrl = imagenUrl;

    }


    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getPrivilegio() {
        return privilegio;
    }

    public void setPrivilegio(String privilegio) {
        this.privilegio = privilegio;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
