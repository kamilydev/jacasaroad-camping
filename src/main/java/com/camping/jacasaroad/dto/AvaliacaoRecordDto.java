package com.camping.jacasaroad.dto;

import com.camping.jacasaroad.models.Espaco;
import com.camping.jacasaroad.models.Nota;
import com.camping.jacasaroad.models.Usuario;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AvaliacaoRecordDto (@NotBlank @Size(max=255) String comentario,
                                  @NotNull @Min(1) int notaValor,
                                  @NotBlank LocalDate dataAvaliacao,
                                  @NotBlank Usuario usuario,
                                  @NotBlank Espaco espaco,
                                  Nota nota) {
}
