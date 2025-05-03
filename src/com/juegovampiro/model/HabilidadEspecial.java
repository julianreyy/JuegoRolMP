package com.juegovampiro.model;

public abstract class HabilidadEspecial {
    private String nombre;
    private int ataque;   // 1..3
    private int defensa;  // 1..3

    public HabilidadEspecial(String nombre, int ataque, int defensa) {
        this.nombre = nombre;
        setAtaque(ataque);
        setDefensa(defensa);
    }

    public String getNombre() {
        return nombre;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        if (ataque < 1) ataque = 1;
        if (ataque > 3) ataque = 3;
        this.ataque = ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public void setDefensa(int defensa) {
        if (defensa < 1) defensa = 1;
        if (defensa > 3) defensa = 3;
        this.defensa = defensa;
    }

    @Override
    public String toString() {
        return String.format("%s (Atq:%d Def:%d)", nombre, ataque, defensa);
    }
}
