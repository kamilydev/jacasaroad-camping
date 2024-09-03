package com.camping.jacasaroad.models;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class FavoritoId implements Serializable {
    // Getters and Setters
    private String cpfUsuario;
    private Long espacoId;

    // Construtores
    public FavoritoId() {}

    public FavoritoId(String cpfUsuario, Long espacoId) {
        this.cpfUsuario = cpfUsuario;
        this.espacoId = espacoId;
    }

    // hashCode and equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoritoId that = (FavoritoId) o;
        return Objects.equals(cpfUsuario, that.cpfUsuario) && Objects.equals(espacoId, that.espacoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpfUsuario, espacoId);
    }
}
