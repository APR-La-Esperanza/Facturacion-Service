package com.apr.Facturacion_Service.repository;

import com.apr.Facturacion_Service.model.EstadoFactura;
import com.apr.Facturacion_Service.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findBySocioId(Long socioId);
    List<Factura> findByEstado(EstadoFactura estado);
    List<Factura> findByPeriodo(String periodo);
    List<Factura> findBySocioIdAndPeriodo(Long socioId, String periodo);
}
