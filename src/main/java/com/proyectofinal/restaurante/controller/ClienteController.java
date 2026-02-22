package com.proyectofinal.restaurante.controller;


import com.proyectofinal.restaurante.dto.ClienteDto;
import com.proyectofinal.restaurante.dto.Mensaje;
import com.proyectofinal.restaurante.entity.Cliente;
import com.proyectofinal.restaurante.service.ClienteService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    @GetMapping("/lista")
    public ResponseEntity<List<Cliente>> list() {
        List<Cliente> list = clienteService.list();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        if (!clienteService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Cliente cliente = clienteService.getOne(id).get();
        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ClienteDto clienteDto) {
        if (StringUtils.isBlank(clienteDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(clienteDto.getApellido()))
            return new ResponseEntity<>(new Mensaje("el apellido es obligatorio"), HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(clienteDto.getEmail()))
            return new ResponseEntity<>(new Mensaje("el email es obligatorio"), HttpStatus.BAD_REQUEST);
        if (clienteService.existsByEmail(clienteDto.getEmail()))
            return new ResponseEntity<>(new Mensaje("el email ya está registrado"), HttpStatus.BAD_REQUEST);

        Cliente cliente = new Cliente();
        cliente.setNombre(clienteDto.getNombre());
        cliente.setApellido(clienteDto.getApellido());
        cliente.setTelefono(clienteDto.getTelefono());
        cliente.setEmail(clienteDto.getEmail());
        clienteService.save(cliente);
        return new ResponseEntity<>(cliente, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody ClienteDto clienteDto) {
        if (!clienteService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        if (StringUtils.isBlank(clienteDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(clienteDto.getEmail()))
            return new ResponseEntity<>(new Mensaje("el email es obligatorio"), HttpStatus.BAD_REQUEST);
        if (clienteService.existsByEmail(clienteDto.getEmail()) &&
                clienteService.getByEmail(clienteDto.getEmail()).get().getId() != id)
            return new ResponseEntity<>(new Mensaje("el email ya está registrado"), HttpStatus.BAD_REQUEST);

        Cliente cliente = clienteService.getOne(id).get();
        cliente.setNombre(clienteDto.getNombre());
        cliente.setApellido(clienteDto.getApellido());
        cliente.setTelefono(clienteDto.getTelefono());
        cliente.setEmail(clienteDto.getEmail());
        clienteService.save(cliente);
        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        if (!clienteService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        clienteService.delete(id);
        return new ResponseEntity<>(new Mensaje("cliente eliminado"), HttpStatus.OK);
    }
}