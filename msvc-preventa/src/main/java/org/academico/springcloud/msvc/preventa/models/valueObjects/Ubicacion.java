package org.academico.springcloud.msvc.preventa.models.valueObjects;

public class Ubicacion {
    private String ubigeo;
    private String ciudad;
    private String direccion;

    public Ubicacion() {}

    public Ubicacion(String ubigeo, String ciudad, String direccion) {
        this.ubigeo = ubigeo;
        this.ciudad = ciudad;
        this.direccion = direccion;
    }

    public String getUbigeo() { return ubigeo; }
    public void setUbigeo(String ubigeo) { this.ubigeo = ubigeo; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}