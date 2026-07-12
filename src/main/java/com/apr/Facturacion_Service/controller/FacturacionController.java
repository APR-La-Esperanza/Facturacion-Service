package com.apr.Facturacion_Service.controller;

import com.apr.Facturacion_Service.dto.FacturaDTO;
import com.apr.Facturacion_Service.dto.FacturaResponseDTO;
import com.apr.Facturacion_Service.model.EstadoFactura;
import com.apr.Facturacion_Service.service.FacturacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facturas")
@Tag(name = "Facturación", description = "Endpoints para la gestión, emisión y consulta de facturas de consumo de agua potable.")
@SecurityRequirement(name = "bearerAuth")
public class FacturacionController {

    private final FacturacionService service;

    public FacturacionController(FacturacionService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar facturas", description = "Retorna una lista de facturas. Permite filtrar por socioId, estado (PENDIENTE, PAGADA, VENCIDA) o período (YYYY-MM).")
    @ApiResponse(responseCode = "200", description = "Listado de facturas obtenido con éxito.")
    public ResponseEntity<List<FacturaResponseDTO>> listarTodas(
            @Parameter(description = "ID del socio para filtrar") @RequestParam(required = false) Long socioId,
            @Parameter(description = "Estado de la factura para filtrar") @RequestParam(required = false) EstadoFactura estado,
            @Parameter(description = "Período (formato YYYY-MM) para filtrar") @RequestParam(required = false) String periodo) {
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
    @Operation(summary = "Buscar factura por ID", description = "Obtiene los detalles de una factura específica.")
    @ApiResponse(responseCode = "200", description = "Factura encontrada y devuelta.")
    @ApiResponse(responseCode = "404", description = "La factura especificada no existe.")
    public ResponseEntity<FacturaResponseDTO> buscarPorId(
            @Parameter(description = "ID único de la factura", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear factura", description = "Emite una nueva factura de consumo. Calcula el consumo en M3 como lecturaActual - lecturaAnterior.")
    @ApiResponse(responseCode = "201", description = "Factura emitida de manera correcta.")
    @ApiResponse(responseCode = "400", description = "Datos de entrada incorrectos o error de validación de socio.")
    public ResponseEntity<FacturaResponseDTO> guardar(
            @RequestBody(description = "Datos para la nueva factura de consumo", required = true,
                         content = @Content(schema = @Schema(implementation = FacturaDTO.class),
                                            examples = @ExampleObject(value = "{\n  \"socioId\": 1,\n  \"periodo\": \"2025-06\",\n  \"lecturaAnterior\": 120.5,\n  \"lecturaActual\": 135.2,\n  \"montoPesos\": 15000.0,\n  \"estado\": \"PENDIENTE\",\n  \"fechaEmision\": \"2025-06-01\",\n  \"fechaVencimiento\": \"2025-06-20\"\n}")))
            @Valid @org.springframework.web.bind.annotation.RequestBody FacturaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar factura", description = "Modifica los datos de una factura existente por su ID.")
    @ApiResponse(responseCode = "200", description = "Factura modificada con éxito.")
    @ApiResponse(responseCode = "400", description = "Datos provistos inválidos.")
    @ApiResponse(responseCode = "404", description = "No se encontró la factura a modificar.")
    public ResponseEntity<FacturaResponseDTO> actualizar(
            @Parameter(description = "ID de la factura a actualizar", required = true) @PathVariable Long id,
            @RequestBody(description = "Nuevos datos de la factura", required = true,
                         content = @Content(schema = @Schema(implementation = FacturaDTO.class),
                                            examples = @ExampleObject(value = "{\n  \"socioId\": 1,\n  \"periodo\": \"2025-06\",\n  \"lecturaAnterior\": 120.5,\n  \"lecturaActual\": 135.2,\n  \"montoPesos\": 15000.0,\n  \"estado\": \"PAGADA\",\n  \"fechaEmision\": \"2025-06-01\",\n  \"fechaVencimiento\": \"2025-06-20\"\n}")))
            @Valid @org.springframework.web.bind.annotation.RequestBody FacturaDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @PostMapping("/{id}/notificar")
    @Operation(summary = "Notificar factura simulada", description = "Genera y retorna un mensaje simulado de notificación de factura emitida al socio.")
    @ApiResponse(responseCode = "200", description = "Mensaje simulado retornado con éxito.")
    @ApiResponse(responseCode = "404", description = "La factura no existe.")
    public ResponseEntity<java.util.Map<String, Object>> notificar(
            @Parameter(description = "ID de la factura a notificar", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(service.notificarFactura(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar factura", description = "Elimina permanentemente una factura del sistema.")
    @ApiResponse(responseCode = "204", description = "Factura eliminada de forma exitosa.")
    @ApiResponse(responseCode = "404", description = "La factura no existe.")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la factura a eliminar", required = true) @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
