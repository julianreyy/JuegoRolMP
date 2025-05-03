package com.juegovampiro.model;

public class Arma extends Equipo {
    private int manos; // 1 ó 2

    public Arma(String nombre, int modAtaque, int modDefensa, int manos) {
        super(nombre, modAtaque, modDefensa);
        setManos(manos);
    }

    public int getManos() {
        return manos;
    }

    public void setManos(int manos) {
        if (manos < 1) manos = 1;
        if (manos > 2) manos = 2;
        this.manos = manos;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [%d mano(s)]", manos);
    }
}
