package com.camping.jacasaroad.services;

import com.camping.jacasaroad.models.Reserva;
import com.camping.jacasaroad.models.StatusReserva;
import com.camping.jacasaroad.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    // Listar reservas do usuário
    public List<Reserva> listarReservasDoUsuario(String nomeDeUsuario) {
        return reservaRepository.findByUsuarioNomeDeUsuario(nomeDeUsuario);
    }

    // Listar reservas dos anúncios de um anfitrião
    public List<Reserva> listarReservasPorAnfitriao(String nomeDeUsuarioAnfitriao) {
        return reservaRepository.findByEspacoAnfitriaoNomeDeUsuario(nomeDeUsuarioAnfitriao);
    }

    // Cancelar reserva de um usuário
    public void cancelarReservaUsuario(Long reservaId, String nomeDeUsuario) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        // Verificar se o usuário tem permissão
        if (!reserva.getUsuario().getNomeDeUsuario().equals(nomeDeUsuario)) {
            throw new RuntimeException("Você não tem permissão para cancelar esta reserva.");
        }

        // Verificar se faltam mais de 15 dias para a data de início da reserva
        LocalDate dataAtual = LocalDate.now();
        long diasParaReserva = dataAtual.until(reserva.getDataInicio()).getDays();

        if (diasParaReserva < 15) {
            // Orienta o usuário a entrar em contato com o suporte
            throw new RuntimeException("A reserva só pode ser cancelada se faltarem mais de 15 dias para a data de início. "
                    + "Por favor, entre em contato com o suporte para discutir o cancelamento.");
        }

        // Cancelar a reserva
        reserva.setStatus(StatusReserva.CANCELADA);
        reservaRepository.save(reserva);
    }

    // Cancelar reserva de um anúncio pelo anfitrião
    public void cancelarReservaAnfitriao(Long reservaId, String nomeDeUsuarioAnfitriao) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        // Verificar se o anfitrião tem permissão
        if (!reserva.getEspaco().getAnfitriao().getNomeDeUsuario().equals(nomeDeUsuarioAnfitriao)) {
            throw new RuntimeException("Você não tem permissão para cancelar esta reserva");
        }

        reserva.setStatus(StatusReserva.CANCELADA); // Usando o enum StatusReserva
        reservaRepository.save(reserva);
    }

    // Atualizar dados da reserva por parte do usuário que fez a reserva
    public Reserva atualizarReservaUsuario(Long reservaId, String nomeDeUsuario,
                                           LocalDate novaDataInicio, LocalDate novaDataFim) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        // Verificar se o usuário tem permissão para atualizar a reserva
        if (!reserva.getUsuario().getNomeDeUsuario().equals(nomeDeUsuario)) {
            throw new RuntimeException("Você não tem permissão para atualizar esta reserva.");
        }

        // Verificar se faltam mais de 15 dias para a data de início da reserva
        LocalDate dataAtual = LocalDate.now();
        long diasParaReserva = dataAtual.until(reserva.getDataInicio()).getDays();

        if (diasParaReserva < 15) {
            // Aqui orientamos o usuário a entrar em contato com o anfitrião
            throw new RuntimeException("A reserva só pode ser atualizada se faltarem mais de 15 dias para a data de início. "
                    + "Por favor, entre em contato com o anfitrião para discutir mudanças.");
        }

        // Verificar se as novas datas são válidas
        if (novaDataInicio.isBefore(dataAtual)) {
            throw new RuntimeException("A nova data de início não pode ser anterior à data atual.");
        }

        if (novaDataFim.isBefore(novaDataInicio)) {
            throw new RuntimeException("A data de término não pode ser anterior à data de início.");
        }

        // Atualizar as datas da reserva
        reserva.setDataInicio(novaDataInicio);
        reserva.setDataFim(novaDataFim);

        // Salvar a reserva atualizada
        return reservaRepository.save(reserva);
    }

    // Método para criar uma nova reserva
    public Reserva criarReserva(Reserva reserva) {
        // Aqui você pode adicionar qualquer lógica de validação adicional se necessário
        return reservaRepository.save(reserva);
    }

    // Método para obter a reserva com base no nome do usuário e no ID do espaço
    public Reserva obterReservaPorUsuarioEEspaco(String nomeDeUsuario, Long espacoId) {
        return reservaRepository.findByUsuarioNomeDeUsuarioAndEspacoId(nomeDeUsuario, espacoId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada para o espaço e usuário fornecidos."));
    }


}