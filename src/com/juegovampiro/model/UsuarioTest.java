package com.juegovampiro.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private Usuario u;

    @BeforeEach
    void setUp() {
        // Registro válido para empezar
        u = new Usuario("Ana", "ana123", "clave123", "A12BC");
    }

    @Test
    void testConstructorInicializaCampos() {
        assertEquals("Ana", u.getNombre());
        assertEquals("ana123", u.getNick());
        assertEquals("clave123", u.getPassword());
        assertEquals("A12BC", u.getRegistro());
        assertFalse(u.isBloqueado());
        assertTrue(u.getPersonajes().isEmpty());
    }

    @Test
    void testSetPasswordValido() {
        u.setPassword("abcdefgh");
        assertEquals("abcdefgh", u.getPassword());
    }

    @Test
    void testSetPasswordDemasiadoCortaLanzoExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> u.setPassword("short"));
    }

    @Test
    void testSetPasswordDemasiadoLargaLanzoExcepcion() {
        String longPwd = "0123456789012"; // 13 chars
        assertThrows(IllegalArgumentException.class, () -> u.setPassword(longPwd));
    }

    @Test
    void testSetPasswordNullLanzoExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> u.setPassword(null));
    }

    @Test
    void testSetRegistroValido() {
        u.setRegistro("Z99XY");
        assertEquals("Z99XY", u.getRegistro());
    }

    @Test
    void testSetRegistroInvalidoLanzoExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> u.setRegistro("12345"));
    }

    @Test
    void testBloqueadoFlag() {
        u.setBloqueado(true);
        assertTrue(u.isBloqueado());
        u.setBloqueado(false);
        assertFalse(u.isBloqueado());
    }

    @Test
    void testAddPersonaje() {
        Personaje p = new Personaje("Héroe", 2, 2, 5) {};
        u.addPersonaje(p);
        List<Personaje> lista = u.getPersonajes();
        assertEquals(1, lista.size());
        assertSame(p, lista.get(0));
    }

    @Test
    void testToStringContieneCamposClave() {
        String s = u.toString();
        assertTrue(s.contains("Ana"));
        assertTrue(s.contains("ana123"));
        assertTrue(s.contains("A12BC"));
        assertTrue(s.contains("bloqueado=false"));
        assertTrue(s.contains("#chars=0"));
    }
}