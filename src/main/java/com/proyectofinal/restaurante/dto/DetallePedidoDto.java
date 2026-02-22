package com.proyectofinal.restaurante.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetallePedidoDto {

    @NotNull
    private Long pedidoId;
    @NotNull
    private Long platoId;
    @NotNull
    private Integer cantidad;

    public DetallePedidoDto() {}

    public DetallePedidoDto(Long pedidoId, Long platoId, Integer cantidad) {
        this.pedidoId = pedidoId;
        this.platoId = platoId;
        this.cantidad = cantidad;
    }
}