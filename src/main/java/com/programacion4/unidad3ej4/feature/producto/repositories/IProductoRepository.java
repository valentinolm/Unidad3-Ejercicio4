package com.programacion4.unidad3ej4.feature.producto.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.programacion4.unidad3ej4.feature.producto.models.Producto;

@Repository
public interface IProductoRepository extends CrudRepository<Producto, Long> {

    boolean existsByNombreIgnoreCaseAndEstaEliminadoFalse(String nombre);
    java.util.List<Producto> findAllByEstaEliminadoFalse();
    java.util.Optional<Producto> findByIdAndEstaEliminadoFalse(Long id);
}
