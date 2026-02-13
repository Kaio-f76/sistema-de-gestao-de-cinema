package com.project.cinema.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name="ingresso")
public class Ingresso {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "filme_id")
    private Filme filme;
    @ManyToOne
    @JoinColumn(name = "sessao_id")
    private Sessao sessao;
    private int numAssento;
    private String tipoIngresso;
    private Double ValorI;
    private Double ValorDesconto;
//    @ManyToOne
//    @JoinColumn(name = "usuario_id")
//    private Usuario usuario;

    //geters seters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Filme getFilme() {
        return filme;
    }

    public void setFilme(Filme filme) {
        this.filme = filme;
    }

    public Sessao getSessao() {
        return sessao;
    }

    public void setSessao(Sessao sessao) {
        this.sessao = sessao;
    }

    public int getNumAssento() {
        return numAssento;
    }

    public void setNumAssento(int numAssento) {
        this.numAssento = numAssento;
    }

    public String getTipoIngresso() {
        return tipoIngresso;
    }

    public void setTipoIngresso(String tipoIngresso) {
        this.tipoIngresso = tipoIngresso;
    }

    public Double getValorI() {
        return ValorI;
    }

    public void setValorI(Double valorI) {
        ValorI = valorI;
    }

    public Double getValorDesconto() {
        return ValorDesconto;
    }

    public void setValorDesconto(Double valorDesconto) {
        ValorDesconto = valorDesconto;
    }
}
