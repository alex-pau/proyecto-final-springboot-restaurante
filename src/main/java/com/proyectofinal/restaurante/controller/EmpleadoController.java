package com.proyectofinal.restaurante.controller;

import com.proyectofinal.restaurante.dto.EmpleadoDto;
import com.proyectofinal.restaurante.dto.Mensaje;
import com.proyectofinal.restaurante.entity.Empleado;
import com.proyectofinal.restaurante.service.EmpleadoService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empleado")
@CrossOrigin(origins = "*")
public class EmpleadoController {

    @Autowired
    EmpleadoService empleadoService;

    @GetMapping("/lista")
    public ResponseEntity<List<Empleado>> list() {
        List<Empleado> list = empleadoService.list();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        if (!empleadoService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Empleado empleado = empleadoService.getOne(id).get();
        return new ResponseEntity<>(empleado, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody EmpleadoDto empleadoDto) {
        if (StringUtils.isBlank(empleadoDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(empleadoDto.getApellido()))
            return new ResponseEntity<>(new Mensaje("el apellido es obligatorio"), HttpStatus.BAD_REQUEST);

        Empleado empleado = new Empleado();
        empleado.setNombre(empleadoDto.getNombre());
        empleado.setApellido(empleadoDto.getApellido());
        empleado.setCargo(empleadoDto.getCargo());
        empleadoService.save(empleado);
        return new ResponseEntity<>(empleado, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody EmpleadoDto empleadoDto) {
        if (!empleadoService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        if (StringUtils.isBlank(empleadoDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(empleadoDto.getApellido()))
            return new ResponseEntity<>(new Mensaje("el apellido es obligatorio"), HttpStatus.BAD_REQUEST);

        Empleado empleado = empleadoService.getOne(id).get();
        empleado.setNombre(empleadoDto.getNombre());
        empleado.setApellido(empleadoDto.getApellido());
        empleado.setCargo(empleadoDto.getCargo());
        empleadoService.save(empleado);
        return new ResponseEntity<>(empleado, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        if (!empleadoService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        empleadoService.delete(id);
        return new ResponseEntity<>(new Mensaje("empleado eliminado"), HttpStatus.OK);
    }
}