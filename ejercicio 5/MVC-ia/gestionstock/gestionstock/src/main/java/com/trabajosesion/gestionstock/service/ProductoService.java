package com.trabajosesion.gestionstock.service;

import com.trabajosesion.gestionstock.dto.ProductoDTO;

import java.util.List;
import java.util.Optional;

/**
 * SERVICE - Logica de negocio de Producto. Notese que la firma de estos
 * metodos habla en terminos de ProductoDTO, nunca de la entidad Producto:
 * es la materializacion concreta del requisito "la informacion que pasa
 * entre las capas debera utilizar DTO" (punto 4 del enunciado). El
 * Controller jamas ve ni construye una entidad Producto.
 */
public interface ProductoService {

    List<ProductoDTO> listarActivos();

    Optional<ProductoDTO> buscarPorId(Long id);

    ProductoDTO registrar(ProductoDTO dto);

    ProductoDTO editar(Long id, ProductoDTO dto);

    /** Baja logica: marca eliminado=true, no borra la fila de la base. */
    void eliminar(Long id);
}
