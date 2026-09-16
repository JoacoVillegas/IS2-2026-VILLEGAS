package com.example.colegio.service.impl;

import com.example.colegio.dto.ProfesorDTO;
import com.example.colegio.entity.Profesor;
import com.example.colegio.entity.Role;
import com.example.colegio.entity.Usuario;
import com.example.colegio.exception.EmailYaRegistradoException;
import com.example.colegio.exception.ResourceNotFoundException;
import com.example.colegio.mapper.ProfesorMapper;
import com.example.colegio.repository.ProfesorRepository;
import com.example.colegio.service.EmailService;
import com.example.colegio.service.ProfesorService;
import com.example.colegio.service.UsuarioService;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Transactional a nivel de clase: TODOS los metodos publicos de esta clase
 * se ejecutan dentro de una transaccion (salvo que un metodo puntual la
 * sobrescriba). Es especialmente importante en "registrar()", donde se
 * guarda un Usuario+Profesor Y se dispara el envio de correo: si algo falla
 * antes de terminar, no debe quedar un Profesor a medio crear en la base.
 */
@Service
@Transactional
public class ProfesorServiceImpl implements ProfesorService {

    private final ProfesorRepository profesorRepository;
    private final ProfesorMapper profesorMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UsuarioService usuarioService;

    public ProfesorServiceImpl(ProfesorRepository profesorRepository,
                                ProfesorMapper profesorMapper,
                                PasswordEncoder passwordEncoder,
                                EmailService emailService,
                                UsuarioService usuarioService) {
        this.profesorRepository = profesorRepository;
        this.profesorMapper = profesorMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.usuarioService = usuarioService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfesorDTO> listarActivos() {
        return profesorRepository.findByEliminadoFalse().stream()
                .map(profesorMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProfesorDTO buscarPorId(Long id) {
        return profesorMapper.toDTO(obtenerEntidadActivaPorId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ProfesorDTO buscarPorEmail(String email) {
        Profesor profesor = profesorRepository.findByUsuario_Email(email)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado para el correo: " + email));
        return profesorMapper.toDTO(profesor);
    }

    @Override
    public ProfesorDTO registrar(ProfesorDTO dto) {
        // Regla de negocio (punto 9 del enunciado): el correo debe ser
        // unico. Se valida ANTES de tocar la base de datos, delegando en
        // UsuarioService (nunca se llama directamente a UsuarioRepository
        // desde este Service, respetando la regla de arquitectura).
        if (usuarioService.existeEmail(dto.getCorreo())) {
            throw new EmailYaRegistradoException(dto.getCorreo());
        }

        // 1) Se crea la identidad de acceso (Usuario) con la contraseña
        //    cifrada mediante BCrypt. NUNCA se guarda dto.getPasswordInicial()
        //    tal cual: passwordEncoder.encode() la transforma en un hash
        //    irreversible antes de llegar a la base de datos.
        Usuario usuario = Usuario.builder()
                .email(dto.getCorreo())
                .passwordHash(passwordEncoder.encode(dto.getPasswordInicial()))
                .role(Role.PROFESOR)
                .habilitado(true)
                .build();

        // 2) Se arma la entidad Profesor a partir del DTO.
        Profesor profesor = Profesor.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .sexo(dto.getSexo())
                .fechaNacimiento(dto.getFechaNacimiento())
                .especialidad(dto.getEspecialidad())
                .eliminado(false)
                .usuario(usuario)
                .build();

        // cascade = ALL en Profesor.usuario (ver Profesor.java) hace que, al
        // guardar el Profesor, Hibernate guarde tambien el Usuario asociado
        // en una unica operacion/transaccion.
        Profesor guardado = profesorRepository.save(profesor);

        // 3) Envio de correo de bienvenida. Se delega en EmailService (nunca
        // se usa JavaMailSender directamente aqui): requisito explicito del
        // enunciado, punto 10.
        emailService.enviarBienvenidaProfesor(guardado.getUsuario().getEmail(),
                guardado.getNombre() + " " + guardado.getApellido());

        return profesorMapper.toDTO(guardado);
    }

    @Override
    public ProfesorDTO actualizar(Long id, ProfesorDTO dto) {
        Profesor profesor = obtenerEntidadActivaPorId(id);
        // El mapper NO toca el correo/contraseña: esos campos de Usuario se
        // gestionan por otro flujo (cambio de contraseña) y el correo, al
        // ser el username de login, se considera inmutable una vez creado
        // el usuario (decision de diseno adicional para simplificar el
        // ejercicio y evitar romper la sesion activa del docente).
        profesorMapper.actualizarEntidadDesdeDTO(profesor, dto);
        return profesorMapper.toDTO(profesorRepository.save(profesor));
    }

    @Override
    public void eliminar(Long id) {
        Profesor profesor = obtenerEntidadActivaPorId(id);
        // Baja logica: se marca eliminado=true (tanto en Profesor como se
        // deshabilita su Usuario) en lugar de deleteById(), preservando el
        // historial de auditoria y evitando romper Notas/DictadoClases que
        // referencian a este profesor.
        profesor.setEliminado(true);
        profesor.getUsuario().setHabilitado(false);
        profesorRepository.save(profesor);
    }

    @Override
    @Transactional(readOnly = true)
    public Profesor obtenerEntidadPorId(Long id) {
        return obtenerEntidadActivaPorId(id);
    }

    /** Metodo privado de apoyo: evita repetir el findById+orElseThrow en cada metodo publico. */
    private Profesor obtenerEntidadActivaPorId(Long id) {
        Profesor profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado, id: " + id));
        if (profesor.isEliminado()) {
            throw new ResourceNotFoundException("El profesor con id " + id + " fue dado de baja");
        }
        return profesor;
    }
}
