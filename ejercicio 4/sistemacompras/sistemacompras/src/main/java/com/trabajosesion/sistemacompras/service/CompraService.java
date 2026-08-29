package com.trabajosesion.sistemacompras.service;

import com.trabajosesion.sistemacompras.model.Compra;
import com.trabajosesion.sistemacompras.model.Usuario;

import java.util.List;
import java.util.Map;

/**
 * SERVICE - Logica de negocio de Compra. Es el Service que mas capas
 * coordina: Usuario, Producto, Compra, DetalleCompra e Inventario,
 * exactamente el tipo de orquestacion entre varias entidades que NO debe
 * vivir en el Controller.
 */
public interface CompraService {

    /**
     * Registra una compra completa para un usuario.
     *
     * @param usuario           usuario que realiza la compra (obtenido de la sesion HTTP por el Controller)
     * @param cantidadesPorProductoId mapa {idProducto -> cantidad} armado a partir del formulario Thymeleaf
     * @return la Compra ya persistida, con sus DetalleCompra y el precioTotal calculado
     */
    Compra registrarCompra(Usuario usuario, Map<Long, Integer> cantidadesPorProductoId);

    List<Compra> historialDeCompras(Usuario usuario);

    void anularCompra(Long idCompra);
}
