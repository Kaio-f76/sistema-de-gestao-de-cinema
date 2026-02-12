package com.project.cinema;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.project.cinema.models.TipoUsuario;


import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TipoUsuarioTest {

    @Test
    void deveConterTresTiposDeUsuario() {
        TipoUsuario[] tipos = TipoUsuario.values();
        assertEquals(3, tipos.length);
    }

    @Test
    void deveRetornarAdministrador() {
        TipoUsuario tipo = TipoUsuario.valueOf("ADMINISTRADOR");
        assertEquals(TipoUsuario.ADMINISTRADOR, tipo);
    }

    @Test
    void deveVerificarNomeDoEnum() {
        TipoUsuario tipo = TipoUsuario.CLIENTE;
        assertEquals("CLIENTE", tipo.name());
    }
}
