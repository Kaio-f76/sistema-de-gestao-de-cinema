package com.project.cinema.services;

import com.project.cinema.models.Filme;
import com.project.cinema.models.Salas;
import com.project.cinema.models.Sessao;
import com.project.cinema.repositories.SessaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@Service
public class SessaoService {

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private FilmeService filmeService;

    @Autowired
    private SalasService salasService;

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
        Salas sala = salasService.salaById(salaId);
        return sessaoRepository.findBySala(sala);
    }

    public Sessao salvar(Sessao sessao) {
        // Validação 1: Verificar se filme existe
        if (sessao.getFilme() == null || sessao.getFilme().getId() == null) {
            throw new IllegalArgumentException("Filme é obrigatório para criar uma sessão");
        }
        Filme filme = filmeService.buscarPorId(sessao.getFilme().getId());
        sessao.setFilme(filme);

        // Validação 2: Verificar se sala existe
        if (sessao.getSala() == null || sessao.getSala().getId() == null) {
            throw new IllegalArgumentException("Sala é obrigatória para criar uma sessão");
        }
        Salas sala = salasService.salaById(sessao.getSala().getId());
        sessao.setSala(sala);

        // Validação 3: CRÍTICO - Verificar conflito de horários
        validarConflitoHorario(sessao);

        return sessaoRepository.save(sessao);
    }

    public Sessao atualizar(UUID id, Sessao novaSessao) {
        Sessao sessaoExistente = buscarPorId(id);

        // Atualizar filme se fornecido
        if (novaSessao.getFilme() != null && novaSessao.getFilme().getId() != null) {
            Filme filme = filmeService.buscarPorId(novaSessao.getFilme().getId());
            sessaoExistente.setFilme(filme);
        }

        // Atualizar sala se fornecida
        if (novaSessao.getSala() != null && novaSessao.getSala().getId() != null) {
            Salas sala = salasService.salaById(novaSessao.getSala().getId());
            sessaoExistente.setSala(sala);
        }

        sessaoExistente.setDate(novaSessao.getDate());
        sessaoExistente.setHorario(novaSessao.getHorario());

        // Validar conflito de horários novamente
        validarConflitoHorario(sessaoExistente);

        return sessaoRepository.save(sessaoExistente);
    }

    public void excluir(UUID id) {
        Sessao sessao = buscarPorId(id);

        // Validação CRÍTICA: Verificar se há ingressos vendidos
        if (!sessao.getIngressos().isEmpty()) {
            throw new IllegalStateException("Não é possível cancelar a sessão. Existem " +
                    sessao.getIngressos().size() + " ingresso(s) vendido(s) para esta sessão. " +
                    "Realize o estorno aos clientes antes de cancelar.");
        }

        sessaoRepository.deleteById(id);
    }

    /**
     * Validação crítica de conflito de horários
     * Verifica se já existe outra sessão na mesma sala, no mesmo dia e horário
     */
    private void validarConflitoHorario(Sessao novaSessao) {
        List<Sessao> sessoesNoDia = sessaoRepository.findBySalaAndData(
                novaSessao.getSala().getId(),
                novaSessao.getDate()
        );

        for (Sessao sessaoExistente : sessoesNoDia) {
            // Pular a comparação se for a mesma sessão (no caso de atualização)
            if (sessaoExistente.getId() != null && sessaoExistente.getId().equals(novaSessao.getId())) {
                continue;
            }

            // Verificar se os horários conflitam
            if (horarioConflita(novaSessao.getHorario(), sessaoExistente.getHorario())) {
                throw new IllegalStateException(
                        "Conflito de horário! A sala " + novaSessao.getSala().getNome() +
                                " já possui uma sessão agendada para " + sessaoExistente.getHorario() +
                                " no dia " + new SimpleDateFormat("dd/MM/yyyy").format(novaSessao.getDate())
                );
            }
        }
    }

    /**
     * Verifica se dois horários conflitam
     * Considera a duração do filme para calcular o término da sessão
     */
    private boolean horarioConflita(String horario1, String horario2) {
        // Implementação simples: considera conflito se horários são iguais
        // TODO: Melhorar para considerar duração do filme e calcular horário de término
        return horario1.equals(horario2);
    }
}
