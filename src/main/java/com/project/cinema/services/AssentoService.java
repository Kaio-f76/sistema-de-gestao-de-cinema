package com.project.cinema.services;

import com.project.cinema.dtos.assento.AssentoRequest;
import com.project.cinema.dtos.assento.AssentoResponse;
import com.project.cinema.dtos.assento.AssentoStatusResponse;
import com.project.cinema.exceptions.DadosInvalidosException;
import com.project.cinema.models.Assento;
import com.project.cinema.models.AssentoSessao;
import com.project.cinema.models.Sala;
import com.project.cinema.models.Sessao;
import com.project.cinema.repositories.AssentoRepository;
import com.project.cinema.repositories.AssentoSessaoRepository;
import com.project.cinema.repositories.SalaRepository;
import com.project.cinema.repositories.SessaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class AssentoService {

    private final AssentoRepository assentoRepository;
    private final AssentoSessaoRepository assentoSessaoRepository;
    private final SalaRepository salaRepository;
    private final SessaoRepository sessaoRepository;

    public AssentoService(AssentoRepository assentoRepository,
                          AssentoSessaoRepository assentoSessaoRepository,
                          SalaRepository salaRepository,
                          SessaoRepository sessaoRepository) {
        this.assentoRepository = assentoRepository;
        this.assentoSessaoRepository = assentoSessaoRepository;
        this.salaRepository = salaRepository;
        this.sessaoRepository = sessaoRepository;
    }

    @Transactional(readOnly = true)
    public List<AssentoResponse> listarPorSala(UUID salaId) {
        validarSalaId(salaId);
        buscarSalaPorId(salaId);
        return assentoRepository.findBySalaIdOrderByFilaAscNumeroAsc(salaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AssentoResponse buscarPorId(UUID id) {
        return toResponse(buscarAssentoPorId(id));
    }

    @Transactional
    public AssentoResponse criar(AssentoRequest request) {
        validarRequest(request);
        Sala sala = buscarSalaPorId(request.getSalaId());
        validarDuplicidade(sala.getId(), request.getFila(), request.getNumero(), null);

        Assento assento = new Assento();
        assento.setNumero(request.getNumero());
        assento.setFila(normalizarFila(request.getFila()));
        assento.setTipo(normalizarTipo(request.getTipo()));
        assento.setSala(sala);

        return toResponse(assentoRepository.save(assento));
    }

    @Transactional
    public AssentoResponse atualizar(UUID id, AssentoRequest request) {
        validarRequest(request);
        Assento assento = buscarAssentoPorId(id);
        Sala sala = buscarSalaPorId(request.getSalaId());

        validarDuplicidade(sala.getId(), request.getFila(), request.getNumero(), assento.getId());

        assento.setNumero(request.getNumero());
        assento.setFila(normalizarFila(request.getFila()));
        assento.setTipo(normalizarTipo(request.getTipo()));
        assento.setSala(sala);

        return toResponse(assentoRepository.save(assento));
    }

    @Transactional
    public void excluir(UUID id) {
        Assento assento = buscarAssentoPorId(id);
        assentoRepository.delete(assento);
    }

    @Transactional(readOnly = true)
    public List<AssentoStatusResponse> listarAssentos(UUID sessaoId) {
        buscarSessaoPorId(sessaoId);
        List<AssentoSessao> assentos = assentoSessaoRepository.findBySessaoIdOrderByAssentoFilaAscAssentoNumeroAsc(sessaoId);
        return assentos.stream().map(this::toStatusResponse).toList();
    }

    @Transactional
    public void inicializarAssentosParaSessao(Sessao sessao) {
        UUID salaId = sessao.getSala().getId();
        List<Assento> assentosSala = assentoRepository.findBySalaIdOrderByFilaAscNumeroAsc(salaId);
        if (assentosSala.isEmpty()) {
            throw new DadosInvalidosException("Nenhum assento cadastrado para a sala desta sessao.");
        }

        for (Assento assento : assentosSala) {
            AssentoSessao assentoSessao = new AssentoSessao();
            assentoSessao.setSessao(sessao);
            assentoSessao.setAssento(assento);
            assentoSessao.setOcupado(false);
            assentoSessaoRepository.save(assentoSessao);
        }
    }

    private AssentoResponse toResponse(Assento assento) {
        AssentoResponse response = new AssentoResponse();
        response.setId(assento.getId());
        response.setNumero(assento.getNumero());
        response.setFila(assento.getFila());
        response.setTipo(assento.getTipo());
        response.setSalaId(assento.getSala().getId());
        return response;
    }

    private AssentoStatusResponse toStatusResponse(AssentoSessao assentoSessao) {
        AssentoStatusResponse response = new AssentoStatusResponse();
        response.setId(assentoSessao.getAssento().getId());
        response.setNumero(assentoSessao.getAssento().getNumero());
        response.setFila(assentoSessao.getAssento().getFila());
        response.setTipo(assentoSessao.getAssento().getTipo());
        response.setOcupado(assentoSessao.isOcupado());
        return response;
    }

    private Sessao buscarSessaoPorId(UUID sessaoId) {
        return sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessao nao encontrada"));
    }

    private Assento buscarAssentoPorId(UUID id) {
        if (id == null) {
            throw new DadosInvalidosException("ID do assento nao pode ser nulo.");
        }
        return assentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assento nao encontrado"));
    }

    private Sala buscarSalaPorId(UUID salaId) {
        return salaRepository.findById(salaId)
                .orElseThrow(() -> new EntityNotFoundException("Sala nao encontrada"));
    }

    private void validarSalaId(UUID salaId) {
        if (salaId == null) {
            throw new DadosInvalidosException("Sala nao informada.");
        }
    }

    private void validarRequest(AssentoRequest request) {
        if (request == null) {
            throw new DadosInvalidosException("Dados do assento nao informados.");
        }
        if (request.getSalaId() == null) {
            throw new DadosInvalidosException("Sala nao informada.");
        }
        if (request.getNumero() <= 0) {
            throw new DadosInvalidosException("Numero do assento deve ser maior que zero.");
        }
        if (request.getFila() == null || request.getFila().trim().isEmpty()) {
            throw new DadosInvalidosException("Fila do assento e obrigatoria.");
        }
    }

    private void validarDuplicidade(UUID salaId, String fila, int numero, UUID assentoAtualId) {
        assentoRepository.findBySalaIdAndFilaIgnoreCaseAndNumero(salaId, fila, numero)
                .ifPresent(existente -> {
                    if (assentoAtualId == null || !existente.getId().equals(assentoAtualId)) {
                        throw new DadosInvalidosException("Ja existe assento nesta sala com a mesma fila e numero.");
                    }
                });
    }

    private String normalizarFila(String fila) {
        return fila.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizarTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            return "COMUM";
        }
        return tipo.trim().toUpperCase(Locale.ROOT);
    }
}
