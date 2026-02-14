package com.project.cinema.controllers;

import com.project.cinema.models.Filme;
import com.project.cinema.services.FilmeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/filmes")
public class FilmeController {

    @Autowired
    private FilmeService filmeService;

    @GetMapping
    public ResponseEntity<List<Filme>> listar() {
        return ResponseEntity.ok(filmeService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Filme> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(filmeService.buscarPorId(id));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<Filme> buscarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(filmeService.buscarPorNome(nome));
    }

    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<Filme>> buscarPorGenero(@PathVariable String genero) {
        return ResponseEntity.ok(filmeService.buscarPorGenero(genero));
    }

    @PostMapping
    public ResponseEntity<Filme> salvar(@RequestBody Filme filme) {
        return ResponseEntity.status(HttpStatus.CREATED).body(filmeService.salvar(filme));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Filme> atualizar(@PathVariable UUID id, @RequestBody Filme filme) {
        return ResponseEntity.ok(filmeService.atualizar(id, filme));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        filmeService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
