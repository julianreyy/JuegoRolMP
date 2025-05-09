package com.juegovampiro.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Personaje {
    private String nombre;
    private int salud;          // 0..5
    private int poder;          // 1..5
    private int oro;            // >=0
    private List<Arma> armas;               // todas las armas que posee
    private List<Arma> armasActivas;        // 1 o 2 armas activas según manos
    //private List<Armadura> armaduras;
    private Armadura armaduraActiva;        // una sola
    private List<Esbirro> esbirros;// todos los esbirros (indefinidos)
    private int saludEsbirros ;
    private List<Debilidad> debilidades;
    private List<Fortaleza> fortalezas;

    public Personaje(String nombre, int salud, int poder, int oro) {
        this.nombre = nombre;
        setSalud(salud);
        setPoder(poder);
        setOro(oro);

        this.armas = new ArrayList<>();
        //this.armaduras = new ArrayList<>();
        this.armasActivas = new ArrayList<>();
        this.esbirros = new ArrayList<>();
        this.debilidades = new ArrayList<>();
        this.fortalezas = new ArrayList<>();
        this.saludEsbirros=0;
    }

    // Getters y setters con validación
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getSalud() {

        return salud;
    }
    public void setSalud(int salud) {
        if (salud < 0) salud = 0;
        if (salud > 5) salud = 5;
        this.salud = salud;
    }
    public int getSaludEsbirros(){return saludEsbirros;}
    public void setSaludEsbirros(int s){
        saludEsbirros =s;
    }
    public int getPoder() {
        return poder;
    }
    public void setPoder(int poder) {
        if (poder < 1) poder = 1;
        if (poder > 5) poder = 5;
        this.poder = poder;
    }
    public int getOro() {
        return oro;
    }
    public void setOro(int oro) {
        if (oro < 0) oro = 0;
        this.oro = oro;
    }
    public List<Arma> getArmas() {
        return armas;
    }

    /* public List<Armadura> getArmaduras() {
        return armaduras;
    } */

    public List<Arma> getArmasActivas() {
        return armasActivas;
    }
    public Armadura getArmaduraActiva() {
        return armaduraActiva;
    }
    public void setArmaduraActiva(Armadura armaduraActiva) {
        this.armaduraActiva = armaduraActiva;
    }
    public List<Esbirro> getEsbirros() {
        return esbirros;
    }
    public List<Debilidad> getDebilidades() {
        return debilidades;
    }
    public List<Fortaleza> getFortalezas() {
        return fortalezas;
    }
    // Métodos de conveniencia
    public void addArma(Arma a) {
      armas.add(a);
    }

    /* public void addArmadura(Armadura a) {
        armaduras.add(a);
    } */

    public boolean activarArma(Arma a) {
        if (armas.contains(a)) return false;

        if (a.getManos() == 2) {
            desactivarArmas();
            armasActivas.add(a);
            return true;
        } else {
            if ((armasActivas.size() >= 2) || (!armasActivas.isEmpty() && armasActivas.get(0).getManos() == 2)) desactivarArmas();;
            armasActivas.add(a);
            return true;
        }
    }

    public void desactivarArmas() {
        armasActivas.clear();
    }

    public void addEsbirro(Esbirro e) {

        esbirros.add(e);
        this.saludEsbirros=e.getSalud();
    }

    public void addDebilidad(Debilidad d) {
        debilidades.add(d);
    }

    public void addFortaleza(Fortaleza f) {
        fortalezas.add(f);
    }

    public int calcularPdAt(){
        return 0;
    } // hay que mirarlo
    public int calcularPdDf(){
        return 0;
    }// hay que mirarlo
    @Override
    public String toString() {
        return String.format("%s [Salud=%d, Poder=%d, Oro=%d]", nombre, salud, poder, oro);
    }
}
