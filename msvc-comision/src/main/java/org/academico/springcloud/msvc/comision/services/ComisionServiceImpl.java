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
import java.math.RoundingMode;
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
        Venta venta = ventaClientRest.detalle(comision.getVentaId());
        if (venta == null || venta.getPrecioVenta() == null) {
            throw new IllegalArgumentException("La venta o el precio de venta no están disponibles.");
        }
        BigDecimal montoComisionCalculado = calcularComision(venta.getPrecioVenta().getPrecioVenta(), comision.getTipoComision());
        comision.setMontoComision(new MontoComision(montoComisionCalculado, venta.getPrecioVenta().getMoneda()));
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
    public BigDecimal calcularComision(BigDecimal montoBase, TipoComision tipoComision) {
        BigDecimal resultado = tipoComision.calcular(montoBase);
        return resultado.setScale(2, RoundingMode.HALF_UP);
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
    public List<Comision> listarActivas() {
        return comisionRepository.findByEstadoComisionIn(
                List.of(EstadoComision.PENDIENTE, EstadoComision.CONFIRMADA)
        );
    }
}
