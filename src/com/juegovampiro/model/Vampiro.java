package com.juegovampiro.model;

import java.util.ArrayList;
import java.util.List;

public class Vampiro extends Personaje {
    private int sangre;   // 0..10
    private int edad;     // edad en años
    private List<Disciplina> disciplinas;

    public Vampiro(String nombre, int salud, int poder, int oro, int edad) {
        super(nombre, salud, poder, oro);
        this.disciplinas = new ArrayList<>();
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

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void addDisciplina(Disciplina d) {
        disciplinas.add(d);
    }

    @Override
    public int calcularPdAt(){
        int p ;
        int disclipinaAttack = 0;
        int extraAttack = 0;
        if(this.sangre >= disclipina.getCostoSangre()){
            disclipinaAttack = disclipina.getAtaque();
        }
        if (this.sangre >= 5){
            extraAttack= 2;
        }
        p = this.getPoder()+ disclipinaAttack+this.getArmasActivas().stream().mapToInt(a -> a.getModAtaque()).sum()+this.getArmaduraActiva().getModAtaque()+extraAttack;//falta modificar los talentos a que sea 1 talento
        if(this.sangre >= disclipina.getCostoSangre()){
            this.sangre -= disclipina.getCostoSangre();
        }
        return p;
    }
    public int calcularPdAt(){
        int p ;
        int disclipinaDefensa = 0;
        int extraDefensa = 0;
        if(this.sangre >= disclipina.getCostoSangre()){
            disclipinaDefensa = disclipina.getDefensa();
        }
        if (this.sangre >= 5){
            extraAttack= 2;
        }
        p = this.getPoder()+ disclipinaDefensa+this.getArmasActivas().stream().mapToInt(a -> a.getModDefensa()).sum()+this.getArmaduraActiva().getModDefensa()+extraDefensa;//falta modificar los talentos a que sea 1 talento
        if(this.sangre >= disclipina.getCostoSangre()){
            this.sangre -= disclipina.getCostoSangre();
        }
        return p;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Vampiro, Edad=%d, Sangre=%d]", edad, sangre);
    }
}
