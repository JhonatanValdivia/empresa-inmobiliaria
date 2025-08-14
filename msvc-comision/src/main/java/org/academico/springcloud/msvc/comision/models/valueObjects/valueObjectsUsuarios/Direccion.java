package org.academico.springcloud.msvc.comision.models.valueObjects.valueObjectsUsuarios;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Direccion {
    private String ubigeo;
    private String ciudad;
    private String direccion;

    protected Direccion() {}

    @JsonCreator
    public Direccion(@JsonProperty("ubigeo") String ubigeo, @JsonProperty("ciudad") String ciudad,
                     @JsonProperty("direccion") String direccion) {
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