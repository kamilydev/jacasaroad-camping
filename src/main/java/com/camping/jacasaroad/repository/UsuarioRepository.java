package com.camping.jacasaroad.repository;

import com.camping.jacasaroad.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    // Buscar por Username
    Optional<Usuario> findByNomeDeUsuario(String nomeDeUsuario);  // Corrigido para usar o nome correto da propriedade

    // Buscar por Email
    Optional<Usuario> findByEmail(String email);

    // Buscar por CPF
    Optional<Usuario> findByCpf(String cpf);  // CPF é a chave primária, mas podemos manter este método se necessário
}