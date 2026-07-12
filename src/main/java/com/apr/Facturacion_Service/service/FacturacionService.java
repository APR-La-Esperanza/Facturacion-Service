package com.apr.Facturacion_Service.service;

import com.apr.Facturacion_Service.dto.FacturaDTO;
import com.apr.Facturacion_Service.dto.FacturaResponseDTO;
import com.apr.Facturacion_Service.exception.ResourceNotFoundException;
import com.apr.Facturacion_Service.mapper.FacturaMapper;
import com.apr.Facturacion_Service.model.EstadoFactura;
import com.apr.Facturacion_Service.model.Factura;
import com.apr.Facturacion_Service.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FacturacionService {

    private final FacturaRepository repository;
    private final WebClient webClient;

    @Value("${service.socio.url:http://socio-service}")
    private String socioServiceUrl;

    public FacturacionService(FacturaRepository repository, WebClient webClient) {
        this.repository = repository;
        this.webClient = webClient;
    }

    public List<FacturaResponseDTO> listarTodas() {
        return repository.findAll()
                .stream()
                .map(FacturaMapper::toResponseDTO)
                .toList();
    }

    public FacturaResponseDTO buscarPorId(Long id) {
        Factura factura = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id: " + id));
        return FacturaMapper.toResponseDTO(factura);
    }

    public List<FacturaResponseDTO> buscarPorSocioId(Long socioId) {
        return repository.findBySocioId(socioId)
                .stream()
                .map(FacturaMapper::toResponseDTO)
                .toList();
    }

    public List<FacturaResponseDTO> buscarPorEstado(EstadoFactura estado) {
        return repository.findByEstado(estado)
                .stream()
                .map(FacturaMapper::toResponseDTO)
                .toList();
    }

    public List<FacturaResponseDTO> buscarPorPeriodo(String periodo) {
        return repository.findByPeriodo(periodo)
                .stream()
                .map(FacturaMapper::toResponseDTO)
                .toList();
    }

    public FacturaResponseDTO guardar(FacturaDTO dto) {
        // Validar que el socioId exista en Socio-Service
        validarSocioEnSocioService(dto.getSocioId());

        Factura factura = FacturaMapper.toEntity(dto);
        Factura guardada = repository.save(factura);
        return FacturaMapper.toResponseDTO(guardada);
    }

    public FacturaResponseDTO actualizar(Long id, FacturaDTO dto) {
        Factura factura = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id: " + id));

        // Validar que el socioId exista en Socio-Service si cambió
        if (!factura.getSocioId().equals(dto.getSocioId())) {
            validarSocioEnSocioService(dto.getSocioId());
        }

        factura.setSocioId(dto.getSocioId());
        factura.setPeriodo(dto.getPeriodo());
        factura.setLecturaAnterior(dto.getLecturaAnterior());
        factura.setLecturaActual(dto.getLecturaActual());
        factura.setMontoPesos(dto.getMontoPesos());
        if (dto.getEstado() != null) factura.setEstado(dto.getEstado());
        if (dto.getFechaEmision() != null) factura.setFechaEmision(dto.getFechaEmision());
        if (dto.getFechaVencimiento() != null) factura.setFechaVencimiento(dto.getFechaVencimiento());

        // Recalcular consumo en base a las nuevas lecturas
        if (dto.getLecturaAnterior() != null && dto.getLecturaActual() != null) {
            factura.setConsumoM3(dto.getLecturaActual() - dto.getLecturaAnterior());
        }

        Factura actualizada = repository.save(factura);
        return FacturaMapper.toResponseDTO(actualizada);
    }

    public Map<String, Object> notificarFactura(Long id) {
        Factura factura = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id: " + id));

        Map<String, Object> notificacion = new HashMap<>();
        notificacion.put("tipo", "NOTIFICACION_FACTURA");
        notificacion.put("socioId", factura.getSocioId());
        notificacion.put("facturaId", factura.getId());
        notificacion.put("periodo", factura.getPeriodo());
        notificacion.put("consumoM3", factura.getConsumoM3());
        notificacion.put("montoPesos", factura.getMontoPesos());
        notificacion.put("estado", factura.getEstado());
        notificacion.put("fechaVencimiento", factura.getFechaVencimiento());
        notificacion.put("mensaje", String.format(
                "Estimado socio %d: Su factura del periodo %s por $%.0f ha sido emitida. " +
                "Consumo: %.2f m³. Vence el %s.",
                factura.getSocioId(),
                factura.getPeriodo(),
                factura.getMontoPesos() != null ? factura.getMontoPesos().doubleValue() : 0.0,
                factura.getConsumoM3() != null ? factura.getConsumoM3() : 0.0,
                factura.getFechaVencimiento()));
        notificacion.put("simulado", true);
        return notificacion;
    }
    public void eliminar(Long id) {
        Factura factura = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id: " + id));
        repository.delete(factura);
    }

    private void validarSocioEnSocioService(Long socioId) {
        try {
            RestClient restClient = RestClient.builder()
                    .baseUrl(socioServiceUrl)
                    .build();
            restClient.get()
                    .uri("/socios/" + socioId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            throw new IllegalArgumentException("El Socio con ID " + socioId + " no existe en el sistema.");
        } catch (Exception e) {
            // Si Socio-Service no responde, continuamos
            System.out.println("[WARN] No se pudo validar socio en Socio-Service: " + e.getMessage());
        }
    }
}
