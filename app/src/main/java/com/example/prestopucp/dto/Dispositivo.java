package com.example.prestopucp.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class Dispositivo implements Serializable {
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ArrayList<String> getImagenes() {
        return imagenes;
    }

    public void setUrlImagen(ArrayList<String> imagenes) {
        this.imagenes = imagenes;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String marca) {
        Marca = marca;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public String getIncluye() {
        return incluye;
    }

    public void setIncluye(String incluye) {
        this.incluye = incluye;
    }

    private String tipo;
    private ArrayList<String> imagenes;
    private String Marca;
    private int stock;
    private String caracteristicas;
    private String incluye;
    private String key;

    public Dispositivo(String tipo,ArrayList<String> imagenes, String marca, int stock, String caracteristicas, String incluye) {
        this.tipo = tipo;
        this.imagenes = imagenes;
        Marca = marca;
        this.stock = stock;
        this.caracteristicas = caracteristicas;
        this.incluye = incluye;
    }

    public Dispositivo(String tipo,ArrayList<String> imagenes, String marca, int stock, String caracteristicas, String incluye, String key) {
        this.tipo = tipo;
        this.imagenes = imagenes;
        Marca = marca;
        this.stock = stock;
        this.caracteristicas = caracteristicas;
        this.incluye = incluye;
        this.setKey(key);
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
