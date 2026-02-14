package com.project.cinema.repositories;

import com.project.cinema.models.Filme;
import com.project.cinema.models.Salas;
import com.project.cinema.models.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, UUID> {
    List<Sessao> findByFilme(Filme filme);
    List<Sessao> findBySala(Salas sala);
    
    // Query para verificar conflito de horários na mesma sala
    @Query("SELECT s FROM Sessao s WHERE s.sala.id = :salaId AND s.data = :data")
    List<Sessao> findBySalaAndData(@Param("salaId") UUID salaId, @Param("data") Date data);
}
