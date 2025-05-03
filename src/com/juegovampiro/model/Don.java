package com.juegovampiro.model;

public class Don extends HabilidadEspecial {
    private int rabiaMinima;  // 0..3

    public Don(String nombre, int ataque, int defensa, int rabiaMinima) {
        super(nombre, ataque, defensa);
        setRabiaMinima(rabiaMinima);
    }

    public int getRabiaMinima() {
        return rabiaMinima;
    }

    public void setRabiaMinima(int rabiaMinima) {
        if (rabiaMinima < 0) rabiaMinima = 0;
        if (rabiaMinima > 3) rabiaMinima = 3;
        this.rabiaMinima = rabiaMinima;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Rabia≥%d]", rabiaMinima);
    }
}
