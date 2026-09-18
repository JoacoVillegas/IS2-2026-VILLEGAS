package com.example.club.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.club.dto.PagoDTO;
import com.example.club.entity.EstadoPago;
import com.example.club.entity.GrupoFamiliar;
import com.example.club.entity.MedioPago;
import com.example.club.entity.Pago;
import com.example.club.entity.Socio;
import com.example.club.exception.ResourceNotFoundException;
import com.example.club.mapper.PagoMapper;
import com.example.club.repository.PagoRepository;
import com.example.club.service.impl.PagoServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * QUE ES UNA PRUEBA UNITARIA: verifica el comportamiento de UNA SOLA unidad
 * de codigo (aqui, PagoServiceImpl) de forma AISLADA del resto del sistema
 * (base de datos real, otros Services reales, HTTP). Se logra "aislando"
 * las dependencias con MOCKS.
 *
 * QUE ES UN MOCK: un objeto "doble" que simula el comportamiento de una
 * dependencia real (aqui, PagoRepository y GrupoFamiliarService) sin
 * ejecutar su logica real ni tocar una base de datos. Se le indica con
 * when(...).thenReturn(...) que responder ante cada llamada.
 *
 * POR QUE MOCKITO: permite crear estos dobles y verificar interacciones
 * (verify(...)) de forma declarativa, sin escribir clases falsas a mano.
 *
 * QUE PARTE DEL SISTEMA SE PRUEBA: unicamente la logica de negocio de
 * PagoServiceImpl (validaciones, orquestacion con GrupoFamiliarService,
 * mapeo Entity<->DTO), NO la base de datos real ni los Controllers.
 *
 * @ExtendWith(MockitoExtension.class): habilita las anotaciones @Mock e
 * @InjectMocks de Mockito dentro de un test JUnit 5.
 */
@ExtendWith(MockitoExtension.class)
class PagoServiceImplTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private PagoMapper pagoMapper;

    @Mock
    private GrupoFamiliarService grupoFamiliarService;

    @InjectMocks
    private PagoServiceImpl pagoService;

    private GrupoFamiliar familiaDeEjemplo;

    @BeforeEach
    void setUp() {
        Socio titular = Socio.builder().nombre("Juan").apellido("Perez").build();
        familiaDeEjemplo = GrupoFamiliar.builder().id(1L).socioTitular(titular).eliminado(false).build();
    }

    private PagoDTO dtoValido(MedioPago medio) {
        return PagoDTO.builder()
                .grupoFamiliarId(1L)
                .fecha(LocalDate.of(2026, 9, 1))
                .periodo("2026-09")
                .importe(new BigDecimal("15000.00"))
                .medioPago(medio)
                .build();
    }

    // ---------- 1. Registrar pago correctamente ----------
    @Test
    void registrarPago_conDatosValidos_loGuardaCorrectamente() {
        PagoDTO dto = dtoValido(MedioPago.EFECTIVO);
        when(grupoFamiliarService.obtenerEntidadPorId(1L)).thenReturn(familiaDeEjemplo);
        when(pagoRepository.save(any(Pago.class))).thenAnswer(inv -> inv.getArgument(0));
        when(pagoMapper.toDTO(any(Pago.class))).thenReturn(dto);

        PagoDTO resultado = pagoService.registrar(dto);

        assertThat(resultado).isNotNull();
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    // ---------- 2. Rechazar un pago con importe invalido ----------
    @Test
    void pagoDTO_conImporteNegativoOCero_esRechazadoPorValidacion() {
        // Esta prueba NO llama a PagoServiceImpl: valida que las anotaciones
        // Jakarta Validation del DTO (@Positive en PagoDTO.importe) rechacen
        // un importe invalido ANTES de que el dato llegue al Service -- que
        // es exactamente donde Spring MVC aplica @Valid en el Controller.
        PagoDTO dtoInvalido = dtoValido(MedioPago.EFECTIVO);
        dtoInvalido.setImporte(new BigDecimal("-100.00"));

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<PagoDTO>> violaciones = validator.validate(dtoInvalido);
            assertThat(violaciones).isNotEmpty();
        }
    }

    // ---------- 3. Rechazar una familia inexistente ----------
    @Test
    void registrarPago_conFamiliaInexistente_lanzaExcepcion() {
        PagoDTO dto = dtoValido(MedioPago.EFECTIVO);
        dto.setGrupoFamiliarId(999L);
        when(grupoFamiliarService.obtenerEntidadPorId(999L))
                .thenThrow(new ResourceNotFoundException("Grupo familiar no encontrado, id: 999"));

        assertThatThrownBy(() -> pagoService.registrar(dto))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ---------- 4. Registrar pago en efectivo ----------
    @Test
    void registrarPago_medioEfectivo_seGuardaConEseMedio() {
        PagoDTO dto = dtoValido(MedioPago.EFECTIVO);
        when(grupoFamiliarService.obtenerEntidadPorId(1L)).thenReturn(familiaDeEjemplo);
        when(pagoRepository.save(any(Pago.class))).thenAnswer(inv -> {
            Pago p = inv.getArgument(0);
            assertThat(p.getMedioPago()).isEqualTo(MedioPago.EFECTIVO);
            assertThat(p.getEstado()).isEqualTo(EstadoPago.REGISTRADO);
            return p;
        });
        when(pagoMapper.toDTO(any(Pago.class))).thenReturn(dto);

        pagoService.registrar(dto);
        verify(pagoRepository).save(any(Pago.class));
    }

    // ---------- 5. Registrar pago mediante transferencia ----------
    @Test
    void registrarPago_medioTransferencia_seGuardaConEseMedio() {
        PagoDTO dto = dtoValido(MedioPago.TRANSFERENCIA);
        when(grupoFamiliarService.obtenerEntidadPorId(1L)).thenReturn(familiaDeEjemplo);
        when(pagoRepository.save(any(Pago.class))).thenAnswer(inv -> {
            Pago p = inv.getArgument(0);
            assertThat(p.getMedioPago()).isEqualTo(MedioPago.TRANSFERENCIA);
            return p;
        });
        when(pagoMapper.toDTO(any(Pago.class))).thenReturn(dto);

        pagoService.registrar(dto);
        verify(pagoRepository).save(any(Pago.class));
    }

    // ---------- 6. Registrar pago mediante Mercado Pago (SOLO como medio registrado) ----------
    @Test
    void registrarPago_medioMercadoPago_soloRegistraLaCategoria_sinLlamarNingunaApiExterna() {
        // IMPORTANTE: esta prueba comprueba UNICAMENTE que el sistema permite
        // guardar medioPago = MERCADO_PAGO como un dato mas. No existe, ni
        // debe existir, ninguna llamada HTTP/SDK a servicios de Mercado Pago
        // en todo el proyecto (ver MedioPago.java).
        PagoDTO dto = dtoValido(MedioPago.MERCADO_PAGO);
        when(grupoFamiliarService.obtenerEntidadPorId(1L)).thenReturn(familiaDeEjemplo);
        when(pagoRepository.save(any(Pago.class))).thenAnswer(inv -> {
            Pago p = inv.getArgument(0);
            assertThat(p.getMedioPago()).isEqualTo(MedioPago.MERCADO_PAGO);
            return p;
        });
        when(pagoMapper.toDTO(any(Pago.class))).thenReturn(dto);

        PagoDTO resultado = pagoService.registrar(dto);

        assertThat(resultado.getMedioPago()).isEqualTo(MedioPago.MERCADO_PAGO);
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    // ---------- 7. Consultar pagos de una familia ----------
    @Test
    void listarPorFamilia_conPagosExistentes_devuelveLaListaMapeada() {
        Pago pago = mock(Pago.class);
        when(pagoRepository.findByGrupoFamiliar_IdOrderByFechaDesc(1L)).thenReturn(List.of(pago));
        when(pagoMapper.toDTO(pago)).thenReturn(dtoValido(MedioPago.EFECTIVO));

        List<PagoDTO> resultado = pagoService.listarPorFamilia(1L);

        assertThat(resultado).hasSize(1);
    }

    // ---------- 8. Manejar correctamente una familia sin pagos ----------
    @Test
    void listarPorFamilia_sinPagos_devuelveListaVaciaSinError() {
        when(pagoRepository.findByGrupoFamiliar_IdOrderByFechaDesc(2L)).thenReturn(List.of());

        List<PagoDTO> resultado = pagoService.listarPorFamilia(2L);

        assertThat(resultado).isEmpty();
    }
}
