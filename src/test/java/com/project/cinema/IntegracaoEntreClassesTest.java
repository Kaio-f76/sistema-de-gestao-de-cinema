package com.project.cinema;

import com.project.cinema.models.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class IntegracaoEntreClassesTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void devePersistirTodasEntidadesERelacionamentos() {

        // =========================
        // CRIANDO SALA
        // =========================
        Sala sala = new Sala();
        sala.setNome("Sala 1");
        sala.setNumAssentos(100);
        entityManager.persist(sala);

        // =========================
        // CRIANDO SESSAO
        // =========================
        Sessao sessao = new Sessao();
        sessao.setDate(new Date());
        sessao.setHorario("20:00");
        sessao.setSala(sala);
        entityManager.persist(sessao);

        // =========================
        // CRIANDO FILME
        // =========================
        Filme filme = new Filme();
        filme.setNome("Matrix");
        filme.setDescricao("Ficção Científica");
        filme.setValorFilme(30.0);
        filme.setHorario("20:00");
        filme.setData(new Date());
        filme.setGenero("Ação");
        filme.setResumo("caçando demonios ao anoitecer");
        filme.setClassificacaoIndicativa("Maiores de 16 anos");
        filme.setImagem("matrix.jpg");
        entityManager.persist(filme);

        // =========================
        // CRIANDO USUARIO
        // =========================
        Usuario usuario = new Usuario();
        usuario.setNome("Carlos");
        usuario.setEmail("carlos@email.com");
        usuario.setSenha("123456");
        usuario.setTipoUsuario(TipoUsuario.CLIENTE);
        usuario.setSaldo(100L);
        entityManager.persist(usuario);

        // =========================
        // CRIANDO INGRESSO
        // =========================
        Ingresso ingresso = new Ingresso();
        ingresso.setFilme(filme);
        ingresso.setSessao(sessao);
        ingresso.setNumAssento(10);
        ingresso.setTipoIngresso("Inteira");
        ingresso.setValorI(30.0);
        ingresso.setValorDesconto(0.0);
        ingresso.setUsuario(usuario);
        entityManager.persist(ingresso);

        entityManager.flush();
        entityManager.clear();

        // =========================
        // VALIDANDO
        // =========================

        Ingresso ingressoSalvo = entityManager.find(Ingresso.class, ingresso.getId());

        assertNotNull(ingressoSalvo);
        assertEquals("Inteira", ingressoSalvo.getTipoIngresso());
        assertEquals(10, ingressoSalvo.getNumAssento());

        assertNotNull(ingressoSalvo.getUsuario());
        assertEquals("Carlos", ingressoSalvo.getUsuario().getNome());

        assertNotNull(ingressoSalvo.getSessao());
        assertEquals("20:00", ingressoSalvo.getSessao().getHorario());

        assertNotNull(ingressoSalvo.getFilme());
        assertEquals("Matrix", ingressoSalvo.getFilme().getNome());

        assertEquals(100L, ingressoSalvo.getUsuario().getSaldo());
    }
}
