package com.project.cinema.models;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Table(name = "salas")
public class Salas {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    private Integer numAssentos;

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sessao> sessoes = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getNumAssentos() {
        return numAssentos;
    }

    public List<Sessao> getSessoes() {
        return sessoes;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNumAssentos(Integer numAssentos) {
        this.numAssentos = numAssentos;
    }

    public void setSessoes(List<Sessao> sessoes) {
        this.sessoes = sessoes;
    }
// getters e setters
}
