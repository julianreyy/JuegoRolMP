package com.juegovampiro.model;

public class Ghoul extends Esbirro {
    private int dependencia;  // 1..5

    public Ghoul(String nombre, int salud, int dependencia) {
        super(nombre, salud);
        setDependencia(dependencia);
    }

    public int getDependencia() {
        return dependencia;
    }

    public void setDependencia(int dependencia) {
        if (dependencia < 1) dependencia = 1;
        if (dependencia > 5) dependencia = 5;
        this.dependencia = dependencia;
    }

    @Override
    public String toString() {
        return super.toString() + " [Dep=" + dependencia + "]";
    }
}
