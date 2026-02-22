package com.proyectofinal.restaurante.controller;

import com.proyectofinal.restaurante.dto.Mensaje;
import com.proyectofinal.restaurante.dto.PedidoDto;
import com.proyectofinal.restaurante.dto.TicketDto;
import com.proyectofinal.restaurante.dto.TicketLineaDto;
import com.proyectofinal.restaurante.entity.Cliente;
import com.proyectofinal.restaurante.entity.Empleado;
import com.proyectofinal.restaurante.entity.Mesa;
import com.proyectofinal.restaurante.entity.Pedido;
import com.proyectofinal.restaurante.service.ClienteService;
import com.proyectofinal.restaurante.service.EmpleadoService;
import com.proyectofinal.restaurante.service.MesaService;
import com.proyectofinal.restaurante.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pedido")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    PedidoService pedidoService;
    @Autowired
    ClienteService clienteService;
    @Autowired
    MesaService mesaService;
    @Autowired
    EmpleadoService empleadoService;

    @GetMapping("/lista")
    public ResponseEntity<List<Pedido>> list() {
        List<Pedido> list = pedidoService.list();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        if (!pedidoService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Pedido pedido = pedidoService.getOne(id).get();
        return new ResponseEntity<>(pedido, HttpStatus.OK);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<?> getByCliente(@PathVariable("clienteId") long clienteId) {
        if (!clienteService.existsById(clienteId))
            return new ResponseEntity<>(new Mensaje("el cliente no existe"), HttpStatus.NOT_FOUND);
        List<Pedido> pedidos = pedidoService.getByClienteId(clienteId);
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    //crear pedido, asignar mesa, asignar empleado a cliente
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody PedidoDto pedidoDto) {
        if (pedidoDto.getClienteId() == null)
            return new ResponseEntity<>(new Mensaje("el cliente es obligatorio"), HttpStatus.BAD_REQUEST);
        if (pedidoDto.getMesaId() == null)
            return new ResponseEntity<>(new Mensaje("la mesa es obligatoria"), HttpStatus.BAD_REQUEST);
        if (pedidoDto.getEmpleadoId() == null)
            return new ResponseEntity<>(new Mensaje("el empleado es obligatorio"), HttpStatus.BAD_REQUEST);
        if (!clienteService.existsById(pedidoDto.getClienteId()))
            return new ResponseEntity<>(new Mensaje("el cliente no existe"), HttpStatus.NOT_FOUND);
        if (!mesaService.existsById(pedidoDto.getMesaId()))
            return new ResponseEntity<>(new Mensaje("la mesa no existe"), HttpStatus.NOT_FOUND);
        if (!empleadoService.existsById(pedidoDto.getEmpleadoId()))
            return new ResponseEntity<>(new Mensaje("el empleado no existe"), HttpStatus.NOT_FOUND);

        Mesa mesa = mesaService.getOne(pedidoDto.getMesaId()).get();
        if (mesa.getOcupada())
            return new ResponseEntity<>(new Mensaje("la mesa ya está ocupada"), HttpStatus.BAD_REQUEST);

        Cliente cliente = clienteService.getOne(pedidoDto.getClienteId()).get();
        Empleado empleado = empleadoService.getOne(pedidoDto.getEmpleadoId()).get();

        mesa.setOcupada(true);
        mesaService.save(mesa);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setMesa(mesa);
        pedido.setEmpleado(empleado);
        pedido.setFecha(LocalDateTime.now());
        pedido.setCerrado(false);
        pedido.setTotal(0.0);
        pedidoService.save(pedido);

        return new ResponseEntity<>(pedido, HttpStatus.CREATED);
    }

    @PutMapping("/cerrar/{id}")
    public ResponseEntity<?> cerrarCuenta(@PathVariable("id") long id) {
        if (!pedidoService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        Pedido pedido = pedidoService.getOne(id).get();

        if (pedido.getCerrado())
            return new ResponseEntity<>(new Mensaje("el pedido ya está cerrado"), HttpStatus.BAD_REQUEST);

        double total = pedido.getDetalles().stream()
                .mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario())
                .sum();

        pedido.setTotal(total);
        pedido.setCerrado(true);

        Mesa mesa = pedido.getMesa();
        mesa.setOcupada(false);
        mesaService.save(mesa);

        pedidoService.save(pedido);
        return new ResponseEntity<>(pedido, HttpStatus.OK);
    }

    //generar ticket
    @GetMapping("/ticket/{id}")
    public ResponseEntity<?> generarTicket(@PathVariable("id") long id) {
        if (!pedidoService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        Pedido pedido = pedidoService.getOne(id).get();

        if (!pedido.getCerrado())
            return new ResponseEntity<>(new Mensaje("el pedido aún no está cerrado"), HttpStatus.BAD_REQUEST);

        TicketDto ticket = new TicketDto();
        ticket.setPedidoId(pedido.getId());
        ticket.setFecha(pedido.getFecha());
        ticket.setCliente(pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido());
        ticket.setMesa("Mesa " + pedido.getMesa().getNumero());
        ticket.setEmpleado(pedido.getEmpleado().getNombre() + " " + pedido.getEmpleado().getApellido());

        List<TicketLineaDto> lineas = pedido.getDetalles().stream().map(d -> {
            TicketLineaDto linea = new TicketLineaDto();
            linea.setPlato(d.getPlato().getNombre());
            linea.setCantidad(d.getCantidad());
            linea.setPrecioUnitario(d.getPrecioUnitario());
            linea.setSubtotal(d.getCantidad() * d.getPrecioUnitario());
            return linea;
        }).toList();

        ticket.setLineas(lineas);
        ticket.setTotal(pedido.getTotal());

        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        if (!pedidoService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        pedidoService.delete(id);
        return new ResponseEntity<>(new Mensaje("pedido eliminado"), HttpStatus.OK);
    }
}
