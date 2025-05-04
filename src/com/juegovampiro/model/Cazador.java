
package com.juegovampiro.model;

import java.util.ArrayList;
import java.util.List;

public class Cazador extends Personaje {
    private int voluntad;  // 0..3
    private Talento talento;

    public Cazador(String nombre, int salud, int poder, int oro, Talento talento) {
        super(nombre, salud, poder, oro);
        this.voluntad = 3;
        this.talento = talento;
    }

    public int getVoluntad() {
        return voluntad;
    }

    public void setVoluntad(int voluntad) {
        if (voluntad < 0) voluntad = 0;
        if (voluntad > 3) voluntad = 3;
        this.voluntad = voluntad;
    }

    public Talento getTalento() {
        return talento;
    }


    @Override
    public int calcularPdAt(){
        int p ;
        p = this.getPoder()+ talento.getAtaque()+this.getArmasActivas().stream().mapToInt(a -> a.getModAtaque()).sum()+this.getArmaduraActiva().getModAtaque()+this.voluntad;//falta modificar los talentos a que sea 1 talento
        return p;
    }
    @Override
    public int calcularPdDf(){
        int df;
        df =  this.getPoder()+ talento.getDefensa()+this.getArmasActivas().stream().mapToInt(a -> a.getModDefensa()).sum()+this.getArmaduraActiva().getModDefensa()+this.voluntad;
        return df;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Cazador, Voluntad=%d]", voluntad);
    }
}
