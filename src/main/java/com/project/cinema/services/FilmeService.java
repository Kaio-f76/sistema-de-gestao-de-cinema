package com.project.cinema.services;

import com.project.cinema.models.Filme;
import com.project.cinema.repositories.FilmeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FilmeService {
    
    @Autowired
    private FilmeRepository filmeRepository;

    public List<Filme> listar() {
        return filmeRepository.findAll();
    }

    public Filme buscarPorId(UUID id) {
        return filmeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filme não encontrado"));
    }

    public Filme buscarPorNome(String nome) {
        return filmeRepository.findByNome(nome)
                .orElseThrow(() -> new EntityNotFoundException("Filme não encontrado com o nome: " + nome));
    }

    public List<Filme> buscarPorGenero(String genero) {
        return filmeRepository.findByGenero(genero);
    }

    public Filme salvar(Filme filme) {
        return filmeRepository.save(filme);
    }

    public Filme atualizar(UUID id, Filme novoFilme) {
        Filme filmeExistente = buscarPorId(id);

        filmeExistente.setNome(novoFilme.getNome());
        filmeExistente.setDescricao(novoFilme.getDescricao());
        filmeExistente.setDataLancamento(novoFilme.getDataLancamento());
        filmeExistente.setGenero(novoFilme.getGenero());
        filmeExistente.setDiretor(novoFilme.getDiretor());
        filmeExistente.setElenco(novoFilme.getElenco());
        filmeExistente.setClassificacao(novoFilme.getClassificacao());
        filmeExistente.setDistribuidor(novoFilme.getDistribuidor());
        filmeExistente.setDuracao(novoFilme.getDuracao());
        filmeExistente.setValorFilme(novoFilme.getValorFilme());

        return filmeRepository.save(filmeExistente);
    }

    public void excluir(UUID id) {
        Filme filme = buscarPorId(id);
        
        // Validar se existem sessões associadas antes de excluir
        if (!filme.getSessoes().isEmpty()) {
            throw new IllegalStateException("Não é possível excluir o filme. Existem " + 
                    filme.getSessoes().size() + " sessão(ões) associada(s) a este filme.");
        }
        
        filmeRepository.deleteById(id);
    }
}
