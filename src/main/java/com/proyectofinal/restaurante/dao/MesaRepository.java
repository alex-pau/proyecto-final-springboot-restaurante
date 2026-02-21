package com.proyectofinal.restaurante.dao;

import com.proyectofinal.restaurante.entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface MesaRepository extends JpaRepository<Mesa, Long> {
}