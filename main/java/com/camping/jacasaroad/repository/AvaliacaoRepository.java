package com.camping.jacasaroad.repository;

import com.camping.jacasaroad.models.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    // Reaizar melhoria nesta consulta futuramente para paginação
    // Buscar todas as avaliações de um espaço específico
    List<Avaliacao> findByEspacoId(Long espacoId);



    // Buscar todas as avaliações feitas por um usuário específico
    List<Avaliacao> findByUsuarioCpf(String cpf);

    // Buscar avaliações por espaço e uma nota específica
    List<Avaliacao> findByEspacoIdAndNotaValor(Long espacoId, int notaValor);

    // Buscar todas as avaliações de um espaço com notas entre um intervalo
    List<Avaliacao> findByEspacoIdAndNotaValorBetween(Long espacoId, int notaMin, int notaMax);



    //para implementação futura

    //ordenar as avaliações por data, implementar futuramente
    //List<Avaliacao> findByEspacoIdOrderByDataDesc(Long espacoId);

    //garante que o tamanho da busca não seja muito grande
    // Paginação de avaliações por espaço
    //Page<Avaliacao> findByEspacoId(Long espacoId, Pageable pageable);
}