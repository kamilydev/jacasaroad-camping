package com.camping.jacasaroad.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    private String nomeLocal;

    private String rua;

    private String bairro;

    private String cidade;

    @Enumerated(EnumType.STRING)
    private UF uf;

    private int numero;

    private String contato;

    private BigDecimal valorDiaria;

    private String diasPossiveis;

    private String descricao;

    @Enumerated(EnumType.STRING)
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

    @JsonManagedReference
    @OneToMany(mappedBy = "espaco", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Imagem> imagens;
}
