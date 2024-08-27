package com.camping.jacasaroad.controllers;

import com.camping.jacasaroad.models.Favorito;
import com.camping.jacasaroad.services.FavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/favoritos")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    // Visualizar todos os favoritos do usuário
    @GetMapping
    public String listarFavoritos(@RequestParam String nomeDeUsuario, Model model) {
        List<Favorito> favoritos = favoritoService.listarFavoritosPorUsuario(nomeDeUsuario);
        model.addAttribute("favoritos", favoritos);
        return "configMeusFavoritos";  // Nome da view Thymeleaf para exibir os favoritos
    }

    // Adicionar um espaço aos favoritos do usuário (com suporte a AJAX)
    @PostMapping("/adicionarFavorito")
    @ResponseBody  // Retorna resposta direta sem redirecionamento
    public ResponseEntity<String> adicionarAosFavoritos(
            @RequestParam String nomeDeUsuario,
            @RequestParam Long espacoId) {
        try {
            favoritoService.adicionarAosFavoritos(nomeDeUsuario, espacoId);
            return ResponseEntity.ok("Espaço adicionado aos favoritos com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Excluir um espaço dos favoritos do usuário
    @PostMapping("/excluirFavorito")
    @ResponseBody
    public ResponseEntity<String> excluirDosFavoritos(@RequestParam String nomeDeUsuario,
                                                      @RequestParam Long espacoId) {
        try {
            favoritoService.excluirDosFavoritos(nomeDeUsuario, espacoId);
            return ResponseEntity.ok("Espaço removido dos favoritos com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}