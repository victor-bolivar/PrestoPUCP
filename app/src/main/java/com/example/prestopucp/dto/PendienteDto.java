package com.example.prestopucp.dto;

public class PendienteDto {

    public String curso;
    public String detalles;
    public String dispositivoId;
    public String dniUrl;
    public String emailUsuario;
    public String id;
    public String motivo;
    public String nombre;
    public String programas;
    public String rol;
    public String tiempoReserva;

    public PendienteDto() {
    }

    public PendienteDto(String curso, String detalles, String dispositivoId, String dniUrl, String emailUsuario, String id, String motivo, String nombre, String programas, String rol, String tiempoReserva) {
        this.curso = curso;
        this.detalles = detalles;
        this.dispositivoId = dispositivoId;
        this.dniUrl = dniUrl;
        this.emailUsuario = emailUsuario;
        this.id = id;
        this.motivo = motivo;
        this.nombre = nombre;
        this.programas = programas;
        this.rol = rol;
        this.tiempoReserva = tiempoReserva;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public String getDispositivoId() {
        return dispositivoId;
    }

    public void setDispositivoId(String dispositivoId) {
        this.dispositivoId = dispositivoId;
    }

    public String getDniUrl() {
        return dniUrl;
    }

    public void setDniUrl(String dniUrl) {
        this.dniUrl = dniUrl;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProgramas() {
        return programas;
    }

    public void setProgramas(String programas) {
        this.programas = programas;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getTiempoReserva() {
        return tiempoReserva;
    }

    public void setTiempoReserva(String tiempoReserva) {
        this.tiempoReserva = tiempoReserva;
    }
}
