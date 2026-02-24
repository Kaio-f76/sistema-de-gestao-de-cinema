package com.project.cinema.dto;

import java.util.Date;
import java.util.UUID;

public class FilmeCatalogoDTO {
    private UUID id;
    private String nome;
    private String descricao;
    private String genero;
    private String resumo;
    private Date data;
    private String horario;
    private String classificacaoIndicativa;
    private Double valorFilme;
    private String imagem;

    public FilmeCatalogoDTO() {}

    public FilmeCatalogoDTO(UUID id, String nome, String descricao, String genero, String resumo, Date data, String horario,
                            String classificacaoIndicativa, Double valorFilme, String imagem) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.genero = genero;
        this.resumo = resumo;
        this.data= data;
        this.horario = horario;
        this.classificacaoIndicativa = classificacaoIndicativa;
        this.valorFilme = valorFilme;
        this.imagem = imagem;
    }


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

    public String getGenero() {
        return genero;

    }
    public void setGenero(String genero) {
        this.genero = genero;

    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }


    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

   public String getHorario() {
        return horario;
   }

   public void setHorario(String horario) {
        this.horario = horario;
   }

    public String getClassificacaoIndicativa() {
        return classificacaoIndicativa;

    }
    public void setClassificacaoIndicativa(String classificacaoIndicativa) {
        this.classificacaoIndicativa = classificacaoIndicativa;

    }

    public Double getValorFilme() {
        return valorFilme;

    }

    public void setValorFilme(Double valorFilme) {
        this.valorFilme = valorFilme;

    }


    public String getImagem(){
        return imagem;

    }

    public void setImagem(String imagem){
        this.imagem = imagem;

    }


}

