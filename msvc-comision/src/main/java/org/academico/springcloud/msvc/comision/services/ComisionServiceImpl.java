package org.academico.springcloud.msvc.comision.services;

import org.academico.springcloud.msvc.comision.clients.VentaClientRest;
import org.academico.springcloud.msvc.comision.models.Venta;
import org.academico.springcloud.msvc.comision.models.entities.Comision;
import org.academico.springcloud.msvc.comision.models.enums.EstadoComision;
import org.academico.springcloud.msvc.comision.models.enums.TipoComision;
import org.academico.springcloud.msvc.comision.models.valueObjects.MontoComision;
import org.academico.springcloud.msvc.comision.repositories.ComisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ComisionServiceImpl implements ComisionService {
    @Autowired
    private ComisionRepository comisionRepository;

    @Autowired
    private VentaClientRest ventaClientRest;

    @Override
    @Transactional(readOnly = true)
    public List<Comision> listar() {
        return (List<Comision>) comisionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comision> todosPorId(List<Long> ids) {
        return (List<Comision>) comisionRepository.findAllById(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Comision> porId(Long id) {
        return comisionRepository.findById(id);
    }

    @Override
    @Transactional
    public Comision guardar(Comision comision) {
        return comisionRepository.save(comision);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        comisionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public boolean existePorId(Long id) {
        return comisionRepository.existsById(id);
    }

    @Override
    @Transactional
    public long contarComisiones() {
        return comisionRepository.count();
    }

    @Override
    @Transactional
    public void eliminarComision(Comision comision) {
        comisionRepository.delete(comision);
    }

    @Override
    @Transactional
    public Optional<Venta> obtenerVenta(Long ventaId) {
        return Optional.ofNullable(ventaClientRest.detalle(ventaId));
    }

    @Override
    @Transactional
    public Optional<Comision> crearComisionParaVenta(Long ventaId, TipoComision tipoComision) {
        Venta venta = ventaClientRest.detalle(ventaId);

        BigDecimal montoBase = venta.getPrecioVenta().getPrecioVenta();
        BigDecimal montoComision;

        if (tipoComision == TipoComision.PORCENTAJE) {
            montoComision = montoBase.multiply(new BigDecimal("0.10")); // 10%
        } else {
            montoComision = new BigDecimal("100.00"); // comisión fija
        }

        Comision comision = new Comision();
        comision.setVentaId(ventaId);
        comision.setTipoComision(tipoComision);
        comision.setEstadoComision(EstadoComision.PENDIENTE);
        comision.setMontoComision(new MontoComision(montoComision, venta.getPrecioVenta().getMoneda()));

        return Optional.of(comisionRepository.save(comision));
    }

    @Override
    @Transactional
    public void recalcularComision(Long comisionId) {
        Comision comision = comisionRepository.findById(comisionId)
                .orElseThrow(() -> new RuntimeException("Comisión no encontrada"));
        Venta venta = ventaClientRest.detalle(comision.getVentaId());

        BigDecimal nuevoMonto = comision.getTipoComision() == TipoComision.PORCENTAJE ?
                venta.getPrecioVenta().getPrecioVenta().multiply(new BigDecimal("0.10")) :
                new BigDecimal("100.00");

        comision.setMontoComision(new MontoComision(nuevoMonto, venta.getPrecioVenta().getMoneda()));
        comisionRepository.save(comision);
    }

    @Override
    @Transactional
    public void anularComisionesPorVenta(Long ventaId) {
        Venta venta = ventaClientRest.detalle(ventaId);
        if ("ANULADA".equals(venta.getEstadoVenta())) {
            List<Comision> comisiones = comisionRepository.findByVentaId(ventaId);
            for (Comision c : comisiones) {
                c.setEstadoComision(EstadoComision.ANULADA);
                comisionRepository.save(c);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comision> listarPorVenta(Long ventaId) {
        return comisionRepository.findByVentaId(ventaId);
    }

    @Override
    @Transactional
    public void cambiarEstadoComision(Long comisionId, EstadoComision nuevoEstado) {
        Comision comision = comisionRepository.findById(comisionId)
                .orElseThrow(() -> new RuntimeException("Comisión no encontrada"));
        comision.setEstadoComision(nuevoEstado);
        comisionRepository.save(comision);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal totalComisionesPorVenta(Long ventaId) {
        List<Comision> comisiones = comisionRepository.findByVentaId(ventaId);
        return comisiones.stream()
                .map(c -> c.getMontoComision().getMontoComision())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comision> listarActivas() {
        return comisionRepository.findByEstadoComisionIn(
                List.of(EstadoComision.PENDIENTE, EstadoComision.CONFIRMADA)
        );
    }
}
