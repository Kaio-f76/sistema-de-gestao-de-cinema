package com.project.cinema.repositories;

import com.project.cinema.models.Usuario;
import com.project.cinema.models.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByTipoUsuario(TipoUsuario tipoUsuario);

}
