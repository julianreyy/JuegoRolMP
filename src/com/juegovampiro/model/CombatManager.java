package com.juegovampiro.model;

import com.juegovampiro.xml.XMLManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class CombatManager {

    /**
     * Simula un combate entre dos personajes:
     * - Valida que ambos tengan arma(s) activa(s) y armadura.
     * - Aplica daño hasta que la salud de uno llegue a 0.
     * - Ajusta el oro según la apuesta.
     * - Registra el Combate en XMLManager.combates y lo persiste.
     */
    public static Combate simularCombate(Usuario retUser,
                                         Personaje retador,
                                         Usuario desUser,
                                         Personaje desafiado,
                                         int apuesta) throws Exception {
        if (apuesta < 0 || retador.getOro() < apuesta) {
            throw new IllegalArgumentException("Apuesta inválida o fondos insuficientes");
        }
        if (retador.getArmasActivas().isEmpty() || retador.getArmaduraActiva() == null ||
                desafiado.getArmasActivas().isEmpty() || desafiado.getArmaduraActiva() == null) {
            throw new IllegalStateException("Ambos personajes deben tener equipo activo");
        }

        // Guarda salud original por si se quiere resetear después
        int saludOrigR = retador.getSalud();
        int saludOrigD = desafiado.getSalud();
        int saludEsbirrosOrigR = retador.getSaludEsbirros();
        int saludEsbirrosOrigD = desafiado.getSaludEsbirros();

        List<Ronda> rondas = new ArrayList<>();
        int round = 1;
        while (retador.getSalud() > 0 && desafiado.getSalud() > 0 ) {

            int danoD   = 0;
            int danoR   = 0;
            int defensaD = 0;
            int defensaR = 0;


            for(int i = 0; i < desafiado.calcularPdAt(); i++){
                if((Math.floor(Math.random() * 6) + 1)>= 5){
                    danoD+= 1;
                }

            }
            for(int i = 0; i < desafiado.calcularPdDf(); i++){
                if((Math.floor(Math.random() * 6) + 1)>= 5){
                    defensaD+= 1;
                }
            }
            for(int i = 0; i < retador.calcularPdAt(); i++){
                if((Math.floor(Math.random() * 6) + 1)>= 5){
                    danoR+= 1;
                }
            }
            for(int i = 0; i < retador.calcularPdDf(); i++){
                if((Math.floor(Math.random() * 6) + 1)>= 5){
                    defensaR+= 1;
                }
            }
            if (danoR >= defensaD) {

                if (desafiado.getSaludEsbirros() > 0) {
                    desafiado.setSaludEsbirros(desafiado.getSaludEsbirros() - 1);
                } else {
                    desafiado.setSalud(desafiado.getSalud() - 1);
                }
            }
            if (danoD>= defensaR) {
                if (retador.getSaludEsbirros() > 0) {
                    retador.setSaludEsbirros(retador.getSaludEsbirros() - 1);
                } else {
                    retador.setSalud(retador.getSalud() - 1);
                }
            }
            rondas.add(new Ronda(round++, danoR, danoD));
        }

        String ganador;
        if (retador.getSalud() > 0 && desafiado.getSalud() <= 0) {
            ganador = retUser.getNick();
            retador.setOro(retador.getOro() + apuesta);
            desafiado.setOro(desafiado.getOro() - apuesta);
        } else if (desafiado.getSalud() > 0 && retador.getSalud() <= 0) {
            ganador = desUser.getNick();
            desafiado.setOro(desafiado.getOro() + apuesta);
            retador.setOro(retador.getOro() - apuesta);
        } else {
            ganador = "empate";
        }

        Combate c = new Combate(
                retUser.getNick(),
                desUser.getNick(),
                apuesta,
                rondas,
                ganador,
                new Date()
        );

        // Registrar y persistir
        XMLManager.combates.add(c);
        XMLManager.saveCombates("data/combates.xml");

        // (Opcional) restaurar salud original para permitir reuso de objetos
        retador.setSalud(saludOrigR);
        desafiado.setSalud(saludOrigD);
        retador.setSaludEsbirros(saludEsbirrosOrigR);
        desafiado.setSaludEsbirros(saludEsbirrosOrigD);

        return c;
    }
}

