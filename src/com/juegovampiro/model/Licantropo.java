package com.juegovampiro.model;

import java.util.ArrayList;
import java.util.List;

public class Licantropo extends Personaje {
    private int rabia;  // 0..3
    private List<Don> dones;
    private boolean isHuman;
    private int peso;
    private int altura;

    public Licantropo(String nombre, int salud, int poder, int oro, int peso, int altura) {
        super(nombre, salud, poder, oro);
        this.rabia = 0;
        this.dones = new ArrayList<>();
        this.isHuman = true;
        this.peso = peso;
        this.altura = altura;
    }

    public boolean getIsHuman() {
        return isHuman;
    }

    public void setIsHuman(boolean isHuman) {
        this.isHuman = isHuman;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }


    public int getRabia() {
        return rabia;
    }

    public void setRabia(int rabia) {
        if (rabia < 0) rabia = 0;
        if (rabia > 3) rabia = 3;
        this.rabia = rabia;
    }

    public List<Don> getDones() {
        return dones;
    }

    public void addDon(Don d) {
        dones.add(d);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Licántropo, Rabia=%d]", rabia);
    }
}
