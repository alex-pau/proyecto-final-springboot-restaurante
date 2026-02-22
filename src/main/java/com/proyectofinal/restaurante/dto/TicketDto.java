package com.proyectofinal.restaurante.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TicketDto {
    private Long pedidoId;
    private LocalDateTime fecha;
    private String cliente;
    private String mesa;
    private String empleado;
    private List<TicketLineaDto> lineas;
    private Double total;
}