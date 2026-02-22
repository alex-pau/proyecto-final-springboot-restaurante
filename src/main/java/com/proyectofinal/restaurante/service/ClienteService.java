package com.proyectofinal.restaurante.service;

import com.proyectofinal.restaurante.dao.ClienteRepository;
import com.proyectofinal.restaurante.entity.Cliente;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {

    @Autowired
    ClienteRepository clienteRepository;

    public List<Cliente> list() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> getOne(long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> getByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public void save(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    public void delete(long id) {
        clienteRepository.deleteById(id);
    }

    public boolean existsById(long id) {
        return clienteRepository.existsById(id);
    }

    public boolean existsByEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }
}