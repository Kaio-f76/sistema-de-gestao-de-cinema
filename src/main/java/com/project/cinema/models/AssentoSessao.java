package com.project.cinema.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "assento_sessao",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"assento_id", "sessao_id"})
        }
)
public class AssentoSessao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "assento_id")
    private Assento assento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sessao_id")
    @JsonIgnore
    private Sessao sessao;

    @Column(nullable = false)
    private boolean ocupado = false;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Assento getAssento() {
        return assento;
    }

    public void setAssento(Assento assento) {
        this.assento = assento;
    }

    public Sessao getSessao() {
        return sessao;
    }

    public void setSessao(Sessao sessao) {
        this.sessao = sessao;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }
}
