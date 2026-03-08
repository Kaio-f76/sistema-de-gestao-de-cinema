package com.project.cinema.services;

import com.project.cinema.exceptions.EmailJaCadastradoException;
import com.project.cinema.exceptions.DadosInvalidosException;
import com.project.cinema.exceptions.EmailNaoEncontradoException; 
import com.project.cinema.exceptions.SenhaIncorretaException; 
import com.project.cinema.models.TipoUsuario;
import com.project.cinema.models.Usuario;
import com.project.cinema.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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
    public Usuario atualizarConta(Usuario novoUsuario, UUID id) {

        if (novoUsuario.getNome() == null || novoUsuario.getNome().isBlank() ||
                novoUsuario.getEmail() == null || novoUsuario.getEmail().isBlank()) {
            throw new DadosInvalidosException("Dados obrigatórios não informados.");
        }

        Usuario usuarioExistente = usuarioById(id);

        // Verifica se o novo email já pertence a outro usuário
        Optional<Usuario> usuarioComMesmoEmail = usuarioRepository.findByEmail(novoUsuario.getEmail());
        if (usuarioComMesmoEmail.isPresent() &&
                !usuarioComMesmoEmail.get().getId().equals(id)) {
            throw new EmailJaCadastradoException("E-mail já cadastrado.");
        }

        usuarioExistente.setNome(novoUsuario.getNome());
        usuarioExistente.setEmail(novoUsuario.getEmail());

        // Atualiza senha somente se vier preenchida
        if (novoUsuario.getSenha() != null && !novoUsuario.getSenha().isBlank()) {
            usuarioExistente.setSenha(passwordEncoder.encode(novoUsuario.getSenha()));
        }

        return usuarioRepository.save(usuarioExistente);
    }
    public Usuario usuarioById(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new DadosInvalidosException("Usuário não encontrado."));
    }

    public void excluirUsuario(UUID id, UUID usuarioId) {

        // Verifica se quem está tentando excluir é ADMIN
        Usuario usuarioLogado = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

//        if (usuarioLogado.getTipoUsuario() != TipoUsuario.ADMINISTRADOR) {
//            throw new DadosInvalidosException("Usuário não autorizado.");
//        }

        // Busca o usuário que será excluído
        Usuario usuarioParaExcluir = usuarioById(id);

        usuarioRepository.delete(usuarioParaExcluir);
    }
}
