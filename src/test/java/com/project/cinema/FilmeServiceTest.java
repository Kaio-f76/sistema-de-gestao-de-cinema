package com.project.cinema;

import com.project.cinema.exceptions.DadosInvalidosException;
import com.project.cinema.models.Filme;
import com.project.cinema.repositories.FilmeRepository;
import com.project.cinema.services.FilmeService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmeServiceTest {

    @Mock
    private FilmeRepository filmeRepository;

    @InjectMocks
    private FilmeService filmeService;

    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
    }

    // ===== TESTES DE VALIDAÇÃO =====

    @Test
    void deveLancarExcecaoAoSalvarFilmeNulo() {
        assertThrows(DadosInvalidosException.class, () -> filmeService.salvar(null));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   "})
    void deveLancarExcecaoAoSalvarFilmeComNomeInvalido(String nome) {
        assertSalvarInvalido(f -> f.setNome(nome));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   "})
    void deveLancarExcecaoAoSalvarFilmeComDescricaoInvalida(String descricao) {
        assertSalvarInvalido(f -> f.setDescricao(descricao));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(doubles = {-10.0, 0.0})
    void deveLancarExcecaoAoSalvarFilmeComValorInvalido(Double valor) {
        assertSalvarInvalido(f -> f.setValorFilme(valor));
    }

    // ===== TESTES DE PERSISTÊNCIA =====

    @Test
    void deveSalvarFilmeComDadosValidos() {
        Filme filme = novoFilme(id);
        when(filmeRepository.save(any(Filme.class))).thenReturn(filme);

        Filme resultado = filmeService.salvar(filme);

        assertNotNull(resultado);
        assertEquals("Matrix", resultado.getNome());
        assertEquals(30.0, resultado.getValorFilme());
        verify(filmeRepository, times(1)).save(filme);
    }

    @Test
    void deveBuscarFilmePorIdComSucesso() {
        Filme filme = novoFilme(id);
        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));

        Filme resultado = filmeService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Matrix", resultado.getNome());
        verify(filmeRepository, times(1)).findById(id);
    }

    @Test
    void deveLancarExcecaoAoBuscarFilmePorIdInexistente() {
        when(filmeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> filmeService.buscarPorId(id),
                "Filme não encontrado");
    }

    // ===== TESTES DE EXCLUSÃO =====

    @Test
    void deveLancarExcecaoAoExcluirFilmeComSessoes() {
        Filme filme = novoFilme(id);
        filme.getSessoes().add(null);

        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));

        assertThrows(DadosInvalidosException.class, () -> filmeService.excluir(id),
                "Não é possível excluir o filme. Existem");
    }

    @Test
    void deveExcluirFilmeSemSessoes() {
        Filme filme = novoFilme(id);

        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));
        doNothing().when(filmeRepository).deleteById(id);

        assertDoesNotThrow(() -> filmeService.excluir(id));
        verify(filmeRepository, times(1)).deleteById(id);
    }

    // ===== TESTES DE ATUALIZAÇÃO =====

    @Test
    void deveAtualizarFilmeComDadosValidos() {
        Filme filmeAtualizado = new Filme();
        filmeAtualizado.setNome("Matrix Reloaded");
        filmeAtualizado.setDescricao("Ficção Científica - Sequência");
        filmeAtualizado.setGenero("Ficção Científica");
        filmeAtualizado.setValorFilme(35.0);
        filmeAtualizado.setDuracao(138);

        Filme filme = novoFilme(id);
        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));
        when(filmeRepository.save(any(Filme.class))).thenReturn(filmeAtualizado);

        Filme resultado = filmeService.atualizar(id, filmeAtualizado);

        assertNotNull(resultado);
        assertEquals("Matrix Reloaded", resultado.getNome());
        assertEquals(35.0, resultado.getValorFilme());
        verify(filmeRepository, times(1)).save(any(Filme.class));
    }

    @Test
    void deveLancarExcecaoAoAtualizarFilmeComValorInvalido() {
        Filme filmeAtualizado = new Filme();
        filmeAtualizado.setNome("Matrix");
        filmeAtualizado.setDescricao("Ficção Científica");
        filmeAtualizado.setValorFilme(-5.0);

        when(filmeRepository.findById(id)).thenReturn(Optional.of(novoFilme(id)));

        assertThrows(DadosInvalidosException.class, () -> filmeService.atualizar(id, filmeAtualizado));
    }

    private Filme novoFilme(UUID filmeId) {
        Filme filme = new Filme();
        filme.setId(filmeId);
        filme.setNome("Matrix");
        filme.setDescricao("Ficção Científica");
        filme.setGenero("Ficção Científica");
        filme.setValorFilme(30.0);
        filme.setDuracao(136);
        filme.setSessoes(new ArrayList<>());
        return filme;
    }

    private void assertSalvarInvalido(Consumer<Filme> mutator) {
        Filme filme = novoFilme(id);
        mutator.accept(filme);
        assertThrows(DadosInvalidosException.class, () -> filmeService.salvar(filme));
    }
}
