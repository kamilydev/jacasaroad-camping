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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("reserva")
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
    @PostMapping("/criarReserva")
    public ResponseEntity<String> criarReserva(
            @RequestParam String nomeDeUsuario,
            @RequestParam Long espacoId,
            @RequestParam LocalDate dataInicio,
            @RequestParam LocalDate dataFim) {

        Usuario usuario = usuarioRepository.findByNomeDeUsuario(nomeDeUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Espaco espaco = espacoRepository.findById(espacoId)
                .orElseThrow(() -> new RuntimeException("Espaço não encontrado"));

        if (dataInicio.isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().body("A data de início não pode ser anterior à data atual.");
        }
        if (dataFim.isBefore(dataInicio)) {
            return ResponseEntity.badRequest().body("A data de término não pode ser anterior à data de início.");
        }

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setEspaco(espaco);
        reserva.setDataInicio(dataInicio);
        reserva.setDataFim(dataFim);
        reserva.setStatus(StatusReserva.CONFIRMADA);

        reservaRepository.save(reserva);
        return ResponseEntity.ok("Reserva criada com sucesso.");
    }

    // Listar reservas do usuário
    @GetMapping("/{nomeDeUsuario}")
    public ResponseEntity<List<Reserva>> listarReservasDoUsuario(@PathVariable String nomeDeUsuario) {
        List<Reserva> reservas = reservaService.listarReservasDoUsuario(nomeDeUsuario);
        return ResponseEntity.ok(reservas);
    }

    // Listar reservas dos anúncios de um anfitrião
    @GetMapping("/anfitriao/{nomeDeUsuarioAnfitriao}")
    public ResponseEntity<List<Reserva>> listarReservasPorAnfitriao(@PathVariable String nomeDeUsuarioAnfitriao) {
        List<Reserva> reservas = reservaService.listarReservasPorAnfitriao(nomeDeUsuarioAnfitriao);
        return ResponseEntity.ok(reservas);
    }

    // Cancelar reserva de um usuário
    @DeleteMapping("/cancelar/{reservaId}")
    public ResponseEntity<String> cancelarReservaUsuario(
            @PathVariable Long reservaId,
            @RequestParam String nomeDeUsuario) {
        try {
            reservaService.cancelarReservaUsuario(reservaId, nomeDeUsuario);
            return ResponseEntity.ok("Reserva cancelada com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Cancelar reserva de um anúncio pelo anfitrião
    @DeleteMapping("/anfitriao/cancelar/{reservaId}")
    public ResponseEntity<String> cancelarReservaAnfitriao(
            @PathVariable Long reservaId,
            @RequestParam String nomeDeUsuarioAnfitriao) {
        try {
            reservaService.cancelarReservaAnfitriao(reservaId, nomeDeUsuarioAnfitriao);
            return ResponseEntity.ok("Reserva cancelada com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Atualizar reserva do usuário
    @PutMapping("/atualizar/{reservaId}")
    public ResponseEntity<String> atualizarReserva(
            @PathVariable Long reservaId,
            @RequestParam LocalDate novaDataInicio,
            @RequestParam LocalDate novaDataFim,
            @RequestParam String nomeDeUsuario) {
        try {
            Reserva reservaAtualizada = reservaService.atualizarReservaUsuario(reservaId, nomeDeUsuario, novaDataInicio, novaDataFim);
            return ResponseEntity.ok("Reserva atualizada com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
