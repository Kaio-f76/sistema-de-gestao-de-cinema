package com.project.cinema;

import com.project.cinema.models.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class IntegracaoEntreClassesTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void devePersistirTodasEntidadesERelacionamentos() {
        Sala sala = criarSala();
        Filme filme = criarFilme();
        Sessao sessao = criarSessao(sala, filme);
        Usuario usuario = criarUsuario();
        Ingresso ingresso = criarIngresso(sessao, usuario);

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

        assertNotNull(ingressoSalvo.getSessao().getFilme());
        assertEquals("Matrix", ingressoSalvo.getSessao().getFilme().getNome());

        assertEquals(100L, ingressoSalvo.getUsuario().getSaldo());
    }

    private Sala criarSala() {
        Sala sala = new Sala();
        sala.setNome("Sala 1");
        sala.setNumAssentos(100);
        entityManager.persist(sala);
        return sala;
    }

    private Filme criarFilme() {
        Filme filme = new Filme();
        filme.setNome("Matrix");
        filme.setDescricao("Ficção Científica");
        filme.setValorFilme(30.0);
        filme.setGenero("Ficção Científica");
        filme.setDiretor("Wachowski");
        filme.setDuracao(136);
        entityManager.persist(filme);
        return filme;
    }

    private Sessao criarSessao(Sala sala, Filme filme) {
        Sessao sessao = new Sessao();
        sessao.setDate(new Date());
        sessao.setHorario("20:00");
        sessao.setSala(sala);
        sessao.setFilme(filme);
        entityManager.persist(sessao);
        return sessao;
    }

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome("Carlos");
        usuario.setEmail("carlos@email.com");
        usuario.setSenha("123456");
        usuario.setTipoUsuario(TipoUsuario.CLIENTE);
        usuario.setSaldo(100L);
        entityManager.persist(usuario);
        return usuario;
    }

    private Ingresso criarIngresso(Sessao sessao, Usuario usuario) {
        Ingresso ingresso = new Ingresso();
        ingresso.setSessao(sessao);
        ingresso.setNumAssento(10);
        ingresso.setTipoIngresso("Inteira");
        ingresso.setValorI(30.0);
        ingresso.setValorDesconto(0.0);
        ingresso.setUsuario(usuario);
        entityManager.persist(ingresso);
        return ingresso;
    }
}
