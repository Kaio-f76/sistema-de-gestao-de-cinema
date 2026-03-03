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
    private final FilmeRepository filmeRepository;
    private final SalaService salaService;
    private final AssentoService assentoService;
    private final AssentoSessaoRepository assentoSessaoRepository;

    public SessaoService(SessaoRepository sessaoRepository, FilmeService filmeService, SalaService salaService,
                         AssentoService assentoService, AssentoSessaoRepository assentoSessaoRepository,
                         FilmeRepository filmeRepository) {
        this.sessaoRepository = sessaoRepository;
        this.filmeService = filmeService;
        this.salaService = salaService;
        this.assentoService = assentoService;
        this.assentoSessaoRepository = assentoSessaoRepository;
        this.filmeRepository = filmeRepository;
    }

    public List<Sessao> listar() {
        return sessaoRepository.findAll();
    }

    public Sessao buscarPorId(UUID id) {
        return sessaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sessao nao encontrada"));
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
        if (sessao.getFilme() == null || sessao.getFilme().getId() == null) {
            throw new DadosInvalidosException("Filme e obrigatorio para criar uma sessao");
        }
        Filme filme = filmeService.buscarPorId(sessao.getFilme().getId());
        sessao.setFilme(filme);

        if (sessao.getSala() == null || sessao.getSala().getId() == null) {
            throw new DadosInvalidosException("Sala e obrigatoria para criar uma sessao");
        }
        Sala sala = salaService.salaById(sessao.getSala().getId());
        sessao.setSala(sala);

        if (sessao.getData() == null || sessao.getHorarioFilme() == null || sessao.getHorarioFilme().trim().isEmpty()) {
            throw new DadosInvalidosException("Data e horario da sessao sao obrigatorios");
        }

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
            Filme filme = filmeService.buscarPorId(novaSessao.getFilme().getId());
            sessaoExistente.setFilme(filme);
        }

        if (novaSessao.getSala() != null && novaSessao.getSala().getId() != null) {
            Sala sala = salaService.salaById(novaSessao.getSala().getId());
            sessaoExistente.setSala(sala);
        }

        if (novaSessao.getData() != null) {
            sessaoExistente.setData(novaSessao.getData());
        }

        if (novaSessao.getHorarioFilme() != null && !novaSessao.getHorarioFilme().trim().isEmpty()) {
            sessaoExistente.setHorarioFilme(novaSessao.getHorarioFilme());
        }

        if (sessaoExistente.getData() == null || sessaoExistente.getHorarioFilme() == null
                || sessaoExistente.getHorarioFilme().trim().isEmpty()) {
            throw new DadosInvalidosException("Data e horario da sessao sao obrigatorios");
        }

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
            throw new DadosInvalidosException("Nao e possivel cancelar a sessao. Existem " +
                    sessao.getIngressos().size() + " ingresso(s) emitido(s) para esta sessao. " +
                    "Realize o estorno aos clientes antes de cancelar.");
        }

        boolean temSala = sessao.getSala() != null;
        boolean temFilme = sessao.getFilme() != null;

        if (temSala || temFilme) {
            throw new DadosInvalidosException(
                    "Nao e possivel apagar a sessao pois ela esta vinculada" +
                            (temFilme ? " ao filme \"" + sessao.getFilme().getNome() + "\"" : "") +
                            (temSala ? " a sala \"" + sessao.getSala().getNome() + "\"" : "") + "."
            );
        }

        sessaoRepository.deleteById(id);
    }

    private void validarDataHorarioNaoPassado(Sessao sessao) {
        LocalTime horario;
        try {
            horario = LocalTime.parse(sessao.getHorarioFilme());
        } catch (Exception e) {
            throw new DadosInvalidosException("Horario invalido. Use o formato HH:mm");
        }

        LocalDate dataSessao = sessao.getData()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDateTime inicioSessao = LocalDateTime.of(dataSessao, horario);
        if (inicioSessao.isBefore(LocalDateTime.now())) {
            throw new DadosInvalidosException("Data e horario da sessao nao podem ser menores que a data/hora atual");
        }
    }

    private void validarConflitoHorario(Sessao novaSessao) {
        List<Sessao> sessoesNaSala = sessaoRepository.findBySala(novaSessao.getSala());

        List<Sessao> sessoesNoDia = sessoesNaSala.stream()
                .filter(s -> s.getData().equals(novaSessao.getData()))
                .collect(Collectors.toList());

        for (Sessao sessaoExistente : sessoesNoDia) {
            if (sessaoExistente.getId() != null && sessaoExistente.getId().equals(novaSessao.getId())) {
                continue;
            }

            if (horariosConflitam(novaSessao, sessaoExistente)) {
                throw new DadosInvalidosException(
                        "Conflito de horario! A sala " + novaSessao.getSala().getNome() +
                                " ja possui uma sessao agendada para " + sessaoExistente.getHorarioFilme() +
                                " no dia " + new SimpleDateFormat("dd/MM/yyyy").format(novaSessao.getData())
                );
            }
        }
    }

    private boolean horariosConflitam(Sessao sessao1, Sessao sessao2) {
        try {
            LocalTime inicio1 = LocalTime.parse(sessao1.getHorarioFilme());
            LocalTime inicio2 = LocalTime.parse(sessao2.getHorarioFilme());

            int duracao1 = sessao1.getFilme().getDuracao() != null ? sessao1.getFilme().getDuracao() : 120;
            int duracao2 = sessao2.getFilme().getDuracao() != null ? sessao2.getFilme().getDuracao() : 120;

            LocalTime fim1 = inicio1.plusMinutes(duracao1);
            LocalTime fim2 = inicio2.plusMinutes(duracao2);

            return inicio1.isBefore(fim2) && inicio2.isBefore(fim1);

        } catch (Exception e) {
            return true;
        }
    }
}
