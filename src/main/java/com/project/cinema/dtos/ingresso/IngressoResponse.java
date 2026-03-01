package com.project.cinema.dtos.ingresso;

import java.util.Date;
import java.util.UUID;

public class IngressoResponse {
    private UUID id;
    private UUID usuarioId;
    private UUID sessaoId;
    private UUID assentoSessaoId;
    private String tipoIngresso;
    private Double valorI;
    private Double valorDesconto;
    private Date dataCompra;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public UUID getSessaoId() {
        return sessaoId;
    }

    public void setSessaoId(UUID sessaoId) {
        this.sessaoId = sessaoId;
    }

    public UUID getAssentoSessaoId() {
        return assentoSessaoId;
    }

    public void setAssentoSessaoId(UUID assentoSessaoId) {
        this.assentoSessaoId = assentoSessaoId;
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
