package com.juegovampiro.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CombateTest {

    @Test
    void testConstructorYGetters() {
        List<Ronda> rondas = Arrays.asList(
                new Ronda(1, 2, 3),
                new Ronda(2, 1, 0)
        );
        Date ahora = new Date();
        Combate c = new Combate("alice", "bob", 50, rondas, "alice", ahora);

        assertEquals("alice", c.getRetadorNick());
        assertEquals("bob", c.getDesafiadoNick());
        assertEquals(50, c.getApuesta());

        List<Ronda> copiada = c.getRondas();
        assertEquals(2, copiada.size());
        assertEquals(1, copiada.get(0).getNumero());
        assertEquals(2, copiada.get(0).getDanioDesafiado());
        assertEquals(1, copiada.get(1).getDanioRetador());

        assertEquals("alice", c.getGanadorNick());
        assertSame(ahora, c.getFecha());
    }

    @Test
    void testRondasInmutables() {
        List<Ronda> rondas = Arrays.asList(new Ronda(1,1,1));
        Combate c = new Combate("r", "d", 10, rondas, "r", new Date());

        // Intentar modificar la lista original no debe afectar al Combate
        rondas.set(0, new Ronda(99,9,9));
        assertEquals(1, c.getRondas().get(0).getNumero());
    }
}