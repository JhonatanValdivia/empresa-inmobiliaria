package org.academico.springcloud.msvc.usuario.models.valueObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class CorreoElectronico {
    private String dominio;
    private String valorCorreo;

    protected CorreoElectronico() {}

    @JsonCreator
    public CorreoElectronico(@JsonProperty("dominio") String dominio, @JsonProperty("valorCorreo") String valorCorreo) {
        if (dominio == null || dominio.trim().isEmpty()) {
            throw new IllegalArgumentException("El dominio no puede ser vacío.");
        }
        if (valorCorreo == null || valorCorreo.trim().isEmpty()) {
            throw new IllegalArgumentException("El valor del correo no puede ser vacío.");
        }

        this.dominio = dominio.trim().toLowerCase();
        this.valorCorreo = valorCorreo.trim().toLowerCase();

        String correoCompleto = valorCorreo + "@" + dominio;
        if (correoCompleto.length() > 254) {
            throw new IllegalArgumentException("El correo excede la longitud máxima permitida.");
        }
        if (!correoCompleto.matches("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Formato de correo inválido.");
        }
    }

    public String getDominio() {
        return dominio;
    }

    public String getValorCorreo() {
        return valorCorreo;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof CorreoElectronico)) return false;
        CorreoElectronico correo = (CorreoElectronico) object;
        return dominio.equals(correo.dominio) && valorCorreo.equals(correo.valorCorreo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dominio, valorCorreo);
    }

    @Override
    public String toString() {
        return valorCorreo + "@" + dominio;
    }
}