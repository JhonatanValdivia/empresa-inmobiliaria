package org.academico.springcloud.msvc.comision.models.valueObjects.valueObjectsUsuarios;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Telefono {
    private String numero;
    private String codigoPais;

    protected Telefono() {}

    @JsonCreator
    public Telefono(@JsonProperty("numero") String numero, @JsonProperty("codigoPais") String codigoPais) {
        this.numero = numero;
        this.codigoPais = codigoPais;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(String codigoPais) {
        this.codigoPais = codigoPais;
    }
}