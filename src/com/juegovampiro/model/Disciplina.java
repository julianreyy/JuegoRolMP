package com.juegovampiro.model;

public class Disciplina extends HabilidadEspecial {
    private int costoSangre;  // 1..3

    public Disciplina(String nombre, int ataque, int defensa, int costoSangre) {
        super(nombre, ataque, defensa);
        setCostoSangre(costoSangre);
    }

    public int getCostoSangre() {
        return costoSangre;
    }

    public void setCostoSangre(int costoSangre) {
        if (costoSangre < 1) costoSangre = 1;
        if (costoSangre > 3) costoSangre = 3;
        this.costoSangre = costoSangre;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Coste:%d sangre]", costoSangre);
    }
}
