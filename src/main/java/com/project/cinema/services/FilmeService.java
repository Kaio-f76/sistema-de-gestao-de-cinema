package com.project.cinema.services;

import com.project.cinema.dto.FilmeCatalogoDTO;
import com.project.cinema.models.Filme;
import com.project.cinema.repositories.FilmeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FilmeService {

    private final FilmeRepository filmeRepository;

    public FilmeService(FilmeRepository filmeRepository) {
        this.filmeRepository = filmeRepository;

    }

     private FilmeCatalogoDTO toDTO(Filme filme) {
        FilmeCatalogoDTO filmeCatalogoDTO = new FilmeCatalogoDTO();

        filmeCatalogoDTO.setId(filme.getId());
        filmeCatalogoDTO.setNome(filme.getNome());
        filmeCatalogoDTO.setDescricao(filme.getDescricao());
        filmeCatalogoDTO.setGenero(filme.getGenero());
        filmeCatalogoDTO.setHorario(filme.getHorario());
        filmeCatalogoDTO.setResumo(filme.getResumo());
        filmeCatalogoDTO.setData(filme.getData());
        filmeCatalogoDTO.setClassificacaoIndicativa(filme.getClassificacaoIndicativa());
        filmeCatalogoDTO.setValorFilme(filme.getValorFilme());
        filmeCatalogoDTO.setImagem(filme.getImagem());

        return filmeCatalogoDTO;

     }

     //DTO

     public List<FilmeCatalogoDTO>listarFilmesCatalogo() {

        return filmeRepository.findAll().stream().map(this::toDTO).toList();

     }

    public List<FilmeCatalogoDTO> pesquisarFilmesCatalogo(String nome, String genero) {

        List<Filme> filmes;

        if((nome == null || nome.isBlank()) && (genero == null  || genero.isBlank())) {
          filmes = filmeRepository.findAll();

        } else if(nome!= null && nome.isBlank()) {
             filmes = filmeRepository.findByNomeContainingIgnoreCaseAndGeneroIgnoreCase(nome,genero);
        } else if (nome != null && !nome.isBlank()) {
            filmes = filmeRepository.findByNomeContainingIgnoreCase(nome);

        } else {
            filmes = filmeRepository.findByGeneroContainingIgnoreCase(genero);
        }
        return filmes.stream().map(this::toDTO).toList();
    }



    public Filme atualizarFilme(UUID id, Filme filmeAtualizado) {

        Filme filme = filmeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filme não encontrado"));

        filme.setNome(filmeAtualizado.getNome());
        filme.setDescricao(filmeAtualizado.getDescricao());
        filme.setValorFilme(filmeAtualizado.getValorFilme());
        filme.setHorario(filmeAtualizado.getHorario());
        filme.setData(filmeAtualizado.getData());
        filme.setGenero(filmeAtualizado.getGenero());
        filme.setResumo(filmeAtualizado.getResumo());
        filme.setClassificacaoIndicativa(filmeAtualizado.getClassificacaoIndicativa());
        filme.setImagem(filmeAtualizado.getImagem());


        return filmeRepository.save(filme);
    }

    public Filme salvarFilme(Filme filme){
        return filmeRepository.save(filme);
    }

    public boolean deletarPorId(UUID id) {
        if (!filmeRepository.existsById(id)) {
           return false;
        }

        filmeRepository.deleteById(id);
        return true;
    }

    public Filme buscarFilmePorId(UUID id) {
        return filmeRepository.findById(id).orElse(null);
    }


    public List<Filme> pesquisarFilmes(String nome, String genero) {

        if ((nome == null || nome.isBlank()) && (genero == null || genero.isBlank())) {
            return filmeRepository.findAll();
        }

        if (nome != null && !nome.isBlank() && genero != null && !genero.isBlank()) {
            return filmeRepository.findByNomeContainingIgnoreCaseAndGeneroIgnoreCase(nome, genero);
        }

        if (nome != null && !nome.isBlank()) {
            return filmeRepository.findByNomeContainingIgnoreCase(nome);
        }

        return filmeRepository.findByGeneroContainingIgnoreCase(genero);
    }


}
