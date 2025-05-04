package com.juegovampiro.model;

import java.util.ArrayList;
import java.util.List;

public class Vampiro extends Personaje {
    private int sangre;   // 0..10
    private int edad;     // edad en años
    private Disciplina disciplina;

    public Vampiro(String nombre, int salud, int poder, int oro, int edad,Disciplina disciplina) {
        super(nombre, salud, poder, oro);
        this.disciplina = null;
        setEdad(edad);
        this.sangre = 0;
    }

    public int getSangre() {
        return sangre;
    }

    public void setSangre(int sangre) {
        if (sangre < 0) sangre = 0;
        if (sangre > 10) sangre = 10;
        this.sangre = sangre;
    }

    public int getEdad() {
        return edad;
    }

    /** Ajusta la edad del vampiro; mínima 0, no hay máximo estricto */
    public void setEdad(int edad) {
        if (edad < 0) edad = 0;
        this.edad = edad;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void addDisciplina(Disciplina d) {
        this.disciplina = d;
    }

    @Override
    public int calcularPdAt(){
        int p ;
        int disclipinaAttack = 0;
        int extraAttack = 0;
        if(this.sangre >= disciplina.getCostoSangre()){
            disclipinaAttack = disciplina.getAtaque();
        }
        if (this.sangre >= 5){
            extraAttack= 2;
        }
        p = this.getPoder()+ disclipinaAttack+this.getArmasActivas().stream().mapToInt(a -> a.getModAtaque()).sum()+this.getArmaduraActiva().getModAtaque()+extraAttack;//falta modificar los talentos a que sea 1 talento
        if(this.sangre >= disciplina.getCostoSangre()){
            this.sangre -= disciplina.getCostoSangre();
        }
        return p;
    }
    public int calcularPdDf(){
        int p ;
        int disclipinaDefensa = 0;
        int extraDefensa = 0;
        if(this.sangre >= disciplina.getCostoSangre()){
            disclipinaDefensa = disciplina.getDefensa();
        }
        if (this.sangre >= 5){
            extraDefensa= 2;
        }
        p = this.getPoder()+ disclipinaDefensa+this.getArmasActivas().stream().mapToInt(a -> a.getModDefensa()).sum()+this.getArmaduraActiva().getModDefensa()+extraDefensa;//falta modificar los talentos a que sea 1 talento
        if(this.sangre >= disciplina.getCostoSangre()){
            this.sangre -= disciplina.getCostoSangre();
        }
        return p;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Vampiro, Edad=%d, Sangre=%d]", edad, sangre);
    }
}
