package com.proyectofinal.restaurante.dao;

import com.proyectofinal.restaurante.entity.Plato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface PlatoRepository extends JpaRepository<Plato, Long> {
    Optional<Plato> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}