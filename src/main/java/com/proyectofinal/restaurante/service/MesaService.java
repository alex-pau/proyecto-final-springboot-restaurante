package com.proyectofinal.restaurante.service;

import com.proyectofinal.restaurante.dao.MesaRepository;
import com.proyectofinal.restaurante.entity.Mesa;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MesaService {

    @Autowired
    MesaRepository mesaRepository;

    public List<Mesa> list() {
        return mesaRepository.findAll();
    }

    public Optional<Mesa> getOne(long id) {
        return mesaRepository.findById(id);
    }

    public Optional<Mesa> getByNumero(Integer numero) {
        return mesaRepository.findByNumero(numero);
    }

    public void save(Mesa mesa) {
        mesaRepository.save(mesa);
    }

    public void delete(long id) {
        mesaRepository.deleteById(id);
    }

    public boolean existsById(long id) {
        return mesaRepository.existsById(id);
    }

    public boolean existsByNumero(Integer numero) {
        return mesaRepository.existsByNumero(numero);
    }
}