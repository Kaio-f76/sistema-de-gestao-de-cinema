package com.project.cinema.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "ingresso")
public class Ingresso {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "assento_sessao_id", nullable = false, unique = true)
    private AssentoSessao assentoSessao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference("usuario-ingressos")
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sessao_id", nullable = false)
    @JsonBackReference("sessao-ingressos")
    private Sessao sessao;

    private String tipoIngresso;
    private Double valorI;
    private Double valorDesconto;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dataCompra;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public AssentoSessao getAssentoSessao() {
        return assentoSessao;
    }

    public void setAssentoSessao(AssentoSessao assentoSessao) {
        this.assentoSessao = assentoSessao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Sessao getSessao() {
        return sessao;
    }

    public void setSessao(Sessao sessao) {
        this.sessao = sessao;
    }

    public String getTipoIngresso() {
        return tipoIngresso;
    }

    public void setTipoIngresso(String tipoIngresso) {
        this.tipoIngresso = tipoIngresso;
    }

    public Double getValorI() {
        return valorI;
    }

    public void setValorI(Double valorI) {
        this.valorI = valorI;
    }

    public Double getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(Double valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public Date getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(Date dataCompra) {
        this.dataCompra = dataCompra;
    }
}
