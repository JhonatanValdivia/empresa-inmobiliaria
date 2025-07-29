package org.academico.springcloud.msvc.preventa.models.entity.entityPropiedades;

import org.academico.springcloud.msvc.preventa.models.enums.enumsPropiedades.EstadoDocumentoLegal;
import org.academico.springcloud.msvc.preventa.models.enums.enumsPropiedades.TipoDocumentoLegal;

public class DocumentoLegal {
    private Long idDocumentoLegal;
    private TipoDocumentoLegal tipoDocumento;
    private String nombreNotaria;
    private String numeroInscripcionSunarp;
    private EstadoDocumentoLegal estadoDocumento;

    public Long getIdDocumentoLegal() { return idDocumentoLegal; }
    public void setIdDocumentoLegal(Long idDocumentoLegal) { this.idDocumentoLegal = idDocumentoLegal; }
    public TipoDocumentoLegal getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(TipoDocumentoLegal tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    public String getNombreNotaria() { return nombreNotaria; }
    public void setNombreNotaria(String nombreNotaria) { this.nombreNotaria = nombreNotaria; }
    public String getNumeroInscripcionSunarp() { return numeroInscripcionSunarp; }
    public void setNumeroInscripcionSunarp(String numeroInscripcionSunarp) { this.numeroInscripcionSunarp = numeroInscripcionSunarp; }
    public EstadoDocumentoLegal getEstadoDocumento() { return estadoDocumento; }
    public void setEstadoDocumento(EstadoDocumentoLegal estadoDocumento) { this.estadoDocumento = estadoDocumento; }
}