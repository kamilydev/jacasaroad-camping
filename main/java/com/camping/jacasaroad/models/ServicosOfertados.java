package com.camping.jacasaroad.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ServicosOfertados {
    private boolean banheiroCompartilhado;
    private boolean armario;
    private boolean abastecimentoEletrico;
    private boolean eletricidade;
    private boolean abastecimentoHidrico;
    private boolean pointDescarteSaneamento;
    private boolean lavagem;
    private boolean calibracaoPneus;
    private boolean lojaConveniencia;
    private boolean piscina;
    private boolean petFriendly;
    private boolean restauranteProximo;
    private boolean abastecimentoCombustivelProximo;
    private boolean abastecimentoCarrosEletricos;
}
