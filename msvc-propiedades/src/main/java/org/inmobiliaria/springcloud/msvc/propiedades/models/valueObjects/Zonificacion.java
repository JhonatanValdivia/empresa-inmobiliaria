package org.inmobiliaria.springcloud.msvc.propiedades.models.valueObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Zonificacion {

    private String tipoZona;
    private String descripcionNormativa;
    private String usoPermitido;

    protected Zonificacion() {}

    public Zonificacion(String tipoZona, String descripcionNormativa, String usoPermitido) {
        if (tipoZona == null || tipoZona.isEmpty()) {
            throw new IllegalArgumentException("Tipo de zona no puede ser vacío");
        }
        if (descripcionNormativa == null || descripcionNormativa.isEmpty()) {
            throw new IllegalArgumentException("Descripción normativa no puede ser vacía");
        }
        if (usoPermitido == null || usoPermitido.isEmpty()) {
            throw new IllegalArgumentException("Uso permitido no puede ser vacío");
        }

        this.tipoZona = tipoZona;
        this.descripcionNormativa = descripcionNormativa;
        this.usoPermitido = usoPermitido;
    }

    public String getTipoZona() {
        return tipoZona;
    }

    public String getDescripcionNormativa() {
        return descripcionNormativa;
    }

    public String getUsoPermitido() {
        return usoPermitido;
    }

    @Override
    public String toString() {
        return "Zonificación{" +
                "tipoZona='" + tipoZona + '\'' +
                ", descripcionNormativa='" + descripcionNormativa + '\'' +
                ", usoPermitido='" + usoPermitido + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Zonificacion)) return false;
        Zonificacion other = (Zonificacion) obj;
        return tipoZona.equals(other.tipoZona) &&
                descripcionNormativa.equals(other.descripcionNormativa) &&
                usoPermitido.equals(other.usoPermitido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipoZona, descripcionNormativa, usoPermitido);
    }
}