package com.proyectofinal.restaurante.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MesaDto {

    @NotNull
    private Integer numero;
    @NotNull
    private Integer capacidad;

    public MesaDto() {}

    public MesaDto(Integer numero, Integer capacidad) {
        this.numero = numero;
        this.capacidad = capacidad;
    }
}