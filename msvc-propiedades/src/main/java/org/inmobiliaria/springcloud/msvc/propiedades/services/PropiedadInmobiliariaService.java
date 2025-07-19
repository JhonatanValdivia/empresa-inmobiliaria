package org.inmobiliaria.springcloud.msvc.propiedades.services;

import org.inmobiliaria.springcloud.msvc.propiedades.models.Norma;
import org.inmobiliaria.springcloud.msvc.propiedades.models.entitys.*;

import java.util.List;
import java.util.Optional;

public interface PropiedadInmobiliariaService {
    // Propiedad CRUD
    List<PropiedadInmobiliaria> buscarTodas();
    Optional<PropiedadInmobiliaria> buscarPorId(Long id);
    PropiedadInmobiliaria guardar(PropiedadInmobiliaria propiedad);
    void eliminar(Long id);

    // Documentos legales (1:N)
    Optional<PropiedadInmobiliaria> agregarDocumento(Long propiedadId, DocumentoLegal documento);
    Optional<PropiedadInmobiliaria> removerDocumento(Long propiedadId, Long documentoId);
    Optional<PropiedadInmobiliaria> editarDocumento(Long propiedadId, DocumentoLegal documento);


    // Servicios (1:N)
    Optional<PropiedadInmobiliaria> agregarServicio(Long propiedadId, Servicio servicio);
    Optional<PropiedadInmobiliaria> removerServicio(Long propiedadId, Long servicioId);
    Optional<PropiedadInmobiliaria> editarServicio(Long propiedadId, Servicio servicio);
    // Expediente (1:1)
    Optional<PropiedadInmobiliaria> asignarExpediente(Long propiedadId, Expediente expediente);
    Optional<PropiedadInmobiliaria> quitarExpediente(Long propiedadId);
    Optional<PropiedadInmobiliaria> editarExpediente(Long propiedadId, Expediente expediente);
    // Planos via expediente
    Optional<PropiedadInmobiliaria> agregarPlano(Long propiedadId, Plano plano);
    Optional<PropiedadInmobiliaria> removerPlano(Long propiedadId, Long planoId);
    Optional<PropiedadInmobiliaria> editarPlano(Long propiedadId, Plano plano);




    //relacion con el agregado nomrma
    Optional<Norma> asignarNorma(Norma norma, Long propiedadId);
    Optional<Norma> eliminarNorma(Norma norma, Long propiedadId);


    List<PropiedadInmobiliaria> listarNormas();

}
