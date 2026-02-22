package com.proyectofinal.restaurante.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "pedido")
@Getter
@Setter
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "cerrado")
    private Boolean cerrado;

    @Column(name = "total")
    private Double total;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "mesa_id")
    private Mesa mesa;

    @ManyToOne
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
    @JsonManagedReference
    private Set<DetallePedido> detalles;

    public Pedido() {}
}