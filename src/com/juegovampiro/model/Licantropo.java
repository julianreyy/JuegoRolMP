package com.juegovampiro.model;

import java.util.ArrayList;
import java.util.List;

public class Licantropo extends Personaje {
    private int rabia;  // 0..3
    private Don don;
    private boolean isHuman;
    private int peso;
    private int altura;

    public Licantropo(String nombre, int salud, int poder, int oro, int peso, int altura, Don don) {
        super(nombre, salud, poder, oro);
        this.rabia = 0;
        this.don = don;
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

    public Don getDon() {
        return don;
    }

    @Override
    public int calcularPdAt(){
        int p ;
        int donAttack = 0;
        if(this.rabia >= don.getRabiaMinima()){
            donAttack = don.getAtaque();
        }
        p = this.getPoder()+ donAttack+this.getArmasActivas().stream().mapToInt(a -> a.getModAtaque()).sum()+this.getArmaduraActiva().getModAtaque()+this.rabia; //falta modificar los talentos a que sea 1 talento
        if(this.rabia >= don.getRabiaMinima()){
            this.rabia -= don.getRabiaMinima();
        }
        return p;
    }
    @Override
    public int calcularPdDf(){
        int p ;
        int donDefense = 0;
        if(this.rabia >= don.getRabiaMinima()){
            donDefense= don.getDefensa();
        }
        p = this.getPoder()+ donDefense+this.getArmasActivas().stream().mapToInt(a -> a.getModDefensa()).sum()+this.getArmaduraActiva().getModDefensa()+this.rabia; //falta modificar los talentos a que sea 1 talento
        if(this.rabia >= don.getRabiaMinima()){
            this.rabia -= don.getRabiaMinima();
        }
        return p;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Licántropo, Rabia=%d]", rabia);
    }
}
