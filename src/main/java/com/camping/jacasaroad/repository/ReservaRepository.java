package com.camping.jacasaroad.repository;

import com.camping.jacasaroad.models.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuarioNomeDeUsuario(String nomeDeUsuario);

    List<Reserva> findByEspacoAnfitriaoNomeDeUsuario(String nomeDeUsuarioAnfitriao);

    // Consulta para buscar uma reserva por nome de usuário e espaço ID
    Optional<Reserva> findByUsuarioNomeDeUsuarioAndEspacoId(String nomeDeUsuario, Long espacoId);


    List<Reserva> findByStatusAndEspacoAnfitriaoNomeDeUsuario(String status, String nomeDeUsuarioAnfitriao);

    // Buscar reservas do usuário em um espaço específico com data de fim anterior a determinada data
    Optional<Reserva> findByUsuarioNomeDeUsuarioAndEspacoIdAndDataFimBefore(String nomeDeUsuario, Long espacoId, LocalDate dataFim);

    // Verificar se existe uma reserva em um espaço pertencente a um determinado anfitrião
    boolean existsByEspacoIdAndEspacoAnfitriaoNomeDeUsuario(Long espacoId, String nomeDeUsuarioAnfitriao);
}