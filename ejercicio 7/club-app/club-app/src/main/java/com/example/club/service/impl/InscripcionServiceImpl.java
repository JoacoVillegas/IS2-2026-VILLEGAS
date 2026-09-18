package com.example.club.service.impl;

import com.example.club.dto.InscripcionDTO;
import com.example.club.entity.Actividad;
import com.example.club.entity.EstadoInscripcion;
import com.example.club.entity.Inscripcion;
import com.example.club.entity.Socio;
import com.example.club.exception.CupoExcedidoException;
import com.example.club.exception.ResourceNotFoundException;
import com.example.club.exception.SocioYaInscriptoException;
import com.example.club.mapper.InscripcionMapper;
import com.example.club.repository.InscripcionRepository;
import com.example.club.service.ActividadService;
import com.example.club.service.InscripcionService;
import com.example.club.service.SocioService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * InscripcionServiceImpl SOLO accede a InscripcionRepository (su propio
 * repository). Para obtener las entidades Socio y Actividad, colabora con
 * SocioService y ActividadService (Service->Service). Para saber cuantos
 * cupos ya estan ocupados usa ActividadService.contarInscriptosActivos()
 * en lugar de consultar InscripcionRepository el mismo: esto puede parecer
 * redundante (InscripcionServiceImpl SI tiene su propio InscripcionRepository
 * inyectado), pero se hace asi a proposito para que la logica de "cuantos
 * cupos ocupados tiene una Actividad" viva en un unico lugar (ActividadService,
 * el dueño conceptual del concepto "cupo"), reutilizable tambien desde
 * ActividadServiceImpl.listarActivas() al armar cada ActividadDTO.
 */
@Service
@Transactional
public class InscripcionServiceImpl implements InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final InscripcionMapper inscripcionMapper;
    private final SocioService socioService;
    private final ActividadService actividadService;

    public InscripcionServiceImpl(InscripcionRepository inscripcionRepository,
                                   InscripcionMapper inscripcionMapper,
                                   SocioService socioService,
                                   ActividadService actividadService) {
        this.inscripcionRepository = inscripcionRepository;
        this.inscripcionMapper = inscripcionMapper;
        this.socioService = socioService;
        this.actividadService = actividadService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> listarPorActividad(Long actividadId) {
        return inscripcionRepository.findByActividad_IdAndEstado(actividadId, EstadoInscripcion.ACTIVA).stream()
                .map(inscripcionMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> listarPorSocio(Long socioId) {
        return inscripcionRepository.findBySocio_Id(socioId).stream()
                .map(inscripcionMapper::toDTO)
                .toList();
    }

    @Override
    public InscripcionDTO inscribirSocio(Long socioId, Long actividadId) {
        Socio socio = socioService.obtenerEntidadPorId(socioId);
        Actividad actividad = actividadService.obtenerEntidadPorId(actividadId);

        // BUG CORREGIDO: antes de este chequeo, un mismo socio podia
        // inscribirse repetidas veces a la misma actividad (cada clic en
        // "Inscribir" creaba una fila nueva), generando duplicados e
        // inflando el conteo de cupos ocupados. Se valida primero esto,
        // porque es una regla mas especifica que la de cupo disponible.
        boolean yaInscripto = inscripcionRepository.existsBySocio_IdAndActividad_IdAndEstado(
                socioId, actividadId, EstadoInscripcion.ACTIVA);
        if (yaInscripto) {
            throw new SocioYaInscriptoException(socio.getNombre() + " " + socio.getApellido(), actividad.getNombre());
        }

        long inscriptosActivos = actividadService.contarInscriptosActivos(actividadId);
        if (inscriptosActivos >= actividad.getCupos()) {
            throw new CupoExcedidoException(actividad.getNombre());
        }

        Inscripcion inscripcion = Inscripcion.builder()
                .socio(socio)
                .actividad(actividad)
                .fechaInscripcion(LocalDate.now())
                .estado(EstadoInscripcion.ACTIVA)
                .build();
        return inscripcionMapper.toDTO(inscripcionRepository.save(inscripcion));
    }

    @Override
    public void cancelarInscripcion(Long inscripcionId) {
        Inscripcion inscripcion = inscripcionRepository.findById(inscripcionId)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripcion no encontrada, id: " + inscripcionId));
        inscripcion.setEstado(EstadoInscripcion.CANCELADA);
        inscripcionRepository.save(inscripcion);
    }
}
