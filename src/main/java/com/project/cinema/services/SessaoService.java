package com.project.cinema.services;

import com.project.cinema.exceptions.DadosInvalidosException;
import com.project.cinema.models.Filme;
import com.project.cinema.models.Sala;
import com.project.cinema.models.Sessao;
import com.project.cinema.repositories.SessaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SessaoService {

    private final SessaoRepository sessaoRepository;
    private final FilmeService filmeService;
    private final SalaService salaService;
    
    public SessaoService(SessaoRepository sessaoRepository, FilmeService filmeService, SalaService salaService) {
        this.sessaoRepository = sessaoRepository;
        this.filmeService = filmeService;
        this.salaService = salaService;
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

    public Sessao salvar(Sessao sessao) {
        // Validação 1: Verificar se filme existe
        if (sessao.getFilme() == null || sessao.getFilme().getId() == null) {
            throw new DadosInvalidosException("Filme é obrigatório para criar uma sessão");
        }
        Filme filme = filmeService.buscarPorId(sessao.getFilme().getId());
        sessao.setFilme(filme);

        // Validação 2: Verificar se sala existe
        if (sessao.getSala() == null || sessao.getSala().getId() == null) {
            throw new DadosInvalidosException("Sala é obrigatória para criar uma sessão");
        }
        Sala sala = salaService.salaById(sessao.getSala().getId());
        sessao.setSala(sala);

        // Validação 3: Verificar se data e horário foram fornecidos
        if (sessao.getDate() == null || sessao.getHorario() == null || sessao.getHorario().trim().isEmpty()) {
            throw new DadosInvalidosException("Data e horário da sessão são obrigatórios");
        }

        // Validação 4: Verificar conflito de horários
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
            Sala sala = salaService.salaById(novaSessao.getSala().getId());
            sessaoExistente.setSala(sala);
        }

        if (novaSessao.getDate() != null) {
            sessaoExistente.setDate(novaSessao.getDate());
        }
        
        if (novaSessao.getHorario() != null && !novaSessao.getHorario().trim().isEmpty()) {
            sessaoExistente.setHorario(novaSessao.getHorario());
        }

        // Validar conflito de horários novamente
        validarConflitoHorario(sessaoExistente);

        return sessaoRepository.save(sessaoExistente);
    }

    public void excluir(UUID id) {
        Sessao sessao = buscarPorId(id);

        // Validação CRÍTICA: Verificar se há ingressos vendidos
        if (!sessao.getIngressos().isEmpty()) {
            throw new DadosInvalidosException("Não é possível cancelar a sessão. Existem " +
                    sessao.getIngressos().size() + " ingresso(s) vendido(s) para esta sessão. " +
                    "Realize o estorno aos clientes antes de cancelar.");
        }

        sessaoRepository.deleteById(id);
    }

    /**
     * Validação de conflito de horários
     * Verifica se há sobreposição de sessões na mesma sala, no mesmo dia
     * Considera a duração do filme para calcular o horário de término
     */
    private void validarConflitoHorario(Sessao novaSessao) {
        // Buscar todas as sessões da sala
        List<Sessao> sessoesNaSala = sessaoRepository.findBySala(novaSessao.getSala());

        // Filtrar por data em memória
        List<Sessao> sessoesNoDia = sessoesNaSala.stream()
                .filter(s -> s.getDate().equals(novaSessao.getDate()))
                .collect(Collectors.toList());

        for (Sessao sessaoExistente : sessoesNoDia) {
            // Pular a comparação se for a mesma sessão (no caso de atualização)
            if (sessaoExistente.getId() != null && sessaoExistente.getId().equals(novaSessao.getId())) {
                continue;
            }

            // Verificar se há sobreposição de horários
            if (horariosConflitam(novaSessao, sessaoExistente)) {
                throw new DadosInvalidosException(
                        "Conflito de horário! A sala " + novaSessao.getSala().getNome() +
                                " já possui uma sessão agendada para " + sessaoExistente.getHorario() +
                                " no dia " + new SimpleDateFormat("dd/MM/yyyy").format(novaSessao.getDate())
                );
            }
        }
    }

    /**
     * Verifica se dois horários conflitam considerando a duração dos filmes
     * Calcula o horário de término baseado na duração e verifica sobreposição
     * 
     * @param sessao1 - Primeira sessão
     * @param sessao2 - Segunda sessão
     * @return true se os horários se sobrepõem, false caso contrário
     */
    private boolean horariosConflitam(Sessao sessao1, Sessao sessao2) {
        try {
            // Parse dos horários
            LocalTime inicio1 = LocalTime.parse(sessao1.getHorario());
            LocalTime inicio2 = LocalTime.parse(sessao2.getHorario());
            
            // Calcular duração em minutos (padrão 120 se não especificado)
            int duracao1 = sessao1.getFilme().getDuracao() != null ? sessao1.getFilme().getDuracao() : 120;
            int duracao2 = sessao2.getFilme().getDuracao() != null ? sessao2.getFilme().getDuracao() : 120;
            
            // Calcular horário de término (adicionando duração ao horário de início)
            LocalTime fim1 = inicio1.plusMinutes(duracao1);
            LocalTime fim2 = inicio2.plusMinutes(duracao2);
            
            // Verificar sobreposição: sessão 1 começa antes de sessão 2 terminar E sessão 2 começa antes de sessão 1 terminar
            return inicio1.isBefore(fim2) && inicio2.isBefore(fim1);
            
        } catch (Exception e) {
            // Se houver erro no parse do horário, considerar como conflito para ser seguro
            return true;
        }
    }
}
