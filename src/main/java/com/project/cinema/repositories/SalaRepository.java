package com.project.cinema.repositories;

import com.project.cinema.models.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SalaRepository extends JpaRepository<Sala, UUID> {
    Optional<Sala> findByNome(String nome);
}
