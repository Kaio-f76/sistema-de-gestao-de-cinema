package com.project.cinema.controllers;

import com.project.cinema.models.Ingresso;
import com.project.cinema.services.IngressoService;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

    @GetMapping
    public ResponseEntity<List<Ingresso>> listar() {
        return ResponseEntity.ok(ingressoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingresso> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(ingressoService.buscarPorId(id));
    }

    @GetMapping("/sessao/{sessaoId}")
    public ResponseEntity<List<Ingresso>> buscarPorSessao(@PathVariable UUID sessaoId) {
        return ResponseEntity.ok(ingressoService.buscarPorSessao(sessaoId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Ingresso>> buscarPorUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(ingressoService.buscarPorUsuario(usuarioId));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, "application/json;charset=UTF-8"})
    public ResponseEntity<Ingresso> salvar(@Validated @RequestBody Ingresso ingresso) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ingressoService.salvar(ingresso));
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, "application/json;charset=UTF-8"})
    public ResponseEntity<Ingresso> atualizar(@PathVariable UUID id, @Validated @RequestBody Ingresso ingresso) {
        return ResponseEntity.ok(ingressoService.atualizar(id, ingresso));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        ingressoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
