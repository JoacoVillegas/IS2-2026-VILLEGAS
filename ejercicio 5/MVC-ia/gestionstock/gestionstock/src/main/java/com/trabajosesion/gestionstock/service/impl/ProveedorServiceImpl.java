package com.trabajosesion.gestionstock.service.impl;

import com.trabajosesion.gestionstock.dao.ProveedorDAO;
import com.trabajosesion.gestionstock.dto.ProveedorDTO;
import com.trabajosesion.gestionstock.mapper.ProveedorMapper;
import com.trabajosesion.gestionstock.model.Proveedor;
import com.trabajosesion.gestionstock.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/** SERVICE - Implementacion de la logica de negocio de Proveedor (mismo patron DTO<->Entity que ProductoServiceImpl). */
@Service
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorDAO proveedorDAO;
    private final ProveedorMapper proveedorMapper;

    @Autowired
    public ProveedorServiceImpl(ProveedorDAO proveedorDAO, ProveedorMapper proveedorMapper) {
        this.proveedorDAO = proveedorDAO;
        this.proveedorMapper = proveedorMapper;
    }

    @Override
    public List<ProveedorDTO> listarActivos() {
        return proveedorDAO.findByEliminadoFalse().stream().map(proveedorMapper::toDTO).toList();
    }

    @Override
    public Optional<ProveedorDTO> buscarPorId(Long id) {
        return proveedorDAO.findById(id).map(proveedorMapper::toDTO);
    }

    @Override
    @Transactional
    public ProveedorDTO registrar(ProveedorDTO dto) {
        Proveedor proveedor = proveedorMapper.toEntity(dto);
        return proveedorMapper.toDTO(proveedorDAO.save(proveedor));
    }

    @Override
    @Transactional
    public ProveedorDTO editar(Long id, ProveedorDTO dto) {
        Proveedor proveedor = proveedorDAO.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe el proveedor con id " + id));
        proveedorMapper.actualizarEntity(proveedor, dto);
        return proveedorMapper.toDTO(proveedorDAO.save(proveedor));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        proveedorDAO.findById(id).ifPresent(proveedor -> {
            proveedor.setEliminado(true);
            proveedorDAO.save(proveedor);
        });
    }
}
