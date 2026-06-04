package com.apr.Facturacion_Service.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "socio_id", nullable = false)
    private Long socioId;

    @Column(nullable = false, length = 7)
    private String periodo; // yyyy-MM

    @Column(name = "lectura_anterior", nullable = false)
    private Double lecturaAnterior;

    @Column(name = "lectura_actual", nullable = false)
    private Double lecturaActual;

    @Column(name = "consumo_m3")
    private Double consumoM3;

    @Column(name = "monto_pesos", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoPesos;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoFactura estado;

    @Column(name = "fecha_emision")
    private LocalDate fechaEmision;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @PrePersist
    protected void onCreate() {
        if (this.estado == null) this.estado = EstadoFactura.PENDIENTE;
        if (this.fechaEmision == null) this.fechaEmision = LocalDate.now();
        if (this.fechaVencimiento == null) this.fechaVencimiento = LocalDate.now().plusDays(30);
        if (this.lecturaAnterior != null && this.lecturaActual != null)
            this.consumoM3 = this.lecturaActual - this.lecturaAnterior;
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
