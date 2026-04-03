package com.programacion4.unidad3ej4.feature.producto.services.impl.domain;

import java.util.List;

import org.springframework.stereotype.Service;

import com.programacion4.unidad3ej4.config.exceptions.BadRequestException;
import com.programacion4.unidad3ej4.config.exceptions.ResourceConflictException;
import com.programacion4.unidad3ej4.config.exceptions.ResourceNotFoundException;
import com.programacion4.unidad3ej4.feature.producto.dtos.request.ProductoCreateRequestDto;
import com.programacion4.unidad3ej4.feature.producto.dtos.request.ProductoUpdateRequestDto;
import com.programacion4.unidad3ej4.feature.producto.dtos.response.ProductoResponseDto;
import com.programacion4.unidad3ej4.feature.producto.mappers.ProductoMapper;
import com.programacion4.unidad3ej4.feature.producto.models.Categoria;
import com.programacion4.unidad3ej4.feature.producto.models.Producto;
import com.programacion4.unidad3ej4.feature.producto.repositories.ICategoriaRepository;
import com.programacion4.unidad3ej4.feature.producto.repositories.IProductoRepository;
import com.programacion4.unidad3ej4.feature.producto.services.interfaces.commons.IProductoExistByNameService;
import com.programacion4.unidad3ej4.feature.producto.services.interfaces.domain.IProductoCreateService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductoCreateService implements IProductoCreateService {

    private final IProductoExistByNameService productoExistByNameService;
    private final IProductoRepository productoRepository;
    private final ICategoriaRepository categoriaRepository;

    @Override
    public ProductoResponseDto create(ProductoCreateRequestDto dto) {
        if (productoExistByNameService.existByName(dto.getNombre())) {
            throw new ResourceConflictException("El nombre del producto ya existe");
        }

        Categoria categoria = findCategoriaById(dto.getCategoriaId());

        Producto productoAGuardar = ProductoMapper.toEntity(dto, categoria);
        productoAGuardar.setNombre(capitalizeText(dto.getNombre()));
        productoAGuardar.setDescripcion(capitalizeText(dto.getDescripcion()));
        productoAGuardar.setEstaEliminado(false);

        Producto productoGuardado = productoRepository.save(productoAGuardar);

        return ProductoMapper.toResponseDto(productoGuardado);
    }

    @Override
    public List<ProductoResponseDto> findAll() {
        return productoRepository.findAllByEstaEliminadoFalse()
                .stream()
                .map(ProductoMapper::toResponseDto)
                .toList();
    }

    @Override
    public ProductoResponseDto findById(Long id) {
        Producto producto = findProductoById(id);
        return ProductoMapper.toResponseDto(producto);
    }

    @Override
    public ProductoResponseDto update(Long id, ProductoUpdateRequestDto dto) {
        validateUpdateDto(dto);

        Producto producto = findProductoById(id);

        if (!producto.getNombre().equalsIgnoreCase(dto.getNombre())
                && productoExistByNameService.existByName(dto.getNombre())) {
            throw new ResourceConflictException("El nombre del producto ya existe");
        }

        Categoria categoria = findCategoriaById(dto.getCategoriaId());

        producto.setNombre(capitalizeText(dto.getNombre()));
        producto.setCodigo(dto.getCodigo());
        producto.setDescripcion(capitalizeText(dto.getDescripcion()));
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setCategoria(categoria);

        Producto actualizado = productoRepository.save(producto);
        return ProductoMapper.toResponseDto(actualizado);
    }

    @Override
    public ProductoResponseDto patch(Long id, ProductoUpdateRequestDto dto) {
        Producto producto = findProductoById(id);

        if (dto.getNombre() != null) {
            if (!producto.getNombre().equalsIgnoreCase(dto.getNombre())
                    && productoExistByNameService.existByName(dto.getNombre())) {
                throw new ResourceConflictException("El nombre del producto ya existe");
            }
            producto.setNombre(capitalizeText(dto.getNombre()));
        }

        if (dto.getCodigo() != null) {
            producto.setCodigo(dto.getCodigo());
        }

        if (dto.getDescripcion() != null) {
            producto.setDescripcion(capitalizeText(dto.getDescripcion()));
        }

        if (dto.getPrecio() != null) {
            producto.setPrecio(dto.getPrecio());
        }

        if (dto.getStock() != null) {
            producto.setStock(dto.getStock());
        }

        if (dto.getCategoriaId() != null) {
            Categoria categoria = findCategoriaById(dto.getCategoriaId());
            producto.setCategoria(categoria);
        }

        Producto actualizado = productoRepository.save(producto);
        return ProductoMapper.toResponseDto(actualizado);
    }

    @Override
    public void delete(Long id) {
        Producto producto = findProductoById(id);
        producto.setEstaEliminado(true);
        productoRepository.save(producto);
    }

    private Producto findProductoById(Long id) {
        return productoRepository.findByIdAndEstaEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
    }

    private Categoria findCategoriaById(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada"));
    }

    private void validateUpdateDto(ProductoUpdateRequestDto dto) {
        if (dto.getNombre() == null || dto.getCodigo() == null || dto.getDescripcion() == null
                || dto.getPrecio() == null || dto.getStock() == null || dto.getCategoriaId() == null) {
            throw new BadRequestException("Todos los campos son obligatorios");
        }
    }

    private String capitalizeText(String text) {
        if (text == null || text.isBlank()) {
            return text;
        }

        String normalized = text.trim().toLowerCase();
        return Character.toUpperCase(normalized.charAt(0)) + normalized.substring(1);
    }
}