package com.camping.jacasaroad.dto;


import com.camping.jacasaroad.models.*;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record EspacoRecordDto (@NotBlank String nomeLocal,
                               @NotBlank String rua,
                               @NotBlank String bairro,
                               @NotBlank String cidade,
                               @NotBlank UF uf,
                               @NotNull @Min(1) int numero,
                               @NotBlank @Size(max = 13) String contato,
                               @Column(precision = 38, scale = 2) @NotNull BigDecimal valorDiaria,
                               @NotBlank String diasPossiveis,
                               @Size(max = 255)String descricao,
                               @NotBlank TipoEspaco tipo,
                               @NotBlank ServicosOfertados servicosOfertados,
                               @NotBlank Usuario anfitriao,
                               List<Imagem> imagens){
}
