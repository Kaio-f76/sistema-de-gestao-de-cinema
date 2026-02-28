package com.project.cinema;

import com.project.cinema.models.Assento;
import com.project.cinema.models.AssentoSessao;
import com.project.cinema.models.Filme;
import com.project.cinema.models.Ingresso;
import com.project.cinema.models.Sala;
import com.project.cinema.models.Sessao;
import com.project.cinema.models.TipoUsuario;
import com.project.cinema.models.Usuario;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class IntegracaoEntreClassesTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void devePersistirIngressoComRelacionamentos() {
        Sala sala = criarSala();
        Filme filme = criarFilme();
        Sessao sessao = criarSessao(sala, filme);
        Assento assento = criarAssento(sala);
        AssentoSessao assentoSessao = criarAssentoSessao(sessao, assento);
        Usuario usuario = criarUsuario();
        Ingresso ingresso = criarIngresso(usuario, sessao, assentoSessao);

        entityManager.flush();
        entityManager.clear();

        Ingresso ingressoSalvo = entityManager.find(Ingresso.class, ingresso.getId());

        assertNotNull(ingressoSalvo);
        assertEquals("COMUM", ingressoSalvo.getTipoIngresso());
        assertEquals(30.0, ingressoSalvo.getValorI());
        assertEquals(0.0, ingressoSalvo.getValorDesconto());

        assertNotNull(ingressoSalvo.getAssentoSessao());
        assertEquals(10, ingressoSalvo.getAssentoSessao().getAssento().getNumero());

        assertNotNull(ingressoSalvo.getUsuario());
        assertEquals("Carlos", ingressoSalvo.getUsuario().getNome());

        assertNotNull(ingressoSalvo.getSessao());
        assertEquals("20:00", ingressoSalvo.getSessao().getHorarioFilme());

        assertNotNull(ingressoSalvo.getDataCompra());
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
        filme.setDescricao("Ficcao Cientifica");
        filme.setValorFilme(30.0);
        filme.setGenero("Ficcao Cientifica");
        filme.setDiretor("Wachowski");
        filme.setDuracao(136);
        entityManager.persist(filme);
        return filme;
    }

    private Sessao criarSessao(Sala sala, Filme filme) {
        Sessao sessao = new Sessao();
        sessao.setData(new Date());
        sessao.setHorarioFilme("20:00");
        sessao.setSala(sala);
        sessao.setFilme(filme);
        entityManager.persist(sessao);
        return sessao;
    }

    private Assento criarAssento(Sala sala) {
        Assento assento = new Assento();
        assento.setSala(sala);
        assento.setFila("A");
        assento.setNumero(10);
        assento.setTipo("comum");
        entityManager.persist(assento);
        return assento;
    }

    private AssentoSessao criarAssentoSessao(Sessao sessao, Assento assento) {
        AssentoSessao assentoSessao = new AssentoSessao();
        assentoSessao.setSessao(sessao);
        assentoSessao.setAssento(assento);
        assentoSessao.setOcupado(true);
        entityManager.persist(assentoSessao);
        return assentoSessao;
    }

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome("Carlos");
        usuario.setEmail("carlos@email.com");
        usuario.setSenha("123456");
        usuario.setTipoUsuario(TipoUsuario.CLIENTE);
        usuario.setSaldo(100.00);
        entityManager.persist(usuario);
        return usuario;
    }

    private Ingresso criarIngresso(Usuario usuario, Sessao sessao, AssentoSessao assentoSessao) {
        Ingresso ingresso = new Ingresso();
        ingresso.setUsuario(usuario);
        ingresso.setSessao(sessao);
        ingresso.setDataCompra(new Date());
        ingresso.setAssentoSessao(assentoSessao);
        ingresso.setTipoIngresso("COMUM");
        ingresso.setValorI(30.0);
        ingresso.setValorDesconto(0.0);
        entityManager.persist(ingresso);
        return ingresso;
    }
}
