package org.inmobiliaria.springcloud.msvc.propiedades.services;

import org.inmobiliaria.springcloud.msvc.propiedades.models.entitys.*;
import org.inmobiliaria.springcloud.msvc.propiedades.repositories.PropiedadInmobiliariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class PropiedadInmobiliariaServiceImpl implements PropiedadInmobiliariaService {

    @Autowired
    private  PropiedadInmobiliariaRepository repo;
    @Override
    @Transactional(readOnly = true)
    public List<PropiedadInmobiliaria> buscarTodas() {
        return (List<PropiedadInmobiliaria>) repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PropiedadInmobiliaria> buscarPorId(Long id) {
        return repo.findById(id);
    }

    @Override
    @Transactional
    public PropiedadInmobiliaria guardar(PropiedadInmobiliaria propiedad) {
        return repo.save(propiedad);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<PropiedadInmobiliaria> agregarDocumento(Long propiedadId, DocumentoLegal documento) {
        return repo.findById(propiedadId)
                .map(p -> {
                    p.agregarDocumentoLegal(documento);
                    return repo.save(p);
                });
    }

    @Override
    @Transactional
    public Optional<PropiedadInmobiliaria> removerDocumento(Long propiedadId, Long documentoId) {
        return repo.findById(propiedadId)
                .map(p -> {
                    p.removerDocumentoLegal(documentoId);
                    return repo.save(p);
                });
    }

    @Override
    @Transactional
    public Optional<PropiedadInmobiliaria> agregarServicio(Long propiedadId, Servicio servicio) {
        return repo.findById(propiedadId)
                .map(p -> {
                    p.agregarServicio(servicio);
                    return repo.save(p);
                });
    }

    @Override
    @Transactional
    public Optional<PropiedadInmobiliaria> removerServicio(Long propiedadId, Long servicioId) {
        return repo.findById(propiedadId)
                .map(p -> {
                    p.removerServicio(servicioId);
                    return repo.save(p);
                });
    }

    @Override
    @Transactional
    public Optional<PropiedadInmobiliaria> asignarExpediente(Long propiedadId, Expediente expediente) {
        return repo.findById(propiedadId)
                .map(p -> {
                    p.asignarExpediente(expediente);
                    return repo.save(p);
                });
    }

    @Override
    @Transactional
    public Optional<PropiedadInmobiliaria> quitarExpediente(Long propiedadId) {
        return repo.findById(propiedadId)
                .map(p -> {
                    p.desvincularExpediente();
                    return repo.save(p);
                });
    }

    @Override
    @Transactional
    public Optional<PropiedadInmobiliaria> agregarPlano(Long propiedadId, Plano plano) {
        return repo.findById(propiedadId)
                .map(p -> {
                    if (p.getExpediente() != null) {
                        p.getExpediente().agregarPlano(plano);
                        return repo.save(p);
                    }
                    return p;
                });
    }

    @Override
    @Transactional
    public Optional<PropiedadInmobiliaria> removerPlano(Long propiedadId, Long planoId) {
        return repo.findById(propiedadId)
                .map(p -> {
                    if (p.getExpediente() != null) {
                        p.getExpediente().removerPlano(planoId);
                        return repo.save(p);
                    }
                    return p;
                });
    }
}
