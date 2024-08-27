package com.camping.jacasaroad.models;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "favoritosId")
@Embeddable
public class FavoritoId implements Serializable {
    private String cpfUsuario;
    private Long espacoId;

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