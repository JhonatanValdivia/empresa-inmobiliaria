package org.inmobiliaria.springcloud.msvc.propiedades.models.valueObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Ubicacion {

    private String ubigeo;
    private String ciudad;
    private String direccion;

    protected Ubicacion() {}

    public Ubicacion(String ubigeo, String ciudad, String direccion) {
        if (ubigeo == null || ubigeo.isEmpty()) {
            throw new IllegalArgumentException("Ubigeo no puede ser vacío");
        }
        if (ciudad == null || ciudad.isEmpty()) {
            throw new IllegalArgumentException("Ciudad no puede ser vacía");
        }
        if (direccion == null || direccion.isEmpty()) {
            throw new IllegalArgumentException("Dirección no puede ser vacía");
        }

        this.ubigeo = ubigeo;
        this.ciudad = ciudad;
        this.direccion = direccion;
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
    public String toString() {
        return "Ubicacion{" +
                "ubigeo='" + ubigeo + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Ubicacion)) return false;
        Ubicacion other = (Ubicacion) obj;
        return ubigeo.equals(other.ubigeo) &&
                ciudad.equals(other.ciudad) &&
                direccion.equals(other.direccion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ubigeo, ciudad, direccion);
    }
}

