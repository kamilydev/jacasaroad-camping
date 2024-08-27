package com.camping.jacasaroad.repository;

import com.camping.jacasaroad.models.Favorito;
import com.camping.jacasaroad.models.FavoritoId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritoRepository extends JpaRepository<Favorito, FavoritoId> {
    // Busca todos os favoritos de um usuário
    List<Favorito> findByUsuarioNomeDeUsuario(String nomeDeUsuario);

    // Verifica se um espaço é favorito de um usuário
    boolean existsByUsuarioNomeDeUsuarioAndEspacoId(String nomeDeUsuario, Long espacoId);

    // Remove um favorito de um usuário
    void deleteByUsuarioNomeDeUsuarioAndEspacoId(String nomeDeUsuario, Long espacoId);
}