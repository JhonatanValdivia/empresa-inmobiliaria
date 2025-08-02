package org.academico.springcloud.msvc.usuario.models.valueObjects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Embeddable
public class Telefono {
    @NotBlank(message = "El número de teléfono no puede estar vacío")
    private String numero;

    @NotBlank(message = "El país no puede estar vacío")
    private String pais;

    public Telefono() {}

    public Telefono(String numero, String pais) {
        if (numero == null || numero.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de teléfono es obligatorio.");
        }
        if (pais == null || pais.trim().isEmpty()) {
            throw new IllegalArgumentException("El país es obligatorio.");
        }
        this.numero = numero.trim();
        this.pais = pais.trim();
    }

    public String getNumero() { return numero; }
    public String getPais() { return pais; }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Telefono)) return false;
        Telefono telefono = (Telefono) object;
        return numero.equals(telefono.numero) && pais.equals(telefono.pais);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, pais);
    }

    @Override
    public String toString() {
        return numero + " (" + pais + ")";
    }
}