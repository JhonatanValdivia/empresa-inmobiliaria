package org.academico.springcloud.msvc.comision.models.valueObjects.valueObjectsUsuarios;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CorreoElectronico {
    private String dominio;
    private String valorCorreo;

    protected CorreoElectronico() {}

    @JsonCreator
    public CorreoElectronico(@JsonProperty("dominio") String dominio, @JsonProperty("valorCorreo") String valorCorreo) {
        this.dominio = dominio;
        this.valorCorreo = valorCorreo;
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public String getValorCorreo() {
        return valorCorreo;
    }

    public void setValorCorreo(String valorCorreo) {
        this.valorCorreo = valorCorreo;
    }
}