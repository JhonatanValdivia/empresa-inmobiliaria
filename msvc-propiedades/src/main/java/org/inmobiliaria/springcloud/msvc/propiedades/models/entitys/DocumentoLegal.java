    package org.inmobiliaria.springcloud.msvc.propiedades.models.entitys;

    import com.fasterxml.jackson.annotation.JsonBackReference;
    import jakarta.persistence.*;
    import org.inmobiliaria.springcloud.msvc.propiedades.models.enums.EstadoDocumentoLegal;
    import org.inmobiliaria.springcloud.msvc.propiedades.models.enums.TipoDocumentoLegal;

    @Entity
    @Table(name = "documentos_legales")
    public class DocumentoLegal {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_documento_legal")
        private Long idDocumentoLegal;


        @Enumerated(EnumType.STRING)
        @Column(name = "tipo", nullable = false)
        private TipoDocumentoLegal tipoDocumento;

        @Column(name = "nombre_notaria", nullable = false)
        private String nombreNotaria;

        @Column(name = "numero_inscripcion_sunarp", nullable = false)
        private String numeroInscripcionSunarp;


        @Enumerated(EnumType.STRING)
        @Column(name = "estado", nullable = false)
        private EstadoDocumentoLegal estadoDocumento;

        @ManyToOne
        @JoinColumn(name = "id_propiedad", nullable = false)
        @JsonBackReference
        private PropiedadInmobiliaria propiedad;

        protected DocumentoLegal() {
        }


        public TipoDocumentoLegal getTipoDocumento() {
            return tipoDocumento;
        }

        public void setTipoDocumento(TipoDocumentoLegal tipoDocumento) {
            this.tipoDocumento = tipoDocumento;
        }

        public EstadoDocumentoLegal getEstadoDocumento() {
            return estadoDocumento;
        }

        public void setEstadoDocumento(EstadoDocumentoLegal estadoDocumento) {
            this.estadoDocumento = estadoDocumento;
        }

        public String getNombreNotaria() {
            return nombreNotaria;
        }

        public void setNombreNotaria(String nombreNotaria) {
            this.nombreNotaria = nombreNotaria;
        }

        public String getNumeroInscripcionSunarp() {
            return numeroInscripcionSunarp;
        }

        public void setNumeroInscripcionSunarp(String numeroInscripcionSunarp) {
            this.numeroInscripcionSunarp = numeroInscripcionSunarp;
        }


        public PropiedadInmobiliaria getPropiedad() {
            return propiedad;
        }

        public void setPropiedad(PropiedadInmobiliaria propiedad) {
            this.propiedad = propiedad;
        }

        public Long getIdDocumentoLegal() {
            return idDocumentoLegal;
        }

        public void setIdDocumentoLegal(Long idDocumentoLegal) {
            this.idDocumentoLegal = idDocumentoLegal;
        }
    }
