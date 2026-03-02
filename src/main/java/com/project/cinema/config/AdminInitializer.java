package com.project.cinema.config;

import com.project.cinema.models.TipoUsuario;
import com.project.cinema.models.Usuario;
import com.project.cinema.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminInitializer(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) {

        Optional<Usuario> adminExistente =
                usuarioRepository.findByTipoUsuario(TipoUsuario.ADMINISTRADOR);

        if (adminExistente.isEmpty()) {

            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail("admin@cinema.com");
            admin.setSenha(passwordEncoder.encode("admin123")); 
            admin.setTipoUsuario(TipoUsuario.ADMINISTRADOR);
            admin.setSaldo(0.0);

            usuarioRepository.save(admin);

            System.out.println(">>> ADMINISTRADOR PADRÃO CRIADO <<<");
        }
    }
}