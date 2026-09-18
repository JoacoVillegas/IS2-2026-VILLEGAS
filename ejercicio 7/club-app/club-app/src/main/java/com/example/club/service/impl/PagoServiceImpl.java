package com.example.club.service.impl;

import com.example.club.dto.PagoDTO;
import com.example.club.entity.EstadoPago;
import com.example.club.entity.GrupoFamiliar;
import com.example.club.entity.Pago;
import com.example.club.exception.ResourceNotFoundException;
import com.example.club.mapper.PagoMapper;
import com.example.club.repository.PagoRepository;
import com.example.club.service.GrupoFamiliarService;
import com.example.club.service.PagoService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Flujo de registro de pago (ver analisis de diseño, punto 7):
 * PagoController -> PagoDTO -> PagoServiceImpl -> GrupoFamiliarService
 * (para validar que la familia existe) -> PagoRepository. NUNCA
 * PagoServiceImpl -> GrupoFamiliarRepository directamente (regla
 * fundamental del enunciado, punto 6).
 *
 * @Transactional a nivel de clase: si en el futuro "registrar un pago"
 * involucrara mas de una escritura (por ejemplo, actualizar un contador de
 * pagos en GrupoFamiliar), toda la operacion se revertiria junto si algo
 * falla a mitad de camino. Hoy es una unica escritura, pero se mantiene la
 * anotacion por consistencia y porque documenta la intencion de atomicidad.
 */
@Service
@Transactional
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final PagoMapper pagoMapper;
    private final GrupoFamiliarService grupoFamiliarService;

    public PagoServiceImpl(PagoRepository pagoRepository, PagoMapper pagoMapper, GrupoFamiliarService grupoFamiliarService) {
        this.pagoRepository = pagoRepository;
        this.pagoMapper = pagoMapper;
        this.grupoFamiliarService = grupoFamiliarService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> listarPorFamilia(Long grupoFamiliarId) {
        return pagoRepository.findByGrupoFamiliar_IdOrderByFechaDesc(grupoFamiliarId).stream()
                .map(pagoMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> listarTodos() {
        return pagoRepository.findAllByOrderByFechaDesc().stream().map(pagoMapper::toDTO).toList();
    }

    @Override
    public PagoDTO registrar(PagoDTO dto) {
        // Service -> Service -> Repository: se valida/obtiene la familia a
        // traves de GrupoFamiliarService, nunca accediendo directamente a
        // GrupoFamiliarRepository desde este Service.
        GrupoFamiliar familia = grupoFamiliarService.obtenerEntidadPorId(dto.getGrupoFamiliarId());

        Pago pago = Pago.builder()
                .grupoFamiliar(familia)
                .fecha(dto.getFecha())
                .periodo(dto.getPeriodo())
                .importe(dto.getImporte())
                .medioPago(dto.getMedioPago())
                .comprobante(dto.getComprobante())
                .estado(EstadoPago.REGISTRADO)
                .build();
        return pagoMapper.toDTO(pagoRepository.save(pago));
    }

    @Override
    public void anular(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado, id: " + id));
        pago.setEstado(EstadoPago.ANULADO); // baja logica: nunca se borra un pago fisicamente
        pagoRepository.save(pago);
    }
}
