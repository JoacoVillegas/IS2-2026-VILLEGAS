package com.example.club.exception;

/**
 * BUG CORREGIDO: InscripcionServiceImpl.inscribirSocio() validaba el cupo
 * disponible pero nunca verificaba si el socio YA tenia una inscripcion
 * ACTIVA a esa misma actividad, permitiendo inscribirlo varias veces
 * (filas duplicadas en la tabla inscripcion para el mismo socio+actividad,
 * e inflando artificialmente el conteo de cupos ocupados).
 */
public class SocioYaInscriptoException extends RuntimeException {
    public SocioYaInscriptoException(String socioNombreCompleto, String actividadNombre) {
        super(socioNombreCompleto + " ya esta inscripto/a en la actividad '" + actividadNombre + "'");
    }
}
