package com.juegovampiro.model;

import java.util.ArrayList;
import java.util.List;

public class Vampiro extends Personaje {
    private int sangre;   // 0..10
    private int edad;
    private List<Disciplina> disciplinas;

    public Vampiro(String nombre, int salud, int poder, int oro, int edad) {
        super(nombre, salud, poder, oro);
        this.edad = edad;
        this.sangre = 0;
        this.disciplinas = new ArrayList<>();
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

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void addDisciplina(Disciplina d) {
        disciplinas.add(d);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Vampiro, Edad=%d, Sangre=%d]", edad, sangre);
    }
}
