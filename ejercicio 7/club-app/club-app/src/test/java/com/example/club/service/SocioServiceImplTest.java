package com.example.club.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.club.dto.SocioDTO;
import com.example.club.entity.EstadoSocio;
import com.example.club.entity.GrupoFamiliar;
import com.example.club.entity.Socio;
import com.example.club.mapper.SocioMapper;
import com.example.club.repository.GrupoFamiliarRepository;
import com.example.club.repository.SocioRepository;
import com.example.club.service.impl.SocioServiceImpl;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Prueba unitaria adicional (no exigida explicitamente por el enunciado,
 * pero complementaria a las de PagoServiceImplTest): verifica que al
 * registrar un Socio se cree automaticamente su GrupoFamiliar titular
 * (composicion, ver analisis de diseño).
 */
@ExtendWith(MockitoExtension.class)
class SocioServiceImplTest {

    @Mock
    private SocioRepository socioRepository;

    @Mock
    private GrupoFamiliarRepository grupoFamiliarRepository;

    @Mock
    private SocioMapper socioMapper;

    @InjectMocks
    private SocioServiceImpl socioService;

    @Test
    void registrarSocio_creaAutomaticamenteSuGrupoFamiliarTitular() {
        SocioDTO dto = SocioDTO.builder()
                .nombre("Lucia")
                .apellido("Diaz")
                .fechaNacimiento(LocalDate.of(1995, 6, 20))
                .fechaAlta(LocalDate.now())
                .estado(EstadoSocio.ACTIVO)
                .build();

        Socio guardado = Socio.builder().id(10L).nombre("Lucia").apellido("Diaz").estado(EstadoSocio.ACTIVO).build();
        when(socioRepository.save(any(Socio.class))).thenReturn(guardado);
        when(grupoFamiliarRepository.save(any(GrupoFamiliar.class))).thenAnswer(inv -> inv.getArgument(0));
        when(socioMapper.toDTO(any(Socio.class), any())).thenReturn(dto);

        socioService.registrar(dto);

        verify(socioRepository, times(1)).save(any(Socio.class));
        verify(grupoFamiliarRepository, times(1)).save(any(GrupoFamiliar.class));
    }
}
