package com.programacion4.unidad3ej4.feature.producto.services.interfaces.domain;

import com.programacion4.unidad3ej4.feature.producto.dtos.request.ProductoCreateRequestDto;
import com.programacion4.unidad3ej4.feature.producto.dtos.request.ProductoUpdateRequestDto;
import com.programacion4.unidad3ej4.feature.producto.dtos.response.ProductoResponseDto;
import java.util.List;

public interface IProductoCreateService {
    
    ProductoResponseDto create(ProductoCreateRequestDto dto);
    List<ProductoResponseDto> findAll();
    ProductoResponseDto findById(Long id);
    ProductoResponseDto update(Long id, ProductoUpdateRequestDto dto);
    ProductoResponseDto patch(Long id, ProductoUpdateRequestDto dto);
    void delete(Long id);
    
}