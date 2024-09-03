package com.camping.jacasaroad.dto;

import com.camping.jacasaroad.models.Espaco;
import com.camping.jacasaroad.models.StatusReserva;
import com.camping.jacasaroad.models.Usuario;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record ReservaRecordDto (
        @FutureOrPresent LocalDate dataInicio,
        @Future LocalDate dataFim,
        @NotBlank StatusReserva status,
        Espaco espaco,
        Usuario usuario
) {
}
