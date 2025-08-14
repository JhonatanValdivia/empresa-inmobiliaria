package org.inmobiliaria.springcloud.msvc.propiedades.models.entitys;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.inmobiliaria.springcloud.msvc.propiedades.models.Norma;
import org.inmobiliaria.springcloud.msvc.propiedades.models.enums.EstadoPropiedad;
import org.inmobiliaria.springcloud.msvc.propiedades.models.enums.TipoPropiedad;
import org.inmobiliaria.springcloud.msvc.propiedades.models.valueObjects.Precio;
import org.inmobiliaria.springcloud.msvc.propiedades.models.valueObjects.Ubicacion;
import org.inmobiliaria.springcloud.msvc.propiedades.models.valueObjects.Zonificacion;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Entity
@Table
public class PropiedadInmobiliaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_propiedad")
    private Long idPropiedad;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_propiedad", nullable = false)
    private TipoPropiedad tipoPropiedad;


    @Column(name = "estado", nullable = false)
    private EstadoPropiedad estado;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "monto", column = @Column(name = "precio_monto", nullable = false)),
            @AttributeOverride(name = "moneda", column = @Column(name = "precio_moneda", nullable = false))
    })
    private Precio precio;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "ubigeo", column = @Column(name = "ubicacion_ubigeo", nullable = false)),
            @AttributeOverride(name = "ciudad", column = @Column(name = "ubicacion_ciudad", nullable = false)),
            @AttributeOverride(name = "direccion", column = @Column(name = "ubicacion_direccion", nullable = false))
    })
    private Ubicacion ubicacion;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "tipoZona", column = @Column(name = "zonificacion_tipo_zona", nullable = false)),
            @AttributeOverride(name = "descripcionNormativa", column = @Column(name = "zonificacion_descripcion_normativa", nullable = false)),
            @AttributeOverride(name = "usoPermitido", column = @Column(name = "zonificacion_uso_permitido", nullable = false))
    })
    private Zonificacion zonificacion;


    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DocumentoLegal> documentosLegales = new ArrayList<>();

    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Servicio> servicios = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_expediente")
    @JsonManagedReference
    private Expediente expediente;

    //relacion con otros agregados
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "norma_id")
    private List<PropiedadInmobiliariaNorma> propiedadNormas;

    @Transient
    private List<Norma>normas;

    public PropiedadInmobiliaria() {

        propiedadNormas = new ArrayList<>();
    }

    //
    public void addPropiedadNorma(PropiedadInmobiliariaNorma propiedadNorma){
        propiedadNormas.add(propiedadNorma);
    }
    public void removerPropiedadNorma(PropiedadInmobiliariaNorma propiedadNorma){
        propiedadNormas.remove(propiedadNorma);
    }

    public List<PropiedadInmobiliariaNorma> getPropiedadNormas() {
        return propiedadNormas;
    }

    public void setPropiedadNormas(List<PropiedadInmobiliariaNorma> propiedadNormas) {
        this.propiedadNormas = propiedadNormas;
    }

    public List<Norma> getNormas() {
        return normas;
    }

    public void setNormas(List<Norma> normas) {
        this.normas = normas;
    }

//


    public TipoPropiedad getTipoPropiedad() {
        return tipoPropiedad;
    }

    public void setTipoPropiedad(TipoPropiedad tipoPropiedad) {
        this.tipoPropiedad = tipoPropiedad;
    }

    public EstadoPropiedad getEstado() {
        return estado;
    }

    public void setEstado(EstadoPropiedad estado) {
        this.estado = estado;
    }

    public Precio getPrecio() {
        return precio;
    }

    public void setPrecio(Precio precio) {
        this.precio = precio;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Zonificacion getZonificacion() {
        return zonificacion;
    }

    public void setZonificacion(Zonificacion zonificacion) {
        this.zonificacion = zonificacion;
    }

    public List<DocumentoLegal> getDocumentosLegales() {
        return documentosLegales;
    }

    public void setDocumentosLegales(List<DocumentoLegal> documentosLegales) {
        this.documentosLegales = documentosLegales;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    public Long getIdPropiedad() {
        return idPropiedad;
    }

    public void setIdPropiedad(Long idPropiedad) {
        this.idPropiedad = idPropiedad;
    }




    // metodosssssssssssssssss

    public void agregarDocumentoLegal(DocumentoLegal doc) {
        doc.setPropiedad(this);
        documentosLegales.add(doc);
    }

    public void removerDocumentoLegal(Long documentoId) {
        DocumentoLegal docAEliminar = null;
        // Buscamos el documento cuyo ID coincide
        for (DocumentoLegal doc : documentosLegales) {
            if (doc.getIdDocumentoLegal().equals(documentoId)) {
                docAEliminar = doc;
                break;
            }
        }

        if (docAEliminar != null) {
            // Rompemos la relación inversa y lo quitamos de la lista
            docAEliminar.setPropiedad(null);
            documentosLegales.remove(docAEliminar);
        }


    }

    public void editarDocumentoLegal(DocumentoLegal docEditado) {


        for (int i = 0; i < documentosLegales.size(); i++) {
            DocumentoLegal actual = documentosLegales.get(i);
            if (actual.getIdDocumentoLegal() != null && actual.getIdDocumentoLegal().equals(docEditado.getIdDocumentoLegal())) {
                docEditado.setPropiedad(this); // volver a asociar con esta propiedad
                documentosLegales.set(i, docEditado); // reemplazar el documento
                return;
            }
        }


    }


    public void agregarServicio(Servicio servicio) {
        servicio.setPropiedad(this);
        servicios.add(servicio);
    }

    public void removerServicio(Long servicioId) {
        Servicio encontrado = null;
        for (Servicio s : servicios) {
            if (s.getIdServicio().equals(servicioId)) {
                encontrado = s;
                break;
            }
        }
        if (encontrado != null) {
            encontrado.setPropiedad(null);
            servicios.remove(encontrado);
        }
    }

    public void editarServicio(Servicio servicioEditado) {
        for (int i = 0; i < servicios.size(); i++) {
            Servicio actual = servicios.get(i);
            if (actual.getIdServicio() != null && actual.getIdServicio().equals(servicioEditado.getIdServicio())) {
                servicioEditado.setPropiedad(this); // asociar a esta propiedad
                servicios.set(i, servicioEditado);
                return;
            }
        }
    }


    public void asignarExpediente(Expediente expediente) {

        this.expediente = expediente;
        expediente.setPropiedad(this);

    }

    public void desvincularExpediente() {
        if (this.expediente != null) {
            this.expediente.setPropiedad(null);
            this.expediente = null;
        }
    }

    public void editarExpediente(Expediente expedienteEditado) {
        if (this.expediente != null && expedienteEditado.getIdExpediente() != null &&
                this.expediente.getIdExpediente().equals(expedienteEditado.getIdExpediente())) {

            expedienteEditado.setPropiedad(this);
            this.expediente = expedienteEditado;
        }
    }
}