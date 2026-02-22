package com.proyectofinal.restaurante.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoDto {

    @NotNull
    private Long clienteId;
    @NotNull
    private Long mesaId;
    @NotNull
    private Long empleadoId;

    public PedidoDto() {}

    public PedidoDto(Long clienteId, Long mesaId, Long empleadoId) {
        this.clienteId = clienteId;
        this.mesaId = mesaId;
        this.empleadoId = empleadoId;
    }
}