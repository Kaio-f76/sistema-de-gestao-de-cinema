package com.project.cinema.controllers;

import com.project.cinema.dto.FilmeCatalogoDTO;
import com.project.cinema.repositories.FilmeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.cinema.models.Filme;
import com.project.cinema.services.FilmeService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/filmes")
public class FilmeController {

    private final FilmeService filmeService;
    private final FilmeRepository filmeRepository;


    public FilmeController(FilmeService filmeService, FilmeRepository filmeRepository) {
        this.filmeService = filmeService;
        this.filmeRepository = filmeRepository;
    }

    @GetMapping
    public ResponseEntity<List<FilmeCatalogoDTO>>listarFilmes() {
        List<FilmeCatalogoDTO>filmes =  filmeService.listarFilmesCatalogo();

        return ResponseEntity.ok(filmes);

    }

    @GetMapping("/pesquisar")
    public ResponseEntity<List<FilmeCatalogoDTO>> pesquisarFilme(
            @RequestParam(name = "nome", required = false) String nome,
            @RequestParam(name = "genero", required = false) String genero
    ) {
            return ResponseEntity.ok(filmeService.pesquisarFilmesCatalogo(nome, genero)
            );
        }


    @PostMapping
    public Filme criarFilme(@RequestBody Filme filme){
        return filmeService.salvarFilme(filme);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFilme(@PathVariable UUID id) {

        boolean removido = filmeService.deletarPorId(id);

        if(!removido){
            return ResponseEntity.notFound().build();
        }

          return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Filme> atualizarFilme(
            @PathVariable UUID id,
            @RequestBody Filme filmeAtualizado) {

        Filme filme = filmeService.atualizarFilme(id, filmeAtualizado);
        return ResponseEntity.ok(filme);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Filme> buscarFilme(@PathVariable UUID id) {

          Filme filme = filmeService.buscarFilmePorId(id);

                  if(filme == null){
                      return ResponseEntity.notFound().build();

                  }else{
                      return ResponseEntity.ok(filme);
                  }
        }

}


