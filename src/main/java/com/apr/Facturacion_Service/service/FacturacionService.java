package com.apr.Facturacion_Service.service;

import com.apr.Facturacion_Service.dto.FacturaDTO;
import com.apr.Facturacion_Service.dto.FacturaResponseDTO;
import com.apr.Facturacion_Service.exception.ResourceNotFoundException;
import com.apr.Facturacion_Service.mapper.FacturaMapper;
import com.apr.Facturacion_Service.model.EstadoFactura;
import com.apr.Facturacion_Service.model.Factura;
import com.apr.Facturacion_Service.repository.FacturaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class FacturacionService {

    private final FacturaRepository repository;
    private final WebClient webClient;

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

    public void eliminar(Long id) {
        Factura factura = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id: " + id));
        repository.delete(factura);
    }

    private void validarSocioEnSocioService(Long socioId) {
        try {
            Boolean existe = webClient.get()
                    .uri("/socios/" + socioId)
                    .retrieve()
                    .toBodilessEntity()
                    .map(response -> response.getStatusCode().is2xxSuccessful())
                    .onErrorReturn(false)
                    .block();

            if (existe == null || !existe) {
                throw new IllegalArgumentException("El Socio con ID " + socioId + " no existe en el sistema.");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al comunicarse con Socio-Service: " + e.getMessage());
        }
    }
}
