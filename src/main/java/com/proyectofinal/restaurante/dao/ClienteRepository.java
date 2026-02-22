package com.proyectofinal.restaurante.dao;

import com.proyectofinal.restaurante.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
    boolean existsByEmail(String email);
}