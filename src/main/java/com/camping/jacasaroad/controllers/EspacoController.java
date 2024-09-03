package com.camping.jacasaroad.controllers;

import com.camping.jacasaroad.models.*;
import com.camping.jacasaroad.services.EspacoService;
import com.camping.jacasaroad.services.ImagemService;
import com.camping.jacasaroad.services.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("espaco")
public class EspacoController {

    @Autowired
    private EspacoService espacoService;

    @Autowired
    private ImagemService imagemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioService usuarioService;

    // Criar novo espaço
    @PostMapping("/criarEspaco")
    public ResponseEntity<Espaco> criarEspaco(
            @RequestParam("nomeLocal") String nomeLocal,
            @RequestParam("rua") String rua,
            @RequestParam("bairro") String bairro,
            @RequestParam("cidade") String cidade,
            @RequestParam("uf") String uf,
            @RequestParam("numero") String numero,
            @RequestParam("contato") String contato,
            @RequestParam("valorDiaria") String valorDiaria,
            @RequestParam("diasPossiveis") String diasPossiveis,
            @RequestParam("descricao") String descricao,
            @RequestParam("tipo") String tipo,
            @RequestParam("servicosOfertados") String servicosOfertadosJson,
            @RequestParam("imagens") List<String> imagensUrls) {  // Recebe uma lista de URLs como strings

        try {
            // Capturar o usuário logado
            String nomeDeUsuario = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

            // Buscar o usuário pelo nome de usuário
            Usuario anfitriao = usuarioService.obterUsuarioPorUsername(nomeDeUsuario).orElseThrow(() ->
                    new RuntimeException("Usuário não encontrado"));

            // Converter a String JSON em um objeto ServicosOfertados
            ServicosOfertados servicosOfertados = objectMapper.readValue(servicosOfertadosJson, ServicosOfertados.class);

            // Criar o objeto Espaco
            Espaco espaco = new Espaco();
            espaco.setNomeLocal(nomeLocal);
            espaco.setRua(rua);
            espaco.setBairro(bairro);
            espaco.setCidade(cidade);
            espaco.setUf(UF.valueOf(uf));
            espaco.setNumero(Integer.parseInt(numero));
            espaco.setContato(contato);
            espaco.setValorDiaria(new BigDecimal(valorDiaria));
            espaco.setDiasPossiveis(diasPossiveis);
            espaco.setDescricao(descricao);
            espaco.setTipo(TipoEspaco.valueOf(tipo));
            espaco.setServicosOfertados(servicosOfertados);
            espaco.setAnfitriao(anfitriao); // Associando o usuário logado como anfitrião

            Espaco novoEspaco = espacoService.criarEspaco(espaco);

            // Adicionar imagens ao novo espaço usando URLs
            for (String imagemUrl : imagensUrls) {
                if (imagemUrl != null && !imagemUrl.isEmpty()) {
                    Imagem novaImagem = new Imagem();
                    novaImagem.setEspaco(novoEspaco);
                    novaImagem.setUrl(imagemUrl);
                    imagemService.adicionarImagem(novaImagem);
                }
            }

            return ResponseEntity.ok(novoEspaco);
        } catch (RuntimeException | IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }



    // Exibir formulário de atualização de espaço com imagens
    @GetMapping("/viewAtualizar/{id}")
    public ResponseEntity<Espaco> exibirFormularioAtualizacao(@PathVariable Long id) {
        Espaco espaco = espacoService.obterEspacoPorId(id)
                .orElseThrow(() -> new RuntimeException("Espaço não encontrado!"));

        return ResponseEntity.ok(espaco);
    }

    @PostMapping("/atualizar/{id}")
    public ResponseEntity<Espaco> atualizarEspaco(@PathVariable Long id,
                                                  @ModelAttribute Espaco espacoAtualizado,
                                                  @RequestParam("imagens") List<MultipartFile> novasImagens,
                                                  @RequestParam(value = "imagensExistentes", required = false) List<Long> imagensExistentesIds,
                                                  @RequestParam(value = "imagensParaDeletar", required = false) List<Long> imagensParaDeletarIds) {
        try {
            Espaco espaco = espacoService.atualizarEspaco(id, espacoAtualizado);

            if (imagensParaDeletarIds != null) {
                for (Long imagemId : imagensParaDeletarIds) {
                    imagemService.deletarImagem(imagemId);
                }
            }

            if (novasImagens != null && !novasImagens.isEmpty()) {
                for (MultipartFile novaImagem : novasImagens) {
                    if (!novaImagem.isEmpty()) {
                        Imagem imagem = new Imagem();
                        imagem.setEspaco(espaco);
                        imagem.setUrl(imagemService.salvarImagem(novaImagem));
                        imagemService.adicionarImagem(imagem);
                    }
                }
            }

            return ResponseEntity.ok(espaco);
        } catch (RuntimeException | IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Deletar um espaço e suas imagens
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarEspaco(@PathVariable Long id) {
        try {
            espacoService.deletarEspaco(id);
            return ResponseEntity.ok("Espaço deletado com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro ao deletar o espaço: " + e.getMessage());
        }
    }

    // Listar todos os espaços disponíveis na base de dados
    @GetMapping("/listarEspacos")
    public ResponseEntity<List<Espaco>> listarTodosEspacos() {
        List<Espaco> espacos = espacoService.listarTodosEspacos();
        return ResponseEntity.ok(espacos);
    }

    // Listar todos os espaços criados por um usuário específico (pelo nome de usuário do anfitrião)
    @GetMapping("/anfitriao/{nomeDeUsuario}")
    public ResponseEntity<List<Espaco>> listarEspacosPorAnfitriao(@PathVariable String nomeDeUsuario) {
        List<Espaco> espacos = espacoService.listarEspacosPorAnfitriao(nomeDeUsuario);
        return ResponseEntity.ok(espacos);
    }

    // Filtrar espaços por múltiplos critérios
    @GetMapping("/filtrar")
    public ResponseEntity<List<Espaco>> filtrarEspacos(
            @RequestParam(required = false) String nomeLocal,
            @RequestParam(required = false) String rua,
            @RequestParam(required = false) String bairro,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String uf,
            @RequestParam(required = false) Integer numero,
            @RequestParam(required = false) String contato,
            @RequestParam(required = false) BigDecimal precoMinimo,
            @RequestParam(required = false) BigDecimal precoMaximo,
            @RequestParam(required = false) String diasPossiveis,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Boolean banheiroCompartilhado,
            @RequestParam(required = false) Boolean armario,
            @RequestParam(required = false) Boolean abastecimentoEletrico,
            @RequestParam(required = false) Boolean eletricidade,
            @RequestParam(required = false) Boolean abastecimentoHidrico,
            @RequestParam(required = false) Boolean pointDescarteSaneamento,
            @RequestParam(required = false) Boolean lavagem,
            @RequestParam(required = false) Boolean calibracaoPneus,
            @RequestParam(required = false) Boolean lojaConveniencia,
            @RequestParam(required = false) Boolean piscina,
            @RequestParam(required = false) Boolean petFriendly,
            @RequestParam(required = false) Boolean restauranteProximo,
            @RequestParam(required = false) Boolean abastecimentoCombustivelProximo,
            @RequestParam(required = false) Boolean abastecimentoCarrosEletricos) {

        UF estado = uf != null ? UF.valueOf(uf.toUpperCase()) : null;
        TipoEspaco tipoEspaco = tipo != null ? TipoEspaco.valueOf(tipo.toUpperCase()) : null;

        List<Espaco> espacos = espacoService.filtrarEspacos(
                nomeLocal, rua, bairro, cidade, estado, numero, contato, precoMinimo, precoMaximo,
                diasPossiveis, descricao, tipoEspaco, banheiroCompartilhado, armario, abastecimentoEletrico,
                eletricidade, abastecimentoHidrico, pointDescarteSaneamento, lavagem, calibracaoPneus, lojaConveniencia,
                piscina, petFriendly, restauranteProximo, abastecimentoCombustivelProximo, abastecimentoCarrosEletricos);

        return ResponseEntity.ok(espacos);
    }
}