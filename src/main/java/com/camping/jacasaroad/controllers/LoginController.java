package com.camping.jacasaroad.controllers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    private final AuthenticationManager authenticationManager;

    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public ModelAndView logar(@RequestParam String nomeDeUsuario, @RequestParam String senha) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(nomeDeUsuario, senha);
            Authentication auth = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(auth);

            // Se o login for bem-sucedido, redireciona para a página desejada
            modelAndView.setViewName("redirect:/homeLogin");
        } catch (BadCredentialsException e) {
            // Se o login falhar, retorna para a página de login com uma mensagem de erro
            modelAndView.setViewName("userLogin");
            modelAndView.addObject("errorMessage", "Nome de usuário ou senha inválidos");
        }

        return modelAndView;
    }
}
