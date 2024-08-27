package com.camping.jacasaroad.repository;

import com.camping.jacasaroad.models.Espaco;
import com.camping.jacasaroad.models.TipoEspaco;
import com.camping.jacasaroad.models.UF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface EspacoRepository extends JpaRepository<Espaco, Long>, JpaSpecificationExecutor<Espaco> {

    // Encontrar espaços por anfitrião (CPF do anfitrião)
    List<Espaco> findByAnfitriaoNomeDeUsuario(String NomeDeUsuario);
}