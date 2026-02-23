package com.proyectofinal.restaurante.controller;

import com.proyectofinal.restaurante.dto.Mensaje;
import com.proyectofinal.restaurante.dto.PlatoDto;
import com.proyectofinal.restaurante.entity.Plato;
import com.proyectofinal.restaurante.service.DetallePedidoService;
import com.proyectofinal.restaurante.service.PlatoService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plato")
@CrossOrigin(origins = "*")
public class PlatoController {

    @Autowired
    PlatoService platoService;

    @Autowired
    DetallePedidoService detallePedidoService;

    @GetMapping("/lista")
    public ResponseEntity<List<Plato>> list() {
        List<Plato> list = platoService.list();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        if (!platoService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Plato plato = platoService.getOne(id).get();
        return new ResponseEntity<>(plato, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody PlatoDto platoDto) {
        if (StringUtils.isBlank(platoDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        if (platoDto.getPrecio() == null)
            return new ResponseEntity<>(new Mensaje("el precio es obligatorio"), HttpStatus.BAD_REQUEST);
        if (platoService.existsByNombre(platoDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("el plato ya existe"), HttpStatus.BAD_REQUEST);
        if (platoDto.getPrecio() <= 0)
            return new ResponseEntity<>(new Mensaje("el precio debe ser mayor que 0"), HttpStatus.BAD_REQUEST);

        Plato plato = new Plato();
        plato.setNombre(platoDto.getNombre());
        plato.setDescripcion(platoDto.getDescripcion());
        plato.setPrecio(platoDto.getPrecio());
        plato.setImagenUrl(platoDto.getImagenUrl());
        platoService.save(plato);
        return new ResponseEntity<>(plato, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody PlatoDto platoDto) {
        if (!platoService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        if (StringUtils.isBlank(platoDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        if (platoDto.getPrecio() == null)
            return new ResponseEntity<>(new Mensaje("el precio es obligatorio"), HttpStatus.BAD_REQUEST);
        if (platoService.existsByNombre(platoDto.getNombre()) &&
                platoService.getByNombre(platoDto.getNombre()).get().getId() != id)
            return new ResponseEntity<>(new Mensaje("el plato ya existe"), HttpStatus.BAD_REQUEST);
        if (platoDto.getPrecio() <= 0)
            return new ResponseEntity<>(new Mensaje("el precio debe ser mayor que 0"), HttpStatus.BAD_REQUEST);

        Plato plato = platoService.getOne(id).get();
        plato.setNombre(platoDto.getNombre());
        plato.setDescripcion(platoDto.getDescripcion());
        plato.setPrecio(platoDto.getPrecio());
        plato.setImagenUrl(platoDto.getImagenUrl());
        platoService.save(plato);
        return new ResponseEntity<>(new Mensaje("plato actualizado"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        if (!platoService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        if (detallePedidoService.existsByPlatoId(id))
            return new ResponseEntity<>(new Mensaje("no se puede eliminar el plato porque tiene pedidos asociados"),
                    HttpStatus.BAD_REQUEST);
        platoService.delete(id);
        return new ResponseEntity<>(new Mensaje("plato eliminado"), HttpStatus.OK);
    }
}
