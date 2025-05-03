
package com.juegovampiro.model;

import java.util.ArrayList;
import java.util.List;

public class Cazador extends Personaje {
    private int voluntad;  // 0..3
    private List<Talento> talentos;

    public Cazador(String nombre, int salud, int poder, int oro) {
        super(nombre, salud, poder, oro);
        this.voluntad = 3;
        this.talentos = new ArrayList<>();
    }

    public int getVoluntad() {
        return voluntad;
    }

    public void setVoluntad(int voluntad) {
        if (voluntad < 0) voluntad = 0;
        if (voluntad > 3) voluntad = 3;
        this.voluntad = voluntad;
    }

    public List<Talento> getTalentos() {
        return talentos;
    }

    public void addTalento(Talento t) {
        talentos.add(t);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Cazador, Voluntad=%d]", voluntad);
    }
}
