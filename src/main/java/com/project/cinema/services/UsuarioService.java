package com.project.cinema.services;

import com.project.cinema.exceptions.EmailJaCadastradoException;
import com.project.cinema.exceptions.DadosInvalidosException;
import com.project.cinema.exceptions.EmailNaoEncontradoException; 
import com.project.cinema.exceptions.SenhaIncorretaException; 
import com.project.cinema.models.TipoUsuario;
import com.project.cinema.models.Usuario;
import com.project.cinema.repositories.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Usuario criarConta(Usuario usuario) {
        // Validação dos campos obrigatórios
        if (usuario.getEmail() == null || usuario.getSenha() == null || usuario.getNome() == null) {
            throw new DadosInvalidosException("Dados obrigatórios não informados.");
        }

        // Verifica se já existe usuário com o mesmo e-mail
        Optional<Usuario> existente = usuarioRepository.findByEmail(usuario.getEmail());
        if (existente.isPresent()) {
            throw new EmailJaCadastradoException("E-mail já cadastrado.");
        }

        // Criptografa a senha
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        // Define regras de negócio: todo novo usuário é CLIENTE com saldo inicial 0
        usuario.setTipoUsuario(TipoUsuario.CLIENTE);
        usuario.setSaldo(5000.00);

        return usuarioRepository.save(usuario);
    }

    public Usuario login(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNaoEncontradoException("Email incorreto."));

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new SenhaIncorretaException("Senha incorreta.");
        }

        return usuario;
    }
}
