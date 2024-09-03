package com.camping.jacasaroad.services;

import com.camping.jacasaroad.models.Usuario;
import com.camping.jacasaroad.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImagemPerfilService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final String IMAGEM_PADRAO_PERFIL_CAMINHO = "static/images/default-image-perfil.jpg";


    // Adicionar ou atualizar a imagem de perfil
    public Usuario adicionarOuAtualizarImagemPerfil(String cpf, String novaImagemCaminho) {
        Usuario usuario = usuarioRepository.findById(cpf)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // Validar o caminho da nova imagem
        validarImagemCaminho(novaImagemCaminho);

        // Atualizar a imagem de perfil com o novo caminho
        usuario.setImagemPerfil(novaImagemCaminho);
        return usuarioRepository.save(usuario);
    }
    // Validação da URL ou caminho da imagem
    private void validarImagemCaminho(String caminho) {
        if (caminho == null || caminho.isEmpty()) {
            throw new IllegalArgumentException("O caminho da imagem não pode ser vazio.");
        }
    }

    // Deletar a imagem de perfil e definir uma imagem padrão
    public Usuario deletarImagemPerfil(String cpf) {
        Usuario usuario = usuarioRepository.findById(cpf)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // Definir imagem padrão ao deletar a imagem do perfil
        usuario.setImagemPerfil(IMAGEM_PADRAO_PERFIL_CAMINHO);

        return usuarioRepository.save(usuario);
    }

    // Consultar a imagem de perfil
    public String consultarImagemPerfil(String cpf) {
        Usuario usuario = usuarioRepository.findById(cpf)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        return usuario.getImagemPerfil();
    }
}