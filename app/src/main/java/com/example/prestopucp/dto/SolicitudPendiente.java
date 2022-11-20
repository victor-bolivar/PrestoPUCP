package com.example.prestopucp.dto;

public class SolicitudPendiente {




    // solicitud rechazada
    private String motivoRechazo;

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }


    // solicitud pendiente

    public SolicitudPendiente(String curso, String detalles, String dispositivoId, String dniUrl, String motivo, String nombre, String programas, String rol, String tiempoReserva, String id) {
        this.curso = curso;
        this.detalles = detalles;
        this.dispositivoId = dispositivoId;
        this.dniUrl = dniUrl;
        this.motivo = motivo;
        this.nombre = nombre;
        this.programas = programas;
        this.rol = rol;
        this.tiempoReserva = tiempoReserva;
        this.id = id;
    }

    private String id;

    private String curso;

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

    private String detalles;
    private String dispositivoId;
    private String dniUrl;
    private String motivo;
    private String nombre;
    private String programas;
    private String rol;
    private String tiempoReserva;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
