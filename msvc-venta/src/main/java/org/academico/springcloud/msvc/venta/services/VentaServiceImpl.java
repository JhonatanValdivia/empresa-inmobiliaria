package org.academico.springcloud.msvc.venta.services;

//import org.academico.springcloud.msvc.venta.clients.ComisionClientRest;
import org.academico.springcloud.msvc.venta.models.entities.DetalleVenta;
import org.academico.springcloud.msvc.venta.models.entities.Venta;
import org.academico.springcloud.msvc.venta.repositories.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VentaServiceImpl implements VentaService{

@Autowired
private VentaRepository ventaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listar() {
        return (List<Venta>) ventaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> todosPorId(List<Long> ids) {
        return (List<Venta>) ventaRepository.findAllById(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> porId(Long id) {
      return ventaRepository.findById(id);
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
}
