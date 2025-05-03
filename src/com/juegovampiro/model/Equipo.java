package com.juegovampiro.model;

public abstract class Equipo {
    private String nombre;
    private int modAtaque;  // 1..3
    private int modDefensa; // 1..3

    public Equipo(String nombre, int modAtaque, int modDefensa) {
        this.nombre = nombre;
        setModAtaque(modAtaque);
        setModDefensa(modDefensa);
    }

    public String getNombre() {
        return nombre;
    }

    public int getModAtaque() {
        return modAtaque;
    }

    public void setModAtaque(int modAtaque) {
        if (modAtaque < 1) modAtaque = 1;
        if (modAtaque > 3) modAtaque = 3;
        this.modAtaque = modAtaque;
    }

    public int getModDefensa() {
        return modDefensa;
    }

    public void setModDefensa(int modDefensa) {
        if (modDefensa < 1) modDefensa = 1;
        if (modDefensa > 3) modDefensa = 3;
        this.modDefensa = modDefensa;
    }

    @Override
    public String toString() {
        return String.format("%s (Atq+%d Def+%d)", nombre, modAtaque, modDefensa);
    }
}
