package org.academico.springcloud.msvc.campania.services;

import org.academico.springcloud.msvc.campania.clients.PropiedadClientRest;
import org.academico.springcloud.msvc.campania.models.entities.Campania;

import java.util.List;
import java.util.Optional;

public interface CampaniaService {
    List<Campania> listar();
    Optional<Campania> porId(Long id);
    Campania guardar(Campania campania);
    void eliminar(Long id);
    void eliminarProveedor(Long idCampania, Long idProveedor);
    PropiedadClientRest getPropiedadClient();
}