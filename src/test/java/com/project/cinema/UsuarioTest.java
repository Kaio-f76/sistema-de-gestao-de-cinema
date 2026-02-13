package com.project.cinema;

import com.project.cinema.models.Usuario;
import com.project.cinema.models.TipoUsuario;
import com.project.cinema.models.Ingresso;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void deveCriarUsuarioComValoresCorretos() {

        Usuario usuario = new Usuario();

        UUID id = UUID.randomUUID();
        usuario.setId(id);
        usuario.setNome("João");
        usuario.setEmail("joao@email.com");
        usuario.setSenha("123456");
        usuario.setTipoUsuario(TipoUsuario.CLIENTE);
        usuario.setSaldo(100L);

        assertEquals(id, usuario.getId());
        assertEquals("João", usuario.getNome());
        assertEquals("joao@email.com", usuario.getEmail());
        assertEquals("123456", usuario.getSenha());
        assertEquals(TipoUsuario.CLIENTE, usuario.getTipoUsuario());
        assertEquals(100L, usuario.getSaldo());
    }

    @Test
    void deveAdicionarIngressosAoUsuario() {

        Usuario usuario = new Usuario();

        Ingresso ingresso1 = new Ingresso();
        Ingresso ingresso2 = new Ingresso();

        List<Ingresso> ingressos = new ArrayList<>();
        ingressos.add(ingresso1);
        ingressos.add(ingresso2);

        usuario.setIngressos(ingressos);

        assertNotNull(usuario.getIngressos());
        assertEquals(2, usuario.getIngressos().size());
    }

    @Test
    void deveAlterarSaldoCorretamente() {

        Usuario usuario = new Usuario();

        usuario.setSaldo(50L);
        usuario.setSaldo(usuario.getSaldo() + 25L);

        assertEquals(75L, usuario.getSaldo());
    }

    @Test
    void deveAlterarTipoUsuario() {

        Usuario usuario = new Usuario();

        usuario.setTipoUsuario(TipoUsuario.VISITANTE);
        assertEquals(TipoUsuario.VISITANTE, usuario.getTipoUsuario());

        usuario.setTipoUsuario(TipoUsuario.ADMINISTRADOR);
        assertEquals(TipoUsuario.ADMINISTRADOR, usuario.getTipoUsuario());
    }
}
