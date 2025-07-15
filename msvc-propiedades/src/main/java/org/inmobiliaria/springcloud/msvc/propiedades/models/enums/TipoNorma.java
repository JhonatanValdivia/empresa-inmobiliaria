package org.inmobiliaria.springcloud.msvc.propiedades.models.enums;

public enum TipoNorma {
    SEGURIDAD("Seguridad"),
    AMBIENTAL("Ambiental"),
    CONSTRUCCION("Construcción");

    private final String descripcion;

    TipoNorma(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}