package org.academico.springcloud.msvc.comision.models;

import org.academico.springcloud.msvc.comision.models.valueObjects.valueObjectsUsuarios.CorreoElectronico;
import org.academico.springcloud.msvc.comision.models.valueObjects.valueObjectsUsuarios.Direccion;
import org.academico.springcloud.msvc.comision.models.valueObjects.valueObjectsUsuarios.NombreCompleto;
import org.academico.springcloud.msvc.comision.models.valueObjects.valueObjectsUsuarios.Telefono;

public class Usuario {
    private Long id;
    private NombreCompleto nombreCompleto;
    private String tipoUsuario;
    private Telefono telefono;
    private CorreoElectronico correoElectronico;
    private Direccion direccion;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NombreCompleto getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(NombreCompleto nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Telefono getTelefono() {
        return telefono;
    }

    public void setTelefono(Telefono telefono) {
        this.telefono = telefono;
    }

    public CorreoElectronico getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(CorreoElectronico correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
}