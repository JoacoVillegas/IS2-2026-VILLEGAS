package com.trabajosesion.gestionstock.mapper;

import com.trabajosesion.gestionstock.dto.ProveedorDTO;
import com.trabajosesion.gestionstock.model.Proveedor;
import org.springframework.stereotype.Component;

/** MAPPER de Proveedor. Misma responsabilidad que ProductoMapper. */
@Component
public class ProveedorMapper {

    public ProveedorDTO toDTO(Proveedor proveedor) {
        ProveedorDTO dto = new ProveedorDTO();
        dto.setIdProveedor(proveedor.getIdProveedor());
        dto.setRazonSocial(proveedor.getRazonSocial());
        dto.setCuit(proveedor.getCuit());
        dto.setRubro(proveedor.getRubro());
        dto.setTelefono(proveedor.getTelefono());
        dto.setDireccion(proveedor.getDireccion());
        dto.setCorreo(proveedor.getCorreo());
        return dto;
    }

    public Proveedor toEntity(ProveedorDTO dto) {
        Proveedor proveedor = new Proveedor();
        actualizarEntity(proveedor, dto);
        return proveedor;
    }

    public void actualizarEntity(Proveedor proveedor, ProveedorDTO dto) {
        proveedor.setRazonSocial(dto.getRazonSocial());
        proveedor.setCuit(dto.getCuit());
        proveedor.setRubro(dto.getRubro());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setCorreo(dto.getCorreo());
    }
}
