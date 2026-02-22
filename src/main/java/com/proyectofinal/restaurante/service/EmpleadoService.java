package com.proyectofinal.restaurante.service;

import com.proyectofinal.restaurante.dao.EmpleadoRepository;
import com.proyectofinal.restaurante.entity.Empleado;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmpleadoService {

    @Autowired
    EmpleadoRepository empleadoRepository;

    public List<Empleado> list() {
        return empleadoRepository.findAll();
    }

    public Optional<Empleado> getOne(long id) {
        return empleadoRepository.findById(id);
    }

    public void save(Empleado empleado) {
        empleadoRepository.save(empleado);
    }

    public void delete(long id) {
        empleadoRepository.deleteById(id);
    }

    public boolean existsById(long id) {
        return empleadoRepository.existsById(id);
    }

    public Optional<Empleado> getByNombre(String nombre) {
        return empleadoRepository.findByNombre(nombre);
    }

    public boolean existsByNombre(String nombre) {
        return empleadoRepository.existsByNombre(nombre);
    }
}