package com.camping.jacasaroad.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "avaliacao")
public class Avaliacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comentario;

    @Column(name = "nota")
    private int notaValor;

    private LocalDate dataAvaliacao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "espaco_id")
    private Espaco espaco;

    @Transient
    private Nota nota;  // Mantém o enum, mas não o armazena diretamente no banco de dados

    @PostLoad
    public void carregarNota() {
        this.nota = Nota.fromValor(this.notaValor);
    }

    @PrePersist
    @PreUpdate
    public void salvarNota() {
        if (this.nota != null) {
            this.notaValor = this.nota.getValor();
        }
    }


    public Avaliacao(Long id, String comentario, Nota nota, LocalDate dataAvaliacao, Usuario usuario, Espaco espaco) {
        this.id = id;
        this.comentario = comentario;
        this.nota = nota;
        this.dataAvaliacao = dataAvaliacao;
        this.usuario = usuario;
        this.espaco = espaco;
    }
}