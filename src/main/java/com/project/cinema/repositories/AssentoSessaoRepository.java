package com.project.cinema.repositories;

import com.project.cinema.models.AssentoSessao;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssentoSessaoRepository extends JpaRepository<AssentoSessao, UUID> {

    List<AssentoSessao> findBySessaoIdOrderByAssentoFilaAscAssentoNumeroAsc(UUID sessaoId);

    Optional<AssentoSessao> findBySessaoIdAndAssentoId(UUID sessaoId, UUID assentoId);

    void deleteBySessaoId(UUID sessaoId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM AssentoSessao a WHERE a.sessao.id = :sessaoId AND a.assento.id = :assentoId")
    Optional<AssentoSessao> findBySessaoIdAndAssentoIdForUpdate(@Param("sessaoId") UUID sessaoId,
                                                                 @Param("assentoId") UUID assentoId);
}
