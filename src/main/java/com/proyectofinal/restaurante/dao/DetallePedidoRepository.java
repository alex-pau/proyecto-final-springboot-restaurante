package com.proyectofinal.restaurante.dao;

import com.proyectofinal.restaurante.entity.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
}