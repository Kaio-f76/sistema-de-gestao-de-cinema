package com.project.cinema;

import com.project.cinema.exceptions.EmailJaCadastradoException;
import com.project.cinema.exceptions.DadosInvalidosException;
import com.project.cinema.models.TipoUsuario;
import com.project.cinema.models.Usuario;
import com.project.cinema.repositories.UsuarioRepository;
import com.project.cinema.services.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioServiceTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioService(usuarioRepository);
    }

    @Test
    void deveCriarContaComSucesso() {
        Usuario usuario = new Usuario();
        usuario.setNome("Carlos");
        usuario.setEmail("carlos@email.com");
        usuario.setSenha("123456");

        Usuario salvo = usuarioService.criarConta(usuario);

        assertNotNull(salvo.getId());
        assertEquals(TipoUsuario.CLIENTE, salvo.getTipoUsuario());
        assertTrue(salvo.getSenha().startsWith("$2a$")); // senha criptografada
    }

    @Test
    void deveFalharSeEmailJaCadastrado() {
        Usuario usuario = new Usuario();
        usuario.setNome("Carlos");
        usuario.setEmail("carlos@email.com");
        usuario.setSenha("123456");
        usuarioService.criarConta(usuario);

        Usuario outro = new Usuario();
        outro.setNome("Maria");
        outro.setEmail("carlos@email.com");
        outro.setSenha("abcdef");

        assertThrows(EmailJaCadastradoException.class, () -> usuarioService.criarConta(outro));
    }

    @Test
    void deveRealizarLoginComSucesso() {
        Usuario usuario = new Usuario();
        usuario.setNome("Carlos");
        usuario.setEmail("carlos@email.com");
        usuario.setSenha("123456");
        usuarioService.criarConta(usuario);

        Usuario logado = usuarioService.login("carlos@email.com", "123456");

        assertEquals("Carlos", logado.getNome());
    }

    @Test
    void deveFalharLoginComSenhaInvalida() {
        Usuario usuario = new Usuario();
        usuario.setNome("Carlos");
        usuario.setEmail("carlos@email.com");
        usuario.setSenha("123456");
        usuarioService.criarConta(usuario);

        assertThrows(DadosInvalidosException.class, () -> usuarioService.login("carlos@email.com", "errada"));
    }
}
