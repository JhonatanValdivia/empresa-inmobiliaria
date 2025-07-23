package org.academico.springcloud.msvc.usuario.models.valueObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Embeddable
public class Telefono {
    private String numero;
    private String codigoPais;

    protected Telefono() {
    }

    @JsonCreator
    public Telefono(@JsonProperty("numero") String numero, @JsonProperty("codigoPais") String codigoPais) {
        if (codigoPais == null || codigoPais.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de país es obligatorio.");
        }
        if (numero == null || numero.trim().isEmpty()) {
            throw new IllegalArgumentException("El número es obligatorio.");
        }

        codigoPais = codigoPais.trim();
        numero = numero.trim().replaceAll("[^\\d]", "");

        if (!codigoPais.matches("^\\+\\d{1,4}$")) {
            throw new IllegalArgumentException("El código de país debe comenzar con '+' seguido de 1 a 4 dígitos.");
        }
        if (!numero.matches("^\\d{6,15}$")) {
            throw new IllegalArgumentException("El número debe tener entre 6 y 15 dígitos.");
        }

        this.codigoPais = codigoPais;
        this.numero = numero;
    }

    public String getNumero() {
        return numero;
    }

    public String getCodigoPais() {
        return codigoPais;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Telefono)) return false;
        Telefono telefono = (Telefono) object;
        return numero.equals(telefono.numero) && codigoPais.equals(telefono.codigoPais);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, codigoPais);
    }

    @Override
    public String toString() {
        return codigoPais + " " + numero;
    }
}