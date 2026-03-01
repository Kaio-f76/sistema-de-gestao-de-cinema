package com.project.cinema.controllers;

import com.project.cinema.dtos.assento.AssentoRequest;
import com.project.cinema.dtos.assento.AssentoResponse;
import com.project.cinema.services.AssentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/assentos")
public class AssentoController {

    private final AssentoService assentoService;

    public AssentoController(AssentoService assentoService) {
        this.assentoService = assentoService;
    }

    @GetMapping("/sala/{salaId}")
    public ResponseEntity<List<AssentoResponse>> listarPorSala(@PathVariable UUID salaId) {
        return ResponseEntity.ok(assentoService.listarPorSala(salaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssentoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(assentoService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<AssentoResponse> criar(@RequestBody AssentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(assentoService.criar(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<AssentoResponse> atualizar(@PathVariable UUID id, @RequestBody AssentoRequest request) {
        return ResponseEntity.ok(assentoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        assentoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
