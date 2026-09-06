package com.trabajosesion.gestionstock.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO de ENTRADA para el caso de uso "Registrar orden de compra"
 * (pantalla "Realizar compra" del prototipo). Agrupa el proveedor elegido
 * y la lista de lineas (ItemOrdenDTO). OrdenCompraController arma este
 * DTO a partir de los arrays paralelos que envia el formulario Thymeleaf
 * (productoId[], cantidad[], precioUnitario[]) y se lo pasa completo a
 * OrdenCompraService.registrarOrden(...): el Service nunca recibe HTML,
 * request params sueltos ni nada especifico de Spring MVC, solo este DTO.
 */
public class NuevaOrdenDTO {

    private Long idProveedor;
    private List<ItemOrdenDTO> items = new ArrayList<>();

    public NuevaOrdenDTO() {
    }

    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public List<ItemOrdenDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemOrdenDTO> items) {
        this.items = items;
    }
}
