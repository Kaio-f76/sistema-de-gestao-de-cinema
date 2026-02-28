package com.project.cinema.repositories;

import com.project.cinema.models.Ingresso;
import com.project.cinema.models.Sessao;
import com.project.cinema.models.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IngressoRepository extends JpaRepository<Ingresso, UUID> {
    List<Ingresso> findBySessao(Sessao sessao);
    List<Ingresso> findByUsuario(Usuario usuario);
    Optional<Ingresso> findBySessaoAndNumAssento(Sessao sessao, int numAssento);
}
