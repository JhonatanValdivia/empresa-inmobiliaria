package org.academico.springcloud.msvc.venta.services;

//import org.academico.springcloud.msvc.venta.clients.ComisionClientRest;
import feign.FeignException;
import org.academico.springcloud.msvc.venta.clients.PreventaClientRest;
import org.academico.springcloud.msvc.venta.models.Preventa;
import org.academico.springcloud.msvc.venta.models.entities.DetalleVenta;
import org.academico.springcloud.msvc.venta.models.entities.Venta;
import org.academico.springcloud.msvc.venta.repositories.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VentaServiceImpl implements VentaService{



    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private PreventaClientRest preventaClient;

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listar() {
        List<Venta> ventas = (List<Venta>) ventaRepository.findAll();
        // Usamos un stream para aplicar la lógica de enriquecimiento a cada elemento de la lista.
        return ventas.stream()
                .map(this::enriquecerConPreventa)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> todosPorId(List<Long> ids) {
        return (List<Venta>) ventaRepository.findAllById(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> porId(Long id) {
        // Usamos map para aplicar la lógica de enriquecimiento solo si la venta existe.
        return ventaRepository.findById(id).map(this::enriquecerConPreventa);
    }

    @Override
    @Transactional
    public Venta guardar(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        ventaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public boolean existePorId(Long id) {
        return ventaRepository.existsById(id);
    }

    @Override
    @Transactional
    public long contarVentas() {
        return ventaRepository.count();
    }

    @Override
    @Transactional
    public void eliminarVenta(Venta venta) {
        ventaRepository.delete(venta);
    }


    // Métodos para manejar la relación entre Venta y DetalleVenta
    @Override
    @Transactional
    public void agregarDetalle(Long ventaId, DetalleVenta detalleVenta) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        DetalleVenta nuevoDetalle = new DetalleVenta();
        nuevoDetalle.setCronogramaPago(detalleVenta.getCronogramaPago());
        nuevoDetalle.setMetodoPago(detalleVenta.getMetodoPago());
        nuevoDetalle.setEstadoDetalle(detalleVenta.getEstadoDetalle());
        nuevoDetalle.setVenta(venta);

        venta.agregarDetalleVenta(nuevoDetalle);

        ventaRepository.save(venta);
    }

    @Override
    @Transactional
    public void eliminarDetalle(Long ventaId, Long detalleId) {
        Venta venta=ventaRepository.findById(ventaId).orElseThrow(()-> new RuntimeException("Venta no encontrada"));
        DetalleVenta detalleAEliminar = null;
        for (DetalleVenta d : venta.getDetalleVentaLista()) {
            if (d.getId().equals(detalleId)) {
                detalleAEliminar = d;
                break;
            }
        }

        if (detalleAEliminar == null) {
            throw new RuntimeException("DetalleVenta no encontrado");
        }

        venta.eliminarDetalleVenta(detalleAEliminar);
        ventaRepository.save(venta);
    }

    // Métodos para manejar la relación con Preventa
    @Override
    @Transactional
    public Optional<Venta> asignarPreventa(Long ventaId, Long preventaId) {
        Optional<Venta> ventaOpt = ventaRepository.findById(ventaId);
        if (ventaOpt.isPresent()) {
            Venta venta = ventaOpt.get();
            if (venta.getPreventaId() != null) {
                throw new IllegalStateException("La Venta " + ventaId + " ya tiene una preventa asignada.");
            }

            try {

                Preventa preventaPojo = preventaClient.getPreventaById(preventaId);

                  if (!"APROBADA".equals(preventaPojo.getEstado())) {
                    throw new IllegalStateException(
                            "No se puede asignar la Preventa con ID " + preventaId +
                                    ". Su estado es '" + preventaPojo.getEstado() + "', pero se requiere el estado 'APROBADA'."
                    );
                }

                System.out.println("Preventa encontrada y aprobada: " + preventaPojo.getId());

            } catch (FeignException.NotFound e) {
                throw new IllegalArgumentException("La Preventa con id " + preventaId + " no existe.", e);
            } catch (FeignException e) {
                throw new RuntimeException("Error de comunicación con el servicio de preventas: " + e.getMessage(), e);
            }

             venta.setPreventaId(preventaId);
            return Optional.of(ventaRepository.save(venta));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Venta> desasignarPreventa(Long ventaId) {
        Optional<Venta> ventaOpt = ventaRepository.findById(ventaId);
        if (ventaOpt.isPresent()) {
            Venta venta = ventaOpt.get();
            if (venta.getPreventaId() == null) {
                throw new IllegalStateException("La Venta " + ventaId + " no tiene ninguna preventa asignada.");
            }
            venta.setPreventaId(null);
            return Optional.of(ventaRepository.save(venta));
        }
        return Optional.empty();
    }

    private Venta enriquecerConPreventa(Venta venta) {
        if (venta.getPreventaId() != null) {
            try {
                Preventa preventaPojo = preventaClient.getPreventaById(venta.getPreventaId());
                venta.setDetallePreventa(preventaPojo);
            } catch (FeignException e) {
                System.err.println("ADVERTENCIA: No se pudieron obtener detalles de la Preventa " + venta.getPreventaId() + ". La Venta se devolverá sin esta información. Error: " + e.getMessage());
            }
        }
        return venta;
    }
}

