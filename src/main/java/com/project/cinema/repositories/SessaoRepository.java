package com.project.cinema.repositories;

import com.project.cinema.models.Filme;
import com.project.cinema.models.Sala;
import com.project.cinema.models.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, UUID> {
    List<Sessao> findByFilme(Filme filme);
    List<Sessao> findBySala(Sala sala);
}
