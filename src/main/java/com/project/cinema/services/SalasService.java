package com.project.cinema.services;

import com.project.cinema.models.Salas;
import com.project.cinema.repositories.SalasRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SalasService {
    @Autowired
    private SalasRepository salasRepository;

    public List<Salas> listar(){
        return salasRepository.findAll();
    }

    public Salas salvar(Salas salas){
        return salasRepository.save(salas);
    }

    public Salas salaById(UUID id){
        return salasRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sala não encontrada"));
    }

    public Salas SalaByName(String nome){
        return (Salas) salasRepository.findByNome(nome);
    }

    public Salas atualizar(UUID id, Salas novaSala){

        Salas salaExistente = salaById(id);

        salaExistente.setNome(novaSala.getNome());
        salaExistente.setNumAssentos(novaSala.getNumAssentos());
        // atualizar outros campos se necessário

        return salasRepository.save(salaExistente);
    }

    public void excluir(UUID id){
        Salas sala = salaById(id);
        salasRepository.delete(sala);
    }
}
