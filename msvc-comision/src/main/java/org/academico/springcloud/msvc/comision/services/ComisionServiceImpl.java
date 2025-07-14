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
        if (comision.getMontoComision() == null) {
            Venta venta = ventaClientRest.detalle(comision.getVentaId());
            BigDecimal monto = calcularComision(venta.getPrecioVenta().getPrecioVenta(), comision.getTipoComision());
            comision.setMontoComision(new MontoComision(monto, venta.getPrecioVenta().getMoneda()));
        }
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
    public BigDecimal calcularComision(BigDecimal montoBase, TipoComision tipoComision) {
        return tipoComision == TipoComision.PORCENTAJE
                ? montoBase.multiply(new BigDecimal("0.10"))
                : new BigDecimal("400.00");
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
        comision.actualizarEstado(nuevoEstado);
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
