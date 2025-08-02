package org.inmobiliaria.springcloud.msvc.propiedades.services;

import org.inmobiliaria.springcloud.msvc.propiedades.clients.InmobiliariaClientRest;
import org.inmobiliaria.springcloud.msvc.propiedades.models.Norma;
import org.inmobiliaria.springcloud.msvc.propiedades.models.entitys.*;
import org.inmobiliaria.springcloud.msvc.propiedades.repositories.PropiedadInmobiliariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public Optional<PropiedadInmobiliaria> editarDocumento(Long propiedadId, DocumentoLegal documento) {
        return repo.findById(propiedadId).map(p -> {
            p.editarDocumentoLegal(documento);
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
    public Optional<PropiedadInmobiliaria> editarServicio(Long propiedadId, Servicio servicio) {
        return repo.findById(propiedadId).map(p -> {
            p.editarServicio(servicio);
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
    public Optional<PropiedadInmobiliaria> editarExpediente(Long propiedadId, Expediente expediente) {
        return repo.findById(propiedadId).map(p -> {
            p.editarExpediente(expediente);
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

    @Override
    @Transactional
    public Optional<PropiedadInmobiliaria> editarPlano(Long propiedadId, Plano planoEditado) {
        return repo.findById(propiedadId)
                .map(p -> {
                    Expediente expediente = p.getExpediente();
                    if (expediente != null) {
                        expediente.editarPlano(planoEditado);
                        return repo.save(p);
                    } else {
                        throw new IllegalStateException("La propiedad no tiene un expediente asignado.");
                    }
                });
    }


    @Autowired
    private InmobiliariaClientRest inmobiliariaClientRest;

    @Override
    public Optional<Norma> asignarNorma(Norma norma, Long propiedadId) {
        Optional<PropiedadInmobiliaria> op = repo.findById(propiedadId);

        if(op.isPresent()){
            Norma normaMsvc = inmobiliariaClientRest.detalle(norma.getIdNorma());
            PropiedadInmobiliaria propiedadInmobiliaria = op.get();
            PropiedadInmobiliariaNorma propiedadInmobiliariaNorma = new PropiedadInmobiliariaNorma();
            propiedadInmobiliariaNorma.setNormaId(normaMsvc.getIdNorma());

            propiedadInmobiliaria.addPropiedadNorma(propiedadInmobiliariaNorma);
            repo.save(propiedadInmobiliaria);
            return Optional.of(normaMsvc);

        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Norma> eliminarNorma(Norma norma, Long propiedadId) {
        Optional<PropiedadInmobiliaria> op = repo.findById(propiedadId);

        if(op.isPresent()){
            Norma normaMsvc = inmobiliariaClientRest.detalle(norma.getIdNorma());
            PropiedadInmobiliaria propiedadInmobiliaria = op.get();
            PropiedadInmobiliariaNorma propiedadInmobiliariaNorma = new PropiedadInmobiliariaNorma();
            propiedadInmobiliariaNorma.setNormaId(normaMsvc.getIdNorma());

            propiedadInmobiliaria.removerPropiedadNorma(propiedadInmobiliariaNorma);
            repo.save(propiedadInmobiliaria);
            return Optional.of(normaMsvc);

        }

        return Optional.empty();
    }



    @Override
    @Transactional
    public List<PropiedadInmobiliaria> listarNormas() {
        List<PropiedadInmobiliaria> propiedadInmobiliarias  =(List<PropiedadInmobiliaria>) repo.findAll();
        for(PropiedadInmobiliaria propiedad: propiedadInmobiliarias){
            List<Norma> normas = new ArrayList<>();
            for(PropiedadInmobiliariaNorma propiedadNormas: propiedad.getPropiedadNormas()){
                Norma norma =  inmobiliariaClientRest.detalle(propiedadNormas.getNormaId());
                normas.add(norma);
            }
            propiedad.setNormas(normas);
        }

        return propiedadInmobiliarias;
    }
}
