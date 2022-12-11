package com.example.prestopucp.dto;

import java.io.Serializable;

public class ReservaDispositivo implements Serializable {

    String idReserva;
    String idDispositivo;
    String idUsuario;
    String curso;
    String motivo;
    //tiempo en dias
    String tiempoReserva;
    String programasNecesarios;
    String otros;
    String estadoReserva;
    String dniUrl;

    public ReservaDispositivo(){}

    public ReservaDispositivo(String idDispositivo, String idUsuario, String curso, String motivo, String tiempoReserva, String programasNecesarios, String otros, String estadoReserva,String idReserva, String dniUrl) {
        this.idDispositivo = idDispositivo;
        this.idUsuario = idUsuario;
        this.curso = curso;
        this.motivo = motivo;
        this.tiempoReserva = tiempoReserva;
        this.programasNecesarios = programasNecesarios;
        this.otros = otros;
        this.estadoReserva = estadoReserva;
        this.idReserva=idReserva;
        this.dniUrl = dniUrl;
    }

    public String getDniUrl() {
        return dniUrl;
    }

    public void setDniUrl(String dniUrl) {
        this.dniUrl = dniUrl;
    }

    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    public String getIdDispositivo() {
        return idDispositivo;
    }

    public void setIdDispositivo(String idDispositivo) {
        this.idDispositivo = idDispositivo;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getTiempoReserva() {
        return tiempoReserva;
    }

    public void setTiempoReserva(String tiempoReserva) {
        this.tiempoReserva = tiempoReserva;
    }

    public String getProgramasNecesarios() {
        return programasNecesarios;
    }

    public void setProgramasNecesarios(String programasNecesarios) {
        this.programasNecesarios = programasNecesarios;
    }

    public String getOtros() {
        return otros;
    }

    public void setOtros(String otros) {
        this.otros = otros;
    }

    public String getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(String estadoReserva) {
        this.estadoReserva = estadoReserva;
    }
}
