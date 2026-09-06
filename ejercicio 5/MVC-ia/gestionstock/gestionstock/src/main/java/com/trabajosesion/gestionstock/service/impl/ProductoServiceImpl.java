package com.trabajosesion.gestionstock.service.impl;

import com.trabajosesion.gestionstock.dao.ProductoDAO;
import com.trabajosesion.gestionstock.dto.ProductoDTO;
import com.trabajosesion.gestionstock.mapper.ProductoMapper;
import com.trabajosesion.gestionstock.model.Producto;
import com.trabajosesion.gestionstock.model.Stock;
import com.trabajosesion.gestionstock.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * SERVICE - Implementacion de la logica de negocio de Producto.
 *
 * FLUJO DTO <-> ENTITY COMPLETO (punto 14 del enunciado), ejemplo de
 * "registrar":
 *   ProductoController recibe un ProductoDTO ya validado (@Valid)
 *   -> se lo pasa TAL CUAL a este metodo (el Controller no lo toca)
 *   -> ProductoMapper.toEntity(dto) construye una entidad Producto nueva
 *   -> ProductoDAO.save(entity) persiste esa entidad via Hibernate/MySQL
 *   -> si vino stock inicial, se crea tambien la entidad Stock asociada
 *   -> ProductoMapper.toDTO(entity) vuelve a convertir a DTO
 *   -> el Controller recibe ese DTO de vuelta (nunca la entidad).
 */
@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoDAO productoDAO;
    private final ProductoMapper productoMapper;

    @Autowired
    public ProductoServiceImpl(ProductoDAO productoDAO, ProductoMapper productoMapper) {
        this.productoDAO = productoDAO;
        this.productoMapper = productoMapper;
    }

    @Override
    public List<ProductoDTO> listarActivos() {
        return productoDAO.findByEliminadoFalse().stream()
                .map(this::toDTOConStock)
                .toList();
    }

    @Override
    public Optional<ProductoDTO> buscarPorId(Long id) {
        return productoDAO.findById(id).map(this::toDTOConStock);
    }

    /**
     * El Mapper convierte los campos "planos" (nombre/precio/etc), pero
     * el "stockActual" del DTO se completa aca, leyendo la entidad Stock
     * asociada: decidir COMO se obtiene el stock (sumando movimientos,
     * leyendo un campo directo, etc.) es una regla de negocio del
     * Service, no un simple mapeo de campos del Mapper.
     */
    private ProductoDTO toDTOConStock(Producto producto) {
        ProductoDTO dto = productoMapper.toDTO(producto);
        dto.setStockActual(producto.getStock() != null ? producto.getStock().consultarStock() : 0);
        return dto;
    }

    @Override
    @Transactional
    public ProductoDTO registrar(ProductoDTO dto) {
        Producto producto = productoMapper.toEntity(dto);

        // Todo producto nuevo nace con su registro de Stock asociado
        // (agregacion 1 a 1 del diagrama de clases): sin este paso,
        // Producto.getStock() seria null y no se podria incrementar el
        // stock mas adelante desde una OrdenCompra.
        Stock stockInicial = new Stock(dto.getStockInicial(), "ALTA_INICIAL", producto);
        producto.setStock(stockInicial);

        Producto guardado = productoDAO.save(producto); // cascade=ALL guarda tambien el Stock
        return toDTOConStock(guardado);
    }

    @Override
    @Transactional
    public ProductoDTO editar(Long id, ProductoDTO dto) {
        Producto producto = productoDAO.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe el producto con id " + id));
        // Notese que la edicion NUNCA toca el stock: el stock solo cambia
        // a traves de movimientos explicitos (altas por OrdenCompra), tal
        // como exige la separacion de responsabilidades del diagrama
        // (Producto no tiene metodos para modificar su propio stock; eso
        // es exclusivo de la entidad Stock).
        productoMapper.actualizarEntity(producto, dto);
        Producto actualizado = productoDAO.save(producto);
        return toDTOConStock(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        productoDAO.findById(id).ifPresent(producto -> {
            producto.setEliminado(true); // baja logica: nunca deleteById
            productoDAO.save(producto);
        });
    }
}
