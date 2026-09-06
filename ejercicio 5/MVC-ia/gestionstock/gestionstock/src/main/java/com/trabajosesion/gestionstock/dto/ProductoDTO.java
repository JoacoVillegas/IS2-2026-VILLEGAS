package com.trabajosesion.gestionstock.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) - capa de transporte entre Controller y
 * Service para la entidad Producto.
 *
 * POR QUE EXISTE ESTE DTO (y no se usa directamente la entidad Producto):
 *   1) Desacople: la vista Thymeleaf y el Controller nunca deberian
 *      depender de detalles de persistencia (anotaciones @Entity/@Column,
 *      relaciones @OneToOne, proxies LAZY de Hibernate). Si mañana cambia
 *      el modelo de persistencia (por ejemplo, se agrega una relacion
 *      nueva a Producto), el formulario HTML no deberia romperse.
 *   2) Seguridad de datos: el DTO expone SOLO los campos que la vista
 *      realmente necesita mostrar o completar. La entidad Producto tiene
 *      un campo "stock" que es una relacion a otra entidad (Stock) con
 *      su propio ciclo de vida en la base de datos; el DTO en cambio
 *      expone directamente "stockActual" como un numero simple, mucho
 *      mas comodo para un formulario HTML plano (th:field).
 *   3) Evita el problema de serializar entidades JPA con relaciones
 *      bidireccionales/proxies Lazy directamente hacia la vista.
 *
 * Este DTO NO tiene ninguna anotacion JPA (@Entity, @Column, etc.): es
 * una clase Java plana ("POJO"), justamente para remarcar que NO es un
 * objeto de persistencia.
 *
 * Las anotaciones de Bean Validation (@NotBlank, @NotNull, etc.) se
 * validan en el Controller mediante @Valid, ANTES de que el Service
 * reciba el dato: es la forma estandar de Spring de rechazar un
 * formulario mal completado sin necesidad de escribir los "if" de
 * validacion a mano.
 */
public class ProductoDTO {

    // Nulo en el alta (todavia no tiene id); presente en edicion/listado.
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    private String marca;

    private String categoria;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo")
    private BigDecimal precio;

    // Solo se usa al CREAR un producto nuevo (carga de stock inicial);
    // en edicion se ignora, porque el stock se modifica unicamente a
    // traves de movimientos (ver comentario en ProductoServiceImpl).
    @PositiveOrZero(message = "El stock inicial no puede ser negativo")
    private int stockInicial;

    // Solo se completa al LISTAR productos (viene calculado por el
    // Service a partir de la entidad Stock); se ignora si llega en un
    // formulario de alta/edicion.
    private int stockActual;

    public ProductoDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStockInicial() {
        return stockInicial;
    }

    public void setStockInicial(int stockInicial) {
        this.stockInicial = stockInicial;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }
}
