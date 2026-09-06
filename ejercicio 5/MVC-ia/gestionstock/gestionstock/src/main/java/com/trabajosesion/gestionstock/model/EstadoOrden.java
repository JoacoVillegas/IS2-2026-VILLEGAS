package com.trabajosesion.gestionstock.model;

/**
 * MODEL - Enum del dominio.
 *
 * ADAPTACION RESPECTO DEL DIAGRAMA: el diagrama original modela
 * "estado: String" en OrdenCompra (un texto libre). Se reemplaza por un
 * enum porque un campo de texto libre permitiria guardar cualquier valor
 * ("pendiente", "Pendiente", "PENDIENTE ", etc.), lo que haria muy fragil
 * cualquier logica que dependa del estado (como el metodo
 * cambiarEstado() del diagrama). Con un enum, Hibernate y el compilador
 * garantizan que solo existan estos tres valores posibles.
 */
public enum EstadoOrden {
    PENDIENTE,
    CONFIRMADA,
    ANULADA
}
