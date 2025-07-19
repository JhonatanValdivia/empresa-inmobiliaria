package org.academico.springcloud.msvc.venta.models;

import java.time.LocalDate;

public class Preventa {

    private Long id;
    private LocalDate fechaInicio;
    private String estado;

    // --- Getters y Setters ---

    public Preventa() {
    }

    public Preventa(Long id, String estado, LocalDate fechaInicio) {
        this.id = id;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

}
