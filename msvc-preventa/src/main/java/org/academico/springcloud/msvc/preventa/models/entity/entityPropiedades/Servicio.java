package org.academico.springcloud.msvc.preventa.models.entity.entityPropiedades;

import org.academico.springcloud.msvc.preventa.models.enums.enumsPropiedades.EstadoServicio;
import org.academico.springcloud.msvc.preventa.models.enums.enumsPropiedades.TipoServicio;

public class Servicio {
    private Long idServicio;
    private TipoServicio tipoServicio;
    private EstadoServicio estadoServicio;
    private String proveedor;
    private String numeroSuministro;

    public Long getIdServicio() { return idServicio; }
    public void setIdServicio(Long idServicio) { this.idServicio = idServicio; }
    public TipoServicio getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(TipoServicio tipoServicio) { this.tipoServicio = tipoServicio; }
    public EstadoServicio getEstadoServicio() { return estadoServicio; }
    public void setEstadoServicio(EstadoServicio estadoServicio) { this.estadoServicio = estadoServicio; }
    public String getProveedor() { return proveedor; }
    public void setProveedor(String proveedor) { this.proveedor = proveedor; }
    public String getNumeroSuministro() { return numeroSuministro; }
    public void setNumeroSuministro(String numeroSuministro) { this.numeroSuministro = numeroSuministro; }
}