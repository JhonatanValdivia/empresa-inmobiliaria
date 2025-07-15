package org.academico.springcloud.msvc.venta.models.dtos;

import org.academico.springcloud.msvc.venta.models.enums.EstadoPreventa;

import java.time.LocalDate;

public class PreventaDTO {

    private Long id;
    private LocalDate fechaInicio;
    private EstadoPreventa estado;
    // Añade aquí cualquier otro campo de Preventa que Venta necesite consumir
    // Ejemplo: private String nombreClientePreventa; // Si existiera en Preventa

    public PreventaDTO() {
    }

    public PreventaDTO(Long id, LocalDate fechaInicio, EstadoPreventa estado) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.estado = estado;
    }

    // Getters y Setters (autogenerar en IDE)
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

    public EstadoPreventa getEstado() {
        return estado;
    }

    public void setEstado(EstadoPreventa estado) {
        this.estado = estado;
    }
}
