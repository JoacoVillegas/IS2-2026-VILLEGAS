package com.example.colegio.service;

import com.example.colegio.dto.MateriaDTO;
import com.example.colegio.entity.Materia;
import java.util.List;

public interface MateriaService {

    List<MateriaDTO> listarActivas();

    MateriaDTO buscarPorId(Long id);

    MateriaDTO registrar(MateriaDTO dto);

    MateriaDTO actualizar(Long id, MateriaDTO dto);

    void eliminar(Long id);

    /** Uso interno de otros Services (NotaServiceImpl, DictadoClasesServiceImpl). */
    Materia obtenerEntidadPorId(Long id);
}
