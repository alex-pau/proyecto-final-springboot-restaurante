package com.proyectofinal.restaurante.service;

import com.proyectofinal.restaurante.dao.PedidoRepository;
import com.proyectofinal.restaurante.entity.Pedido;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PedidoService {

    @Autowired
    PedidoRepository pedidoRepository;

    public List<Pedido> list() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> getOne(long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> getByClienteId(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public Optional<Pedido> getPedidoAbierto(long id) {
        return pedidoRepository.findByIdAndCerradoFalse(id);
    }

    public void save(Pedido pedido) {
        pedidoRepository.save(pedido);
    }

    public void delete(long id) {
        pedidoRepository.deleteById(id);
    }

    public boolean existsById(long id) {
        return pedidoRepository.existsById(id);
    }
}