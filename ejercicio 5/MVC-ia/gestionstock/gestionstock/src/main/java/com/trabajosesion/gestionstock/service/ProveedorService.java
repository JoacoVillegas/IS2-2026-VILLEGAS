package com.trabajosesion.gestionstock.service;

import com.trabajosesion.gestionstock.dto.ProveedorDTO;

import java.util.List;
import java.util.Optional;

/** SERVICE - Logica de negocio de Proveedor, expresada 100% en terminos de DTO hacia el Controller. */
public interface ProveedorService {

    List<ProveedorDTO> listarActivos();

    Optional<ProveedorDTO> buscarPorId(Long id);

    ProveedorDTO registrar(ProveedorDTO dto);

    ProveedorDTO editar(Long id, ProveedorDTO dto);

    void eliminar(Long id);
}
