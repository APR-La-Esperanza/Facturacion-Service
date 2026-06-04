package com.apr.Facturacion_Service.mapper;

import com.apr.Facturacion_Service.dto.FacturaDTO;
import com.apr.Facturacion_Service.dto.FacturaResponseDTO;
import com.apr.Facturacion_Service.model.Factura;

public class FacturaMapper {

    public static Factura toEntity(FacturaDTO dto) {
        if (dto == null) return null;
        Factura factura = new Factura();
        factura.setSocioId(dto.getSocioId());
        factura.setPeriodo(dto.getPeriodo());
        factura.setLecturaAnterior(dto.getLecturaAnterior());
        factura.setLecturaActual(dto.getLecturaActual());
        factura.setMontoPesos(dto.getMontoPesos());
        if (dto.getEstado() != null) factura.setEstado(dto.getEstado());
        if (dto.getFechaEmision() != null) factura.setFechaEmision(dto.getFechaEmision());
        if (dto.getFechaVencimiento() != null) factura.setFechaVencimiento(dto.getFechaVencimiento());
        return factura;
    }

    public static FacturaResponseDTO toResponseDTO(Factura factura) {
        if (factura == null) return null;
        FacturaResponseDTO dto = new FacturaResponseDTO();
        dto.setId(factura.getId());
        dto.setSocioId(factura.getSocioId());
        dto.setPeriodo(factura.getPeriodo());
        dto.setLecturaAnterior(factura.getLecturaAnterior());
        dto.setLecturaActual(factura.getLecturaActual());
        dto.setConsumoM3(factura.getConsumoM3());
        dto.setMontoPesos(factura.getMontoPesos());
        dto.setEstado(factura.getEstado());
        dto.setFechaEmision(factura.getFechaEmision());
        dto.setFechaVencimiento(factura.getFechaVencimiento());
        return dto;
    }
}
