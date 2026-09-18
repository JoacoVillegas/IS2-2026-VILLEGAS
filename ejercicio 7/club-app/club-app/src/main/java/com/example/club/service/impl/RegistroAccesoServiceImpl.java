package com.example.club.service.impl;

import com.example.club.dto.RegistroAccesoDTO;
import com.example.club.entity.Persona;
import com.example.club.entity.RegistroAcceso;
import com.example.club.exception.ResourceNotFoundException;
import com.example.club.mapper.RegistroAccesoMapper;
import com.example.club.repository.RegistroAccesoRepository;
import com.example.club.service.PersonaService;
import com.example.club.service.RegistroAccesoService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegistroAccesoServiceImpl implements RegistroAccesoService {

    private final RegistroAccesoRepository registroAccesoRepository;
    private final RegistroAccesoMapper registroAccesoMapper;
    // Service->Service: para obtener la entidad Persona se consulta a
    // PersonaService, nunca a PersonaRepository directamente.
    private final PersonaService personaService;

    public RegistroAccesoServiceImpl(RegistroAccesoRepository registroAccesoRepository,
                                      RegistroAccesoMapper registroAccesoMapper,
                                      PersonaService personaService) {
        this.registroAccesoRepository = registroAccesoRepository;
        this.registroAccesoMapper = registroAccesoMapper;
        this.personaService = personaService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistroAccesoDTO> listarTodos() {
        return registroAccesoRepository.findAllByOrderByFechaDescHoraEntradaDesc().stream()
                .map(registroAccesoMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistroAccesoDTO> listarPorPersona(Long personaId) {
        return registroAccesoRepository.findByPersona_IdOrderByFechaDesc(personaId).stream()
                .map(registroAccesoMapper::toDTO)
                .toList();
    }

    @Override
    public RegistroAccesoDTO registrarEntrada(Long personaId) {
        Persona persona = personaService.obtenerEntidadPorId(personaId);

        // Regla de negocio: no permitir una nueva entrada si la persona ya
        // tiene un registro abierto (sin salida) -- evita duplicar accesos.
        List<RegistroAcceso> abiertos = registroAccesoRepository.findByPersona_IdAndHoraSalidaIsNull(personaId);
        if (!abiertos.isEmpty()) {
            throw new IllegalStateException("Esta persona ya tiene una entrada registrada sin salida");
        }

        RegistroAcceso registro = RegistroAcceso.builder()
                .persona(persona)
                .fecha(LocalDate.now())
                .horaEntrada(LocalTime.now())
                .build();
        return registroAccesoMapper.toDTO(registroAccesoRepository.save(registro));
    }

    @Override
    public RegistroAccesoDTO registrarSalida(Long registroId) {
        RegistroAcceso registro = registroAccesoRepository.findById(registroId)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de acceso no encontrado, id: " + registroId));
        // BUG CORREGIDO (revision adicional): antes no se validaba nada
        // aqui, asi que hacer doble clic en "Registrar salida" (o volver a
        // enviar el POST) pisaba la hora de salida ya guardada con la hora
        // actual, sin ningun aviso.
        if (registro.getHoraSalida() != null) {
            throw new IllegalStateException("Este registro ya tiene una salida registrada");
        }
        registro.setHoraSalida(LocalTime.now());
        return registroAccesoMapper.toDTO(registroAccesoRepository.save(registro));
    }
}
