package com.juegovampiro.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Usuario {
    private String nombre;
    private String nick;
    private String password;
    private String registro;           // Formato LNNLL, p.ej. A12BC
    private boolean bloqueado;         // Nuevo campo
    private List<Personaje> personajes;

    private static final Pattern REGISTRO_PATTERN =
            Pattern.compile("^[A-Za-z]\\d{2}[A-Za-z]{2}$");

    public Usuario(String nombre, String nick, String password, String registro) {
        this.nombre    = nombre;
        this.nick      = nick;
        this.bloqueado = false;
        setPassword(password);
        setRegistro(registro);
        this.personajes = new ArrayList<>();
    }

    public boolean isBloqueado()     { return bloqueado; }
    public void    setBloqueado(boolean b) { bloqueado = b; }

    public String getNombre()        { return nombre; }
    public String getNick()          { return nick; }
    public String getPassword()      { return password; }
    public String getRegistro()      { return registro; }
    public List<Personaje> getPersonajes() { return personajes; }

    public void setPassword(String password) {
        if (password == null || password.length() < 8 || password.length() > 12) {
            throw new IllegalArgumentException(
                    "Password debe tener entre 8 y 12 caracteres");
        }
        this.password = password;
    }

    public void setRegistro(String registro) {
        if (registro != null &&
                !REGISTRO_PATTERN.matcher(registro).matches()) {
            throw new IllegalArgumentException(
                    "Registro inválido: debe cumplir LNNLL (p.ej. A12BC)");
        }
        this.registro = registro;
    }

    public void addPersonaje(Personaje p) {
        personajes.add(p);
    }

    @Override
    public String toString() {
        return String.format(
                "Usuario[nombre=%s, nick=%s, registro=%s, bloqueado=%s, #chars=%d]",
                nombre, nick, registro, bloqueado, personajes.size()
        );
    }
}
