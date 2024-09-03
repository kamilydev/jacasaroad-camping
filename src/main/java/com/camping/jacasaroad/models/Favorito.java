package com.camping.jacasaroad.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "favorito")
public class Favorito {

    @EmbeddedId
    private FavoritoId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cpfUsuario", insertable = false, updatable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "espacoId", insertable = false, updatable = false)
    private Espaco espaco;

    // Construtor adicional para conveniência
    public Favorito(Usuario usuario, Espaco espaco) {
        this.id = new FavoritoId(usuario.getCpf(), espaco.getId());
        this.usuario = usuario;
        this.espaco = espaco;
    }
}
