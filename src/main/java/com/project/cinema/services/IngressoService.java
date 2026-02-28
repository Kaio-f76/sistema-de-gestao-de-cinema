package com.project.cinema.services;

import com.project.cinema.exceptions.DadosInvalidosException;
import com.project.cinema.models.Ingresso;
import com.project.cinema.models.Sessao;
import com.project.cinema.models.Usuario;
import com.project.cinema.repositories.IngressoRepository;
import com.project.cinema.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IngressoService {

    private final IngressoRepository ingressoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SessaoService sessaoService;

    public IngressoService(IngressoRepository ingressoRepository, UsuarioRepository usuarioRepository, SessaoService sessaoService) {
        this.ingressoRepository = ingressoRepository;
        this.usuarioRepository = usuarioRepository;
        this.sessaoService = sessaoService;
    }

    public List<Ingresso> listar() {
        return ingressoRepository.findAll();
    }

    public Ingresso buscarPorId(UUID id) {
        return ingressoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ingresso não encontrado"));
    }

    public List<Ingresso> buscarPorSessao(UUID sessaoId) {
        Sessao sessao = sessaoService.buscarPorId(sessaoId);
        return ingressoRepository.findBySessao(sessao);
    }

    public List<Ingresso> buscarPorUsuario(UUID usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        return ingressoRepository.findByUsuario(usuario);
    }

    public Ingresso salvar(Ingresso ingresso) {
        validarDadosObrigatorios(ingresso);

        Sessao sessao = sessaoService.buscarPorId(ingresso.getSessao().getId());
        Usuario usuario = usuarioRepository.findById(ingresso.getUsuario().getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        validarAssento(sessao, ingresso.getNumAssento(), null);

        ingresso.setSessao(sessao);
        ingresso.setUsuario(usuario);

        if (ingresso.getValorI() == null && sessao.getFilme() != null) {
            ingresso.setValorI(sessao.getFilme().getValorFilme());
        }

        if (ingresso.getValorDesconto() == null) {
            ingresso.setValorDesconto(0.0);
        }

        return ingressoRepository.save(ingresso);
    }

    public Ingresso atualizar(UUID id, Ingresso novoIngresso) {
        Ingresso ingressoExistente = buscarPorId(id);
        validarDadosObrigatorios(novoIngresso);

        Sessao sessao = sessaoService.buscarPorId(novoIngresso.getSessao().getId());
        Usuario usuario = usuarioRepository.findById(novoIngresso.getUsuario().getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        validarAssento(sessao, novoIngresso.getNumAssento(), ingressoExistente.getId());

        ingressoExistente.setSessao(sessao);
        ingressoExistente.setUsuario(usuario);
        ingressoExistente.setNumAssento(novoIngresso.getNumAssento());
        ingressoExistente.setTipoIngresso(novoIngresso.getTipoIngresso());
        ingressoExistente.setValorI(novoIngresso.getValorI());
        ingressoExistente.setValorDesconto(novoIngresso.getValorDesconto());

        return ingressoRepository.save(ingressoExistente);
    }

    public void excluir(UUID id) {
        Ingresso ingresso = buscarPorId(id);
        ingressoRepository.delete(ingresso);
    }

    private void validarDadosObrigatorios(Ingresso ingresso) {
        if (ingresso == null) {
            throw new DadosInvalidosException("Ingresso não informado.");
        }
        if (ingresso.getSessao() == null || ingresso.getSessao().getId() == null) {
            throw new DadosInvalidosException("Sessão não informada.");
        }
        if (ingresso.getUsuario() == null || ingresso.getUsuario().getId() == null) {
            throw new DadosInvalidosException("Usuário não informado.");
        }
        if (ingresso.getNumAssento() <= 0) {
            throw new DadosInvalidosException("Número de assento inválido.");
        }
        if (ingresso.getTipoIngresso() == null || ingresso.getTipoIngresso().trim().isEmpty()) {
            throw new DadosInvalidosException("Tipo de ingresso não informado.");
        }
    }

    private void validarAssento(Sessao sessao, int numAssento, UUID ingressoIdIgnorar) {
        if (sessao.getSala() == null || sessao.getSala().getNumAssentos() == null || sessao.getSala().getNumAssentos() <= 0) {
            throw new DadosInvalidosException("A sala da sessão não possui quantidade de assentos válida.");
        }

        if (numAssento > sessao.getSala().getNumAssentos()) {
            throw new DadosInvalidosException("O assento informado não existe nesta sala.");
        }

        ingressoRepository.findBySessaoAndNumAssento(sessao, numAssento).ifPresent(ingresso -> {
            if (ingressoIdIgnorar == null || !ingresso.getId().equals(ingressoIdIgnorar)) {
                throw new DadosInvalidosException("Este assento já está ocupado para esta sessão.");
            }
        });
    }
}
