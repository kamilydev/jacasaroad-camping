package com.camping.jacasaroad.services;

import com.camping.jacasaroad.models.Usuario;
import com.camping.jacasaroad.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;  // Injete o PasswordEncoder

    public Usuario criarUsuario(Usuario usuario) {
        if (usuarioRepository.findByNomeDeUsuario(usuario.getNomeDeUsuario()).isPresent()) {
            throw new IllegalArgumentException("Já existe uma conta com esse nome de usuário.");
        }

        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Já existe uma conta vinculada a esse email.");
        }

        if (usuarioRepository.findById(usuario.getCpf()).isPresent()) {
            throw new IllegalArgumentException("Já existe uma conta vinculada a esse CPF.");
        }

        // Codifica a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        return usuarioRepository.save(usuario);
    }

    // Atualizar usuário logado
    public Usuario atualizarUsuario(String cpfAutenticado, Usuario usuarioAtualizado, String usuarioAutenticadoCpf) {
        if (!cpfAutenticado.equals(usuarioAutenticadoCpf)) {
            throw new IllegalArgumentException("Você só pode atualizar os dados da sua própria conta.");
        }

        Optional<Usuario> usuarioExistente = usuarioRepository.findById(cpfAutenticado);
        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();

            // Verificar se o nome de usuário ou email já está em uso
            if (usuarioRepository.findByNomeDeUsuario(usuarioAtualizado.getNomeDeUsuario()).isPresent() &&
                    !usuario.getNomeDeUsuario().equals(usuarioAtualizado.getNomeDeUsuario())) {
                throw new IllegalArgumentException("Já existe uma conta com esse nome de usuário.");
            }

            if (usuarioRepository.findByEmail(usuarioAtualizado.getEmail()).isPresent() &&
                    !usuario.getEmail().equals(usuarioAtualizado.getEmail())) {
                throw new IllegalArgumentException("Já existe uma conta vinculada a esse email.");
            }

            usuario.setNome(usuarioAtualizado.getNome());
            usuario.setNomeDeUsuario(usuarioAtualizado.getNomeDeUsuario());
            usuario.setEmail(usuarioAtualizado.getEmail());
            usuario.setContato(usuarioAtualizado.getContato());
            usuario.setEndereco(usuarioAtualizado.getEndereco());

            return usuarioRepository.save(usuario);
        } else {
            throw new RuntimeException("Usuário não encontrado!");
        }
    }

    // Obter usuário por username
    public Optional<Usuario> obterUsuarioPorUsername(String username) {
        return usuarioRepository.findByNomeDeUsuario(username);
    }

    // Deletar o usuário logado
    public void deletarUsuario(String cpfAutenticado, String usuarioAutenticadoCpf) {
        if (!cpfAutenticado.equals(usuarioAutenticadoCpf)) {
            throw new IllegalArgumentException("Você só pode deletar a sua própria conta.");
        }
        usuarioRepository.deleteById(cpfAutenticado);
    }
}