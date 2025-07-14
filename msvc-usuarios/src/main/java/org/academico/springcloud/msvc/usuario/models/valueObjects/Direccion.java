package org.academico.springcloud.msvc.usuario.models.valueObjects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Embeddable
public class Direccion {
    @NotBlank(message = "El ubigeo es obligatorio.")
    private String ubigeo;

    @NotBlank(message = "La ciudad es obligatoria.")
    private String ciudad;

    @NotBlank(message = "La dirección es obligatoria.")
    private String direccion;

    public Direccion() {}

    public Direccion(String ubigeo, String ciudad, String direccion) {
        if (ubigeo == null || ubigeo.trim().isEmpty()) {
            throw new IllegalArgumentException("El ubigeo es obligatorio.");
        }
        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new IllegalArgumentException("La ciudad es obligatoria.");
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección es obligatoria.");
        }
        this.ubigeo = ubigeo.trim();
        this.ciudad = ciudad.trim();
        this.direccion = direccion.trim();
    }

    public String getUbigeo() { return ubigeo; }
    public String getCiudad() { return ciudad; }
    public String getDireccion() { return direccion; }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Direccion)) return false;
        Direccion dir = (Direccion) object;
        return ubigeo.equals(dir.ubigeo) && ciudad.equals(dir.ciudad) && direccion.equals(dir.direccion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ubigeo, ciudad, direccion);
    }

    @Override
    public String toString() {
        return ubigeo + ", " + ciudad + ": " + direccion;
    }
}