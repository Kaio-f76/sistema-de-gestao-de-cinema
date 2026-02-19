package com.project.cinema.controllers;

import com.project.cinema.models.Sala;
import com.project.cinema.services.SalaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/salas")
public class SalaController {

    private final SalaService salaService;

    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }
    @GetMapping
    public ResponseEntity<List<Sala>> listar(){
        return ResponseEntity.ok(salaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sala> buscarPorId(@PathVariable UUID id){
        return ResponseEntity.ok(salaService.salaById(id));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<Sala> buscarPorNome(@PathVariable String nome){
        return ResponseEntity.ok(salaService.SalaByName(nome));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Sala> salvar(@Validated @RequestBody Sala salas, @RequestParam UUID usuarioId){
        return ResponseEntity.status(HttpStatus.CREATED).body(salaService.salvar(salas, usuarioId));
    }

    @PutMapping("/{id}/{usuarioId}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Sala> atualizar(@PathVariable UUID id, @RequestBody Sala salas, @PathVariable UUID usuarioId){
        return ResponseEntity.ok(salaService.atualizar(id, salas, usuarioId));
    }

    @DeleteMapping("/{id}/{usuarioId}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> excluir(@PathVariable UUID id, @PathVariable UUID usuarioId){
        salaService.excluir(id, usuarioId);
        return ResponseEntity.noContent().build();
    }
}