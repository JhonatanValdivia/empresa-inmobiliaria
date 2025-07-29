package org.academico.springcloud.msvc.preventa.models.entity.entityPropiedades;

import org.academico.springcloud.msvc.preventa.models.enums.enumsPropiedades.TipoNorma;
import org.academico.springcloud.msvc.preventa.models.valueObjects.Fecha;

public class Norma {
    private Long idNorma;
    private Fecha fecha;
    private TipoNorma tipo;
    private String estadoNorma;
    private String descripcion;

    public Long getIdNorma() { return idNorma; }
    public void setIdNorma(Long idNorma) { this.idNorma = idNorma; }
    public Fecha getFecha() { return fecha; }
    public void setFecha(Fecha fecha) { this.fecha = fecha; }
    public TipoNorma getTipo() { return tipo; }
    public void setTipo(TipoNorma tipo) { this.tipo = tipo; }
    public String getEstadoNorma() { return estadoNorma; }
    public void setEstadoNorma(String estadoNorma) { this.estadoNorma = estadoNorma; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}