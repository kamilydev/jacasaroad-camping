package com.camping.jacasaroad.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {  // Implementa UserDetails
    @Id
    @NotNull
    @Size(max = 11)
    private String cpf;

    @NotNull
    private String nome;

    @NotNull
    @Column(unique = true)
    private String nomeDeUsuario;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    private String senha;

    @NotNull
    @Size(max = 13)
    private String contato;

    @NotNull
    private String endereco;

    private String imagemPerfil;



    // Construtor sem imagemPerfil (assume imagem padrão)
    public Usuario(String cpf, String nome, String nomeDeUsuario, String email, String senha, String contato, String endereco) {
        this.cpf = cpf;
        this.nome = nome;
        this.nomeDeUsuario = nomeDeUsuario;
        this.email = email;
        this.senha = senha;
        this.contato = contato;
        this.endereco = endereco;
        this.imagemPerfil = "default-image-perfil.jpg"; // Imagem padrão
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // lista vazia
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.nomeDeUsuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

}
