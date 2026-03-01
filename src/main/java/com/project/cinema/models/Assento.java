package com.project.cinema.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "assento")
public class Assento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private int numero;

    @Column(nullable = false, length = 5)
    private String fila;

    @Column(nullable = false, length = 20)
    private String tipo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sala_id")
    @JsonIgnore
    private Sala sala;

    @OneToMany(mappedBy = "assento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<AssentoSessao> assentosSessao = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public List<AssentoSessao> getAssentosSessao() {
        return assentosSessao;
    }

    public void setAssentosSessao(List<AssentoSessao> assentosSessao) {
        this.assentosSessao = assentosSessao;
    }
}
