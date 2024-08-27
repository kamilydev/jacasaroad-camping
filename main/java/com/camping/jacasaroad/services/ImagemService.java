package com.camping.jacasaroad.services;

import com.camping.jacasaroad.models.Imagem;
import com.camping.jacasaroad.repository.ImagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ImagemService {

    @Autowired
    private ImagemRepository imagemRepository;

    public Imagem adicionarImagem(Imagem imagem) {
        // Regras de negócio, como verificar limite de 10 imagens
        Long espacoId = imagem.getEspaco().getId();
        List<Imagem> imagensExistentes = imagemRepository.findByEspacoId(espacoId);

        if (imagensExistentes.size() >= 10) {
            throw new RuntimeException("O espaço já possui 10 imagens. Não é possível adicionar mais.");
        }

        return imagemRepository.save(imagem);
    }

    public void deletarImagem(Long id) {
        Imagem imagem = imagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imagem não encontrada."));
        imagemRepository.delete(imagem);
    }

    public List<Imagem> listarImagensPorEspaco(Long espacoId) {
        return imagemRepository.findByEspacoId(espacoId);
    }

    public String salvarImagem(MultipartFile arquivo) throws IOException {
        // Lógica para salvar a imagem no sistema de arquivos
        String destino = "diretorio/para/salvar/" + arquivo.getOriginalFilename();
        arquivo.transferTo(new File(destino));
        return destino;  // Retorna o caminho para salvar no banco de dados
    }
}