package org.academico.springcloud.msvc.campania.services;

import org.academico.springcloud.msvc.campania.models.entities.Campania;
import org.academico.springcloud.msvc.campania.models.entities.ProveedorPublicidad;
import org.academico.springcloud.msvc.campania.repositories.CampaniaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CampaniaServiceImpl implements CampaniaService {
    @Autowired
    private CampaniaRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Campania> listar() {
        return (List<Campania>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Campania> porId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Campania guardar(Campania campania) {
        return repository.save(campania);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void eliminarProveedor(Long idCampania, Long idProveedor) {
        Optional<Campania> campaniaOpt = porId(idCampania);
        if (campaniaOpt.isPresent()) {
            Campania campania = campaniaOpt.get();
            ProveedorPublicidad proveedorAEliminar = campania.getProveedores().stream()
                    .filter(p -> p.getIdProveedor().equals(idProveedor))
                    .findFirst()
                    .orElse(null);
            if (proveedorAEliminar != null) {
                proveedorAEliminar.eliminarProveedor(); // Marca la intención
                campania.getProveedores().remove(proveedorAEliminar);
                repository.save(campania); // Esto desencadenará la eliminación física debido a cascade
            } else {
                throw new IllegalArgumentException("Proveedor no encontrado en la campaña");
            }
        } else {
            throw new IllegalArgumentException("Campaña no encontrada");
        }
    }
}
