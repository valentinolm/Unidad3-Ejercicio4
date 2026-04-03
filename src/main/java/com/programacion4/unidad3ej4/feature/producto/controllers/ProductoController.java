package com.programacion4.unidad3ej4.feature.producto.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programacion4.unidad3ej4.config.BaseResponse;
import com.programacion4.unidad3ej4.feature.producto.dtos.request.ProductoUpdateRequestDto;
import com.programacion4.unidad3ej4.feature.producto.dtos.response.ProductoResponseDto;
import com.programacion4.unidad3ej4.feature.producto.services.interfaces.domain.IProductoCreateService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/productos")
@AllArgsConstructor
public class ProductoController {

    private final IProductoCreateService productoService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<ProductoResponseDto>>> findAll() {
        return ResponseEntity.ok(
                BaseResponse.ok(productoService.findAll(), "Listado de productos"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ProductoResponseDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                BaseResponse.ok(productoService.findById(id), "Producto encontrado"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<ProductoResponseDto>> update(
            @PathVariable Long id,
            @RequestBody ProductoUpdateRequestDto dto) {
        return ResponseEntity.ok(
                BaseResponse.ok(productoService.update(id, dto), "Producto actualizado"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<ProductoResponseDto>> patch(
            @PathVariable Long id,
            @RequestBody ProductoUpdateRequestDto dto) {
        return ResponseEntity.ok(
                BaseResponse.ok(productoService.patch(id, dto), "Producto actualizado parcialmente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}