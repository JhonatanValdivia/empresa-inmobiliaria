package org.academico.springcloud.msvc.cobro.services;

import org.academico.springcloud.msvc.cobro.models.entities.Cobro;
import org.academico.springcloud.msvc.cobro.repositories.CobroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CobroServiceImpl implements CobroService {
    @Autowired
    private CobroRepository cobroRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Cobro> listarCobros() {
        return (List<Cobro>) cobroRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cobro> porId(Long id) {
        return cobroRepository.findById(id);
    }

    @Override
    @Transactional
    public Cobro crearCobro(Cobro cobro) {
        if (cobro.getIdVenta() == null) {
            throw new IllegalArgumentException("El ID de la venta es requerido.");
        }
        return cobroRepository.save(cobro);
    }

    @Override
    @Transactional
    public Cobro actualizarCobro(Long id, Cobro cobro) {
        Optional<Cobro> optionalCobro = cobroRepository.findById(id);
        if (optionalCobro.isPresent()) {
            Cobro cobroActual = optionalCobro.get();
            cobroActual.setIdVenta(cobro.getIdVenta());
            cobroActual.setFechaCobro(cobro.getFechaCobro());
            cobroActual.setMontoCobro(cobro.getMontoCobro());
            cobroActual.setEstadoCobro(cobro.getEstadoCobro());
            cobroActual.setMedioPago(cobro.getMedioPago());
            return cobroRepository.save(cobroActual);
        }
        return null;
    }

    @Override
    @Transactional
    public void eliminarCobro(Long id) {
        cobroRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Cobro registrarPago(Long id, BigDecimal monto) {
        Optional<Cobro> optionalCobro = cobroRepository.findById(id);
        if (optionalCobro.isPresent()) {
            Cobro cobro = optionalCobro.get();
            cobro.registrarPago(monto); // La validación de monto igual ya se hace en el controlador
            return cobroRepository.save(cobro);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public String generarComprobante(Long id) {
        Optional<Cobro> optionalCobro = cobroRepository.findById(id);
        return optionalCobro.map(Cobro::generarComprobante).orElse("Cobro no encontrado");
    }
}