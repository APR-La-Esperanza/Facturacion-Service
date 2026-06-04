package com.apr.Facturacion_Service.dto;

import com.apr.Facturacion_Service.model.EstadoFactura;
import java.math.BigDecimal;
import java.time.LocalDate;

public class FacturaResponseDTO {

    private Long id;
    private Long socioId;
    private String periodo;
    private Double lecturaAnterior;
    private Double lecturaActual;
    private Double consumoM3;
    private BigDecimal montoPesos;
    private EstadoFactura estado;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;

    public FacturaResponseDTO() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSocioId() { return socioId; }
    public void setSocioId(Long socioId) { this.socioId = socioId; }
    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }
    public Double getLecturaAnterior() { return lecturaAnterior; }
    public void setLecturaAnterior(Double lecturaAnterior) { this.lecturaAnterior = lecturaAnterior; }
    public Double getLecturaActual() { return lecturaActual; }
    public void setLecturaActual(Double lecturaActual) { this.lecturaActual = lecturaActual; }
    public Double getConsumoM3() { return consumoM3; }
    public void setConsumoM3(Double consumoM3) { this.consumoM3 = consumoM3; }
    public BigDecimal getMontoPesos() { return montoPesos; }
    public void setMontoPesos(BigDecimal montoPesos) { this.montoPesos = montoPesos; }
    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }
    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
}
