package com.project.cinema.services;

import com.project.cinema.exceptions.DadosInvalidosException;
import com.project.cinema.models.Filme;
import com.project.cinema.models.Sala;
import com.project.cinema.models.Sessao;
import com.project.cinema.repositories.AssentoSessaoRepository;
import com.project.cinema.repositories.FilmeRepository;
import com.project.cinema.repositories.SessaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SessaoService {

    private final SessaoRepository sessaoRepository;
    private final FilmeService filmeService;
    private final SalaService salaService;
    private final AssentoService assentoService;
    private final AssentoSessaoRepository assentoSessaoRepository;

    public SessaoService(SessaoRepository sessaoRepository, FilmeService filmeService, SalaService salaService,
                         AssentoService assentoService, AssentoSessaoRepository assentoSessaoRepository) {
        this.sessaoRepository = sessaoRepository;
        this.filmeService = filmeService;
        this.salaService = salaService;
        this.assentoService = assentoService;
        this.assentoSessaoRepository = assentoSessaoRepository;
    }

    public List<Sessao> listar() {
        return sessaoRepository.findAll();
    }

    public Sessao buscarPorId(UUID id) {
        return sessaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));
    }

    public List<Sessao> buscarPorFilme(UUID filmeId) {
        Filme filme = filmeService.buscarPorId(filmeId);
        return sessaoRepository.findByFilme(filme);
    }

    public List<Sessao> buscarPorSala(UUID salaId) {
        Sala sala = salaService.salaById(salaId);
        return sessaoRepository.findBySala(sala);
    }

    @Transactional
    public Sessao salvar(Sessao sessao) {
        validarCamposObrigatorios(sessao);
        
        Filme filme = filmeService.buscarPorId(sessao.getFilme().getId());
        sessao.setFilme(filme);
        
        Sala sala = salaService.salaById(sessao.getSala().getId());
        sessao.setSala(sala);

        validarDataHorarioNaoPassado(sessao);
        validarConflitoHorario(sessao);

        Sessao sessaoSalva = sessaoRepository.save(sessao);
        assentoService.inicializarAssentosParaSessao(sessaoSalva);
        return sessaoSalva;
    }

    @Transactional
    public Sessao atualizar(UUID id, Sessao novaSessao) {
        Sessao sessaoExistente = buscarPorId(id);
        UUID salaAnteriorId = sessaoExistente.getSala() != null ? sessaoExistente.getSala().getId() : null;

        if (novaSessao.getFilme() != null && novaSessao.getFilme().getId() != null) {
            sessaoExistente.setFilme(filmeService.buscarPorId(novaSessao.getFilme().getId()));
        }

        if (novaSessao.getSala() != null && novaSessao.getSala().getId() != null) {
            sessaoExistente.setSala(salaService.salaById(novaSessao.getSala().getId()));
        }

        if (novaSessao.getData() != null) sessaoExistente.setData(novaSessao.getData());
        if (novaSessao.getHorarioFilme() != null) sessaoExistente.setHorarioFilme(novaSessao.getHorarioFilme());

        validarCamposObrigatorios(sessaoExistente);
        validarDataHorarioNaoPassado(sessaoExistente);
        validarConflitoHorario(sessaoExistente);

        Sessao sessaoAtualizada = sessaoRepository.save(sessaoExistente);

        if (salaAnteriorId != null && !salaAnteriorId.equals(sessaoAtualizada.getSala().getId())) {
            assentoSessaoRepository.deleteBySessaoId(sessaoAtualizada.getId());
            assentoService.inicializarAssentosParaSessao(sessaoAtualizada);
        }

        return sessaoAtualizada;
    }

    public void excluir(UUID id) {
        Sessao sessao = buscarPorId(id);
        if (!sessao.getIngressos().isEmpty()) {
            throw new DadosInvalidosException("Não é possível excluir: existem ingressos vendidos.");
        }
        sessaoRepository.deleteById(id);
    }

    private void validarCamposObrigatorios(Sessao sessao) {
        if (sessao.getFilme() == null || sessao.getFilme().getId() == null) throw new DadosInvalidosException("Filme é obrigatório.");
        if (sessao.getSala() == null || sessao.getSala().getId() == null) throw new DadosInvalidosException("Sala é obrigatória.");
        if (sessao.getData() == null || sessao.getHorarioFilme() == null) throw new DadosInvalidosException("Data e horário são obrigatórios.");
    }

    private void validarDataHorarioNaoPassado(Sessao sessao) {
        LocalTime horario = LocalTime.parse(sessao.getHorarioFilme());
        LocalDate dataSessao = sessao.getData().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (LocalDateTime.of(dataSessao, horario).isBefore(LocalDateTime.now())) {
            throw new DadosInvalidosException("A sessão não pode ser retroativa.");
        }
    }

    private void validarConflitoHorario(Sessao novaSessao) {
        List<Sessao> sessoesNaSala = sessaoRepository.findBySala(novaSessao.getSala());
        
        // Formatar data para comparação manual segura
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dataNova = sdf.format(novaSessao.getData());

        for (Sessao existente : sessoesNaSala) {
            // Ignora a própria sessão em caso de update
            if (novaSessao.getId() != null && existente.getId().equals(novaSessao.getId())) continue;

            if (sdf.format(existente.getData()).equals(dataNova)) {
                if (horariosConflitam(novaSessao, existente)) {
                    throw new DadosInvalidosException(
                        "Conflito de horário na sala " + novaSessao.getSala().getNome() + 
                        " com a sessão de " + existente.getHorarioFilme() + " (" + existente.getFilme().getNome() + ")"
                    );
                }
            }
        }
    }

    private boolean horariosConflitam(Sessao s1, Sessao s2) {
        LocalTime inicio1 = LocalTime.parse(s1.getHorarioFilme());
        LocalTime inicio2 = LocalTime.parse(s2.getHorarioFilme());

        // Se a duração não estiver definida, assume 120min + 20min de intervalo/limpeza
        int duracao1 = (s1.getFilme().getDuracao() != null ? s1.getFilme().getDuracao() : 120) + 20;
        int duracao2 = (s2.getFilme().getDuracao() != null ? s2.getFilme().getDuracao() : 120) + 20;

        LocalTime fim1 = inicio1.plusMinutes(duracao1);
        LocalTime fim2 = inicio2.plusMinutes(duracao2);

        // Lógica de interseção de intervalos
        return inicio1.isBefore(fim2) && inicio2.isBefore(fim1);
    }
}