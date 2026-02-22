package com.proyectofinal.restaurante.service;

import com.proyectofinal.restaurante.dao.PlatoRepository;
import com.proyectofinal.restaurante.entity.Plato;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlatoService {

    @Autowired
    PlatoRepository platoRepository;

    public List<Plato> list() {
        return platoRepository.findAll();
    }

    public Optional<Plato> getOne(long id) {
        return platoRepository.findById(id);
    }

    public Optional<Plato> getByNombre(String nombre) {
        return platoRepository.findByNombre(nombre);
    }

    public void save(Plato plato) {
        platoRepository.save(plato);
    }

    public void delete(long id) {
        platoRepository.deleteById(id);
    }

    public boolean existsById(long id) {
        return platoRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre) {
        return platoRepository.existsByNombre(nombre);
    }
}