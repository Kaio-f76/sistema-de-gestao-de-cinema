package com.project.cinema.repositories;

import com.project.cinema.models.Assento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssentoRepository extends JpaRepository<Assento, UUID> {
    List<Assento> findBySalaIdOrderByFilaAscNumeroAsc(UUID salaId);

    Optional<Assento> findBySalaIdAndFilaIgnoreCaseAndNumero(UUID salaId, String fila, int numero);
}
