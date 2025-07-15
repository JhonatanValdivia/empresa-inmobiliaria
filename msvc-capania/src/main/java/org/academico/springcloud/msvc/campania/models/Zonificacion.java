package org.academico.springcloud.msvc.campania.models;

public class Zonificacion {
    private String tipoZona;
    private String descripcionNormativa;
    private String usoPermitido;

    public Zonificacion() {}

    public Zonificacion(String tipoZona, String descripcionNormativa, String usoPermitido) {
        this.tipoZona = tipoZona;
        this.descripcionNormativa = descripcionNormativa;
        this.usoPermitido = usoPermitido;
    }

    public String getTipoZona() { return tipoZona; }
    public void setTipoZona(String tipoZona) { this.tipoZona = tipoZona; }
    public String getDescripcionNormativa() { return descripcionNormativa; }
    public void setDescripcionNormativa(String descripcionNormativa) { this.descripcionNormativa = descripcionNormativa; }
    public String getUsoPermitido() { return usoPermitido; }
    public void setUsoPermitido(String usoPermitido) { this.usoPermitido = usoPermitido; }
}