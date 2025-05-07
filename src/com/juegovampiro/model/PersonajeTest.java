package com.juegovampiro.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonajeTest {

    private Personaje personaje;

    @BeforeEach
    void setUp() {
        // Subclase anónima de Personaje para poder instanciarlo
        personaje = new Personaje("Héroe", 3, 4, 10) { };
    }

    // —— setSalud() —— //

    @Test
    void testSetSaludNegativaSeClampaACero() {
        personaje.setSalud(-5);
        assertEquals(0, personaje.getSalud());
    }

    @Test
    void testSetSaludSobreLimiteSeClampaAMaximoCinco() {
        personaje.setSalud(10);
        assertEquals(5, personaje.getSalud());
    }

    @Test
    void testSetSaludDentroDeRangoPermaneceIgual() {
        personaje.setSalud(2);
        assertEquals(2, personaje.getSalud());
    }

    // —— setPoder() —— //

    @Test
    void testSetPoderInferiorARangoSeClampaAUno() {
        personaje.setPoder(0);
        assertEquals(1, personaje.getPoder());
    }

    @Test
    void testSetPoderSuperiorARangoSeClampaACinco() {
        personaje.setPoder(10);
        assertEquals(5, personaje.getPoder());
    }

    @Test
    void testSetPoderDentroDeRangoPermaneceIgual() {
        personaje.setPoder(3);
        assertEquals(3, personaje.getPoder());
    }

    // —— setOro() —— //

    @Test
    void testSetOroNegativoSeClampaACero() {
        personaje.setOro(-100);
        assertEquals(0, personaje.getOro());
    }

    @Test
    void testSetOroValidoPermaneceIgual() {
        personaje.setOro(42);
        assertEquals(42, personaje.getOro());
    }

    // —— Armas —— //

    @Test
    void testAddArmaYGetArmas() {
        Arma espada = new Arma("Espada", 2, 1, 1);
        personaje.addArma(espada);
        assertTrue(personaje.getArmas().contains(espada));
    }

    @Test
    void testActivarArmaUnaMano() {
        Arma daga = new Arma("Daga", 1, 1, 1);
        personaje.addArma(daga);
        assertTrue(personaje.activarArma(daga));
        assertEquals(1, personaje.getArmasActivas().size());
        assertEquals(daga, personaje.getArmasActivas().get(0));
    }

    @Test
    void testActivarArmaDosManosReemplaza() {
        Arma espada = new Arma("Espada", 2, 1, 1);
        Arma mandoble = new Arma("Mandoble", 3, 0, 2);
        personaje.addArma(espada);
        personaje.addArma(mandoble);

        assertTrue(personaje.activarArma(espada));
        assertTrue(personaje.activarArma(mandoble));
        assertEquals(1, personaje.getArmasActivas().size());
        assertEquals(mandoble, personaje.getArmasActivas().get(0));
    }

    @Test
    void testDesactivarArmasLimpiaLista() {
        Arma hacha = new Arma("Hacha", 2, 1, 1);
        personaje.addArma(hacha);
        personaje.activarArma(hacha);

        personaje.desactivarArmas();
        assertTrue(personaje.getArmasActivas().isEmpty());
    }

    // —— Esbirros —— //

    @Test
    void testAddEsbirroYSaludEsbirros() {
        Humano h = new Humano("Siervo", 2, Humano.Lealtad.ALTA);
        personaje.addEsbirro(h);
        assertTrue(personaje.getEsbirros().contains(h));
        assertEquals(h.getSalud(), personaje.getSaludEsbirros());
    }

    // —— Debilidades y fortalezas —— //

    @Test
    void testAddDebilidad() {
        Debilidad d = new Debilidad("Fuego", 3);
        personaje.addDebilidad(d);
        assertTrue(personaje.getDebilidades().contains(d));
    }

    @Test
    void testAddFortaleza() {
        Fortaleza f = new Fortaleza("Luna", 2);
        personaje.addFortaleza(f);
        assertTrue(personaje.getFortalezas().contains(f));
    }
}