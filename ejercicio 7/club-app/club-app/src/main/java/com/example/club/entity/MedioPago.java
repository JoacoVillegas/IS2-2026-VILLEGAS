package com.example.club.entity;

/**
 * Medios de pago soportados (REQUISITO EXPLICITO del enunciado, punto 4).
 * IMPORTANTE: MERCADO_PAGO es solamente una categoria/etiqueta dentro de
 * este enum. NO existe ningun SDK, credencial, webhook ni llamada HTTP
 * hacia servicios de Mercado Pago en todo el proyecto. Una integracion real
 * queda documentada como EXTENSION FUTURA / FUERA DE ALCANCE (ver README).
 */
public enum MedioPago {
    EFECTIVO,
    TRANSFERENCIA,
    MERCADO_PAGO
}
