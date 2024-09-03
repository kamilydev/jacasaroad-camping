package com.camping.jacasaroad.services;

import com.camping.jacasaroad.models.Avaliacao;
import com.camping.jacasaroad.models.Nota;
import com.camping.jacasaroad.models.Reserva;
import com.camping.jacasaroad.repository.AvaliacaoRepository;
import com.camping.jacasaroad.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ReservaService reservaService;

    // Adicionar avaliação
    public Avaliacao adicionarAvaliacao(String nomeDeUsuario, Long espacoId, Avaliacao avaliacao, int nota) {
        // Buscar a reserva correspondente
        Reserva reserva = reservaService.obterReservaPorUsuarioEEspaco(nomeDeUsuario, espacoId);

        // Definir os parâmetros da avaliação
        avaliacao.setUsuario(reserva.getUsuario());
        avaliacao.setEspaco(reserva.getEspaco());
        avaliacao.setNota(Nota.fromValor(nota));  // Corrigido para aceitar valor numérico corretamente
        avaliacao.setDataAvaliacao(LocalDate.now());

        // Salvar e retornar a avaliação
        return avaliacaoRepository.save(avaliacao);
    }

    // Consultar todas as avaliações de um espaço
    public List<Avaliacao> listarAvaliacoesPorEspaco(Long espacoId) {
        return avaliacaoRepository.findByEspacoId(espacoId);
    }

    // Consultar todas as avaliações de um espaço pelo anfitrião
    public List<Avaliacao> listarAvaliacoesDoAnfitriao(Long espacoId, String nomeDeUsuarioAnfitriao) {
        boolean eAnfitriao = reservaRepository.existsByEspacoIdAndEspacoAnfitriaoNomeDeUsuario(espacoId, nomeDeUsuarioAnfitriao);
        if (!eAnfitriao) {
            throw new AvaliacaoNaoPermitidaException("Você não tem permissão para visualizar estas avaliações");
        }

        return avaliacaoRepository.findByEspacoId(espacoId);
    }

    // Exceção personalizada
    public static class AvaliacaoNaoPermitidaException extends RuntimeException {
        public AvaliacaoNaoPermitidaException(String message) {
            super(message);
        }
    }
}
