package com.apr.Facturacion_Service.dto;

import com.apr.Facturacion_Service.model.EstadoFactura;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

public class FacturaDTO {

    @NotNull(message = "El ID de socio es obligatorio")
    private Long socioId;

    @NotBlank(message = "El periodo es obligatorio")
    @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "El periodo debe tener el formato yyyy-MM")
    private String periodo;

    @NotNull(message = "La lectura anterior es obligatoria")
    private Double lecturaAnterior;

    @NotNull(message = "La lectura actual es obligatoria")
    private Double lecturaActual;

    @NotNull(message = "El monto en pesos es obligatorio")
    private BigDecimal montoPesos;

    private EstadoFactura estado;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;

    public FacturaDTO() {
    }

    public Long getSocioId() { return socioId; }
    public void setSocioId(Long socioId) { this.socioId = socioId; }
    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }
    public Double getLecturaAnterior() { return lecturaAnterior; }
    public void setLecturaAnterior(Double lecturaAnterior) { this.lecturaAnterior = lecturaAnterior; }
    public Double getLecturaActual() { return lecturaActual; }
    public void setLecturaActual(Double lecturaActual) { this.lecturaActual = lecturaActual; }
    public BigDecimal getMontoPesos() { return montoPesos; }
    public void setMontoPesos(BigDecimal montoPesos) { this.montoPesos = montoPesos; }
    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }
    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
}
