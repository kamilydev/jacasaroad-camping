package com.camping.jacasaroad.controllers;

import com.camping.jacasaroad.models.Role;
import com.camping.jacasaroad.models.Usuario;
import com.camping.jacasaroad.repository.UsuarioRepository;
import com.camping.jacasaroad.services.ImagemPerfilService;
import com.camping.jacasaroad.services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ImagemPerfilService imagemPerfilService;



    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Criar um novo usuário
    @PostMapping("/newUser")
    public ResponseEntity<String> criarNovoUsuario(@RequestBody Usuario usuario) {

        // Criptografando a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        // Salvando o usuário com a role atribuída
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuário criado com sucesso.");
    }

    // Exibir formulário de atualização do usuário com dados e imagem de perfil preenchidos
    @GetMapping("/viewUpdateUser")
    public ResponseEntity<Usuario> exibirFormularioAtualizacao(Principal principal) {
        String nomeDeUsuario = principal.getName();
        Optional<Usuario> usuarioOpt = usuarioService.obterUsuarioPorUsername(nomeDeUsuario);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String imagemPerfilUrl = imagemPerfilService.consultarImagemPerfil(usuario.getCpf());
            usuario.setImagemPerfil(imagemPerfilUrl);
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PutMapping("/updateUser")
    public ResponseEntity<String> atualizarPerfil(@RequestBody Usuario usuarioAtualizado, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }

        String nomeDeUsuario = principal.getName();
        try {
            Usuario usuario = usuarioService.atualizarUsuario(usuarioAtualizado.getCpf(), usuarioAtualizado, nomeDeUsuario);
            usuario = imagemPerfilService.adicionarOuAtualizarImagemPerfil(usuario.getCpf(), usuarioAtualizado.getImagemPerfil());
            return ResponseEntity.ok("Perfil atualizado com sucesso.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/home")
    public ResponseEntity<String> home(Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok("Redirecionando para homeLogin...");
        } else {
            return ResponseEntity.ok("Redirecionando para homeLogout...");
        }
    }

    // Processar login
    @GetMapping("/login")
    public ResponseEntity<String> logar(@RequestParam String nomeDeUsuario, @RequestParam String senha) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(nomeDeUsuario, senha);
            Authentication auth = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
            return ResponseEntity.ok("Login bem-sucedido. Redirecionando...");
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Nome de usuário ou senha inválidos");
        }
    }

    // Processar logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok("Logout bem-sucedido. Redirecionando...");
    }
}