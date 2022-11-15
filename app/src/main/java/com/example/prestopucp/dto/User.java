package com.example.prestopucp.dto;

public class User {
    public String nombre;
    public String codigo;
    public String email;
    public String rol;
    public String privilegio;
    public String imagenUrl;

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
}
