package org.academico.springcloud.msvc.preventa.models;

import org.academico.springcloud.msvc.preventa.models.entity.entityPropiedades.*;
import org.academico.springcloud.msvc.preventa.models.enums.enumsPropiedades.EstadoPropiedad;
import org.academico.springcloud.msvc.preventa.models.enums.enumsPropiedades.TipoPropiedad;
import org.academico.springcloud.msvc.preventa.models.valueObjects.Precio;
import org.academico.springcloud.msvc.preventa.models.valueObjects.Ubicacion;
import org.academico.springcloud.msvc.preventa.models.valueObjects.Zonificacion;

import java.util.List;

public class PropiedadInmobiliaria {
    private Long idPropiedad;
    private TipoPropiedad tipoPropiedad;
    private EstadoPropiedad estado;
    private Precio precio;
    private Ubicacion ubicacion;
    private Zonificacion zonificacion;
    private List<DocumentoLegal> documentosLegales;
    private List<Servicio> servicios;
    private Expediente expediente;
    private List<PropiedadInmobiliariaNorma> propiedadNormas;
    private List<Norma> normas;

    // Getters y Setters
    public Long getIdPropiedad() { return idPropiedad; }
    public void setIdPropiedad(Long idPropiedad) { this.idPropiedad = idPropiedad; }
    public TipoPropiedad getTipoPropiedad() { return tipoPropiedad; }
    public void setTipoPropiedad(TipoPropiedad tipoPropiedad) { this.tipoPropiedad = tipoPropiedad; }
    public EstadoPropiedad getEstado() { return estado; }
    public void setEstado(EstadoPropiedad estado) { this.estado = estado; }
    public Precio getPrecio() { return precio; }
    public void setPrecio(Precio precio) { this.precio = precio; }
    public Ubicacion getUbicacion() { return ubicacion; }
    public void setUbicacion(Ubicacion ubicacion) { this.ubicacion = ubicacion; }
    public Zonificacion getZonificacion() { return zonificacion; }
    public void setZonificacion(Zonificacion zonificacion) { this.zonificacion = zonificacion; }
    public List<DocumentoLegal> getDocumentosLegales() { return documentosLegales; }
    public void setDocumentosLegales(List<DocumentoLegal> documentosLegales) { this.documentosLegales = documentosLegales; }
    public List<Servicio> getServicios() { return servicios; }
    public void setServicios(List<Servicio> servicios) { this.servicios = servicios; }
    public Expediente getExpediente() { return expediente; }
    public void setExpediente(Expediente expediente) { this.expediente = expediente; }
    public List<PropiedadInmobiliariaNorma> getPropiedadNormas() { return propiedadNormas; }
    public void setPropiedadNormas(List<PropiedadInmobiliariaNorma> propiedadNormas) { this.propiedadNormas = propiedadNormas; }
    public List<Norma> getNormas() { return normas; }
    public void setNormas(List<Norma> normas) { this.normas = normas; }
}