package com.camping.jacasaroad.repository;

import com.camping.jacasaroad.models.Favorito;
import com.camping.jacasaroad.models.FavoritoId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, FavoritoId> {
    // Busca todos os favoritos de um usuário
    List<Favorito> findByUsuarioNomeDeUsuario(String nomeDeUsuario);

    // Remove um favorito de um usuário
    void deleteById(FavoritoId favoritoId);

    // Verifica se existe um favorito com o CPF do usuário e o ID do espaço
    boolean existsById(FavoritoId favoritoId);
}
