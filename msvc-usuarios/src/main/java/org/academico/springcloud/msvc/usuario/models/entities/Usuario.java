package org.academico.springcloud.msvc.usuario.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.academico.springcloud.msvc.usuario.models.enums.TipoUsuario;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(name = "nombres")
    private String nombres;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Column(name = "apellidos")
    private String apellidos;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario")
    @NotNull(message = "El tipo de usuario es obligatorio")
    private TipoUsuario tipoUsuario;

    @Column(name = "telefono")
    private String telefono; // Almacena el teléfono como texto plano

    @Column(name = "correo_electronico")
    private String correoElectronico; // Almacena el correo como texto plano

    @Column(name = "direccion")
    private String direccion; // Almacena la dirección como texto plano

    @Column(name = "estado")
    private String estado = "ACTIVO";

    // Constructores
    public Usuario() {}

    public Usuario(String nombres, String apellidos, TipoUsuario tipoUsuario) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.tipoUsuario = tipoUsuario;
    }

    // Getters y setters
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public TipoUsuario getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(TipoUsuario tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}