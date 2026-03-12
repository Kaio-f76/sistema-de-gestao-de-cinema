package com.project.cinema.config;

import com.project.cinema.models.Filme;
import com.project.cinema.repositories.FilmeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.*;
import java.util.*;

@Component
public class FilmeInitializer implements CommandLineRunner {

    private final FilmeRepository filmeRepository;

    public FilmeInitializer(FilmeRepository filmeRepository) {
        this.filmeRepository = filmeRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Path uploadPath = Paths.get("/app/uploads");
        
        System.out.println(">>> [FILME-INIT] Verificando caminho: " + uploadPath.toAbsolutePath());

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            System.out.println(">>> [FILME-INIT] Pasta criada agora, pois nao existia.");
        }

        File[] arquivos = uploadPath.toFile().listFiles();

        if (arquivos == null || arquivos.length == 0) {
            System.err.println(">>> [FILME-INIT] ERRO CRITICO: Nao existem arquivos em /app/uploads dentro do container!");
            return;
        }

        Map<String, Filme> catalogo = carregarDadosMapeados();
        Map<String, Filme> mapaNormalizado = new HashMap<>();
        catalogo.forEach((key, value) -> mapaNormalizado.put(key.trim().toLowerCase(), value));

        int contador = 0;
        for (File arquivoFisico : arquivos) {
            String nomeOriginal = arquivoFisico.getName();
            String nomeLower = nomeOriginal.toLowerCase();

            if (nomeLower.endsWith(".jpg") && !nomeOriginal.matches("^[0-9a-fA-F]{8}-.*")) {
                String nomeFilmeLimpo = nomeOriginal.substring(0, nomeOriginal.lastIndexOf(".")).trim();
                
                Optional<Filme> existente = filmeRepository.findByNome(nomeFilmeLimpo);
                
                if (existente.isEmpty()) {
                    Filme filme = mapaNormalizado.getOrDefault(nomeFilmeLimpo.toLowerCase(), criarFilmePadrao(nomeFilmeLimpo));
                    filme.setNome(nomeFilmeLimpo);
                    filme = filmeRepository.save(filme);

                    String nomeArquivoFinal = filme.getId() + "-" + nomeOriginal;
                    Path destino = uploadPath.resolve(nomeArquivoFinal);

                    try {
                        Files.move(arquivoFisico.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
                        filme.setImagemUrl(nomeArquivoFinal);
                        filmeRepository.save(filme);
                        contador++;
                        System.out.println(">>> [FILME-INIT] Cadastrado: " + nomeFilmeLimpo);
                    } catch (Exception e) {
                        System.err.println(">>> [FILME-INIT] Erro ao mover arquivo: " + nomeOriginal);
                    }
                }
            }
        }
        System.out.println(">>> [FILME-INIT] Concluido! Total de novos filmes: " + contador);
    }

    private Map<String, Filme> carregarDadosMapeados() {
        Map<String, Filme> mapa = new HashMap<>();
        // Classificações corrigidas: L, 10, 12, 14, 16, 18
        mapa.put("POV - Presenca Oculta", newFilme("POV - Presenca Oculta", "Terror", "Brandon Christensen", "Jaime M. Callica", "16", 75, 2023, "Policiais e o sobrenatural."));
        mapa.put("Zero D.C.", newFilme("Zero D.C.", "Documentário", "Vicente Amorim", "N/A", "12", 85, 2022, "Bastidores de Brasilia."));
        mapa.put("Backrooms", newFilme("Backrooms", "Terror", "Kane Parsons", "Kane Parsons", "14", 90, 2023, "Espaços liminares."));
        mapa.put("O Mago do Kremlin", newFilme("O Mago do Kremlin", "Drama", "Guiliano da Empoli", "N/A", "14", 110, 2024, "Estrategista russo."));
        mapa.put("Vingadora", newFilme("Vingadora", "Ação", "Nora Fingscheidt", "Sandra Bullock", "16", 112, 2021, "Redenção familiar."));
        mapa.put("A Bruxa dos Mortos - Baghead", newFilme("A Bruxa dos Mortos - Baghead", "Terror", "Alberto Corredor", "Freya Allan", "14", 95, 2024, "Falar com mortos."));
        mapa.put("Turma da Monica Jovem - Reflexos do Medo", newFilme("Turma da Monica Jovem - Reflexos do Medo", "Aventura", "Maurício Eça", "Sophia Valverde", "L", 88, 2024, "Mistérios no museu."));
        mapa.put("Mamonas Assassinas - O Filme", newFilme("Mamonas Assassinas - O Filme", "Cinebiografia", "Edson Spinello", "Ruy Brissac", "12", 91, 2023, "Grupo musical."));
        mapa.put("Os Segredos do Universo", newFilme("Os Segredos do Universo", "Drama", "Heidi Ewing", "Armando Espitia", "14", 111, 2021, "Amor e destino."));
        mapa.put("Nina - A Heroina dos Sete Mares", newFilme("Nina - A Heroina dos Sete Mares", "Animação", "David Alaux", "N/A", "L", 95, 2022, "Aventura maritima."));
        mapa.put("Nao Abra!", newFilme("Nao Abra!", "Terror", "Bishal Dutta", "Megan Suri", "14", 99, 2023, "Demonio da solidao."));
        mapa.put("Uma Fada Veio Me Visitar", newFilme("Uma Fada Veio Me Visitar", "Infantil", "Vivianne Jundi", "Xuxa Meneghel", "L", 90, 2023, "Xuxa e fada."));
        mapa.put("Os Mercenarios 4", newFilme("Os Mercenarios 4", "Ação", "Scott Waugh", "Jason Statham", "16", 103, 2023, "Açao explosiva."));
        mapa.put("O Porteiro", newFilme("O Porteiro", "Comédia", "Paulo Fontenelle", "Alexandre Lino", "12", 85, 2023, "Porteiro de predio."));
        mapa.put("Gatos no Museu", newFilme("Gatos no Museu", "Animação", "Vasiliy Rovenskiy", "N/A", "L", 80, 2023, "Gatos e arte."));
        mapa.put("O Convento", newFilme("O Convento", "Terror", "Christopher Smith", "Jena Malone", "14", 90, 2023, "Misterio religioso."));
        mapa.put("O Portal Secreto", newFilme("O Portal Secreto", "Fantasia", "Jeffrey Walker", "Patrick Gibson", "L", 116, 2023, "Mundo magico."));
        mapa.put("A Profecia do Mal", newFilme("A Profecia do Mal", "Terror", "Nathan Frankowski", "Alice Orr-Ewing", "16", 111, 2022, "Clonagem satanica."));
        mapa.put("Emily", newFilme("Emily", "Drama", "Frances O'Connor", "Emma Mackey", "14", 130, 2022, "Vida de Emily Brontë."));
        mapa.put("Deixados para Tras - O Inicio do Fim", newFilme("Deixados para Tras - O Inicio do Fim", "Drama", "Kevin Sorbo", "Kevin Sorbo", "12", 120, 2023, "Apocalipse."));
        mapa.put("Desapega!", newFilme("Desapega!", "Comédia", "Hsu Chariani", "Gloria Pires", "10", 95, 2023, "Vicio em compras."));
        mapa.put("O Grande Mauricinho", newFilme("O Grande Mauricinho", "Animação", "Toby Genkel", "N/A", "L", 93, 2022, "Gato esperto."));
        mapa.put("Nas Ondas da Fe", newFilme("Nas Ondas da Fe", "Comédia", "Felipe Joffily", "Marcelo Adnet", "12", 90, 2023, "Pastor acidental."));
        mapa.put("Terrifier 2", newFilme("Terrifier 2", "Terror", "Damien Leone", "David Howard Thornton", "18", 138, 2022, "Palhaço assassino."));
        mapa.put("O Amor da Voltas", newFilme("O Amor da Voltas", "Romance", "Marcos Bernstein", "Igor Angelkorte", "12", 98, 2022, "Cartas de amor."));
        mapa.put("Duetto", newFilme("Duetto", "Drama", "Vicente Amorim", "Marieta Severo", "12", 105, 2022, "Brasil e Italia."));
        mapa.put("Pronto, Falei", newFilme("Pronto, Falei", "Comédia", "Anne Pinheiro Guimarães", "Nicolas Prattes", "14", 95, 2022, "E-mails vazados."));
        mapa.put("Nada E Por Acaso", newFilme("Nada E Por Acaso", "Drama", "Márcio Trigo", "Giovanna Lancellotti", "14", 100, 2022, "Espiritismo."));
        mapa.put("Predestinado", newFilme("Predestinado", "Biografia", "Gustavo Fernández", "Danton Mello", "12", 108, 2022, "Arigo."));
        mapa.put("Maior Que o Mundo", newFilme("Maior Que o Mundo", "Comédia", "Roberto Marquez", "Eriberto Leão", "16", 105, 2022, "Escritor."));
        mapa.put("PartiuFama", newFilme("PartiuFama", "Comédia", "Linhalis", "Guilherme Garcia", "L", 90, 2022, "Fama digital."));
        mapa.put("A Suspeita", newFilme("A Suspeita", "Suspense", "Pedro Peregrino", "Glória Pires", "14", 105, 2022, "Policial e Alzheimer."));
        mapa.put("Aguas Selvagens", newFilme("Aguas Selvagens", "Suspense", "Roly Santos", "Mayana Neiva", "16", 100, 2022, "Fronteira."));
        mapa.put("Me Tira da Mira", newFilme("Me Tira da Mira", "Ação", "Hsu Chariani", "Cleo Pires", "14", 90, 2022, "Clinica de luxo."));
        mapa.put("Confessionario", newFilme("Confessionario", "Terror", "Brad Anderson", "N/A", "16", 95, 2023, "Segredos revelados."));
        mapa.put("Exorcismo Sagrado", newFilme("Exorcismo Sagrado", "Terror", "Alejandro Hidalgo", "Will Beinbrink", "16", 98, 2022, "Exorcismo."));
        mapa.put("Alerta Vermelho", newFilme("Alerta Vermelho", "Ação", "Rawson Marshall Thurber", "Dwayne Johnson", "12", 118, 2021, "Ladrao de arte."));
        mapa.put("Infiltrado", newFilme("Infiltrado", "Ação", "Guy Ritchie", "Jason Statham", "16", 119, 2021, "Carro forte."));
        mapa.put("Bill e Ted - Encare a Musica", newFilme("Bill e Ted - Encare a Musica", "Comédia", "Dean Parisot", "Keanu Reeves", "12", 91, 2020, "Musica."));
        mapa.put("Silvio", newFilme("Silvio", "Biografia", "Marcelo Antunez", "Rodrigo Faro", "12", 110, 2024, "Silvio Santos."));
        mapa.put("Hellboy e o Homem Torto", newFilme("Hellboy e o Homem Torto", "Ação", "Brian Taylor", "Jack Kesy", "16", 99, 2024, "Hellboy."));
        mapa.put("Rambo - Ate o Fim", newFilme("Rambo - Ate o Fim", "Ação", "Adrian Grunberg", "Sylvester Stallone", "18", 89, 2019, "Rambo."));
        mapa.put("O Corvo", newFilme("O Corvo", "Ação", "Rupert Sanders", "Bill Skarsgård", "16", 111, 2024, "Vingança."));
        mapa.put("O Exorcismo", newFilme("O Exorcismo", "Terror", "Joshua John Miller", "Russell Crowe", "16", 93, 2024, "Russel Crowe."));
        mapa.put("Brinquedo Assassino", newFilme("Brinquedo Assassino", "Terror", "Lars Klevberg", "N/A", "16", 90, 2019, "Chucky."));
        mapa.put("Instinto Materno", newFilme("Instinto Materno", "Suspense", "Benoît Delhomme", "Anne Hathaway", "14", 94, 2024, "Vizinhas."));
        mapa.put("Os Oito Odiados", newFilme("Os Oito Odiados", "Faroeste", "Quentin Tarantino", "Samuel L. Jackson", "18", 168, 2015, "Tarantino."));
        mapa.put("Vai Que Cola - O Filme", newFilme("Vai Que Cola - O Filme", "Comédia", "César Rodrigues", "Paulo Gustavo", "12", 94, 2015, "Paulo Gustavo."));
        mapa.put("Ursinho Pooh 2", newFilme("Ursinho Pooh 2", "Terror", "Rhys Frake-Waterfield", "N/A", "18", 93, 2024, "Terror Pooh."));
        mapa.put("Zona de Risco", newFilme("Zona de Risco", "Ação", "William Eubank", "Russell Crowe", "16", 113, 2024, "Militar."));
        mapa.put("Reza a Lenda", newFilme("Reza a Lenda", "Ação", "Homero Olivetto", "Cauã Reymond", "14", 87, 2016, "Sertao."));
        mapa.put("Como Seguir em Frente", newFilme("Como Seguir em Frente", "Drama", "John Carney", "Eve Hewson", "12", 97, 2023, "Musica."));
        mapa.put("As Cores de Tobi", newFilme("As Cores de Tobi", "Documentário", "Alexa Bakony", "N/A", "12", 81, 2021, "Jornada."));
        mapa.put("Sempre Juntos", newFilme("Sempre Juntos", "Drama", "Gustavo Pizzi", "Karine Teles", "12", 98, 2018, "Familia."));
        mapa.put("As Aventuras de Gulliver", newFilme("As Aventuras de Gulliver", "Animação", "Ilya Maksimov", "N/A", "L", 90, 2021, "Gigante."));
        mapa.put("Triunfo - Lutar para Vencer", newFilme("Triunfo - Lutar para Vencer", "Esporte", "Ben Cookson", "RJ Mitte", "12", 100, 2021, "Superaçao."));
        mapa.put("Refugio", newFilme("Refugio", "Terror", "Demian Rugna", "N/A", "16", 99, 2023, "Possessao."));
        mapa.put("Juntos e Enrolados", newFilme("Juntos e Enrolados", "Comédia", "Eduardo Vaisman", "Rafael Portugal", "12", 95, 2022, "Casamento."));
        mapa.put("O Festival do Amor", newFilme("O Festival do Amor", "Comédia", "Woody Allen", "Wallace Shawn", "12", 88, 2020, "Woody Allen."));
        mapa.put("Panda vs Aliens", newFilme("Panda vs Aliens", "Animação", "Sean Patrick O'Reilly", "N/A", "L", 80, 2021, "Panda."));
        mapa.put("Heroi de dois mundos", newFilme("Heroi de dois mundos", "Fantasia", "Lu Yang", "N/A", "14", 130, 2021, "Epico."));
        mapa.put("Veneza", newFilme("Veneza", "Drama", "Miguel Falabella", "Carmen Maura", "12", 90, 2019, "Veneza."));
        mapa.put("Alice e Peter - Onde Nascem os Sonhos", newFilme("Alice e Peter - Onde Nascem os Sonhos", "Fantasia", "Brenda Chapman", "Angelina Jolie", "10", 94, 2020, "Magia."));
        mapa.put("O Auto da Boa Mentira", newFilme("O Auto da Boa Mentira", "Comédia", "José Eduardo Belmonte", "Leandro Hassum", "12", 100, 2021, "Ariano."));
        mapa.put("Pinoquio", newFilme("Pinoquio", "Fantasia", "Matteo Garrone", "Roberto Benigni", "10", 125, 2019, "Pinocchio."));
        mapa.put("A Historia do Som", newFilme("A Historia do Som", "Drama", "Oliver Hermanus", "Paul Mescal", "14", 110, 2024, "Drama."));
        mapa.put("Alerta Apocalipse", newFilme("Alerta Apocalipse", "Ação", "Byun Seung-wook", "Ma Dong-seok", "14", 107, 2023, "Coreia."));
        mapa.put("Tom e Jerry no Museu", newFilme("Tom e Jerry no Museu", "Animação", "N/A", "N/A", "L", 80, 2021, "Tom e Jerry."));
        mapa.put("Sombras no Deserto", newFilme("Sombras no Deserto", "Suspense", "N/A", "N/A", "14", 95, 2022, "Misterio."));

        return mapa;
    }

    private Filme newFilme(String nome, String genero, String dir, String elenco, String classif, Integer dur, int ano, String desc) {
        Filme f = new Filme();
        f.setNome(nome);
        f.setGenero(genero);
        f.setDiretor(dir);
        f.setElenco(elenco);
        f.setClassificacao(classif);
        f.setDuracao(dur);
        f.setDescricao(desc);
        f.setValorFilme(28.0);
        f.setDistribuidor("Imagem Filmes");
        Calendar cal = Calendar.getInstance();
        cal.set(ano, 0, 1);
        f.setDataLancamento(cal.getTime());
        return f;
    }

    private Filme criarFilmePadrao(String nome) {
        Filme f = new Filme();
        f.setNome(nome);
        f.setGenero("Cinema");
        f.setDescricao("Filme populado via script.");
        f.setValorFilme(28.0);
        f.setDataLancamento(new Date());
        return f;
    }
}