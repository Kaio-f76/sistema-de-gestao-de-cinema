package com.project.cinema.services;

import com.project.cinema.exceptions.DadosInvalidosException;
import com.project.cinema.models.Sala;
import com.project.cinema.models.TipoUsuario;
import com.project.cinema.models.Usuario;
import com.project.cinema.repositories.SalaRepository;
import com.project.cinema.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SalaService {

    private final SalaRepository salaRepository;
    private final UsuarioRepository usuarioRepository;

    public SalaService(SalaRepository salaRepository, UsuarioRepository usuarioRepository) {
        this.salaRepository = salaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Sala> listar(){
        return salaRepository.findAll();
    }

    public Sala salvar(Sala sala, UUID usuarioId){
        //validação
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        if(usuario.getTipoUsuario()!= TipoUsuario.ADMINISTRADOR){
            throw new DadosInvalidosException("Usuario invalid.");
        }
        if(sala.getNome() == null || sala.getNome().isEmpty()){
            throw new DadosInvalidosException("Dados obrigatórios não informados.");
        }
        if (salaRepository.findByNome(sala.getNome()).isPresent()) {
            throw new DadosInvalidosException("Esse nome de sala já existe.");
        }
        //criar um if baseado em sessão

        return salaRepository.save(sala);
    }

    public Sala salaById(UUID id){
        return salaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sala não encontrada"));
    }

    public Sala SalaByName(String nome) {
        return salaRepository.findByNome(nome)
                .orElseThrow(() -> new RuntimeException("Sala não encontrada: " + nome));
    }

    public Sala atualizar(UUID id, Sala novaSala, UUID usuarioId){
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        if(usuario.getTipoUsuario()!= TipoUsuario.ADMINISTRADOR){
            throw new DadosInvalidosException("Usuario invalid.");
        }
        if(novaSala.getNome() == null || novaSala.getNome().isEmpty()){
            throw new DadosInvalidosException("Dados obrigatórios não informados.");
        }
        if (salaRepository.findByNome(novaSala.getNome()).isPresent()) {
            throw new DadosInvalidosException("Esse nome de sala já existe.");
        }
        //criar um if baseado em sessão
        Sala salaExistente = salaById(id);

        salaExistente.setNome(novaSala.getNome());
        salaExistente.setNumAssentos(novaSala.getNumAssentos());
        salaExistente.setSessoes(novaSala.getSessoes());

        // atualizar outros campos se necessário

        return salaRepository.save(salaExistente);
    }

    public void excluir(UUID id, UUID usuarioId){
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        if(usuario.getTipoUsuario()!= TipoUsuario.ADMINISTRADOR){
            throw new DadosInvalidosException("Usuario invalid.");
        }
        Sala sala = salaById(id);
        salaRepository.delete(sala);
    }
}
