package com.project.cinema.repositories;

import com.project.cinema.models.AssentoSessao;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssentoSessaoRepository extends JpaRepository<AssentoSessao, UUID> {

    List<AssentoSessao> findBySessaoIdOrderByAssentoFilaAscAssentoNumeroAsc(UUID sessaoId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AssentoSessao> findBySessaoIdAndAssentoId(UUID sessaoId, UUID assentoId);

    boolean existsByAssentoId(UUID assentoId);

    void deleteBySessaoId(UUID sessaoId);
}
