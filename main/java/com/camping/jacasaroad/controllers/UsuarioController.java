package com.camping.jacasaroad.controllers;

import com.camping.jacasaroad.models.Usuario;
import com.camping.jacasaroad.services.ImagemPerfilService;
import com.camping.jacasaroad.services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ImagemPerfilService imagemPerfilService;

    // Exibir formulário de criação de novo usuário
    @GetMapping("/viewNewUser")
    public String exibirFormularioNovoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "userCadastroUsuario";  // Nome do template Thymeleaf para o formulário de criação
    }

    // Criar um novo usuário
    @PostMapping("/newUser")
    public String criarUsuario(@ModelAttribute Usuario usuario, Model model) {
        try {
            Usuario novoUsuario = usuarioService.criarUsuario(usuario);
            model.addAttribute("usuario", novoUsuario);
            return "userLogin";  // Redireciona para página de login
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "userCadastroUsuario";  // Retorna ao formulário de criação com erro
        }
    }

    // Exibir formulário de atualização do usuário com dados e imagem de perfil preenchidos
    @GetMapping("/viewUpdateUser")
    public String exibirFormularioAtualizacao(Model model, Principal principal) {
        // Obter o nome de usuário da sessão atual
        String nomeDeUsuario = principal.getName();

        // Buscar o usuário pelo nome de usuário
        Optional<Usuario> usuarioOpt = usuarioService.obterUsuarioPorUsername(nomeDeUsuario);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // Carregar a imagem de perfil a partir do banco de dados
            String imagemPerfilUrl = imagemPerfilService.consultarImagemPerfil(usuario.getCpf());
            model.addAttribute("usuario", usuario);
            model.addAttribute("imagemPerfilUrl", imagemPerfilUrl);

            return "userAtualizarUsuario";  // Exibição e atualização de dados e imagem do usuário
        } else {
            model.addAttribute("erro", "Usuário não encontrado.");
            return "erroDeBusca";  // Página de erro
        }
    }

    // Atualizar dados pessoais e imagem de perfil em um único formulário
    @PostMapping("/updateUser")
    public String atualizarPerfil(@ModelAttribute Usuario usuarioAtualizado,
                                  @RequestParam("imagemUrl") String novaImagemUrl,
                                  Principal principal,
                                  Model model) {
        String nomeDeUsuario = principal.getName();

        try {
            // Atualizar dados do usuário
            Usuario usuario = usuarioService.atualizarUsuario(usuarioAtualizado.getCpf(), usuarioAtualizado, nomeDeUsuario);

            // Atualizar imagem de perfil
            usuario = imagemPerfilService.adicionarOuAtualizarImagemPerfil(usuario.getCpf(), novaImagemUrl);

            model.addAttribute("usuario", usuario);
            model.addAttribute("mensagem", "Perfil atualizado com sucesso.");
            return "configDadosPessoais";  // View de perfil após a atualização
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "configDadosPessoais";  // Retorna ao formulário em caso de erro com pop-up
        }
    }

    @GetMapping("/home")
    public String home() {
        // Obtém o contexto de autenticação atual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se o usuário está autenticado
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            return "homeLogin";  // Redireciona para homeLogin.html se o usuário estiver autenticado
        } else {
            return "homeLogout";  // Redireciona para homeLogout.html se o usuário não estiver autenticado
        }
    }

    // Exibir formulário de login
    @GetMapping("/login")
    public String loginForm() {
        return "userLogin";  // Nome do template Thymeleaf para o login
    }

    // Processar login
    @PostMapping("/login")
    public String logar(@RequestParam String nomeDeUsuario, @RequestParam String senha, Model model) {
        try {
            // Autenticar o usuário
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(nomeDeUsuario, senha);
            Authentication auth = authenticationManager.authenticate(authToken);

            SecurityContextHolder.getContext().setAuthentication(auth);

            // Redireciona para a página inicial após login bem-sucedido
            return "redirect:/usuario/home";
        } catch (BadCredentialsException e) {
            model.addAttribute("erro", "Nome de usuário ou senha inválidos");
            return "userLogin";  // Retorna ao formulário de login com mensagem de erro
        }
    }

    // Processar logout
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Realizar logout
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        // Redirecionar para a página de login após o logout
        return "redirect:/usuario/login?logout=true";
    }
}