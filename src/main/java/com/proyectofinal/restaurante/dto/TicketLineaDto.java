package com.proyectofinal.restaurante.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketLineaDto {
    private String plato;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}
