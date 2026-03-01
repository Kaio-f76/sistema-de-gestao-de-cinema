package com.project.cinema.controllers;

import com.project.cinema.dtos.assento.AssentoStatusResponse;
import com.project.cinema.exceptions.DadosInvalidosException;
import com.project.cinema.models.Sessao;
import com.project.cinema.services.AssentoService;
import com.project.cinema.services.SessaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping({"/api/sessoes", "/sessoes"})
public class SessaoController {

    private final SessaoService sessaoService;
    private final AssentoService assentoService;
    
    public SessaoController(SessaoService sessaoService, AssentoService assentoService) {
        this.sessaoService = sessaoService;
        this.assentoService = assentoService;
    }

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
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> salvar(@Validated @RequestBody Sessao sessao) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(sessaoService.salvar(sessao));
        } catch (DadosInvalidosException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> atualizar(@PathVariable UUID id, @Validated @RequestBody Sessao sessao) {
        try {
            return ResponseEntity.ok(sessaoService.atualizar(id, sessao));
        } catch (DadosInvalidosException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> excluir(@PathVariable UUID id) {
        try {
            sessaoService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (DadosInvalidosException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{sessaoId}/assentos")
    public ResponseEntity<List<AssentoStatusResponse>> listarAssentos(@PathVariable UUID sessaoId) {
        return ResponseEntity.ok(assentoService.listarAssentos(sessaoId));
    }
}
