package com.proyectofinal.restaurante.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpleadoDto {

    @NotBlank
    private String nombre;
    @NotBlank
    private String apellido;
    private String cargo;

    public EmpleadoDto() {}

    public EmpleadoDto(String nombre, String apellido, String cargo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cargo = cargo;
    }
}