package com.project.cinema.repositories;

import com.project.cinema.models.Assento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssentoRepository extends JpaRepository<Assento, UUID> {
    List<Assento> findBySalaIdOrderByFilaAscNumeroAsc(UUID salaId);

    Optional<Assento> findBySalaIdAndFilaIgnoreCaseAndNumero(UUID salaId, String fila, int numero);

    @Modifying
    @Query("DELETE FROM Assento a WHERE a.sala.id = :salaId")
    void deleteBySalaId(@Param("salaId") UUID salaId);

    @Modifying
    @Query("DELETE FROM Assento a WHERE a.id IN :ids")
    void deleteByIds(@Param("ids") List<UUID> ids);
}
