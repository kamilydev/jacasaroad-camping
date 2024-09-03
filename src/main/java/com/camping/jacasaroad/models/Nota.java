package com.camping.jacasaroad.models;

public enum Nota {
    UM(1),
    DOIS(2),
    TRES(3),
    QUATRO(4),
    CINCO(5);

    private final int valor;

    Nota(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    // Método para obter o enum pela nota
    public static Nota fromValor(int valor) {
        for (Nota nota : Nota.values()) {
            if (nota.getValor() == valor) {
                return nota;
            }
        }
        throw new IllegalArgumentException("Valor inválido para Nota: " + valor);
    }
}
