package org.academico.springcloud.msvc.venta.services;

//import org.academico.springcloud.msvc.venta.clients.ComisionClientRest;
import feign.FeignException;
import org.academico.springcloud.msvc.venta.clients.PreventaClientRest;
import org.academico.springcloud.msvc.venta.models.dtos.PreventaDTO;
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
    private PreventaClientRest preventaClient; // INYECCIÓN: Cliente Feign para msvc-preventa

    // INICIO CAMBIOS: Modificación del método listar()
    @Override
    @Transactional(readOnly = true)
    public List<Venta> listar() {
        List<Venta> ventas = (List<Venta>) ventaRepository.findAll();

        // Iterar sobre cada Venta para cargar los detalles de Preventa
        return ventas.stream().map(venta -> {
            if (venta.getPreventaId() != null) {
                try {
                    // Llamada al microservicio Preventa
                    PreventaDTO preventaDTO = preventaClient.getPreventaById(venta.getPreventaId());
                    venta.setPreventaDetalles(preventaDTO); // Asigna el DTO al campo @Transient
                } catch (FeignException e) {
                    System.err.println("Error al obtener detalles de Preventa " + venta.getPreventaId() + " para Venta " + venta.getId() + ": " + e.getMessage());
                    // El campo preventaDetalles simplemente quedará nulo si hay un error
                }
            }
            return venta;
        }).collect(Collectors.toList());
    }
    // FIN CAMBIOS

    @Override
    @Transactional(readOnly = true)
    public List<Venta> todosPorId(List<Long> ids) {
        return (List<Venta>) ventaRepository.findAllById(ids);
    }

    // Este es el método clave para 'detalleVenta'
    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> porId(Long id) {
        Optional<Venta> opVenta = ventaRepository.findById(id);
        if (opVenta.isPresent()) {
            Venta venta = opVenta.get();
            // INICIO CAMBIOS: Lógica de composición en porId()
            if (venta.getPreventaId() != null) {
                try {
                    PreventaDTO preventaDTO = preventaClient.getPreventaById(venta.getPreventaId());
                    venta.setPreventaDetalles(preventaDTO);
                } catch (FeignException e) {
                    System.err.println("Error al obtener detalles de Preventa " + venta.getPreventaId() + " para Venta " + venta.getId() + ": " + e.getMessage());
                }
            }
            // FIN CAMBIOS
            return Optional.of(venta);
        }
        return Optional.empty();
    }


    @Override
    @Transactional
    public Venta guardar(Venta venta) {
        if (venta.getId() == null) {
            venta.registrarVenta();
        }
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
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        DetalleVenta detalleAEliminar = venta.getDetalleVentaLista().stream()
                .filter(d -> d.getId().equals(detalleId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("DetalleVenta no encontrado"));
        venta.eliminarDetalleVenta(detalleAEliminar);
        ventaRepository.save(venta);
    }
    // INICIO CAMBIOS: Implementación de la relación con Preventa (Rol Activo)

    @Override
    @Transactional
    public Optional<Venta> asignarPreventaAVenta(Long ventaId, Long preventaId) {
        Optional<Venta> opVenta = ventaRepository.findById(ventaId);
        if (opVenta.isPresent()) {
            Venta venta = opVenta.get();

            // 1. Validar si la venta ya tiene una preventa asignada
            if (venta.getPreventaId() != null) {
                if (venta.getPreventaId().equals(preventaId)) {
                    return Optional.of(venta); // Ya está asignada a esta preventa, no hacer nada.
                }
                throw new IllegalStateException("La venta " + ventaId + " ya está asignada a la preventa " + venta.getPreventaId());
            }

            // 2. Validar si la Preventa existe en el microservicio de Preventas
            // NO podemos validar si la Preventa ya está asignada a OTRA Venta,
            // porque Preventa no tiene el campo ventaId para consultarlo.
            // La unicidad 1:1 solo se impone desde el lado de Venta en este escenario.
            try {
                preventaClient.getPreventaById(preventaId); // Solo para verificar que la Preventa exista
            } catch (FeignException.NotFound e) { // La Preventa no existe
                throw new IllegalArgumentException("La Preventa " + preventaId + " no existe.", e);
            } catch (FeignException e) { // Otros errores de comunicación
                throw new RuntimeException("Error al comunicarse con msvc-preventa para verificar Preventa " + preventaId + ": " + e.getMessage(), e);
            }

            // 3. Asignar la preventaId a la venta localmente
            venta.setPreventaId(preventaId);
            Venta ventaGuardada = ventaRepository.save(venta);

            // IMPORTANTE: NO SE NOTIFICA A msvc-preventa para que registre el ventaId,
            // ya que el requisito es NO CAMBIAR msvc-preventa.
            // Esto significa que la relación 1:1 es unidireccional desde Venta.

            return Optional.of(ventaGuardada);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Venta> desasignarPreventaDeVenta(Long ventaId) {
        Optional<Venta> opVenta = ventaRepository.findById(ventaId);
        if (opVenta.isPresent()) {
            Venta venta = opVenta.get();
            Long oldPreventaId = venta.getPreventaId(); // Guardamos el ID por si necesitamos revertir (aunque no hay reversión en Preventa)

            if (oldPreventaId == null) {
                throw new IllegalStateException("La venta " + ventaId + " no tiene una preventa asignada.");
            }

            // Desasignar la preventaId de la venta
            venta.setPreventaId(null);
            return Optional.of(ventaRepository.save(venta));

            // IMPORTANTE: NO SE NOTIFICA A msvc-preventa para que elimine el ventaId,
            // por la misma razón de NO CAMBIAR msvc-preventa.
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isVentaAvailableForPreventa(Long ventaId) {
        Optional<Venta> opVenta = ventaRepository.findById(ventaId);
        // Una venta está disponible si existe y su preventaId es nulo
        return opVenta.map(venta -> venta.getPreventaId() == null).orElse(false);
    }
}
