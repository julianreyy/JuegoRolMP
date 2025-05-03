package com.juegovampiro.model;

import java.util.Date;

public class Desafio {
    public enum Estado { PENDIENTE, ACEPTADO, RECHAZADO }

    private final String retadorNick;
    private final String desafiadoNick;
    private final String retadorChar;
    private final String desafiadoChar;
    private final int apuesta;
    private final Date fecha;
    private Estado estado;

    public Desafio(String retadorNick,
                   String retadorChar,
                   String desafiadoNick,
                   String desafiadoChar,
                   int apuesta) {
        this.retadorNick   = retadorNick;
        this.retadorChar   = retadorChar;
        this.desafiadoNick = desafiadoNick;
        this.desafiadoChar = desafiadoChar;
        this.apuesta       = apuesta;
        this.fecha         = new Date();
        this.estado        = Estado.PENDIENTE;
    }

    public String getRetadorNick() { return retadorNick; }
    public String getDesafiadoNick() { return desafiadoNick; }
    public String getRetadorChar() { return retadorChar; }
    public String getDesafiadoChar() { return desafiadoChar; }
    public int getApuesta() { return apuesta; }
    public Date getFecha() { return fecha; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    @Override
    public String toString() {
        return String.format(
                "%s[%s] vs %s[%s] → %d oro – %s – %s",
                retadorNick, retadorChar,
                desafiadoNick, desafiadoChar,
                apuesta, estado, fecha
        );
    }
}
