package com.project.cinema.controllers;

import com.project.cinema.dtos.ingresso.IngressoRequest;
import com.project.cinema.dtos.ingresso.IngressoResponse;
import com.project.cinema.services.IngressoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/ingressos")
public class IngressoController {

    private final IngressoService ingressoService;

    public IngressoController(IngressoService ingressoService) {
        this.ingressoService = ingressoService;
    }

    @PostMapping("/comprar")
    public ResponseEntity<List<IngressoResponse>> comprar(@RequestBody IngressoRequest request) {
        List<IngressoResponse> response = ingressoService.comprar(
                request.getUsuarioId(),
                request.getSessaoId(),
                request.getAssentoIds()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<IngressoResponse>> listar() {
        return ResponseEntity.ok(ingressoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngressoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(ingressoService.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<IngressoResponse>> buscarPorUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(ingressoService.buscarPorUsuario(usuarioId));
    }

    @GetMapping("/sessao/{sessaoId}")
    public ResponseEntity<List<IngressoResponse>> buscarPorSessao(@PathVariable UUID sessaoId) {
        return ResponseEntity.ok(ingressoService.buscarPorSessao(sessaoId));
    }
}
