package com.project.cinema.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="filme")
public class Filme {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nome;
    private String descricao;
    private Double valorFilme;
    private String horario;
    private String resumo;
    @Temporal(TemporalType.DATE)
    private Date data;
    private String genero;
    private String imagem;
    private String classificacaoIndicativa;

    //getters seters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValorFilme() {
        return valorFilme;
    }

    public void setValorFilme(Double valorFilme) {
        this.valorFilme = valorFilme;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getGenero() { return genero ; }

    public void setGenero(String genero) { this.genero = genero; }

    public String getResumo() { return resumo ; }
    public void setResumo(String resumo) { this.resumo = resumo; }

    public String getImagem() { return imagem; }

    public void setImagem(String imagem) { this.imagem = imagem; }

    public String getClassificacaoIndicativa() { return classificacaoIndicativa; }

    public void setClassificacaoIndicativa(String classificacaoIndicativa) { this.classificacaoIndicativa = classificacaoIndicativa;}
}
