package com.proyectofinal.restaurante.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mesa")
@Getter
@Setter
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "capacidad")
    private Integer capacidad;

    @Column(name = "ocupada")
    private Boolean ocupada;

    public Mesa() {}
}
