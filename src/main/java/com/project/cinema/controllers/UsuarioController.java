package com.project.cinema.controllers;

import com.project.cinema.models.Usuario;
import com.project.cinema.services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Usuario> criarConta(@RequestBody Usuario usuario, HttpSession session) {
        Usuario novoUsuario = usuarioService.criarConta(usuario);
        session.setAttribute("usuario", novoUsuario);
        return ResponseEntity.ok(novoUsuario);
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestParam String email, @RequestParam String senha, HttpSession session) {
        Usuario usuario = usuarioService.login(email, senha);
        session.setAttribute("usuario", usuario);
        return ResponseEntity.ok(usuario);
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
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        return ResponseEntity.status(440).body(null); // sessão expirada ou não iniciada
    }
    return ResponseEntity.ok(usuario);
}

}


