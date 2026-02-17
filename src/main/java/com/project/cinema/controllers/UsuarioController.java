package com.project.cinema.controllers;

import com.project.cinema.models.Usuario;
import com.project.cinema.services.UsuarioService;
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
    public ResponseEntity<Usuario> criarConta(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.criarConta(usuario);
        return ResponseEntity.ok(novoUsuario);
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestParam String email, @RequestParam String senha) {
        Usuario usuario = usuarioService.login(email, senha);
        return ResponseEntity.ok(usuario);
    }
}
