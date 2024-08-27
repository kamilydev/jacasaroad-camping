package com.camping.jacasaroad.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "espacos")
public class Espaco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeLocal;

    @Column(nullable = false)
    private String rua;

    @Column(nullable = false)
    private String bairro;

    @Column(nullable = false)
    private String cidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UF uf;

    @Column(nullable = false)
    private int numero;

    @Size(max = 13)
    @Column(nullable = false)
    private String contato;

    @Column(precision = 38, scale = 2)
    @NotNull
    private BigDecimal valorDiaria;

    @Column(nullable = false)
    private String diasPossiveis;

    @Size(max = 255)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEspaco tipo;

    @Embedded
    private ServicosOfertados servicosOfertados; // substitui todos os booleans

    @ManyToOne
    @JoinColumn(name = "cpf_anfitriao")
    private Usuario anfitriao;


    public Espaco(String nomeLocal, String rua, String bairro, String cidade, UF uf, int numero, String contato, BigDecimal valorDiaria, String diasPossiveis, String descricao, TipoEspaco tipo, Usuario anfitriao,ServicosOfertados servicosOfertados) {
        this.nomeLocal = nomeLocal;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.numero = numero;
        this.contato = contato;
        this.valorDiaria = valorDiaria;
        this.diasPossiveis = diasPossiveis;
        this.descricao = descricao;
        this.tipo = tipo;
        this.anfitriao = anfitriao;
        this.servicosOfertados = servicosOfertados;
    }

    @OneToMany(mappedBy = "espaco", cascade = CascadeType.ALL)
    private List<Imagem> imagens;
}
