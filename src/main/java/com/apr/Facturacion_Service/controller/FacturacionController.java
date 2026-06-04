package com.apr.Facturacion_Service.controller;

import com.apr.Facturacion_Service.dto.FacturaDTO;
import com.apr.Facturacion_Service.dto.FacturaResponseDTO;
import com.apr.Facturacion_Service.model.EstadoFactura;
import com.apr.Facturacion_Service.service.FacturacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facturas")
public class FacturacionController {

    private final FacturacionService service;

    public FacturacionController(FacturacionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<FacturaResponseDTO>> listarTodas(
            @RequestParam(required = false) Long socioId,
            @RequestParam(required = false) EstadoFactura estado,
            @RequestParam(required = false) String periodo) {
        if (socioId != null) {
            return ResponseEntity.ok(service.buscarPorSocioId(socioId));
        }
        if (estado != null) {
            return ResponseEntity.ok(service.buscarPorEstado(estado));
        }
        if (periodo != null) {
            return ResponseEntity.ok(service.buscarPorPeriodo(periodo));
        }
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<FacturaResponseDTO> guardar(@Valid @RequestBody FacturaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacturaResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody FacturaDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
