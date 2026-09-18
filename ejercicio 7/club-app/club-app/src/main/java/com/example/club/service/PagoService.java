package com.example.club.service;

import com.example.club.dto.PagoDTO;
import java.util.List;

public interface PagoService {

    List<PagoDTO> listarPorFamilia(Long grupoFamiliarId);

    List<PagoDTO> listarTodos();

    PagoDTO registrar(PagoDTO dto);

    void anular(Long id);
}
