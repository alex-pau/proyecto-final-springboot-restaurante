package com.proyectofinal.restaurante.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "plato")
@Getter
@Setter
public class Plato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "imagen_url")
    private String imagenUrl;

    public Plato() {}
}