package com.project.cinema.services;

import com.project.cinema.dtos.relatorio.RelatorioItemDTO.RelatorioFiltroDTO;
import com.project.cinema.dtos.relatorio.RelatorioItemDTO;
import com.project.cinema.repositories.IngressoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class RelatorioService {

    private final IngressoRepository ingressoRepository;

    public RelatorioService(IngressoRepository ingressoRepository) {
        this.ingressoRepository = ingressoRepository;
    }

    @Transactional(readOnly = true)
    public List<RelatorioItemDTO> gerarRelatorio(RelatorioFiltroDTO filtro) {
        RelatorioFiltroDTO filtroSeguro = filtro != null ? filtro : new RelatorioFiltroDTO();

        List<RelatorioItemDTO> itens = ingressoRepository.buscarResumoPorFilme(filtroSeguro);
        for (RelatorioItemDTO item : itens) {
            preencherCamposAdicionais(filtroSeguro, item);
        }
        itens.sort(obterComparator(filtroSeguro.getOrdenacao()));
        return itens;
    }

    private void preencherCamposAdicionais(RelatorioFiltroDTO filtro, RelatorioItemDTO item) {
        String nomeFilme = item.getNomeFilme();
        long totalIngressos = item.getTotalIngressos() == null ? 0L : item.getTotalIngressos();

        Long capacidadeTotal = ingressoRepository.buscarCapacidadeTotalPorFilme(filtro, nomeFilme);
        long capacidade = capacidadeTotal == null ? 0L : capacidadeTotal;
        double taxaOcupacao = capacidade == 0L ? 0.0 : (totalIngressos * 100.0) / capacidade;
        item.setTaxaOcupacao(taxaOcupacao);

        List<Object[]> contagemPorTipo = ingressoRepository.buscarContagemPorTipoIngresso(filtro, nomeFilme);
        String tipoMaisVendido = contagemPorTipo.isEmpty() ? null : (String) contagemPorTipo.get(0)[0];
        item.setTipoIngressoMaisVendido(tipoMaisVendido);

        Long totalClientesUnicos = ingressoRepository.buscarTotalClientesUnicosPorFilme(filtro, nomeFilme);
        item.setTotalClientesUnicos(totalClientesUnicos == null ? 0L : totalClientesUnicos);
    }

    private Comparator<RelatorioItemDTO> obterComparator(String ordenacao) {
        String ordem = ordenacao == null ? "" : ordenacao.trim().toLowerCase(Locale.ROOT);

        return switch (ordem) {
            case "ingressos_asc" -> Comparator.comparing(RelatorioItemDTO::getTotalIngressos);
            case "receita_asc" -> Comparator.comparing(RelatorioItemDTO::getReceitaTotal);
            case "sessoes_asc" -> Comparator.comparing(RelatorioItemDTO::getTotalSessoes);
            case "nome_desc" -> Comparator.comparing(RelatorioItemDTO::getNomeFilme, String.CASE_INSENSITIVE_ORDER).reversed();
            case "ingressos_desc" -> Comparator.comparing(RelatorioItemDTO::getTotalIngressos).reversed();
            case "receita_desc" -> Comparator.comparing(RelatorioItemDTO::getReceitaTotal).reversed();
            case "sessoes_desc" -> Comparator.comparing(RelatorioItemDTO::getTotalSessoes).reversed();
            default -> Comparator.comparing(RelatorioItemDTO::getNomeFilme, String.CASE_INSENSITIVE_ORDER);
        };
    }
}
