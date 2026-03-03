package com.project.cinema.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.project.cinema.dtos.relatorio.RelatorioItemDTO;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class PdfExportService {

    public byte[] gerarRelatorio(List<RelatorioItemDTO> itens) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, output);
            document.open();

            Font titulo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            document.add(new Paragraph("Relatorio de Filmes", titulo));
            document.add(new Paragraph(" "));

            PdfPTable tabela = new PdfPTable(4);
            tabela.setWidthPercentage(100);
            tabela.addCell(criarCabecalho("Filme"));
            tabela.addCell(criarCabecalho("Ingressos"));
            tabela.addCell(criarCabecalho("Receita"));
            tabela.addCell(criarCabecalho("Sessoes"));

            NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

            for (RelatorioItemDTO item : itens) {
                tabela.addCell(valorSeguro(item.getNomeFilme()));
                tabela.addCell(String.valueOf(item.getTotalIngressos() == null ? 0L : item.getTotalIngressos()));
                tabela.addCell(moeda.format(item.getReceitaTotal() == null ? 0.0 : item.getReceitaTotal()));
                tabela.addCell(String.valueOf(item.getTotalSessoes() == null ? 0L : item.getTotalSessoes()));
            }

            document.add(tabela);
            document.close();
            return output.toByteArray();
        } catch (DocumentException e) {
            throw new IllegalStateException("Erro ao gerar PDF do relatorio", e);
        } catch (Exception e) {
            throw new IllegalStateException("Falha inesperada ao gerar PDF", e);
        }
    }

    private PdfPCell criarCabecalho(String texto) {
        Font fonte = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        PdfPCell cell = new PdfPCell(new Phrase(texto, fonte));
        cell.setPadding(6);
        return cell;
    }

    private String valorSeguro(String valor) {
        return valor == null ? "" : valor;
    }
}
