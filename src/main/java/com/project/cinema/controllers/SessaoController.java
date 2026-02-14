package com.project.cinema.controllers;

import com.project.cinema.models.Sessao;
import com.project.cinema.services.SessaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sessoes")
public class SessaoController {

    @Autowired
    private SessaoService sessaoService;

    @GetMapping
    public ResponseEntity<List<Sessao>> listar() {
        return ResponseEntity.ok(sessaoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sessao> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(sessaoService.buscarPorId(id));
    }

    @GetMapping("/filme/{filmeId}")
    public ResponseEntity<List<Sessao>> buscarPorFilme(@PathVariable UUID filmeId) {
        return ResponseEntity.ok(sessaoService.buscarPorFilme(filmeId));
    }

    @GetMapping("/sala/{salaId}")
    public ResponseEntity<List<Sessao>> buscarPorSala(@PathVariable UUID salaId) {
        return ResponseEntity.ok(sessaoService.buscarPorSala(salaId));
    }

    @PostMapping
    public ResponseEntity<Sessao> salvar(@RequestBody Sessao sessao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sessaoService.salvar(sessao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sessao> atualizar(@PathVariable UUID id, @RequestBody Sessao sessao) {
        return ResponseEntity.ok(sessaoService.atualizar(id, sessao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        sessaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
