package com.project.cinema.controllers;

import com.project.cinema.dtos.relatorio.RelatorioItemDTO.RelatorioFiltroDTO;
import com.project.cinema.dtos.relatorio.RelatorioItemDTO;
import com.project.cinema.services.PdfExportService;
import com.project.cinema.services.RelatorioService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;
    private final PdfExportService pdfExportService;

    public RelatorioController(RelatorioService relatorioService, PdfExportService pdfExportService) {
        this.relatorioService = relatorioService;
        this.pdfExportService = pdfExportService;
    }

    @GetMapping
    public ResponseEntity<List<RelatorioItemDTO>> listar(@ModelAttribute RelatorioFiltroDTO filtro) {
        return ResponseEntity.ok(relatorioService.gerarRelatorio(filtro));
    }

    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportar(@ModelAttribute RelatorioFiltroDTO filtro) {
        List<RelatorioItemDTO> relatorio = relatorioService.gerarRelatorio(filtro);
        byte[] pdf = pdfExportService.gerarRelatorio(relatorio);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("relatorio-filmes.pdf").build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }
}
