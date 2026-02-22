package com.proyectofinal.restaurante.service;

import com.proyectofinal.restaurante.dao.DetallePedidoRepository;
import com.proyectofinal.restaurante.entity.DetallePedido;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DetallePedidoService {

    @Autowired
    DetallePedidoRepository detallePedidoRepository;

    public List<DetallePedido> list() {
        return detallePedidoRepository.findAll();
    }

    public Optional<DetallePedido> getOne(long id) {
        return detallePedidoRepository.findById(id);
    }

    public void save(DetallePedido detallePedido) {
        detallePedidoRepository.save(detallePedido);
    }

    public void delete(long id) {
        detallePedidoRepository.deleteById(id);
    }

    public boolean existsById(long id) {
        return detallePedidoRepository.existsById(id);
    }

    public List<DetallePedido> getByPedidoId(Long pedidoId) {
        return detallePedidoRepository.findByPedidoId(pedidoId);
    }
}