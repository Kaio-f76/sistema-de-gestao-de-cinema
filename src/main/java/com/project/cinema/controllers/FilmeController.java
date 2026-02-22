package com.project.cinema.controllers;

import com.project.cinema.exceptions.DadosInvalidosException;
import com.project.cinema.models.Filme;
import com.project.cinema.services.FilmeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/filmes")
public class FilmeController {

    private final FilmeService filmeService;
    
    public FilmeController(FilmeService filmeService) {
        this.filmeService = filmeService;
    }

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
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> salvar(@Validated @RequestBody Filme filme) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(filmeService.salvar(filme));
        } catch (DadosInvalidosException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> atualizar(@PathVariable UUID id, @Validated @RequestBody Filme filme) {
        try {
            return ResponseEntity.ok(filmeService.atualizar(id, filme));
        } catch (DadosInvalidosException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> excluir(@PathVariable UUID id) {
        try {
            filmeService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (DadosInvalidosException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
