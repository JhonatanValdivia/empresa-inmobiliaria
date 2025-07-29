package org.academico.springcloud.msvc.usuario.models.valueObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class NombreCompleto {
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;

    protected NombreCompleto() {}

    @JsonCreator
    public NombreCompleto(@JsonProperty("primerNombre") String primerNombre, @JsonProperty("segundoNombre") String segundoNombre,
                          @JsonProperty("primerApellido") String primerApellido, @JsonProperty("segundoApellido") String segundoApellido) {
        if (primerNombre == null || primerNombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El primer nombre es obligatorio.");
        }
        if (primerApellido == null || primerApellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El primer apellido es obligatorio.");
        }

        this.primerNombre = primerNombre.trim().replaceAll("\\s{2,}", " ");
        this.segundoNombre = (segundoNombre != null) ? segundoNombre.trim().replaceAll("\\s{2,}", " ") : "";
        this.primerApellido = primerApellido.trim().replaceAll("\\s{2,}", " ");
        this.segundoApellido = (segundoApellido != null) ? segundoApellido.trim().replaceAll("\\s{2,}", " ") : "";

        if (primerNombre.length() > 50 || (segundoNombre != null && segundoNombre.length() > 50) ||
                primerApellido.length() > 50 || (segundoApellido != null && segundoApellido.length() > 50)) {
            throw new IllegalArgumentException("Cada parte del nombre debe tener como máximo 50 caracteres.");
        }

        String regex = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü'\\- ]+$";
        if (!primerNombre.matches(regex) || (!segundoNombre.isEmpty() && !segundoNombre.matches(regex)) ||
                !primerApellido.matches(regex) || (!segundoApellido.isEmpty() && !segundoApellido.matches(regex))) {
            throw new IllegalArgumentException("El nombre solo puede contener letras, espacios, guiones y tildes.");
        }
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof NombreCompleto)) return false;
        NombreCompleto nombreCompleto = (NombreCompleto) object;
        return primerNombre.equals(nombreCompleto.primerNombre) &&
                segundoNombre.equals(nombreCompleto.segundoNombre) &&
                primerApellido.equals(nombreCompleto.primerApellido) &&
                segundoApellido.equals(nombreCompleto.segundoApellido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(primerNombre, segundoNombre, primerApellido, segundoApellido);
    }

    @Override
    public String toString() {
        return String.join(" ", primerNombre, segundoNombre.isEmpty() ? "" : segundoNombre, primerApellido,
                segundoApellido.isEmpty() ? "" : segundoApellido).replaceAll("\\s{2,}", " ").trim();
    }
}