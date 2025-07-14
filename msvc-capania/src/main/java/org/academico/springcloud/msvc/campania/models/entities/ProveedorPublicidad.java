package org.academico.springcloud.msvc.campania.models.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "ProveedoresPublicidad")
public class ProveedorPublicidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProveedor;

    private String nombre;
    private String cuentaPublicitaria;

    // Constructores
    public ProveedorPublicidad() {}

    // Métodos
    public void agregarProveedor() {
        // Validación: Asegura que el proveedor tenga nombre y cuenta antes de agregarlo
        if (this.nombre == null || this.nombre.trim().isEmpty()) {
            throw new IllegalStateException("El nombre del proveedor es obligatorio");
        }
        if (this.cuentaPublicitaria == null || this.cuentaPublicitaria.trim().isEmpty()) {
            throw new IllegalStateException("La cuenta publicitaria es obligatoria");
        }
    }

    public void editarProveedor(String nuevoNombre, String nuevaCuenta) {
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nuevo nombre no puede estar vacío o nulo.");
        }
        if (nuevaCuenta == null || nuevaCuenta.trim().isEmpty()) {
            throw new IllegalArgumentException("La nueva cuenta publicitaria no puede estar vacía o nula.");
        }

        this.nombre = nuevoNombre.trim();
        this.cuentaPublicitaria = nuevaCuenta.trim();
    }

    public void eliminarProveedor() {
        if (this.idProveedor == null) {
            throw new IllegalStateException("No se puede eliminar un proveedor sin un ID válido.");
        }
    }

    // Getters y setters
    public Long getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Long idProveedor) { this.idProveedor = idProveedor; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCuentaPublicitaria() { return cuentaPublicitaria; }
    public void setCuentaPublicitaria(String cuentaPublicitaria) { this.cuentaPublicitaria = cuentaPublicitaria; }
}