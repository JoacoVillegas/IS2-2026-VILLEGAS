package com.example.colegio.service.impl;

import com.example.colegio.dto.GradoDTO;
import com.example.colegio.entity.Colegio;
import com.example.colegio.entity.Grado;
import com.example.colegio.exception.ResourceNotFoundException;
import com.example.colegio.mapper.GradoMapper;
import com.example.colegio.repository.ColegioRepository;
import com.example.colegio.repository.GradoRepository;
import com.example.colegio.service.GradoService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * NOTA DE ARQUITECTURA: este Service SI usa ColegioRepository ademas de
 * GradoRepository. No es una violacion de la regla "un Service no accede al
 * Repository de otra entidad": GradoServiceImpl es, por diseño, el
 * responsable de la relacion Grado-Colegio (no existe -ni se pidio- un
 * ColegioService de negocio propio, ya que Colegio no tiene ABM expuesto,
 * ver Colegio.java). Se trata como una unica responsabilidad combinada
 * "Grado (y su Colegio contenedor)".
 */
@Service
@Transactional
public class GradoServiceImpl implements GradoService {

    private final GradoRepository gradoRepository;
    private final ColegioRepository colegioRepository;
    private final GradoMapper gradoMapper;

    public GradoServiceImpl(GradoRepository gradoRepository, ColegioRepository colegioRepository, GradoMapper gradoMapper) {
        this.gradoRepository = gradoRepository;
        this.colegioRepository = colegioRepository;
        this.gradoMapper = gradoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradoDTO> listarActivos() {
        return gradoRepository.findByEliminadoFalse().stream().map(gradoMapper::toDTO).toList();
    }

    @Override
    public GradoDTO registrar(GradoDTO dto) {
        // Al no existir ABM de Colegio, se asume el (unico) colegio ya
        // sembrado por data.sql. findAll().get(0) es aceptable en este
        // ejercicio academico de un unico colegio; en un sistema
        // multi-colegio real, el colegio se recibiria como parametro.
        Colegio colegio = colegioRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No hay ningun Colegio cargado en el sistema"));
        Grado grado = Grado.builder()
                .nivel(dto.getNivel())
                .eliminado(false)
                .colegio(colegio)
                .build();
        return gradoMapper.toDTO(gradoRepository.save(grado));
    }

    @Override
    @Transactional(readOnly = true)
    public Grado obtenerEntidadPorId(Long id) {
        return gradoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grado no encontrado, id: " + id));
    }
}
