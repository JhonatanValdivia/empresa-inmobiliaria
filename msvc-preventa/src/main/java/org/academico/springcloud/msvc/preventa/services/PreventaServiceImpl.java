package org.academico.springcloud.msvc.preventa.services;

import org.academico.springcloud.msvc.preventa.clients.PropiedadClientRest;
import org.academico.springcloud.msvc.preventa.clients.UsuarioClientsRest;
import org.academico.springcloud.msvc.preventa.models.PropiedadInmobiliaria;
import org.academico.springcloud.msvc.preventa.models.Usuario;
import org.academico.springcloud.msvc.preventa.models.entity.ContratoVenta;
import org.academico.springcloud.msvc.preventa.models.entity.Preventa;
import org.academico.springcloud.msvc.preventa.models.entity.PropuestaPago;
import org.academico.springcloud.msvc.preventa.models.entity.VisitaProgramada;
import org.academico.springcloud.msvc.preventa.models.enums.EstadoVisita;
import org.academico.springcloud.msvc.preventa.repositories.PreventaRepository;
import org.academico.springcloud.msvc.preventa.models.enums.enumsPropiedades.EstadoPropiedad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PreventaServiceImpl implements PreventaService {

    @Autowired
    private PreventaRepository repository;

    @Autowired
    private UsuarioClientsRest usuarioClientRest;

    @Autowired
    private PropiedadClientRest propiedadClientRest;

    @Override
    @Transactional(readOnly = true)
    public List<Preventa> listar() {
        return (List<Preventa>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Preventa> porId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Preventa guardar(Preventa preventa, Long idPropiedad) {
        // Verificar si la propiedad existe y está disponible
        PropiedadInmobiliaria propiedad = propiedadClientRest.detallePropiedad(idPropiedad);
        if (propiedad == null) {
            throw new IllegalArgumentException("Propiedad no encontrada con ID: " + idPropiedad);
        }
        if (propiedad.getEstado() == null || !propiedad.getEstado().equals(EstadoPropiedad.DISPONIBLE)) {
            throw new IllegalArgumentException("La propiedad con ID " + idPropiedad + " no está disponible");
        }

        // Asignar el idPropiedad a la preventa
        preventa.setPropiedadId(idPropiedad);

        // Guardar la preventa
        return repository.save(preventa);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    // Implementación de métodos de negocio del agregado Preventa


    @Override
    @Transactional
    public Optional<Preventa> aprobarPreventa(Long preventaId) {
        return repository.findById(preventaId).map(preventa -> {
            preventa.aprobarPreventa(); // La lógica está en la entidad
            return repository.save(preventa);
        });
    }

    // Implementación de CRUD Anidado para ContratoVenta


    @Override
    @Transactional
    public Optional<ContratoVenta> agregarContratoVenta(Long preventaId, ContratoVenta contrato) {
        return repository.findById(preventaId).map(preventa -> {
            if (preventa.getContratoVenta() != null) {
                throw new IllegalStateException("La Preventa " + preventaId + " ya tiene un contrato asociado.");
            }
            contrato.setId(null); // Asegura que sea una inserción
            contrato.setPreventa(preventa);
            preventa.setContratoVenta(contrato);
            repository.save(preventa);
            return preventa.getContratoVenta();
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContratoVenta> obtenerContratoPorPreventa(Long preventaId) {
        return repository.findById(preventaId).map(Preventa::getContratoVenta);
    }

    @Override
    @Transactional
    public Optional<ContratoVenta> actualizarContratoVenta(Long preventaId, ContratoVenta contratoActualizado) {
        return repository.findById(preventaId).map(preventa -> {
            ContratoVenta contratoDB = preventa.getContratoVenta();
            if (contratoDB == null) {
                throw new IllegalStateException("La Preventa " + preventaId + " no tiene un contrato para actualizar.");
            }
            contratoDB.setTipoContrato(contratoActualizado.getTipoContrato());
            contratoDB.setFechaFirma(contratoActualizado.getFechaFirma());
            contratoDB.setEstado(contratoActualizado.getEstado());
            repository.save(preventa);
            return contratoDB;
        });
    }

    @Override
    @Transactional
    public Optional<Preventa> eliminarContratoVenta(Long preventaId) {
        return repository.findById(preventaId).map(preventa -> {
            if (preventa.getContratoVenta() == null) {
                throw new IllegalStateException("La Preventa " + preventaId + " no tiene un contrato para eliminar.");
            }
            // orphanRemoval=true se encargará de borrar el registro del contrato
            preventa.setContratoVenta(null);
            return repository.save(preventa);
        });
    }

    @Override
    @Transactional
    public Optional<ContratoVenta> anularContratoPreventa(Long preventaId) {
        return repository.findById(preventaId).map(preventa -> {
            ContratoVenta contrato = preventa.getContratoVenta();
            if (contrato == null) {
                throw new IllegalStateException("No hay contrato para anular.");
            }
            contrato.anularContrato();
            repository.save(preventa);
            return contrato;
        });
    }

    // Implementación de CRUD Anidado para PropuestaPago
    @Override
    @Transactional(readOnly = true)
    public Optional<List<PropuestaPago>> listarPropuestasPagoPorPreventa(Long preventaId) {
        return repository.findById(preventaId).map(Preventa::getPropuestasPago);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PropuestaPago> porIdPropuestaPago(Long preventaId, Long propuestaId) {
        return repository.findById(preventaId)
                .flatMap(preventa -> preventa.getPropuestasPago().stream()
                        .filter(p -> p.getId().equals(propuestaId))
                        .findFirst());
    }

    @Override
    @Transactional
    public Optional<Preventa> agregarPropuestaPago(Long preventaId, PropuestaPago propuesta) {
        Optional<Preventa> opPreventa = repository.findById(preventaId);
        if (opPreventa.isPresent()) {
            Preventa preventa = opPreventa.get();
            propuesta.setId(null);
            preventa.addPropuestaPago(propuesta);
            return Optional.of(repository.save(preventa));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<PropuestaPago> actualizarPropuestaPago(Long preventaId, Long propuestaId, PropuestaPago propuestaActualizada) {
        Optional<Preventa> opPreventa = repository.findById(preventaId);
        if (opPreventa.isPresent()) {
            Preventa preventa = opPreventa.get();
            Optional<PropuestaPago> opPropuestaDB = preventa.findPropuestaPagoById(propuestaId);
            if (opPropuestaDB.isPresent()) {
                PropuestaPago propuestaDB = opPropuestaDB.get();
                propuestaDB.setMonto(propuestaActualizada.getMonto());
                propuestaDB.setFecha(propuestaActualizada.getFecha());
                propuestaDB.setCuotas(propuestaActualizada.getCuotas());
                propuestaDB.setMetodoPago(propuestaActualizada.getMetodoPago());
                repository.save(preventa);
                return Optional.of(propuestaDB);
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<PropuestaPago> aceptarPropuestaPagoPreventa(Long preventaId, Long propuestaId) {
        Optional<Preventa> opPreventa = repository.findById(preventaId);
        if (opPreventa.isPresent()) {
            Preventa preventa = opPreventa.get();
            Optional<PropuestaPago> opPropuesta = preventa.findPropuestaPagoById(propuestaId);
            if (opPropuesta.isPresent()) {
                PropuestaPago propuesta = opPropuesta.get();
                propuesta.aceptarPropuesta();
                repository.save(preventa);
                return Optional.of(propuesta);
            }
        }
        return Optional.empty();
    }


    // Implementación de CRUD Anidado para VisitaProgramada

    @Override
    @Transactional
    public Optional<Preventa> agregarVisitaProgramada(Long preventaId, VisitaProgramada visita) {
        Optional<Preventa> opPreventa = repository.findById(preventaId);
        if (opPreventa.isPresent()) {
            Preventa preventa = opPreventa.get();
            visita.setId(null);
            preventa.addVisitaProgramada(visita);
            return Optional.of(repository.save(preventa));
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<List<VisitaProgramada>> listarVisitasProgramadasPorPreventa(Long preventaId) {
        return repository.findById(preventaId).map(Preventa::getVisitasProgramadas);
    }

    @Override
    @Transactional
    public Optional<VisitaProgramada> actualizarVisitaProgramada(Long preventaId, Long visitaId, VisitaProgramada visitaActualizada) {
        Optional<Preventa> opPreventa = repository.findById(preventaId);
        if (opPreventa.isPresent()) {
            Preventa preventa = opPreventa.get();
            Optional<VisitaProgramada> opVisitaDB = preventa.findVisitaProgramadaById(visitaId);
            if (opVisitaDB.isPresent()) {
                VisitaProgramada visitaDB = opVisitaDB.get();
                visitaDB.setFecha(visitaActualizada.getFecha());
                visitaDB.setEstadoVisita(visitaActualizada.getEstadoVisita());
                repository.save(preventa);
                return Optional.of(visitaDB);
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<VisitaProgramada> reprogramarVisitaPreventa(Long preventaId, Long visitaId, LocalDate fecha) {
        Optional<Preventa> opPreventa = repository.findById(preventaId);
        if (opPreventa.isPresent()) {
            Preventa preventa = opPreventa.get();
            Optional<VisitaProgramada> opVisita = preventa.findVisitaProgramadaById(visitaId);
            if (opVisita.isPresent()) {
                VisitaProgramada visita = opVisita.get();
                try {
                    visita.reprogramarVisita(fecha);
                    repository.save(preventa);
                    return Optional.of(visita);
                } catch (IllegalStateException e) {
                    throw e;
                }
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<VisitaProgramada> actualizarEstadoVisitaPreventa(Long preventaId, Long visitaId, EstadoVisita estadoVisita) {
        Optional<Preventa> opPreventa = repository.findById(preventaId);
        if (opPreventa.isPresent()) {
            Preventa preventa = opPreventa.get();
            Optional<VisitaProgramada> opVisita = preventa.findVisitaProgramadaById(visitaId);
            if (opVisita.isPresent()) {
                VisitaProgramada visita = opVisita.get();
                visita.actualizarEstado(estadoVisita);
                repository.save(preventa);
                return Optional.of(visita);
            }
        }
        return Optional.empty();
    }

    //metodo relación Preventa con Usuarios
    @Override
    @Transactional
    public Preventa asociarUsuariosPreventa(Long idPreventa, Long idAgente, Long idCliente) {
        Usuario agente = usuarioClientRest.detalleUsuario(idAgente);
        if (agente.getTipoUsuario()!= Usuario.TipoUsuario.AGENTE) {
            throw new IllegalArgumentException("El usuario no es un agente válido");
        }

        Usuario cliente = usuarioClientRest.detalleUsuario(idCliente);
        if (cliente.getTipoUsuario()!= Usuario.TipoUsuario.CLIENTE) {
            throw new IllegalArgumentException("El usuario no es un cliente válido");
        }

        Preventa preventa = repository.findById(idPreventa)
                .orElseThrow(() -> new IllegalArgumentException("Preventa no encontrada"));

        preventa.setUsuarioAgenteId(idAgente);
        preventa.setUsuarioClienteId(idCliente);
        return repository.save(preventa);
    }

    @Override
    @Transactional
    public Preventa asociarPropiedadPreventa(Long idPreventa, Long idPropiedad) {
        // Verificar si la propiedad existe
        PropiedadInmobiliaria propiedad = propiedadClientRest.detallePropiedad(idPropiedad);
        if (propiedad == null) {
            throw new IllegalArgumentException("Propiedad no encontrada con ID: " + idPropiedad);
        }

        // Obtener la preventa
        Preventa preventa = repository.findById(idPreventa)
                .orElseThrow(() -> new IllegalArgumentException("Preventa no encontrada con ID: " + idPreventa));

        // Asociar la propiedad (solo el ID por ahora)
        preventa.setPropiedadId(idPropiedad);
        return repository.save(preventa);
    }
}