package com.project.cinema.repositories;

import com.project.cinema.models.AssentoSessao;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssentoSessaoRepository extends JpaRepository<AssentoSessao, UUID> {

    List<AssentoSessao> findBySessaoIdOrderByAssentoFilaAscAssentoNumeroAsc(UUID sessaoId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AssentoSessao> findBySessaoIdAndAssentoId(UUID sessaoId, UUID assentoId);

    boolean existsByAssentoId(UUID assentoId);

    void deleteBySessaoId(UUID sessaoId);

    @Modifying
    @Query("DELETE FROM AssentoSessao a WHERE a.assento.sala.id = :salaId")
    void deleteBySalaId(@Param("salaId") UUID salaId);

    @Modifying
    @Query("DELETE FROM AssentoSessao a WHERE a.assento.id IN :assentoIds")
    void deleteByAssentoIds(@Param("assentoIds") List<UUID> assentoIds);
}
