package com.trabajosesion.gestionstock.mapper;

import com.trabajosesion.gestionstock.dto.ProductoDTO;
import com.trabajosesion.gestionstock.model.Producto;
import org.springframework.stereotype.Component;

/**
 * MAPPER - responsabilidad unica: convertir entre la entidad Producto
 * (objeto de persistencia, con anotaciones JPA) y ProductoDTO (objeto de
 * transporte plano). Es la unica clase del proyecto que "sabe" traducir
 * entre estos dos mundos.
 *
 * Por que una clase Mapper dedicada, en lugar de un metodo toDto() dentro
 * de la propia entidad Producto (como se ve en algunos tutoriales):
 *   - La entidad Producto pertenece conceptualmente a la capa Model/
 *     persistencia; no deberia depender del paquete "dto" (que pertenece
 *     a la capa de transporte hacia Controller/Vista). Mezclar ambas
 *     responsabilidades en la misma clase viola el principio de
 *     responsabilidad unica.
 *   - Centralizar la conversion en un Mapper facilita encontrar y
 *     modificar la logica de mapeo en un unico lugar cuando el DTO o la
 *     entidad cambian.
 *
 * @Component: lo registra como bean de Spring para poder inyectarlo por
 * constructor en ProductoServiceImpl (igual que un @Service o @Repository,
 * solo que @Component es la anotacion generica quese usa cuando la clase
 * no encaja en ninguna de las estereotipadas mas especificas).
 *
 * No convierte "stockActual" desde la entidad (esa lectura implica
 * consultar el Stock asociado y decidir si el producto tiene o no stock
 * cargado): ese dato lo completa ProductoServiceImpl explícitamente,
 * porque calcular el stock es una decision de negocio, no un simple
 * mapeo de campos.
 */
@Component
public class ProductoMapper {

    public ProductoDTO toDTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setMarca(producto.getMarca());
        dto.setCategoria(producto.getCategoria());
        dto.setPrecio(producto.getPrecio());
        return dto;
    }

    /**
     * Crea una NUEVA entidad a partir de un DTO (usado en el alta). No
     * setea el "id" (todavia no existe en la base) ni el "stock" (eso lo
     * arma el Service, porque requiere ademas el valor de stockInicial).
     */
    public Producto toEntity(ProductoDTO dto) {
        Producto producto = new Producto();
        aplicarDatosBasicos(producto, dto);
        return producto;
    }

    /** Actualiza una entidad EXISTENTE (usado en edicion) con los datos del DTO. */
    public void actualizarEntity(Producto producto, ProductoDTO dto) {
        aplicarDatosBasicos(producto, dto);
    }

    private void aplicarDatosBasicos(Producto producto, ProductoDTO dto) {
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setMarca(dto.getMarca());
        producto.setCategoria(dto.getCategoria());
        producto.setPrecio(dto.getPrecio());
    }
}
