package com.example.club.mapper;

import com.example.club.dto.PagoDTO;
import com.example.club.entity.Pago;
import org.springframework.stereotype.Component;

@Component
public class PagoMapper {

    public PagoDTO toDTO(Pago pago) {
        if (pago == null) {
            return null;
        }
        String descripcion = "Familia de " + pago.getGrupoFamiliar().getSocioTitular().getNombre()
                + " " + pago.getGrupoFamiliar().getSocioTitular().getApellido();
        return PagoDTO.builder()
                .id(pago.getId())
                .grupoFamiliarId(pago.getGrupoFamiliar().getId())
                .fecha(pago.getFecha())
                .periodo(pago.getPeriodo())
                .importe(pago.getImporte())
                .medioPago(pago.getMedioPago())
                .comprobante(pago.getComprobante())
                .estado(pago.getEstado().name())
                .familiaDescripcion(descripcion)
                .build();
    }
}
