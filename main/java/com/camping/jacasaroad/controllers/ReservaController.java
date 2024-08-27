package com.camping.jacasaroad.controllers;

import com.camping.jacasaroad.models.Espaco;
import com.camping.jacasaroad.models.Reserva;
import com.camping.jacasaroad.models.StatusReserva;
import com.camping.jacasaroad.models.Usuario;
import com.camping.jacasaroad.repository.EspacoRepository;
import com.camping.jacasaroad.repository.ReservaRepository;
import com.camping.jacasaroad.repository.UsuarioRepository;
import com.camping.jacasaroad.services.ReservaService;
import com.camping.jacasaroad.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EspacoRepository espacoRepository;
    @Autowired
    private ReservaRepository reservaRepository;

    // Criar uma reserva
    @PostMapping("/criar")
    public String criarReserva(
            @RequestParam String nomeDeUsuario,
            @RequestParam Long espacoId,
            @RequestParam LocalDate dataInicio,
            @RequestParam LocalDate dataFim,
            Model model) {

        Usuario usuario = usuarioRepository.findByNomeDeUsuario(nomeDeUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Espaco espaco = espacoRepository.findById(espacoId)
                .orElseThrow(() -> new RuntimeException("Espaço não encontrado"));

        // Verificar se as datas são válidas
        if (dataInicio.isBefore(LocalDate.now())) {
            model.addAttribute("erro", "A data de início não pode ser anterior à data atual.");
            return "explorerVizualizarAnuncio";  // View com pop-up de erro
        }
        if (dataFim.isBefore(dataInicio)) {
            model.addAttribute("erro", "A data de término não pode ser anterior à data de início.");
            return "explorerVizualizarAnuncio";  // View com pop-up de erro
        }

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setEspaco(espaco);
        reserva.setDataInicio(dataInicio);
        reserva.setDataFim(dataFim);
        reserva.setStatus(StatusReserva.CONFIRMADA);  // Define o status como confirmada

        reservaRepository.save(reserva);
        model.addAttribute("reserva", reserva);
        return "explorerVizualizarAnuncio";  // View após a criação da reserva
    }

    // Listar reservas do usuário
    @GetMapping("/usuario/{nomeDeUsuario}")
    public String listarReservasDoUsuario(@PathVariable String nomeDeUsuario, Model model) {
        List<Reserva> reservas = reservaService.listarReservasDoUsuario(nomeDeUsuario);
        model.addAttribute("reservas", reservas);
        return "configMinhasReservas";  // View para listar as reservas do usuário
    }

    // Listar reservas dos anúncios de um anfitrião
    @GetMapping("/anfitriao/{nomeDeUsuarioAnfitriao}")
    public String listarReservasPorAnfitriao(@PathVariable String nomeDeUsuarioAnfitriao, Model model) {
        List<Reserva> reservas = reservaService.listarReservasPorAnfitriao(nomeDeUsuarioAnfitriao);
        model.addAttribute("reservas", reservas);
        return "adsMeusAnunciosAnfitriao";  // View para listar as reservas do anfitrião
    }

    // Cancelar reserva de um usuário
    @DeleteMapping("/cancelar/{reservaId}")
    public String cancelarReservaUsuario(
            @PathVariable Long reservaId,
            @RequestParam String nomeDeUsuario,
            Model model) {
        try {
            reservaService.cancelarReservaUsuario(reservaId, nomeDeUsuario);
            model.addAttribute("mensagem", "Reserva cancelada com sucesso.");
        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());  // Mensagem de erro caso falte menos de 15 dias
        }
        return "configMinhasReservas";  // View após o cancelamento da reserva
    }

    // Cancelar reserva de um anúncio pelo anfitrião
    @DeleteMapping("/anfitriao/cancelar/{reservaId}")
    public String cancelarReservaAnfitriao(
            @PathVariable Long reservaId,
            @RequestParam String nomeDeUsuarioAnfitriao,
            Model model) {
        try {
            reservaService.cancelarReservaAnfitriao(reservaId, nomeDeUsuarioAnfitriao);
            model.addAttribute("mensagem", "Reserva cancelada com sucesso.");
        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());  // Mensagem de erro caso falte menos de 15 dias
        }
        return "adsEspacosReservados";  // View após o cancelamento da reserva pelo anfitrião
    }

    // Atualizar reserva do usuário
    @PutMapping("/atualizar/{reservaId}")
    public String atualizarReserva(
            @PathVariable Long reservaId,
            @RequestParam LocalDate novaDataInicio,
            @RequestParam LocalDate novaDataFim,
            @RequestParam String nomeDeUsuario,
            Model model) {
        try {
            Reserva reservaAtualizada = reservaService.atualizarReservaUsuario(reservaId, nomeDeUsuario, novaDataInicio, novaDataFim);
            model.addAttribute("reserva", reservaAtualizada);
            model.addAttribute("mensagem", "Reserva atualizada com sucesso.");
        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());  // Mensagem de erro caso falte menos de 15 dias
        }
        return "configMinhasReservas";  // View de pop-up após a atualização da reserva
    }
}