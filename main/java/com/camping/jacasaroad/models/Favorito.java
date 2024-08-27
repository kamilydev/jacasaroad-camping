package com.camping.jacasaroad.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "favoritos")
public class Favorito {
    @EmbeddedId
    private FavoritoId id;

    @ManyToOne
    @MapsId("cpfUsuario")
    @JoinColumn(name = "cpf_usuario")
    private Usuario usuario;

    @ManyToOne
    @MapsId("espacoId")
    @JoinColumn(name = "espaco_id")
    private Espaco espaco;
}