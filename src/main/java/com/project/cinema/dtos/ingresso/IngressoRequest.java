package com.project.cinema.dtos.ingresso;

import java.util.List;
import java.util.UUID;

public class IngressoRequest {
    private UUID usuarioId;
    private UUID sessaoId;
    private List<UUID> assentoIds;
    private EntidadeRef usuario;
    private EntidadeRef sessao;
    private String tipoIngresso;
    private Double valorI;
    private Double valorDesconto;

    public static class EntidadeRef {
        private UUID id;

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }
    }

    public UUID getUsuarioId() {
        if (usuarioId != null) {
            return usuarioId;
        }
        return usuario != null ? usuario.getId() : null;
    }

    public UUID getUsuarioIdDireto() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public UUID getSessaoId() {
        if (sessaoId != null) {
            return sessaoId;
        }
        return sessao != null ? sessao.getId() : null;
    }

    public UUID getSessaoIdDireto() {
        return sessaoId;
    }

    public void setSessaoId(UUID sessaoId) {
        this.sessaoId = sessaoId;
    }

    public List<UUID> getAssentoIds() {
        return assentoIds;
    }

    public void setAssentoIds(List<UUID> assentoIds) {
        this.assentoIds = assentoIds;
    }

    public EntidadeRef getUsuario() {
        return usuario;
    }

    public void setUsuario(EntidadeRef usuario) {
        this.usuario = usuario;
    }

    public EntidadeRef getSessao() {
        return sessao;
    }

    public void setSessao(EntidadeRef sessao) {
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
}
