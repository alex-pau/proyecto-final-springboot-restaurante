package com.proyectofinal.restaurante.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteDto {

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    private String telefono;

    private String email;

    public ClienteDto() {}
}