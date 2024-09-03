package com.camping.jacasaroad.repository;

import com.camping.jacasaroad.models.Imagem;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagemRepository extends JpaRepository<Imagem, Long> {

    @Cacheable("imagensPorEspaco")
    List<Imagem> findByEspacoId(Long espacoId);
}