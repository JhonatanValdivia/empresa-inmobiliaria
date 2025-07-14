package org.inmobiliaria.springcloud.msvc.propiedades.models.valueObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Zonificacion {

    @Column(name = "tipo_zona", nullable = false)
    private String tipoZona;

    @Column(name = "descripcion_normativa", nullable = false)
    private String descripcionNormativa;

    @Column(name = "uso_permitido", nullable = false)
    private String usoPermitido;

    protected Zonificacion() {}

    public Zonificacion(String tipoZona, String descripcionNormativa, String usoPermitido) {
        this.tipoZona = tipoZona;
        this.descripcionNormativa = descripcionNormativa;
        this.usoPermitido = usoPermitido;
    }

    public String getTipoZona() {
        return tipoZona;
    }

    public void setTipoZona(String tipoZona) {
        this.tipoZona = tipoZona;
    }

    public String getDescripcionNormativa() {
        return descripcionNormativa;
    }

    public void setDescripcionNormativa(String descripcionNormativa) {
        this.descripcionNormativa = descripcionNormativa;
    }

    public String getUsoPermitido() {
        return usoPermitido;
    }

    public void setUsoPermitido(String usoPermitido) {
        this.usoPermitido = usoPermitido;
    }
}