package com.project.cinema.controllers;

import com.project.cinema.models.Salas;
import com.project.cinema.services.SalasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/salas")
public class SalasController {

    @Autowired
    private SalasService salasService;

    @GetMapping
    public ResponseEntity<List<Salas>> listar(){
        return ResponseEntity.ok(salasService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Salas> buscarPorId(@PathVariable UUID id){
        return ResponseEntity.ok(salasService.salaById(id));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<Salas> buscarPorNome(@PathVariable String nome){
        return ResponseEntity.ok(salasService.SalaByName(nome));
    }

    @PostMapping
    public ResponseEntity<Salas> salvar(@Validated @RequestBody Salas salas){
        return ResponseEntity.status(HttpStatus.CREATED).body(salasService.salvar(salas));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Salas> atualizar(@PathVariable UUID id, @RequestBody Salas salas){
        return ResponseEntity.ok(salasService.atualizar(id, salas));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id){
        salasService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}