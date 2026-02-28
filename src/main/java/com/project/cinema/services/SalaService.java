package com.project.cinema.services;

import com.project.cinema.exceptions.DadosInvalidosException;
import com.project.cinema.models.Assento;
import com.project.cinema.models.Sala;
import com.project.cinema.models.TipoUsuario;
import com.project.cinema.models.Usuario;
import com.project.cinema.repositories.SalaRepository;
import com.project.cinema.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class SalaService {

    private static final int ASSENTOS_POR_FILA = 10;

    private final SalaRepository salaRepository;
    private final UsuarioRepository usuarioRepository;

    public SalaService(SalaRepository salaRepository, UsuarioRepository usuarioRepository) {
        this.salaRepository = salaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Sala> listar() {
        return salaRepository.findAll();
    }

    public Sala salvar(Sala sala, UUID usuarioId) {
        if (usuarioId == null) {
            throw new DadosInvalidosException("Usuario nao informado.");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado"));
        if (usuario.getTipoUsuario() != TipoUsuario.ADMINISTRADOR) {
            throw new DadosInvalidosException("Usuario invalido.");
        }

        if (sala == null || sala.getNome() == null || sala.getNome().trim().isEmpty()) {
            throw new DadosInvalidosException("Dados obrigatorios nao informados.");
        }
        if (sala.getNumAssentos() == null || sala.getNumAssentos() <= 0) {
            throw new DadosInvalidosException("Quantidade de assentos deve ser maior que zero.");
        }

        String nomeSala = sala.getNome().trim();
        if (salaRepository.findByNome(nomeSala).isPresent()) {
            throw new DadosInvalidosException("Esse nome de sala ja existe.");
        }

        sala.setNome(nomeSala);
        sala.setAssentos(gerarAssentosPadrao(sala, sala.getNumAssentos()));
        return salaRepository.save(sala);
    }

    public Sala salaById(UUID id) {
        return salaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sala nao encontrada"));
    }

    public Sala SalaByName(String nome) {
        return salaRepository.findByNome(nome)
                .orElseThrow(() -> new RuntimeException("Sala nao encontrada: " + nome));
    }

    public Sala atualizar(UUID id, Sala novaSala, UUID usuarioId) {
        if (usuarioId == null) {
            throw new DadosInvalidosException("Usuario nao informado.");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado"));

        if (usuario.getTipoUsuario() != TipoUsuario.ADMINISTRADOR) {
            throw new DadosInvalidosException("Usuario invalid.");
        }

        if (novaSala.getNome() == null || novaSala.getNome().trim().isEmpty()) {
            throw new DadosInvalidosException("Dados obrigatorios nao informados.");
        }
        if (novaSala.getNumAssentos() == null || novaSala.getNumAssentos() <= 0) {
            throw new DadosInvalidosException("Quantidade de assentos deve ser maior que zero.");
        }

        Sala salaExistente = salaById(id);
        salaExistente.setNome(novaSala.getNome().trim());
        salaExistente.setNumAssentos(novaSala.getNumAssentos());

        return salaRepository.save(salaExistente);
    }

    public void excluir(UUID id, UUID usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado"));
        if (usuario.getTipoUsuario() != TipoUsuario.ADMINISTRADOR) {
            throw new DadosInvalidosException("Usuario invalid.");
        }

        Sala sala = salaById(id);
        salaRepository.delete(sala);
    }

    private List<Assento> gerarAssentosPadrao(Sala sala, int quantidade) {
        List<Assento> assentos = new ArrayList<>();

        for (int i = 0; i < quantidade; i++) {
            int filaIndex = i / ASSENTOS_POR_FILA;
            int numero = (i % ASSENTOS_POR_FILA) + 1;

            Assento assento = new Assento();
            assento.setSala(sala);
            assento.setFila(gerarNomeFila(filaIndex));
            assento.setNumero(numero);
            assento.setTipo("COMUM");
            assentos.add(assento);
        }

        return assentos;
    }

    private String gerarNomeFila(int index) {
        StringBuilder fila = new StringBuilder();
        int valor = index;

        do {
            int resto = valor % 26;
            fila.insert(0, (char) ('A' + resto));
            valor = (valor / 26) - 1;
        } while (valor >= 0);

        return fila.toString().toUpperCase(Locale.ROOT);
    }
}
