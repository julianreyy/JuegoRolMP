package com.juegovampiro.model;

/**
 * Un Operador del sistema: tiene nombre, nick y password,
 * pero NO número de registro.
 */
public class Operador extends Usuario {
    public Operador(String nombre, String nick, String password) {
        super(nombre, nick, password, null);
    }

    @Override
    public String toString() {
        return String.format("Operador[nombre=%s, nick=%s]",
                getNombre(), getNick());
    }
}
