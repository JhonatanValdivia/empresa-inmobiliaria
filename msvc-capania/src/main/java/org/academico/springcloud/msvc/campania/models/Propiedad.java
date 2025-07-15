package org.academico.springcloud.msvc.campania.models;

//POJO
public class Propiedad {
    private Long idPropiedad;
    private String tipoPropiedad;
    private String estado;
    private Precio precio;
    private Ubicacion ubicacion;
    private Zonificacion zonificacion;

    public Propiedad() {}

    public Propiedad(Long idPropiedad, String tipoPropiedad, String estado, Precio precio, Ubicacion ubicacion, Zonificacion zonificacion) {
        this.idPropiedad = idPropiedad;
        this.tipoPropiedad = tipoPropiedad;
        this.estado = estado;
        this.precio = precio;
        this.ubicacion = ubicacion;
        this.zonificacion = zonificacion;
    }

    public Long getIdPropiedad() { return idPropiedad; }
    public void setIdPropiedad(Long idPropiedad) { this.idPropiedad = idPropiedad; }
    public String getTipoPropiedad() { return tipoPropiedad; }
    public void setTipoPropiedad(String tipoPropiedad) { this.tipoPropiedad = tipoPropiedad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Precio getPrecio() { return precio; }
    public void setPrecio(Precio precio) { this.precio = precio; }
    public Ubicacion getUbicacion() { return ubicacion; }
    public void setUbicacion(Ubicacion ubicacion) { this.ubicacion = ubicacion; }
    public Zonificacion getZonificacion() { return zonificacion; }
    public void setZonificacion(Zonificacion zonificacion) { this.zonificacion = zonificacion; }
}