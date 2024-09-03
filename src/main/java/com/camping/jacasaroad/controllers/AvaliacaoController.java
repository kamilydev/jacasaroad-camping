package com.camping.jacasaroad.controllers;

import com.camping.jacasaroad.models.Avaliacao;
import com.camping.jacasaroad.services.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("avaliacao")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    // Adicionar uma avaliação a um espaço
    @PostMapping("/{espacoId}")
    public ResponseEntity<Avaliacao> adicionarAvaliacao(@PathVariable Long espacoId,
                                                        @RequestParam String nomeDeUsuario,
                                                        @RequestParam int nota,
                                                        @RequestBody Avaliacao avaliacao) {
        try {
            Avaliacao novaAvaliacao = avaliacaoService.adicionarAvaliacao(nomeDeUsuario, espacoId, avaliacao, nota);
            return ResponseEntity.ok(novaAvaliacao);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build(); // Retorna um erro 400 com a mensagem de erro
        }
    }

    // Listar avaliações de um espaço
    @GetMapping("/espaco/{espacoId}")
    public ResponseEntity<List<Avaliacao>> listarAvaliacoesPorEspaco(@PathVariable Long espacoId) {
        List<Avaliacao> avaliacoes = avaliacaoService.listarAvaliacoesPorEspaco(espacoId);
        return ResponseEntity.ok(avaliacoes);
    }

    // Listar avaliações para o anfitrião de um espaço específico
    @GetMapping("anfitriao/{espacoId}")
    public ResponseEntity<List<Avaliacao>> listarAvaliacoesDoAnfitriao(@PathVariable Long espacoId,
                                                                       @RequestParam String nomeDeUsuarioAnfitriao) {
        List<Avaliacao> avaliacoes = avaliacaoService.listarAvaliacoesDoAnfitriao(espacoId, nomeDeUsuarioAnfitriao);
        return ResponseEntity.ok(avaliacoes);
    }
}
