package org.academico.springcloud.msvc.usuario.models.valueObjects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Embeddable
public class CorreoElectronico {
    @NotBlank(message = "El dominio del correo es obligatorio.")
    @Email(message = "El dominio debe ser una dirección de correo válida.")
    private String dominio;

    public CorreoElectronico() {}

    public CorreoElectronico(String dominio) {
        if (dominio == null || dominio.trim().isEmpty()) {
            throw new IllegalArgumentException("El dominio del correo es obligatorio.");
        }
        this.dominio = dominio.trim();
    }

    public String getDominio() { return dominio; }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof CorreoElectronico)) return false;
        CorreoElectronico correo = (CorreoElectronico) object;
        return dominio.equals(correo.dominio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dominio);
    }

    @Override
    public String toString() {
        return "@" + dominio;
    }
}