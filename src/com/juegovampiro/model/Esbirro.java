package com.juegovampiro.model;

public abstract class Esbirro {
    private String nombre;
    private int salud; // 1..3

    public Esbirro(String nombre, int salud) {
        this.nombre = nombre;
        setSalud(salud);
    }

    public String getNombre() {
        return nombre;
    }

    public int getSalud() {
        return salud;
    }

    public void setSalud(int salud) {
        if (salud < 1) salud = 1;
        if (salud > 3) salud = 3;
        this.salud = salud;
    }

    @Override
    public String toString() {
        return String.format("%s [Salud=%d]", nombre, salud);
    }
}
