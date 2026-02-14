package com.project.cinema.repositories;

import com.project.cinema.models.Salas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SalasRepository extends JpaRepository<Salas, UUID> {
    List<Salas> findByNome(String nome);
}
