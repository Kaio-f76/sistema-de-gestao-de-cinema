package com.project.cinema.services;

import com.project.cinema.exceptions.DadosInvalidosException;
import com.project.cinema.models.Filme;
import com.project.cinema.repositories.FilmeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FilmeService {
    
    private final FilmeRepository filmeRepository;
    
    public FilmeService(FilmeRepository filmeRepository) {
        this.filmeRepository = filmeRepository;
    }

    public List<Filme> listar() {
        return filmeRepository.findAll();
    }

    public Filme buscarPorId(UUID id) {
        if (id == null) {
            throw new DadosInvalidosException("ID não pode ser nulo");
        }
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
        validarFilme(filme);
        return filmeRepository.save(filme);
    }

    public Filme atualizar(UUID id, Filme novoFilme) {
        if (id == null) {
            throw new DadosInvalidosException("ID do filme não pode ser nulo");
        }
        if (novoFilme == null) {
            throw new DadosInvalidosException("Filme não pode ser nulo");
        }
        Filme filmeExistente = buscarPorId(id);

        if (novoFilme.getNome() != null) {
            if (novoFilme.getNome().trim().isEmpty()) {
                throw new DadosInvalidosException("O nome do filme é obrigatório");
            }
            filmeExistente.setNome(novoFilme.getNome());
        }

        if (novoFilme.getDescricao() != null) {
            if (novoFilme.getDescricao().trim().isEmpty()) {
                throw new DadosInvalidosException("A descrição do filme é obrigatória");
            }
            filmeExistente.setDescricao(novoFilme.getDescricao());
        }

        if (novoFilme.getDataLancamento() != null) {
            filmeExistente.setDataLancamento(novoFilme.getDataLancamento());
        }

        if (novoFilme.getGenero() != null) {
            filmeExistente.setGenero(novoFilme.getGenero());
        }

        if (novoFilme.getDiretor() != null) {
            filmeExistente.setDiretor(novoFilme.getDiretor());
        }

        if (novoFilme.getElenco() != null) {
            filmeExistente.setElenco(novoFilme.getElenco());
        }

        if (novoFilme.getClassificacao() != null) {
            filmeExistente.setClassificacao(novoFilme.getClassificacao());
        }

        if (novoFilme.getDistribuidor() != null) {
            filmeExistente.setDistribuidor(novoFilme.getDistribuidor());
        }

        if (novoFilme.getDuracao() != null) {
            filmeExistente.setDuracao(novoFilme.getDuracao());
        }

        if (novoFilme.getValorFilme() != null) {
            if (novoFilme.getValorFilme() <= 0) {
                throw new DadosInvalidosException("O valor do filme é obrigatório e deve ser maior que zero");
            }
            filmeExistente.setValorFilme(novoFilme.getValorFilme());
        }

        return filmeRepository.save(filmeExistente);
    }

    public void excluir(UUID id) {
        if (id == null) {
            throw new DadosInvalidosException("ID não pode ser nulo");
        }
        
        Filme filme = buscarPorId(id);
        
        // Validar se existem sessões associadas antes de excluir
        if (!filme.getSessoes().isEmpty()) {
            throw new DadosInvalidosException("Não é possível excluir o filme. Existem " + 
                    filme.getSessoes().size() + " sessão(ões) associada(s) a este filme.");
        }
        
        filmeRepository.deleteById(id);
    }

    private void validarFilme(Filme filme) {
        if (filme == null) {
            throw new DadosInvalidosException("Filme não pode ser nulo");
        }
        
        if (filme.getNome() == null || filme.getNome().trim().isEmpty()) {
            throw new DadosInvalidosException("O nome do filme é obrigatório");
        }
        
        if (filme.getDescricao() == null || filme.getDescricao().trim().isEmpty()) {
            throw new DadosInvalidosException("A descrição do filme é obrigatória");
        }
        
        if (filme.getValorFilme() == null || filme.getValorFilme() <= 0) {
            throw new DadosInvalidosException("O valor do filme é obrigatório e deve ser maior que zero");
        }
    }
}
