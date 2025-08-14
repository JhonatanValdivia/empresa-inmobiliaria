package org.academico.springcloud.msvc.usuario.models.entities;

import jakarta.persistence.*;
import org.academico.springcloud.msvc.usuario.models.enums.TipoUsuario;
import org.academico.springcloud.msvc.usuario.models.valueObjects.CorreoElectronico;
import org.academico.springcloud.msvc.usuario.models.valueObjects.Direccion;
import org.academico.springcloud.msvc.usuario.models.valueObjects.NombreCompleto;
import org.academico.springcloud.msvc.usuario.models.valueObjects.Telefono;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private NombreCompleto nombreCompleto;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    @Embedded
    private Telefono telefono;

    @Embedded
    private CorreoElectronico correoElectronico;

    @Embedded
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

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
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

    public void asignarRol(TipoUsuario rol) {
        this.tipoUsuario = rol;
    }

    public void actualizarDatos(NombreCompleto nombre, Telefono telefono, CorreoElectronico correo, Direccion direccion) {
        this.nombreCompleto = nombre;
        this.telefono = telefono;
        this.correoElectronico = correo;
        this.direccion = direccion;
    }
}