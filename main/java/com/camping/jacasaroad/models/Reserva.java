package com.camping.jacasaroad.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusReserva status;

    @ManyToOne
    @JoinColumn(name = "cpf_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "espaco_id")
    private Espaco espaco;

    public Reserva(Long id, LocalDate dataInicio, LocalDate dataFim, Usuario usuario, Espaco espaco, StatusReserva status) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.usuario = usuario;
        this.espaco = espaco;
        this.status = status;
    }
}
