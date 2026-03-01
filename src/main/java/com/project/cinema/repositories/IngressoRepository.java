package com.project.cinema.repositories;

import com.project.cinema.models.Ingresso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IngressoRepository extends JpaRepository<Ingresso, UUID> {
    List<Ingresso> findByUsuarioId(UUID usuarioId);

    List<Ingresso> findBySessaoId(UUID sessaoId);
}
