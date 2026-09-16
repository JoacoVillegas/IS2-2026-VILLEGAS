package com.example.colegio.mapper;

import com.example.colegio.dto.ProfesorDTO;
import com.example.colegio.entity.Profesor;
import org.springframework.stereotype.Component;

/**
 * Mapper = clase responsable de convertir Entity <-> DTO. Se implementa "a
 * mano" (sin MapStruct/ModelMapper) para que la conversion quede explicita y
 * facil de leer con fines academicos. @Component permite inyectarlo con el
 * resto de las dependencias (constructor injection) en ProfesorServiceImpl.
 */
@Component
public class ProfesorMapper {

    /** Entity -> DTO: se usa al DEVOLVER datos desde el Service hacia el Controller/vista. */
    public ProfesorDTO toDTO(Profesor profesor) {
        if (profesor == null) {
            return null;
        }
        return ProfesorDTO.builder()
                .id(profesor.getId())
                .nombre(profesor.getNombre())
                .apellido(profesor.getApellido())
                .sexo(profesor.getSexo())
                .fechaNacimiento(profesor.getFechaNacimiento())
                .especialidad(profesor.getEspecialidad())
                .correo(profesor.getUsuario() != null ? profesor.getUsuario().getEmail() : null)
                .eliminado(profesor.isEliminado())
                .build();
        // No se mapea el password: la entidad Usuario nunca expone el hash hacia el DTO.
    }

    /**
     * DTO -> Entity: solo copia los campos "propios" de Profesor. La
     * asociacion con Usuario (email/password/rol) la arma ProfesorServiceImpl,
     * porque requiere logica adicional (BCrypt, verificar unicidad de email)
     * que no le corresponde a un mapper "tonto".
     */
    public void actualizarEntidadDesdeDTO(Profesor profesor, ProfesorDTO dto) {
        profesor.setNombre(dto.getNombre());
        profesor.setApellido(dto.getApellido());
        profesor.setSexo(dto.getSexo());
        profesor.setFechaNacimiento(dto.getFechaNacimiento());
        profesor.setEspecialidad(dto.getEspecialidad());
    }
}
