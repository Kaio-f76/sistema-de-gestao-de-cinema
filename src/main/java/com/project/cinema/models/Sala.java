package com.project.cinema.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Table(name = "salas")
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    private Integer numAssentos;

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("sala-sessoes")
    private List<Sessao> sessoes = new ArrayList<>();

    public Sala() {}

    public Sala(String nome, Integer numAssentos, List<Sessao> sessoes) {
        this.nome = nome;
        this.numAssentos = numAssentos;
        this.sessoes = sessoes;
    }
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
