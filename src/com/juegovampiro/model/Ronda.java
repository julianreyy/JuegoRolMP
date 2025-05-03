package com.juegovampiro.model;

public class Ronda {
    private final int numero;
    private final int danioRetador;
    private final int danioDesafiado;

    public Ronda(int numero, int danioRetador, int danioDesafiado) {
        this.numero = numero;
        this.danioRetador = danioRetador;
        this.danioDesafiado = danioDesafiado;
    }

    public int getNumero() {
        return numero;
    }
    public int getDanioRetador() {
        return danioRetador;
    }
    public int getDanioDesafiado() {
        return danioDesafiado;
    }
}
