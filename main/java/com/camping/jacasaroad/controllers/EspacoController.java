package com.camping.jacasaroad.controllers;

import com.camping.jacasaroad.models.Espaco;
import com.camping.jacasaroad.models.Imagem;
import com.camping.jacasaroad.models.TipoEspaco;
import com.camping.jacasaroad.models.UF;
import com.camping.jacasaroad.services.EspacoService;
import com.camping.jacasaroad.services.ImagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/espacos")
public class EspacoController {

    @Autowired
    private EspacoService espacoService;
    @Autowired
    private ImagemService imagemService;


    @GetMapping("/anunciar")
    public String adsCriarAnuncio() {
        return "adsCriarAnuncio";
    }

    @GetMapping("/meusAnuncios")
    public String adsMeusAnunciosAnfitriao() {
        return "adsMeusAnunciosAnfitriao";
    }

    //criar novo espaco
    @PostMapping("/criar")
    public String criarEspaco(@ModelAttribute Espaco espaco,
                              @RequestParam("imagens") List<MultipartFile> imagens,
                              Model model) {
        try {
            // Criação do novo espaço
            Espaco novoEspaco = espacoService.criarEspaco(espaco);

            // Adicionar imagens ao novo espaço
            for (MultipartFile imagem : imagens) {
                if (!imagem.isEmpty()) {
                    Imagem novaImagem = new Imagem();
                    novaImagem.setEspaco(novoEspaco);
                    novaImagem.setUrl(imagemService.salvarImagem(imagem));  // Método para salvar a imagem
                    imagemService.adicionarImagem(novaImagem);
                }
            }

            model.addAttribute("espaco", novoEspaco);
            return "redirect:/espacos/anunciar";  // Redireciona após sucesso

        } catch (RuntimeException | IOException e) {
            // Em caso de erro, adicionar a mensagem de erro ao modelo
            model.addAttribute("erro", "Erro ao criar o espaço: " + e.getMessage());

            // Manter os dados preenchidos pelo usuário no formulário
            model.addAttribute("espaco", espaco);

            // Retornar ao formulário mantendo os dados preenchidos para edição
            return "adsCriarAnuncio";  // Retorna ao formulário com mensagem de erro e dados preenchidos
        }
    }

    // Exibir formulário de atualização de espaço com imagens
    @GetMapping("/viewAtualizar/{id}")
    public String exibirFormularioAtualizacao(@PathVariable Long id, Model model) {
        Espaco espaco = espacoService.obterEspacoPorId(id)
                .orElseThrow(() -> new RuntimeException("Espaço não encontrado!"));

        // Carregar as imagens associadas ao espaço
        List<Imagem> imagens = imagemService.listarImagensPorEspaco(id);

        model.addAttribute("espaco", espaco);
        model.addAttribute("imagens", imagens);

        return "adsAtualizarAnuncio";  // View para o formulário de atualização
    }

    @PostMapping("/atualizar/{id}")
    public String atualizarEspaco(@PathVariable Long id,
                                  @ModelAttribute Espaco espacoAtualizado,
                                  @RequestParam("imagens") List<MultipartFile> novasImagens,
                                  @RequestParam(value = "imagensExistentes", required = false) List<Long> imagensExistentesIds,
                                  @RequestParam(value = "imagensParaDeletar", required = false) List<Long> imagensParaDeletarIds,
                                  Model model) {
        try {
            // Atualizar o espaço com as novas informações
            Espaco espaco = espacoService.atualizarEspaco(id, espacoAtualizado);

            // Excluir imagens selecionadas para exclusão
            if (imagensParaDeletarIds != null) {
                for (Long imagemId : imagensParaDeletarIds) {
                    imagemService.deletarImagem(imagemId);
                }
            }

            // Adicionar novas imagens
            if (novasImagens != null && !novasImagens.isEmpty()) {
                for (MultipartFile novaImagem : novasImagens) {
                    if (!novaImagem.isEmpty()) {
                        Imagem imagem = new Imagem();
                        imagem.setEspaco(espaco);
                        imagem.setUrl(imagemService.salvarImagem(novaImagem));  // Método para salvar a imagem no sistema
                        imagemService.adicionarImagem(imagem);
                    }
                }
            }

            model.addAttribute("espaco", espaco);
            return "redirect:/espacos/meusAnuncios";  // Redireciona após sucesso

        } catch (RuntimeException | IOException e) {
            // Manter os dados preenchidos no formulário em caso de erro
            model.addAttribute("erro", "Erro ao atualizar o espaço: " + e.getMessage());
            model.addAttribute("espaco", espacoAtualizado);

            // Recuperar as imagens existentes para exibição no formulário
            List<Imagem> imagensExistentes = imagemService.listarImagensPorEspaco(id);
            model.addAttribute("imagensExistentes", imagensExistentes);

            return "adsAtualizarAnuncio";  // Retorna ao formulário de atualização com erro e dados preenchidos
        }
    }

    // Deletar um espaço e suas imagens sem redirecionamento
    @DeleteMapping("/deletar/{id}")
    @ResponseBody
    public ResponseEntity<String> deletarEspaco(@PathVariable Long id) {
        try {
            espacoService.deletarEspaco(id);
            return ResponseEntity.ok("Espaço deletado com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro ao deletar o espaço: " + e.getMessage());
        }
    }

    // Listar todos os espaços disponíveis na base de dados
    @GetMapping
    public String listarTodosEspacos(Model model) {
        List<Espaco> espacos = espacoService.listarTodosEspacos();
        model.addAttribute("espacos", espacos);
        return "explorerVisualizarTodosAnuncios";  // Exibe todos os anúncios
    }

    // Listar todos os espaços criados por um usuário específico (pelo nome de usuário do anfitrião)
    @GetMapping("/anfitriao/{nomeDeUsuario}")
    public String listarEspacosPorAnfitriao(@PathVariable String nomeDeUsuario, Model model) {
        List<Espaco> espacos = espacoService.listarEspacosPorAnfitriao(nomeDeUsuario);
        model.addAttribute("espacos", espacos);
        model.addAttribute("anfitriao", nomeDeUsuario);  // Adiciona o nome do anfitrião
        return "explorerVisualizarTodosAnuncios";  // Retorna a view com a lista de espaços criados pelo anfitrião
    }

    // Filtrar espaços por múltiplos critérios
    @GetMapping("/filtrar")
    public String filtrarEspacos(
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
            @RequestParam(required = false) Boolean abastecimentoCarrosEletricos,
            Model model) {

        UF estado = uf != null ? UF.valueOf(uf.toUpperCase()) : null;
        TipoEspaco tipoEspaco = tipo != null ? TipoEspaco.valueOf(tipo.toUpperCase()) : null;

        List<Espaco> espacos = espacoService.filtrarEspacos(
                nomeLocal, rua, bairro, cidade, estado, numero, contato, precoMinimo, precoMaximo,
                diasPossiveis, descricao, tipoEspaco, banheiroCompartilhado, armario, abastecimentoEletrico,
                eletricidade, abastecimentoHidrico, pointDescarteSaneamento, lavagem, calibracaoPneus, lojaConveniencia,
                piscina, petFriendly, restauranteProximo, abastecimentoCombustivelProximo, abastecimentoCarrosEletricos);

        model.addAttribute("espacos", espacos);
        return "explorerVisualizarTodosAnuncios";  // Exibe os resultados filtrados
    }
}