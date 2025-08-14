package org.academico.springcloud.msvc.comision.services;

import org.academico.springcloud.msvc.comision.clients.VentaClientRest;
import org.academico.springcloud.msvc.comision.clients.UsuarioClientRest;
import org.academico.springcloud.msvc.comision.models.Usuario;
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

    @Autowired
    private UsuarioClientRest usuarioClientRest;

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
        // Verificar si ya existe una comisión para esta venta
        if (comisionRepository.findByVentaId(comision.getVentaId()).isPresent()) {
            throw new IllegalStateException("Ya existe una comisión para la venta con ID " + comision.getVentaId());
        }

        // Obtener la venta desde msvc-venta
        Venta venta = ventaClientRest.detalle(comision.getVentaId());
        if (venta == null || venta.getPrecioVenta() == null) {
            throw new IllegalArgumentException("No se encontró la venta o el precio es nulo para el ID " + comision.getVentaId());
        }

        // Obtener y validar el monto base de la venta (solo para PORCENTAJE)
        BigDecimal montoBase = venta.getPrecioVenta().getMontoComision();
        if (montoBase == null || montoBase.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto base de la venta debe ser mayor que cero");
        }

        // Calcular el monto de la comisión automáticamente
        BigDecimal montoCalculado;
        if (comision.getTipoComision() == TipoComision.PORCENTAJE) {
            montoCalculado = montoBase.multiply(new BigDecimal("0.03")); // 3% del monto base
        } else if (comision.getTipoComision() == TipoComision.FIJA) {
            montoCalculado = new BigDecimal("2000.00"); // Monto fijo de 2000 soles
        } else {
            throw new IllegalArgumentException("Tipo de comisión no soportado: " + comision.getTipoComision());
        }

        // Asignar el monto calculado a la comisión
        comision.setMontoComision(new MontoComision(montoCalculado, "PEN"));

        // Guardar la comisión
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

    @Override
    @Transactional(readOnly = true)
    public Optional<Comision> porIdConUsuario(Long id) {
        Optional<Comision> comisionOp = comisionRepository.findById(id);
        if (comisionOp.isPresent()) {
            Comision comision = comisionOp.get();

            // Cargar información de la venta
            Venta venta = ventaClientRest.detalle(comision.getVentaId());
            if (venta == null) {
                throw new IllegalStateException("No se encontró la venta con id=" + comision.getVentaId() + " para la comisión con id=" + id);
            }
            comision.setVenta(venta);

            // Cargar información del usuario
            Usuario usuario = usuarioClientRest.detalleUsuario(comision.getAgenteId());
            if (usuario == null) {
                throw new IllegalStateException("No se encontró el usuario con id=" + comision.getAgenteId() + " para la comisión con id=" + id);
            }
            comision.setUsuario(usuario);

            return Optional.of(comision);
        }
        return Optional.empty();
    }
}