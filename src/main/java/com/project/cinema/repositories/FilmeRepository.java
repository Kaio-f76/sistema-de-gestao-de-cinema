package com.project.cinema.repositories;

import com.project.cinema.models.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, UUID> {
    Optional<Filme> findByNome(String nome);
    List<Filme> findByGenero(String genero);
}
