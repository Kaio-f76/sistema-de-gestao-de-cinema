package com.project.cinema.dtos.relatorio;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class RelatorioItemDTO {

    private String nomeFilme;
    private Long totalIngressos;
    private Double receitaTotal;
    private Long totalSessoes;
    private Double taxaOcupacao;
    private String tipoIngressoMaisVendido;
    private Long totalClientesUnicos;

    public RelatorioItemDTO() {
    }

    public RelatorioItemDTO(String nomeFilme, Long totalIngressos, Double receitaTotal, Long totalSessoes) {
        this.nomeFilme = nomeFilme;
        this.totalIngressos = totalIngressos;
        this.receitaTotal = receitaTotal;
        this.totalSessoes = totalSessoes;
    }

    public RelatorioItemDTO(String nomeFilme, Long totalIngressos, Double receitaTotal, Long totalSessoes, Double taxaOcupacao, String tipoIngressoMaisVendido, Long totalClientesUnicos) {
        this.nomeFilme = nomeFilme;
        this.totalIngressos = totalIngressos;
        this.receitaTotal = receitaTotal;
        this.totalSessoes = totalSessoes;
        this.taxaOcupacao = taxaOcupacao;
        this.tipoIngressoMaisVendido = tipoIngressoMaisVendido;
        this.totalClientesUnicos = totalClientesUnicos;
    }

    public String getNomeFilme() {
        return nomeFilme;
    }

    public void setNomeFilme(String nomeFilme) {
        this.nomeFilme = nomeFilme;
    }

    public Long getTotalIngressos() {
        return totalIngressos;
    }

    public void setTotalIngressos(Long totalIngressos) {
        this.totalIngressos = totalIngressos;
    }

    public Double getReceitaTotal() {
        return receitaTotal;
    }

    public void setReceitaTotal(Double receitaTotal) {
        this.receitaTotal = receitaTotal;
    }

    public Long getTotalSessoes() {
        return totalSessoes;
    }

    public void setTotalSessoes(Long totalSessoes) {
        this.totalSessoes = totalSessoes;
    }

    public Double getTaxaOcupacao() {
        return taxaOcupacao;
    }

    public void setTaxaOcupacao(Double taxaOcupacao) {
        this.taxaOcupacao = taxaOcupacao;
    }

    public String getTipoIngressoMaisVendido() {
        return tipoIngressoMaisVendido;
    }

    public void setTipoIngressoMaisVendido(String tipoIngressoMaisVendido) {
        this.tipoIngressoMaisVendido = tipoIngressoMaisVendido;
    }

    public Long getTotalClientesUnicos() {
        return totalClientesUnicos;
    }

    public void setTotalClientesUnicos(Long totalClientesUnicos) {
        this.totalClientesUnicos = totalClientesUnicos;
    }

    public static class RelatorioFiltroDTO {

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private Date dataInicio;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private Date dataFim;

        private String genero;
        private String ordenacao;

        public Date getDataInicio() {
            return dataInicio;
        }

        public void setDataInicio(Date dataInicio) {
            this.dataInicio = dataInicio;
        }

        public Date getDataFim() {
            return dataFim;
        }

        public void setDataFim(Date dataFim) {
            this.dataFim = dataFim;
        }

        public String getGenero() {
            return genero;
        }

        public void setGenero(String genero) {
            this.genero = genero;
        }

        public String getOrdenacao() {
            return ordenacao;
        }

        public void setOrdenacao(String ordenacao) {
            this.ordenacao = ordenacao;
        }
    }
}
