package com.proyectofinal.restaurante.dao;

import com.proyectofinal.restaurante.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
}