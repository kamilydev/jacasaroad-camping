package com.camping.jacasaroad.controllers;

import com.camping.jacasaroad.models.Avaliacao;
import com.camping.jacasaroad.services.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    //aperfeiçoar código

    @Autowired
    private AvaliacaoService avaliacaoService;

    // Adicionar uma avaliação a um espaço
    @PostMapping("/{espacoId}")
    public String adicionarAvaliacao(@PathVariable Long espacoId,
                                     @RequestParam String nomeDeUsuario,
                                     @RequestParam int nota,
                                     @ModelAttribute Avaliacao avaliacao,
                                     Model model) {
        try {
            Avaliacao novaAvaliacao = avaliacaoService.adicionarAvaliacao(nomeDeUsuario, espacoId, avaliacao, nota);
            model.addAttribute("mensagem", "Avaliação adicionada com sucesso.");
            model.addAttribute("avaliacao", novaAvaliacao);
            return "redirect:/espacos/detalhes/" + espacoId; // Redireciona para a página de detalhes do espaço
        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
            return "erro"; // Exibe página de erro
        }
    }

    // Listar avaliações de um espaço
    @GetMapping("/espaco/{espacoId}")
    public String listarAvaliacoesPorEspaco(@PathVariable Long espacoId, Model model) {
        List<Avaliacao> avaliacoes = avaliacaoService.listarAvaliacoesPorEspaco(espacoId);
        model.addAttribute("avaliacoes", avaliacoes);
        return "avaliacoesEspaco";  // Nome da página para listar as avaliações de um espaço
    }

    // Listar avaliações para o anfitrião de um espaço específico
    @GetMapping("/anfitriao/{espacoId}")
    public String listarAvaliacoesDoAnfitriao(@PathVariable Long espacoId,
                                              @RequestParam String nomeDeUsuarioAnfitriao,
                                              Model model) {
        List<Avaliacao> avaliacoes = avaliacaoService.listarAvaliacoesDoAnfitriao(espacoId, nomeDeUsuarioAnfitriao);
        model.addAttribute("avaliacoes", avaliacoes);
        return "avaliacoesAnfitriao";  // Nome da página para listar as avaliações para o anfitrião
    }
}