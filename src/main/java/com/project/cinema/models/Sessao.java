package com.project.cinema.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sessao")
public class Sessao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Temporal(TemporalType.DATE)
    private Date data;

    private String horarioFilme;

    @ManyToOne
    @JoinColumn(name = "filme_id")
    @JsonIgnoreProperties("sessoes")
    private Filme filme;

    @ManyToOne
    @JoinColumn(name = "sala_id")
    @JsonIgnoreProperties("sessoes")
    private Sala sala;

    @OneToMany(mappedBy = "sessao", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("sessao-ingressos")
    private List<Ingresso> ingressos = new ArrayList<>();

    @OneToMany(mappedBy = "sessao", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<AssentoSessao> assentosSessao = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getHorarioFilme() {
        return horarioFilme;
    }

    public void setHorarioFilme(String horarioFilme) {
        this.horarioFilme = horarioFilme;
    }

    public Filme getFilme() {
        return filme;
    }

    public void setFilme(Filme filme) {
        this.filme = filme;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public List<Ingresso> getIngressos() {
        return ingressos;
    }

    public void setIngressos(List<Ingresso> ingressos) {
        this.ingressos = ingressos;
    }

    public List<AssentoSessao> getAssentosSessao() {
        return assentosSessao;
    }

    public void setAssentosSessao(List<AssentoSessao> assentosSessao) {
        this.assentosSessao = assentosSessao;
    }
}
