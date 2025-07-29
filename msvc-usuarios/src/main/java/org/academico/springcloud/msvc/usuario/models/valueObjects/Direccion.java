package org.academico.springcloud.msvc.usuario.models.valueObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Direccion {
    private String ubigeo;
    private String ciudad;
    private String direccion;

    protected Direccion() {}

    @JsonCreator
    public Direccion(@JsonProperty("ubigeo") String ubigeo, @JsonProperty("ciudad") String ciudad,
                     @JsonProperty("direccion") String direccion) {
        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new IllegalArgumentException("La ciudad es obligatoria.");
        }
        if (ciudad.length() > 100) {
            throw new IllegalArgumentException("La ciudad no puede tener más de 100 caracteres.");
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección es obligatoria.");
        }
        if (direccion.length() > 255) {
            throw new IllegalArgumentException("La dirección no puede tener más de 255 caracteres.");
        }

        this.ubigeo = (ubigeo != null) ? ubigeo.trim() : null;
        this.ciudad = ciudad.trim();
        this.direccion = direccion.trim().replaceAll("\\s+", " ");

        if (this.ubigeo != null && !this.ubigeo.matches("\\d{6}")) {
            throw new IllegalArgumentException("El ubigeo debe tener exactamente 6 dígitos numéricos.");
        }
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Direccion)) return false;
        Direccion dir = (Direccion) object;
        return Objects.equals(ubigeo, dir.ubigeo) && ciudad.equals(dir.ciudad) && direccion.equals(dir.direccion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ubigeo, ciudad, direccion);
    }

    @Override
    public String toString() {
        return (ubigeo != null ? ubigeo + ", " : "") + ciudad + ": " + direccion;
    }
}