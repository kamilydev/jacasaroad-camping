package com.camping.jacasaroad.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record UsuarioRecordDto(@NotBlank @Size(max = 11) @Pattern(regexp = "^\\d{11}$\n", message = "O CPF deve conter exatamente 11 dígitos e ser composto apenas por números. Não utilize separadores") String cpf,
                               @NotBlank String nome,
                               @NotBlank @Column(unique = true) String nomeDeUsuario,
                               @NotBlank @Column(unique = true) @Email String email,
                               @NotBlank String senha,
                               @NotBlank @Pattern(regexp = "^\\(?(\\d{2})\\)?\\s?9\\d{4}-?\\d{4}$", message =  "O número de contato deve seguir o formato válido (incluindo DDD e o '9' na frente)") String contato,
                               @NotBlank String endereco,
                               String imagemPerfil) {

}
