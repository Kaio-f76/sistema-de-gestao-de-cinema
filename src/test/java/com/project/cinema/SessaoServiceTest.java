package com.project.cinema;

import com.project.cinema.exceptions.DadosInvalidosException;
import com.project.cinema.models.Filme;
import com.project.cinema.models.Ingresso;
import com.project.cinema.models.Sala;
import com.project.cinema.models.Sessao;
import com.project.cinema.repositories.SessaoRepository;
import com.project.cinema.services.FilmeService;
import com.project.cinema.services.SalaService;
import com.project.cinema.services.SessaoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessaoServiceTest {

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private FilmeService filmeService;

    @Mock
    private SalaService salaService;

    @InjectMocks
    private SessaoService sessaoService;

    private Sessao sessao;
    private Filme filme;
    private Sala sala;
    private UUID id;
    private UUID filmeId;
    private UUID salaId;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        filmeId = UUID.randomUUID();
        salaId = UUID.randomUUID();

        filme = new Filme();
        filme.setId(filmeId);
        filme.setNome("Matrix");
        filme.setDescricao("Ficção Científica");
        filme.setValorFilme(30.0);
        filme.setDuracao(136);

        sala = new Sala();
        sala.setId(salaId);
        sala.setNome("Sala 1");
        sala.setNumAssentos(100);

        sessao = novaSessao("14:00");
        sessao.setId(id);
    }

    // ===== TESTES DE VALIDAÇÃO =====

    @Test
    void deveLancarExcecaoAoSalvarSessaoSemFilme() {
        sessao.setFilme(null);
        assertThrows(DadosInvalidosException.class, () -> sessaoService.salvar(sessao),
                "Filme é obrigatório");
    }

    @Test
    void deveLancarExcecaoAoSalvarSessaoSemSala() {
        when(filmeService.buscarPorId(filmeId)).thenReturn(filme);
        
        sessao.setSala(null);
        assertThrows(DadosInvalidosException.class, () -> sessaoService.salvar(sessao),
                "Sala é obrigatória");
    }

    @Test
    void deveLancarExcecaoAoSalvarSessaoSemData() {
        stubFilmeESala();
        
        sessao.setDate(null);
        assertThrows(DadosInvalidosException.class, () -> sessaoService.salvar(sessao),
                "Data");
    }

    @Test
    void deveLancarExcecaoAoSalvarSessaoSemHorario() {
        stubFilmeESala();
        
        sessao.setHorario(null);
        assertThrows(DadosInvalidosException.class, () -> sessaoService.salvar(sessao),
                "horário");
    }

    // ===== TESTES DE PERSISTÊNCIA =====

    @Test
    void deveSalvarSessaoComDadosValidos() {
        stubFilmeESala();
        when(sessaoRepository.findBySala(sala)).thenReturn(new ArrayList<>());
        when(sessaoRepository.save(any(Sessao.class))).thenReturn(sessao);

        Sessao resultado = sessaoService.salvar(sessao);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(sessaoRepository, times(1)).save(sessao);
    }

    @Test
    void deveBuscarSessaoPorIdComSucesso() {
        when(sessaoRepository.findById(id)).thenReturn(Optional.of(sessao));

        Sessao resultado = sessaoService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Matrix", resultado.getFilme().getNome());
        verify(sessaoRepository, times(1)).findById(id);
    }

    @Test
    void deveLancarExcecaoAoBuscarSessaoPorIdInexistente() {
        when(sessaoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> sessaoService.buscarPorId(id),
                "Sessão não encontrada");
    }

    // ===== TESTES DE ATUALIZAÇÃO =====

    @Test
    void deveAtualizarSessaoComHorarioValido() {
        Sessao sessaoAtualizada = new Sessao();
        sessaoAtualizada.setHorario("16:30");

        when(sessaoRepository.findById(id)).thenReturn(Optional.of(sessao));
        when(sessaoRepository.findBySala(sala)).thenReturn(new ArrayList<>());
        when(sessaoRepository.save(any(Sessao.class))).thenReturn(sessao);

        Sessao resultado = sessaoService.atualizar(id, sessaoAtualizada);

        assertNotNull(resultado);
        verify(sessaoRepository, times(1)).save(any(Sessao.class));
    }

    @Test
    void deveLancarExcecaoAoAtualizarSessaoInexistente() {
        Sessao sessaoAtualizada = new Sessao();
        when(sessaoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> sessaoService.atualizar(id, sessaoAtualizada),
                "Sessão não encontrada");
    }

    // ===== TESTES DE EXCLUSÃO =====

    @Test
    void deveExcluirSessaoComSucesso() {
        when(sessaoRepository.findById(id)).thenReturn(Optional.of(sessao));
        doNothing().when(sessaoRepository).deleteById(id);

        assertDoesNotThrow(() -> sessaoService.excluir(id));
        verify(sessaoRepository, times(1)).deleteById(id);
    }

    @Test
    void deveLancarExcecaoAoExcluirSessaoComIngressos() {
        Ingresso ingresso = new Ingresso();
        List<Ingresso> ingressos = new ArrayList<>();
        ingressos.add(ingresso);
        sessao.setIngressos(ingressos);

        when(sessaoRepository.findById(id)).thenReturn(Optional.of(sessao));

        assertThrows(DadosInvalidosException.class, () -> sessaoService.excluir(id),
                "Não é possível cancelar a sessão");
        verify(sessaoRepository, never()).deleteById(id);
    }

    @Test
    void deveLancarExcecaoAoExcluirSessaoInexistente() {
        when(sessaoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> sessaoService.excluir(id),
                "Sessão não encontrada");
    }

    // ===== TESTES DE DETECÇÃO DE CONFLITOS =====

    @ParameterizedTest
    @ValueSource(strings = {"15:00", "13:00"})
    void deveDetectarConflitoDeHorario(String novoHorario) {
        Date date = tomorrow();
        Sessao sessao1 = novaSessao(date, "14:00");
        Sessao novaSessao = novaSessao(date, novoHorario);
        List<Sessao> sessoesSala = new ArrayList<>();
        sessoesSala.add(sessao1);

        stubFilmeESala();
        when(sessaoRepository.findBySala(sala)).thenReturn(sessoesSala);

        assertThrows(DadosInvalidosException.class, () -> sessaoService.salvar(novaSessao),
                "Conflito de horário");
    }

    @Test
    void naoDeveExistirConflitoCom0MinutosDeIntervaloNoFinal() {
        Date date = tomorrow();
        Sessao sessao1 = novaSessao(date, "14:00");
        Sessao novaSessao = novaSessao(date, "16:16");
        List<Sessao> sessoesSala = new ArrayList<>();
        sessoesSala.add(sessao1);

        stubFilmeESala();
        when(sessaoRepository.findBySala(sala)).thenReturn(sessoesSala);
        when(sessaoRepository.save(any(Sessao.class))).thenReturn(novaSessao);

        Sessao resultado = sessaoService.salvar(novaSessao);
        assertNotNull(resultado);
    }


    private void stubFilmeESala() {
        when(filmeService.buscarPorId(filmeId)).thenReturn(filme);
        when(salaService.salaById(salaId)).thenReturn(sala);
    }

    private Sessao novaSessao(String horario) {
        return novaSessao(tomorrow(), horario);
    }

    private Sessao novaSessao(Date date, String horario) {
        Sessao sessao = new Sessao();
        sessao.setId(UUID.randomUUID());
        sessao.setFilme(filme);
        sessao.setSala(sala);
        sessao.setDate(date);
        sessao.setHorario(horario);
        return sessao;
    }

    private Date tomorrow() {
        return new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
    }
}
