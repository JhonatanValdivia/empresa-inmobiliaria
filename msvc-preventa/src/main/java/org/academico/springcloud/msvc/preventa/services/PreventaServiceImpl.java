package org.academico.springcloud.msvc.preventa.services;

import org.academico.springcloud.msvc.preventa.models.entity.ContratoVenta;
import org.academico.springcloud.msvc.preventa.models.entity.Preventa;
import org.academico.springcloud.msvc.preventa.models.entity.PropuestaPago;
import org.academico.springcloud.msvc.preventa.models.entity.VisitaProgramada;
import org.academico.springcloud.msvc.preventa.models.enums.EstadoVisita;
import org.academico.springcloud.msvc.preventa.models.enums.MetodoPago;
import org.academico.springcloud.msvc.preventa.models.enums.TipoContrato;
import org.academico.springcloud.msvc.preventa.repositories.PreventaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PreventaServiceImpl implements PreventaService {

    @Autowired
    private PreventaRepository repository;

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
    public Preventa guardar(Preventa preventa) {
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
    public Optional<Preventa> programarVisitaEnPreventa(Long preventaId, LocalDate fechaVisita) {
        Optional<Preventa> opPreventa = repository.findById(preventaId);
        if (opPreventa.isPresent()) {
            Preventa preventa = opPreventa.get();
            preventa.programarVisita(fechaVisita);
            return Optional.of(repository.save(preventa));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Preventa> registrarContratoEnPreventa(Long preventaId, TipoContrato tipoContrato, LocalDate fechaFirma) {
        Optional<Preventa> opPreventa = repository.findById(preventaId);
        if (opPreventa.isPresent()) {
            Preventa preventa = opPreventa.get();
            preventa.registrarContrato(tipoContrato, fechaFirma);
            return Optional.of(repository.save(preventa));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Preventa> registrarPagoEnPreventa(Long preventaId, BigDecimal monto, LocalDate fecha, MetodoPago metodo) {
        Optional<Preventa> opPreventa = repository.findById(preventaId);
        if (opPreventa.isPresent()) {
            Preventa preventa = opPreventa.get();
            preventa.registrarPago(monto, fecha, metodo);
            return Optional.of(repository.save(preventa));
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BigDecimal> calcularComisionPreventa(Long preventaId) {
        Optional<Preventa> opPreventa = repository.findById(preventaId);
        return opPreventa.map(Preventa::calcularComision);
    }

    @Override
    @Transactional
    public Optional<Preventa> marcarPreventaComoFinalizada(Long preventaId) {
        Optional<Preventa> opPreventa = repository.findById(preventaId);
        if (opPreventa.isPresent()) {
            Preventa preventa = opPreventa.get();
            try {
                preventa.marcarComoFinalizado();
                return Optional.of(repository.save(preventa));
            } catch (IllegalStateException e) {
                // Manejar o relanzar la excepción para que el controlador la procese
                throw e;
            }
        }
        return Optional.empty();
    }

    // Implementación de CRUD Anidado para ContratoVenta
    @Override
    @Transactional(readOnly = true)
    public Optional<List<ContratoVenta>> listarContratosPorPreventa(Long preventaId) {
        return repository.findById(preventaId).map(Preventa::getContratosVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContratoVenta> porIdContratoVenta(Long preventaId, Long contratoId) {
        return repository.findById(preventaId)
                .flatMap(preventa -> preventa.getContratosVenta().stream()
                        .filter(c -> c.getId().equals(contratoId))
                        .findFirst());
    }

    @Override
    @Transactional
    public Optional<Preventa> agregarContratoVenta(Long preventaId, ContratoVenta contrato) {
        Optional<Preventa> opPreventa = repository.findById(preventaId);
        if (opPreventa.isPresent()) {
            Preventa preventa = opPreventa.get();
            // Asegurarse de que el contrato no tenga ya un ID para que JPA lo inserte
            contrato.setId(null);
            preventa.addContratoVenta(contrato);
            return Optional.of(repository.save(preventa));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<ContratoVenta> actualizarContratoVenta(Long preventaId, Long contratoId, ContratoVenta contratoActualizado) {
        Optional<Preventa> opPreventa = repository.findById(preventaId);
        if (opPreventa.isPresent()) {
            Preventa preventa = opPreventa.get();
            Optional<ContratoVenta> opContratoDB = preventa.findContratoById(contratoId);
            if (opContratoDB.isPresent()) {
                ContratoVenta contratoDB = opContratoDB.get();
                contratoDB.setTipoContrato(contratoActualizado.getTipoContrato());
                contratoDB.setFechaFirma(contratoActualizado.getFechaFirma());
                contratoDB.setEstado(contratoActualizado.getEstado());
                repository.save(preventa); // Persiste el agregado raíz para guardar cambios en las entidades hijas
                return Optional.of(contratoDB);
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Preventa> eliminarContratoVenta(Long preventaId, Long contratoId) {
        Optional<Preventa> opPreventa = repository.findById(preventaId);
        if (opPreventa.isPresent()) {
            Preventa preventa = opPreventa.get();
            Optional<ContratoVenta> opContrato = preventa.findContratoById(contratoId);
            if (opContrato.isPresent()) {
                preventa.removeContratoVenta(opContrato.get());
                return Optional.of(repository.save(preventa));
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<ContratoVenta> anularContratoPreventa(Long preventaId, Long contratoId) {
        Optional<Preventa> opPreventa = repository.findById(preventaId);
        if (opPreventa.isPresent()) {
            Preventa preventa = opPreventa.get();
            Optional<ContratoVenta> opContrato = preventa.findContratoById(contratoId);
            if (opContrato.isPresent()) {
                ContratoVenta contrato = opContrato.get();
                try {
                    contrato.anularContrato();
                    repository.save(preventa);
                    return Optional.of(contrato);
                } catch (IllegalStateException e) {
                    throw e; // Relanzar para manejar en el controlador
                }
            }
        }
        return Optional.empty();
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
    public Optional<Preventa> eliminarPropuestaPago(Long preventaId, Long propuestaId) {
        Optional<Preventa> opPreventa = repository.findById(preventaId);
        if (opPreventa.isPresent()) {
            Preventa preventa = opPreventa.get();
            Optional<PropuestaPago> opPropuesta = preventa.findPropuestaPagoById(propuestaId);
            if (opPropuesta.isPresent()) {
                preventa.removePropuestaPago(opPropuesta.get());
                return Optional.of(repository.save(preventa));
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
    @Transactional(readOnly = true)
    public Optional<List<VisitaProgramada>> listarVisitasProgramadasPorPreventa(Long preventaId) {
        return repository.findById(preventaId).map(Preventa::getVisitasProgramadas);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VisitaProgramada> porIdVisitaProgramada(Long preventaId, Long visitaId) {
        return repository.findById(preventaId)
                .flatMap(preventa -> preventa.getVisitasProgramadas().stream()
                        .filter(v -> v.getId().equals(visitaId))
                        .findFirst());
    }

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
    public Optional<Preventa> eliminarVisitaProgramada(Long preventaId, Long visitaId) {
        Optional<Preventa> opPreventa = repository.findById(preventaId);
        if (opPreventa.isPresent()) {
            Preventa preventa = opPreventa.get();
            Optional<VisitaProgramada> opVisita = preventa.findVisitaProgramadaById(visitaId);
            if (opVisita.isPresent()) {
                preventa.removeVisitaProgramada(opVisita.get());
                return Optional.of(repository.save(preventa));
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<VisitaProgramada> reprogramarVisitaPreventa(Long preventaId, Long visitaId, LocalDate nuevaFecha) {
        Optional<Preventa> opPreventa = repository.findById(preventaId);
        if (opPreventa.isPresent()) {
            Preventa preventa = opPreventa.get();
            Optional<VisitaProgramada> opVisita = preventa.findVisitaProgramadaById(visitaId);
            if (opVisita.isPresent()) {
                VisitaProgramada visita = opVisita.get();
                try {
                    visita.reprogramarVisita(nuevaFecha);
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
    public Optional<VisitaProgramada> actualizarEstadoVisitaPreventa(Long preventaId, Long visitaId, EstadoVisita nuevoEstado) {
        Optional<Preventa> opPreventa = repository.findById(preventaId);
        if (opPreventa.isPresent()) {
            Preventa preventa = opPreventa.get();
            Optional<VisitaProgramada> opVisita = preventa.findVisitaProgramadaById(visitaId);
            if (opVisita.isPresent()) {
                VisitaProgramada visita = opVisita.get();
                visita.actualizarEstado(nuevoEstado);
                repository.save(preventa);
                return Optional.of(visita);
            }
        }
        return Optional.empty();
    }
}