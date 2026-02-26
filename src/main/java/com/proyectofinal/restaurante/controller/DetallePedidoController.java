package com.proyectofinal.restaurante.controller;

import com.proyectofinal.restaurante.dto.DetallePedidoDto;
import com.proyectofinal.restaurante.dto.Mensaje;
import com.proyectofinal.restaurante.entity.DetallePedido;
import com.proyectofinal.restaurante.entity.Pedido;
import com.proyectofinal.restaurante.entity.Plato;
import com.proyectofinal.restaurante.service.DetallePedidoService;
import com.proyectofinal.restaurante.service.PedidoService;
import com.proyectofinal.restaurante.service.PlatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/detalle")
@CrossOrigin(origins = "*")
public class DetallePedidoController {

    @Autowired
    DetallePedidoService detallePedidoService;
    @Autowired
    PedidoService pedidoService;
    @Autowired
    PlatoService platoService;

    @GetMapping("/lista")
    public ResponseEntity<List<DetallePedido>> list() {
        List<DetallePedido> list = detallePedidoService.list();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        if (!detallePedidoService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        DetallePedido detalle = detallePedidoService.getOne(id).get();
        return new ResponseEntity<>(detalle, HttpStatus.OK);
    }

    @GetMapping("/porPedido/{pedidoId}")
    public ResponseEntity<?> getByPedido(@PathVariable("pedidoId") long pedidoId) {
        if (!pedidoService.existsById(pedidoId))
            return new ResponseEntity<>(new Mensaje("el pedido no existe"), HttpStatus.NOT_FOUND);
        List<DetallePedido> detalles = detallePedidoService.getByPedidoId(pedidoId);
        return new ResponseEntity<>(detalles, HttpStatus.OK);
    }

    //añadir plato al pedido
    @PostMapping("/create/{pedidoId}")
    public ResponseEntity<?> create(@PathVariable("pedidoId") long pedidoId,
                                    @RequestBody List<DetallePedidoDto> detallesDto) {

        if (detallesDto == null || detallesDto.isEmpty())
            return new ResponseEntity<>(new Mensaje("debes añadir al menos un plato"), HttpStatus.BAD_REQUEST);

        if (!pedidoService.existsById(pedidoId))
            return new ResponseEntity<>(new Mensaje("el pedido no existe"), HttpStatus.NOT_FOUND);

        Pedido pedido = pedidoService.getOne(pedidoId).get();

        if (pedido.getCerrado())
            return new ResponseEntity<>(new Mensaje("el pedido ya está cerrado"), HttpStatus.BAD_REQUEST);

        List<DetallePedido> detallesGuardados = new ArrayList<>();

        for (DetallePedidoDto dto : detallesDto) {

            if (dto.getPlatoId() == null)
                return new ResponseEntity<>(new Mensaje("cada línea debe tener un plato"), HttpStatus.BAD_REQUEST);

            if (dto.getCantidad() == null || dto.getCantidad() <= 0)
                return new ResponseEntity<>(new Mensaje("la cantidad debe ser mayor que 0"), HttpStatus.BAD_REQUEST);

            if (!platoService.existsById(dto.getPlatoId()))
                return new ResponseEntity<>(new Mensaje("el plato con id " + dto.getPlatoId() + " no existe"), HttpStatus.NOT_FOUND);

            Plato plato = platoService.getOne(dto.getPlatoId()).get();

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setPlato(plato);
            detalle.setCantidad(dto.getCantidad());
            detalle.setPrecioUnitario(plato.getPrecio());

            detallePedidoService.save(detalle);
            detallesGuardados.add(detalle);
        }

        return new ResponseEntity<>(detallesGuardados, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody DetallePedidoDto detalleDto) {
        if (!detallePedidoService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        if (detalleDto.getCantidad() == null || detalleDto.getCantidad() <= 0)
            return new ResponseEntity<>(new Mensaje("la cantidad debe ser mayor que 0"), HttpStatus.BAD_REQUEST);

        DetallePedido detalle = detallePedidoService.getOne(id).get();

        if (detalle.getPedido().getCerrado())
            return new ResponseEntity<>(new Mensaje("no se puede modificar un pedido cerrado"), HttpStatus.BAD_REQUEST);

        detalle.setCantidad(detalleDto.getCantidad());
        detallePedidoService.save(detalle);
        return new ResponseEntity<>(detalle, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        if (!detallePedidoService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        detallePedidoService.delete(id);
        return new ResponseEntity<>(new Mensaje("detalle eliminado"), HttpStatus.OK);
    }
}
