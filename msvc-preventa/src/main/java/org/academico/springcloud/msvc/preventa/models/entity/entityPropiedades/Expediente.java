package org.academico.springcloud.msvc.preventa.models.entity.entityPropiedades;

import org.academico.springcloud.msvc.preventa.models.enums.enumsPropiedades.EstadoExpediente;
import org.academico.springcloud.msvc.preventa.models.enums.enumsPropiedades.TipoExpediente;
import org.academico.springcloud.msvc.preventa.models.valueObjects.Fecha;

import java.util.List;

public class Expediente {
    private Long idExpediente;
    private TipoExpediente tipoExpediente;
    private EstadoExpediente estadoExpediente;
    private String observaciones;
    private Fecha fecha;
    private List<Plano> planos;

    public Long getIdExpediente() { return idExpediente; }
    public void setIdExpediente(Long idExpediente) { this.idExpediente = idExpediente; }
    public TipoExpediente getTipoExpediente() { return tipoExpediente; }
    public void setTipoExpediente(TipoExpediente tipoExpediente) { this.tipoExpediente = tipoExpediente; }
    public EstadoExpediente getEstadoExpediente() { return estadoExpediente; }
    public void setEstadoExpediente(EstadoExpediente estadoExpediente) { this.estadoExpediente = estadoExpediente; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public Fecha getFecha() { return fecha; }
    public void setFecha(Fecha fecha) { this.fecha = fecha; }
    public List<Plano> getPlanos() { return planos; }
    public void setPlanos(List<Plano> planos) { this.planos = planos; }
}