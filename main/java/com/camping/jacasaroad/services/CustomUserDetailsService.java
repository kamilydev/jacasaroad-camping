package com.camping.jacasaroad.services;

import com.camping.jacasaroad.models.Role;
import com.camping.jacasaroad.models.Usuario;
import com.camping.jacasaroad.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNomeDeUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        String[] roles = usuario.getRoles().stream()
                .map(Role::getNome)  // Atribui os nomes das roles
                .toArray(String[]::new);

        return User.builder()
                .username(usuario.getNomeDeUsuario())
                .password(usuario.getSenha())
                .roles(roles)  // Atribui as roles do usuário
                .build();
    }
}