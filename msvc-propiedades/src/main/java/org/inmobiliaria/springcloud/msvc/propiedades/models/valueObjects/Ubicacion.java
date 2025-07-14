package org.inmobiliaria.springcloud.msvc.propiedades.models.valueObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Ubicacion {

    @Column(name = "ubigeo", nullable = false)
    private String ubigeo;

    @Column(name = "ciudad", nullable = false)
    private String ciudad;

    @Column(name = "direccion", nullable = false)
    private String direccion;

    protected Ubicacion() {}

    public Ubicacion(String ubigeo, String ciudad, String direccion) {
        this.ubigeo = ubigeo;
        this.ciudad = ciudad;
        this.direccion = direccion;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}

