package com.proyectofinal.restaurante.dao;

import com.proyectofinal.restaurante.entity.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    List<DetallePedido> findByPedidoId(Long pedidoId);
    //para comprobar si el plato se usa en algun pedido
    boolean existsByPlatoId(Long platoId);

}