package com.camping.jacasaroad.models;

public enum TipoEspaco {
    CAMPING("Camping"),
    TRAILER_FIXO("Trailer Fixo"),
    TRAILER_MOVEL("Trailer Móvel"),
    ESTACIONAMENTO("Estacionamento");

    private final String descricao;

    TipoEspaco(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
