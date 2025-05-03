package com.juegovampiro.model;

public abstract class Modificador {
    private String nombre;
    private int valor; // 1..5

    public Modificador(String nombre, int valor) {
        this.nombre = nombre;
        setValor(valor);
    }

    public String getNombre() {
        return nombre;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        if (valor < 1) valor = 1;
        if (valor > 5) valor = 5;
        this.valor = valor;
    }

    @Override
    public String toString() {
        return String.format("%s (+%d)", nombre, valor);
    }
}
