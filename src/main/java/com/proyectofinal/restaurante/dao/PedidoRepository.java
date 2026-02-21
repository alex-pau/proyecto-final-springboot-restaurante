package com.proyectofinal.restaurante.dao;

import com.proyectofinal.restaurante.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
