package com.project.cinema.services;

import com.project.cinema.dtos.ingresso.IngressoResponse;
import com.project.cinema.exceptions.AssentoIndisponivelException;
import com.project.cinema.exceptions.IngressoInvalidoException;
import com.project.cinema.models.AssentoSessao;
import com.project.cinema.models.Ingresso;
import com.project.cinema.models.Sessao;
import com.project.cinema.models.Usuario;
import com.project.cinema.repositories.AssentoSessaoRepository;
import com.project.cinema.repositories.IngressoRepository;
import com.project.cinema.repositories.SessaoRepository;
import com.project.cinema.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class IngressoService {

    private static final String TIPO_COMUM = "COMUM";
    private static final String TIPO_VIP = "VIP";
    private static final String TIPO_ACESSIVEL = "ACESSIVEL";

    private final IngressoRepository ingressoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SessaoRepository sessaoRepository;
    private final AssentoSessaoRepository assentoSessaoRepository;

    public IngressoService(IngressoRepository ingressoRepository,
                           UsuarioRepository usuarioRepository,
                           SessaoRepository sessaoRepository,
                           AssentoSessaoRepository assentoSessaoRepository) {
        this.ingressoRepository = ingressoRepository;
        this.usuarioRepository = usuarioRepository;
        this.sessaoRepository = sessaoRepository;
        this.assentoSessaoRepository = assentoSessaoRepository;
    }

    @Transactional
    public List<IngressoResponse> comprar(UUID usuarioId, UUID sessaoId, List<UUID> assentoIds) {
        validarAssentos(assentoIds);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado"));

        Sessao sessao = sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessao nao encontrada"));

        // Calcula o valor total antes de qualquer operação
        double valorFilme = sessao.getFilme() != null ? sessao.getFilme().getValorFilme() : 0.0;
        double valorTotal = valorFilme * assentoIds.size();

        // Valida saldo antes de ocupar qualquer assento
        if (usuario.getSaldo() < valorTotal) {
            throw new IngressoInvalidoException(
                    "Saldo insuficiente. Saldo disponivel: R$" + usuario.getSaldo() +
                            " | Valor da compra: R$" + valorTotal
            );
        }

        Date dataCompra = new Date();
        List<Ingresso> ingressos = new ArrayList<>();

        for (UUID assentoId : assentoIds) {
            AssentoSessao assentoSessao = assentoSessaoRepository
                    .findBySessaoIdAndAssentoId(sessaoId, assentoId)
                    .orElseThrow(() -> new EntityNotFoundException("Assento nao encontrado para esta sessao"));

            if (assentoSessao.isOcupado()) {
                throw new AssentoIndisponivelException("O assento "
                        + assentoSessao.getAssento().getFila()
                        + assentoSessao.getAssento().getNumero()
                        + " ja esta ocupado.");
            }

            assentoSessao.setOcupado(true);

            Ingresso ingresso = new Ingresso();
            ingresso.setUsuario(usuario);
            ingresso.setSessao(sessao);
            ingresso.setAssentoSessao(assentoSessao);
            ingresso.setTipoIngresso(normalizarTipo(assentoSessao.getAssento().getTipo()));
            ingresso.setValorI(valorFilme);
            ingresso.setValorDesconto(0.0);
            ingresso.setDataCompra(dataCompra);
            ingressos.add(ingresso);
        }

        // Abate o saldo somente após validar todos os assentos
        usuario.setSaldo(usuario.getSaldo() - valorTotal);
        usuarioRepository.save(usuario);

        return ingressoRepository.saveAll(ingressos)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<IngressoResponse> listar() {
        return ingressoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public IngressoResponse buscarPorId(UUID id) {
        return toResponse(ingressoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ingresso nao encontrado")));
    }

    @Transactional(readOnly = true)
    public List<IngressoResponse> buscarPorUsuario(UUID usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new EntityNotFoundException("Usuario nao encontrado");
        }
        return ingressoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<IngressoResponse> buscarPorSessao(UUID sessaoId) {
        if (!sessaoRepository.existsById(sessaoId)) {
            throw new EntityNotFoundException("Sessao nao encontrada");
        }
        return ingressoRepository.findBySessaoId(sessaoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ========================
    // PRIVADOS
    // ========================

    private void validarAssentos(List<UUID> assentoIds) {
        if (assentoIds == null || assentoIds.isEmpty()) {
            throw new IngressoInvalidoException("Selecione pelo menos um assento.");
        }
        Set<UUID> unicos = new HashSet<>(assentoIds);
        if (unicos.size() != assentoIds.size()) {
            throw new IngressoInvalidoException("A lista de assentos nao pode conter IDs repetidos.");
        }
    }

    private String normalizarTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            return TIPO_COMUM;
        }
        return switch (tipo.trim().toUpperCase(Locale.ROOT)) {
            case TIPO_VIP -> TIPO_VIP;
            case TIPO_ACESSIVEL -> TIPO_ACESSIVEL;
            default -> TIPO_COMUM;
        };
    }

    private IngressoResponse toResponse(Ingresso ingresso) {
        IngressoResponse response = new IngressoResponse();
        response.setId(ingresso.getId());
        response.setUsuarioId(ingresso.getUsuario().getId());
        response.setSessaoId(ingresso.getSessao().getId());
        response.setAssentoSessaoId(ingresso.getAssentoSessao().getId());
        response.setAssentoFila(ingresso.getAssentoSessao().getAssento().getFila());
        response.setAssentoNumero(ingresso.getAssentoSessao().getAssento().getNumero());
        response.setTipoIngresso(ingresso.getTipoIngresso());
        response.setValorI(ingresso.getValorI());
        response.setValorDesconto(ingresso.getValorDesconto());
        response.setDataCompra(ingresso.getDataCompra());
        return response;
    }
}
