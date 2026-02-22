package com.proyectofinal.restaurante.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlatoDto {

    @NotBlank
    private String nombre;
    private String descripcion;
    @NotNull
    private Double precio;
    private String imagenUrl;

    public PlatoDto() {}

    public PlatoDto(String nombre, String descripcion, Double precio, String imagenUrl) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
    }
}