package com.camping.jacasaroad.services;

import com.camping.jacasaroad.models.Favorito;
import com.camping.jacasaroad.models.FavoritoId;
import com.camping.jacasaroad.repository.FavoritoRepository;
import com.camping.jacasaroad.repository.EspacoRepository;
import com.camping.jacasaroad.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private EspacoRepository espacoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Listar todos os favoritos de um usuário pelo nome de usuário
    public List<Favorito> listarFavoritosPorUsuario(String nomeDeUsuario) {
        return favoritoRepository.findByUsuarioNomeDeUsuario(nomeDeUsuario);
    }

    // Adicionar um espaço aos favoritos de um usuário
    public Favorito adicionarAosFavoritos(String nomeDeUsuario, Long espacoId) {
        var usuario = usuarioRepository.findByNomeDeUsuario(nomeDeUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        var espaco = espacoRepository.findById(espacoId)
                .orElseThrow(() -> new RuntimeException("Espaço não encontrado"));

        FavoritoId favoritoId = new FavoritoId(usuario.getCpf(), espacoId);
        if (favoritoRepository.existsById(favoritoId)) {
            throw new RuntimeException("Este espaço já está nos seus favoritos.");
        }

        Favorito favorito = new Favorito(favoritoId, usuario, espaco);
        return favoritoRepository.save(favorito);
    }

    // Excluir um espaço dos favoritos de um usuário
    public void excluirDosFavoritos(String nomeDeUsuario, Long espacoId) {
        var usuario = usuarioRepository.findByNomeDeUsuario(nomeDeUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        FavoritoId favoritoId = new FavoritoId(usuario.getCpf(), espacoId);
        if (!favoritoRepository.existsById(favoritoId)) {
            throw new RuntimeException("Este espaço não está nos seus favoritos.");
        }

        favoritoRepository.deleteById(favoritoId);
    }
}
