package com.juegovampiro.model;

import java.util.ArrayList;
import java.util.List;

public class Demonio extends Esbirro {
    private String pacto;
    private List<Esbirro> esbirros; // puede anidar

    public Demonio(String nombre, int salud, String pacto) {
        super(nombre, salud);
        this.pacto = pacto;
        this.esbirros = new ArrayList<>();
    }
    @Override
    public int getSalud(){
        int saludfinal = super.getSalud();
        for (Esbirro esbirro : esbirros) {
            saludfinal += esbirro.getSalud();
        }
        return saludfinal;
    }
    public String getPacto() {
        return pacto;
    }

    public List<Esbirro> getEsbirros() {
        return esbirros;
    }

    public void addEsbirro(Esbirro e) {
        esbirros.add(e);
    }

    @Override
    public String toString() {
        return super.toString() + " [Pacto=" + pacto + ", subesbirros=" + esbirros.size() + "]";
    }
}
