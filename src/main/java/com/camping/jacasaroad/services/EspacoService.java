package com.camping.jacasaroad.services;

import com.camping.jacasaroad.dto.EspacoRecordDto;
import com.camping.jacasaroad.dto.ImagemRecordDto;
import com.camping.jacasaroad.models.Espaco;
import com.camping.jacasaroad.models.TipoEspaco;
import com.camping.jacasaroad.models.UF;
import com.camping.jacasaroad.repository.EspacoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EspacoService {

    @Autowired
    private EspacoRepository espacoRepository;

    // Criar um novo espaço
    public Espaco criarEspaco(Espaco espaco) {
        return espacoRepository.save(espaco);
    }

    // Atualizar um espaço existente
    public Espaco atualizarEspaco(Long id, Espaco espacoAtualizado) {
        Espaco espaco = espacoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Espaço não encontrado!"));

        copiarDadosEspaco(espacoAtualizado, espaco);
        return espacoRepository.save(espaco);
    }

    // Obter espaço por ID
    public Optional<Espaco> obterEspacoPorId(Long id) {
        return espacoRepository.findById(id);
    }

    // Copiar dados do espaço atualizado
    private void copiarDadosEspaco(Espaco espacoAtualizado, Espaco espaco) {
        espaco.setNomeLocal(espacoAtualizado.getNomeLocal());
        espaco.setRua(espacoAtualizado.getRua());
        espaco.setBairro(espacoAtualizado.getBairro());
        espaco.setCidade(espacoAtualizado.getCidade());
        espaco.setUf(espacoAtualizado.getUf());
        espaco.setNumero(espacoAtualizado.getNumero());
        espaco.setContato(espacoAtualizado.getContato());
        espaco.setValorDiaria(espacoAtualizado.getValorDiaria());
        espaco.setDiasPossiveis(espacoAtualizado.getDiasPossiveis());
        espaco.setDescricao(espacoAtualizado.getDescricao());
        espaco.setTipo(espacoAtualizado.getTipo());
        espaco.setServicosOfertados(espacoAtualizado.getServicosOfertados());
    }

    // Deletar um espaço
    public void deletarEspaco(Long id) {
        espacoRepository.deleteById(id);
    }

    // Listar todos os espaços criados por um usuário específico (pelo nome de usuário do anfitrião)
    public List<Espaco> listarEspacosPorAnfitriao(String nomeDeUsuario) {
        return espacoRepository.findByAnfitriaoNomeDeUsuario(nomeDeUsuario);
    }

    // Listar todos os espaços
    public List<Espaco> listarTodosEspacos() {
        return espacoRepository.findAll();
    }

    // Filtrar espaços por múltiplos critérios
    public List<Espaco> filtrarEspacos(
            String nomeLocal, String rua, String bairro, String cidade, UF estado, Integer numero, String contato,
            BigDecimal precoMinimo, BigDecimal precoMaximo, String diasPossiveis, String descricao, TipoEspaco tipo,
            Boolean banheiroCompartilhado, Boolean armario, Boolean abastecimentoEletrico, Boolean eletricidade,
            Boolean abastecimentoHidrico, Boolean pointDescarteSaneamento, Boolean lavagem, Boolean calibracaoPneus,
            Boolean lojaConveniencia, Boolean piscina, Boolean petFriendly, Boolean restauranteProximo,
            Boolean abastecimentoCombustivelProximo, Boolean abastecimentoCarrosEletricos) {

        Specification<Espaco> spec = Specification.where(null);

        if (nomeLocal != null && !nomeLocal.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("nomeLocal")), "%" + nomeLocal.toLowerCase() + "%"));
        }

        if (bairro != null && !bairro.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("bairro")), "%" + bairro.toLowerCase() + "%"));
        }

        if (estado != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("uf"), estado));
        }

        if (precoMinimo != null && precoMaximo != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(root.get("valorDiaria"), precoMinimo, precoMaximo));
        }

        // Filtros baseados em serviços ofertados
        if (banheiroCompartilhado != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("servicosOfertados").get("banheiroCompartilhado"), banheiroCompartilhado));
        }

        if (armario != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("servicosOfertados").get("armario"), armario));
        }

        if (abastecimentoEletrico != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("servicosOfertados").get("abastecimentoEletrico"), abastecimentoEletrico));
        }

        if (eletricidade != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("servicosOfertados").get("eletricidade"), eletricidade));
        }

        if (abastecimentoHidrico != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("servicosOfertados").get("abastecimentoHidrico"), abastecimentoHidrico));
        }

        if (pointDescarteSaneamento != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("servicosOfertados").get("pointDescarteSaneamento"), pointDescarteSaneamento));
        }

        if (lavagem != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("servicosOfertados").get("lavagem"), lavagem));
        }

        if (calibracaoPneus != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("servicosOfertados").get("calibracaoPneus"), calibracaoPneus));
        }

        if (lojaConveniencia != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("servicosOfertados").get("lojaConveniencia"), lojaConveniencia));
        }

        if (piscina != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("servicosOfertados").get("piscina"), piscina));
        }

        if (petFriendly != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("servicosOfertados").get("petFriendly"), petFriendly));
        }

        if (restauranteProximo != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("servicosOfertados").get("restauranteProximo"), restauranteProximo));
        }

        if (abastecimentoCombustivelProximo != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("servicosOfertados").get("abastecimentoCombustivelProximo"), abastecimentoCombustivelProximo));
        }

        if (abastecimentoCarrosEletricos != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("servicosOfertados").get("abastecimentoCarrosEletricos"), abastecimentoCarrosEletricos));
        }

        return espacoRepository.findAll(spec);
    }

}