package com.project.cinema;

import com.project.cinema.exceptions.DadosInvalidosException;
import com.project.cinema.models.Filme;
import com.project.cinema.models.Ingresso;
import com.project.cinema.models.Sala;
import com.project.cinema.models.Sessao;
import com.project.cinema.repositories.AssentoSessaoRepository;
import com.project.cinema.repositories.SessaoRepository;
import com.project.cinema.services.AssentoService;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessaoServiceTest {

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private FilmeService filmeService;

    @Mock
    private SalaService salaService;

    @Mock
    private AssentoService assentoService;

    @Mock
    private AssentoSessaoRepository assentoSessaoRepository;

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
        filme.setDescricao("Ficcao Cientifica");
        filme.setValorFilme(30.0);
        filme.setDuracao(136);

        sala = new Sala();
        sala.setId(salaId);
        sala.setNome("Sala 1");
        sala.setNumAssentos(100);

        sessao = novaSessao("14:00");
        sessao.setId(id);
    }

    @Test
    void deveLancarExcecaoAoSalvarSessaoSemFilme() {
        sessao.setFilme(null);
        assertThrows(DadosInvalidosException.class, () -> sessaoService.salvar(sessao));
    }

    @Test
    void deveLancarExcecaoAoSalvarSessaoSemSala() {
        when(filmeService.buscarPorId(filmeId)).thenReturn(filme);
        sessao.setSala(null);
        assertThrows(DadosInvalidosException.class, () -> sessaoService.salvar(sessao));
    }

    @Test
    void deveLancarExcecaoAoSalvarSessaoSemData() {
        stubFilmeESala();
        sessao.setData(null);
        assertThrows(DadosInvalidosException.class, () -> sessaoService.salvar(sessao));
    }

    @Test
    void deveLancarExcecaoAoSalvarSessaoSemHorario() {
        stubFilmeESala();
        sessao.setHorarioFilme(null);
        assertThrows(DadosInvalidosException.class, () -> sessaoService.salvar(sessao));
    }

    @Test
    void deveLancarExcecaoAoSalvarSessaoComDataPassada() {
        stubFilmeESala();
        sessao.setData(yesterday());
        assertThrows(DadosInvalidosException.class, () -> sessaoService.salvar(sessao));
    }

    @Test
    void deveLancarExcecaoAoSalvarSessaoNoDiaAtualComHorarioPassado() {
        stubFilmeESala();
        sessao.setData(today());
        sessao.setHorarioFilme("00:00");
        assertThrows(DadosInvalidosException.class, () -> sessaoService.salvar(sessao));
    }

    @Test
    void deveSalvarSessaoComDadosValidos() {
        stubFilmeESala();
        when(sessaoRepository.findBySala(sala)).thenReturn(new ArrayList<>());
        when(sessaoRepository.save(any(Sessao.class))).thenReturn(sessao);
        doNothing().when(assentoService).inicializarAssentosParaSessao(any(Sessao.class));

        Sessao resultado = sessaoService.salvar(sessao);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(sessaoRepository, times(1)).save(sessao);
        verify(assentoService, times(1)).inicializarAssentosParaSessao(sessao);
    }

    @Test
    void deveBuscarSessaoPorIdComSucesso() {
        when(sessaoRepository.findById(id)).thenReturn(Optional.of(sessao));

        Sessao resultado = sessaoService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(sessaoRepository, times(1)).findById(id);
    }

    @Test
    void deveLancarExcecaoAoBuscarSessaoPorIdInexistente() {
        when(sessaoRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> sessaoService.buscarPorId(id));
    }

    @Test
    void deveAtualizarSessaoComHorarioValido() {
        Sessao sessaoAtualizada = new Sessao();
        sessaoAtualizada.setHorarioFilme("16:30");

        when(sessaoRepository.findById(id)).thenReturn(Optional.of(sessao));
        when(sessaoRepository.findBySala(sala)).thenReturn(new ArrayList<>());
        when(sessaoRepository.save(any(Sessao.class))).thenReturn(sessao);

        Sessao resultado = sessaoService.atualizar(id, sessaoAtualizada);

        assertNotNull(resultado);
        verify(sessaoRepository, times(1)).save(any(Sessao.class));
    }

    @Test
    void deveExcluirSessaoComSucesso() {
        when(sessaoRepository.findById(id)).thenReturn(Optional.of(sessao));
        doNothing().when(sessaoRepository).deleteById(id);

        assertDoesNotThrow(() -> sessaoService.excluir(id));
        verify(sessaoRepository, times(1)).deleteById(id);
    }

    @Test
    void deveLancarExcecaoAoExcluirSessaoComIngressos() {
        List<Ingresso> ingressos = new ArrayList<>();
        ingressos.add(new Ingresso());
        sessao.setIngressos(ingressos);

        when(sessaoRepository.findById(id)).thenReturn(Optional.of(sessao));

        assertThrows(DadosInvalidosException.class, () -> sessaoService.excluir(id));
        verify(sessaoRepository, never()).deleteById(id);
    }

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

        assertThrows(DadosInvalidosException.class, () -> sessaoService.salvar(novaSessao));
    }

    private void stubFilmeESala() {
        when(filmeService.buscarPorId(filmeId)).thenReturn(filme);
        when(salaService.salaById(salaId)).thenReturn(sala);
    }

    private Sessao novaSessao(String horario) {
        return novaSessao(tomorrow(), horario);
    }

    private Sessao novaSessao(Date date, String horario) {
        Sessao s = new Sessao();
        s.setId(UUID.randomUUID());
        s.setFilme(filme);
        s.setSala(sala);
        s.setData(date);
        s.setHorarioFilme(horario);
        return s;
    }

    private Date tomorrow() {
        return new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
    }

    private Date yesterday() {
        return new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
    }

    private Date today() {
        return Date.from(LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());
    }
}
