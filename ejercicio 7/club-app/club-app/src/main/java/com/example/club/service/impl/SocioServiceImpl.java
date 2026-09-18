package com.example.club.service.impl;

import com.example.club.dto.SocioDTO;
import com.example.club.entity.GrupoFamiliar;
import com.example.club.entity.Socio;
import com.example.club.exception.ResourceNotFoundException;
import com.example.club.mapper.SocioMapper;
import com.example.club.repository.GrupoFamiliarRepository;
import com.example.club.repository.SocioRepository;
import com.example.club.service.SocioService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * NOTA DE ARQUITECTURA: SocioServiceImpl usa GrupoFamiliarRepository
 * ademas de SocioRepository. Esto es una excepcion intencional y acotada:
 * al registrar un Socio, la creacion de su GrupoFamiliar titular es parte
 * del MISMO acto de alta (composicion, ver analisis de diseño) y debe
 * ocurrir en la misma transaccion; encapsular esto en GrupoFamiliarService
 * hubiera significado que ese Service reciba una entidad Socio a medio
 * persistir. Se documenta explicitamente esta decision en lugar de
 * ocultarla.
 */
@Service
@Transactional
public class SocioServiceImpl implements SocioService {

    private final SocioRepository socioRepository;
    private final GrupoFamiliarRepository grupoFamiliarRepository;
    private final SocioMapper socioMapper;

    public SocioServiceImpl(SocioRepository socioRepository,
                             GrupoFamiliarRepository grupoFamiliarRepository,
                             SocioMapper socioMapper) {
        this.socioRepository = socioRepository;
        this.grupoFamiliarRepository = grupoFamiliarRepository;
        this.socioMapper = socioMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SocioDTO> listarActivos() {
        return socioRepository.findByEliminadoFalse().stream()
                .map(this::mapearConGrupoFamiliar)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SocioDTO buscarPorId(Long id) {
        return mapearConGrupoFamiliar(obtenerActivoPorId(id));
    }

    @Override
    public SocioDTO registrar(SocioDTO dto) {
        Socio socio = Socio.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .fechaNacimiento(dto.getFechaNacimiento())
                .fechaAlta(dto.getFechaAlta())
                .estado(dto.getEstado())
                .eliminado(false)
                .build();
        Socio guardado = socioRepository.save(socio);

        // Composicion: al dar de alta un Socio, se crea automaticamente su
        // GrupoFamiliar titular (ver analisis de diseño, relacion de
        // titularidad Socio 1--1 GrupoFamiliar).
        GrupoFamiliar grupo = GrupoFamiliar.builder()
                .socioTitular(guardado)
                .eliminado(false)
                .build();
        grupoFamiliarRepository.save(grupo);

        return socioMapper.toDTO(guardado, grupo.getId());
    }

    @Override
    public SocioDTO actualizar(Long id, SocioDTO dto) {
        Socio socio = obtenerActivoPorId(id);
        socioMapper.actualizarEntidadDesdeDTO(socio, dto);
        Socio guardado = socioRepository.save(socio);
        return mapearConGrupoFamiliar(guardado);
    }

    @Override
    public void eliminar(Long id) {
        Socio socio = obtenerActivoPorId(id);
        socio.setEliminado(true); // baja logica
        socioRepository.save(socio);
    }

    @Override
    @Transactional(readOnly = true)
    public Socio obtenerEntidadPorId(Long id) {
        return obtenerActivoPorId(id);
    }

    private SocioDTO mapearConGrupoFamiliar(Socio socio) {
        Optional<GrupoFamiliar> grupo = grupoFamiliarRepository.findBySocioTitular_Id(socio.getId());
        return socioMapper.toDTO(socio, grupo.map(GrupoFamiliar::getId).orElse(null));
    }

    private Socio obtenerActivoPorId(Long id) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado, id: " + id));
        if (socio.isEliminado()) {
            throw new ResourceNotFoundException("El socio con id " + id + " fue dado de baja");
        }
        return socio;
    }
}
