package com.proyectofinal.restaurante.controller;

import com.proyectofinal.restaurante.dto.Mensaje;
import com.proyectofinal.restaurante.dto.MesaDto;
import com.proyectofinal.restaurante.entity.Mesa;
import com.proyectofinal.restaurante.service.MesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mesa")
@CrossOrigin(origins = "*")
public class MesaController {

    @Autowired
    MesaService mesaService;

    @GetMapping("/lista")
    public ResponseEntity<List<Mesa>> list() {
        List<Mesa> list = mesaService.list();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        if (!mesaService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Mesa mesa = mesaService.getOne(id).get();
        return new ResponseEntity<>(mesa, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody MesaDto mesaDto) {
        if (mesaDto.getNumero() == null)
            return new ResponseEntity<>(new Mensaje("el número es obligatorio"), HttpStatus.BAD_REQUEST);
        if (mesaService.existsByNumero(mesaDto.getNumero()))
            return new ResponseEntity<>(new Mensaje("el número de mesa ya existe"), HttpStatus.BAD_REQUEST);

        Mesa mesa = new Mesa();
        mesa.setNumero(mesaDto.getNumero());
        mesa.setCapacidad(mesaDto.getCapacidad());
        mesa.setOcupada(false); // por defecto libre al crearla
        mesaService.save(mesa);
        return new ResponseEntity<>(new Mensaje("mesa creada"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody MesaDto mesaDto) {
        if (!mesaService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        if (mesaDto.getNumero() == null)
            return new ResponseEntity<>(new Mensaje("el número es obligatorio"), HttpStatus.BAD_REQUEST);
        if (mesaService.existsByNumero(mesaDto.getNumero()) &&
                mesaService.getByNumero(mesaDto.getNumero()).get().getId() != id)
            return new ResponseEntity<>(new Mensaje("el número de mesa ya existe"), HttpStatus.BAD_REQUEST);

        Mesa mesa = mesaService.getOne(id).get();
        mesa.setNumero(mesaDto.getNumero());
        mesa.setCapacidad(mesaDto.getCapacidad());
        mesaService.save(mesa);
        return new ResponseEntity<>(new Mensaje("mesa actualizada"), HttpStatus.OK);
    }

    @PutMapping("/asignar/{id}")
    public ResponseEntity<?> asignar(@PathVariable("id") long id) {
        if (!mesaService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        Mesa mesa = mesaService.getOne(id).get();
        if (mesa.getOcupada())
            return new ResponseEntity<>(new Mensaje("la mesa ya está ocupada"), HttpStatus.BAD_REQUEST);

        mesa.setOcupada(true);
        mesaService.save(mesa);
        return new ResponseEntity<>(new Mensaje("mesa asignada"), HttpStatus.OK);
    }

    @PutMapping("/liberar/{id}")
    public ResponseEntity<?> liberar(@PathVariable("id") long id) {
        if (!mesaService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        Mesa mesa = mesaService.getOne(id).get();
        if (!mesa.getOcupada())
            return new ResponseEntity<>(new Mensaje("la mesa ya está libre"), HttpStatus.BAD_REQUEST);

        mesa.setOcupada(false);
        mesaService.save(mesa);
        return new ResponseEntity<>(new Mensaje("mesa liberada"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        if (!mesaService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        mesaService.delete(id);
        return new ResponseEntity<>(new Mensaje("mesa eliminada"), HttpStatus.OK);
    }
}
