package com.juegovampiro.model;

import java.util.Date;
import java.util.List;

public class Combate {
    private final String retadorNick;
    private final String desafiadoNick;
    private final int apuesta;
    private final List<Ronda> rondas;
    private final String ganadorNick;  // nick del ganador o "empate"
    private final Date fecha;

    public Combate(String retadorNick,
                   String desafiadoNick,
                   int apuesta,
                   List<Ronda> rondas,
                   String ganadorNick,
                   Date fecha) {
        this.retadorNick   = retadorNick;
        this.desafiadoNick = desafiadoNick;
        this.apuesta       = apuesta;
        this.rondas        = rondas;
        this.ganadorNick   = ganadorNick;
        this.fecha         = fecha;
    }

    public String getRetadorNick()   { return retadorNick; }
    public String getDesafiadoNick() { return desafiadoNick; }
    public int getApuesta()          { return apuesta; }
    public List<Ronda> getRondas()   { return rondas; }
    public String getGanadorNick()   { return ganadorNick; }
    public Date getFecha()           { return fecha; }
}
