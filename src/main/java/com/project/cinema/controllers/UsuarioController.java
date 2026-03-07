package com.project.cinema.controllers;

import com.project.cinema.models.Usuario;
import com.project.cinema.services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.cinema.exceptions.EmailJaCadastradoException;
import com.project.cinema.exceptions.EmailNaoEncontradoException;
import com.project.cinema.exceptions.SenhaIncorretaException; 
import com.project.cinema.dtos.LoginRequest;
import com.project.cinema.dtos.CadastroRequest;

import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @PostMapping("/cadastro")
    public ResponseEntity<?> criarConta(@RequestBody CadastroRequest cadastroRequest, HttpSession session) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNome(cadastroRequest.getNome());
            usuario.setEmail(cadastroRequest.getEmail());
            usuario.setSenha(cadastroRequest.getSenha());

            Usuario novoUsuario = usuarioService.criarConta(usuario);
            session.setAttribute("usuario", novoUsuario);
            return ResponseEntity.ok(novoUsuario);
        } catch (EmailJaCadastradoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email já cadastrado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            Usuario usuarioLogado = usuarioService.login(loginRequest.getEmail(), loginRequest.getSenha());
            session.setAttribute("usuario", usuarioLogado);
            return ResponseEntity.ok(usuarioLogado);
        } catch (EmailNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email incorreto.");
        } catch (SenhaIncorretaException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta.");
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Sessão encerrada com sucesso.");
    }

    @GetMapping("/session-expired")
    public ResponseEntity<String> sessionExpired() {
        return ResponseEntity.status(440).body("Sessão expirada. Faça login novamente.");
    }

    @GetMapping("/me")
    public ResponseEntity<Usuario> getUsuario(HttpSession session) {
        Usuario usuarioSessao = (Usuario) session.getAttribute("usuario");
        if (usuarioSessao == null) {
            return ResponseEntity.status(440).body(null);
        }
        // Buscar dados atualizados do banco (saldo, etc.)
        Usuario usuarioAtualizado = usuarioService.usuarioById(usuarioSessao.getId());
        session.setAttribute("usuario", usuarioAtualizado);
        return ResponseEntity.ok(usuarioAtualizado);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(
            @PathVariable UUID id,
            @RequestBody Usuario usuario,
            HttpSession session) {

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");

        if (usuarioLogado == null) {
            return ResponseEntity.status(440).body(null);
        }

        Usuario usuarioAtualizado = usuarioService.atualizarConta(usuario, id);
        session.setAttribute("usuario", usuarioAtualizado);
        return ResponseEntity.ok(usuarioAtualizado);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluir(
            @PathVariable UUID id,
            HttpSession session) {

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");

        if (usuarioLogado == null) {
            return ResponseEntity.status(440).body("Sessão expirada.");
        }

        usuarioService.excluirUsuario(id, usuarioLogado.getId());

        return ResponseEntity.ok("Usuário excluído com sucesso.");
    }
}


