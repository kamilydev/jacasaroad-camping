package com.camping.jacasaroad.controllers;

import com.camping.jacasaroad.models.Favorito;
import com.camping.jacasaroad.services.FavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favoritos")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    // Visualizar todos os favoritos do usuário
    @GetMapping("/listarFavoritos")
    public ResponseEntity<List<Favorito>> listarFavoritos(@RequestParam String nomeDeUsuario) {
        List<Favorito> favoritos = favoritoService.listarFavoritosPorUsuario(nomeDeUsuario);
        return ResponseEntity.ok(favoritos);
    }

    // Adicionar um espaço aos favoritos do usuário
    @PostMapping("/adicionarFavorito")
    public ResponseEntity<Favorito> adicionarAosFavoritos(@RequestParam String nomeDeUsuario,
                                                          @RequestParam Long espacoId) {
        Favorito favorito = favoritoService.adicionarAosFavoritos(nomeDeUsuario, espacoId);
        return ResponseEntity.ok(favorito);
    }

    // Excluir um espaço dos favoritos do usuário
    @DeleteMapping("/excluirFavorito")
    public ResponseEntity<String> excluirDosFavoritos(@RequestParam String nomeDeUsuario,
                                                      @RequestParam Long espacoId) {
        favoritoService.excluirDosFavoritos(nomeDeUsuario, espacoId);
        return ResponseEntity.ok("Espaço removido dos favoritos com sucesso.");
    }
}
