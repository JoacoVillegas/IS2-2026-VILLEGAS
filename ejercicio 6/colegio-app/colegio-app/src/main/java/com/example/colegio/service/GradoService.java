package com.example.colegio.service;

import com.example.colegio.dto.GradoDTO;
import com.example.colegio.entity.Grado;
import java.util.List;

public interface GradoService {

    List<GradoDTO> listarActivos();

    GradoDTO registrar(GradoDTO dto);

    /** Devuelve la entidad (no el DTO): uso interno exclusivo de AulaServiceImpl para armar la relacion. */
    Grado obtenerEntidadPorId(Long id);
}
