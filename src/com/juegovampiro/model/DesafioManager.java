package com.juegovampiro.model;

import com.juegovampiro.client.Main;
import com.juegovampiro.xml.XMLManager;
import java.util.List;
import java.util.stream.Collectors;

public class DesafioManager {

    /** Lanza un nuevo desafío. */
    public static boolean lanzarDesafio(Usuario retUser,
                                     Personaje retChar,
                                     String desafiadoNick,
                                     String desafiadoChar,
                                     int apuesta) throws Exception {
        Usuario desUser = XMLManager.usuarios.stream()
                .filter(u -> u.getNick().equals(desafiadoNick))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Usuario desafiado no existe"));


        if (retChar.getOro() < apuesta || apuesta < 0) {
            //throw new IllegalArgumentException("Apuesta inválida o fondos insuficientes");
            System.out.println("Apuesta inválida o fondos insuficientes");
            return false;
        }
        if (retChar.getArmasActivas().isEmpty() || retChar.getArmaduraActiva() == null) {
            //throw new IllegalStateException("Tu personaje no tiene equipo activo");
            System.out.println("Tu personaje no tiene equipo activo");
            return false;
        }

        Desafio d = new Desafio(
                retUser.getNick(),
                retChar.getNombre(),
                desafiadoNick,
                desafiadoChar,
                apuesta
        );
        XMLManager.desafios.add(d);
        XMLManager.saveDesafios("data/desafios.xml");
        return true;
    }

    /** Devuelve la lista de desafíos pendientes para el usuario conectado. */
    public static List<Desafio> pendientesPara(Usuario user) {
        return XMLManager.desafios.stream()
                .filter(d -> d.getDesafiadoNick().equals(user.getNick())
                        && d.getEstado() == Desafio.Estado.PENDIENTE)
                .collect(Collectors.toList());
    }

    /** Acepta un desafío y simula el combate. */
    public static Combate aceptarDesafio(Desafio d) throws Exception {
        Usuario retUser = XMLManager.usuarios.stream()
                .filter(u -> u.getNick().equals(d.getRetadorNick()))
                .findFirst()
                .orElseThrow();
        Usuario desUser = XMLManager.usuarios.stream()
                .filter(u -> u.getNick().equals(d.getDesafiadoNick()))
                .findFirst()
                .orElseThrow();

        Personaje retChar = retUser.getPersonajes().stream()
                .filter(p -> p.getNombre().equals(d.getRetadorChar()))
                .findFirst()
                .orElseThrow();
        Personaje desChar = desUser.getPersonajes().stream()
                .filter(p -> p.getNombre().equals(d.getDesafiadoChar()))
                .findFirst()
                .orElseThrow();

        // Simular combate

        Combate c = CombatManager.simularCombate(
                retUser, retChar, desUser, desChar, d.getApuesta()
        );

        d.setEstado(Desafio.Estado.ACEPTADO);
        XMLManager.saveDesafios("data/desafios.xml");
        return c;
    }

    /** Rechaza un desafío: paga 10% de la apuesta. */
    public static void rechazarDesafio(Desafio d) throws Exception {
        Usuario desUser = XMLManager.usuarios.stream()
                .filter(u -> u.getNick().equals(d.getDesafiadoNick()))
                .findFirst()
                .orElseThrow();
        Personaje desChar = desUser.getPersonajes().stream()
                .filter(p -> p.getNombre().equals(d.getDesafiadoChar()))
                .findFirst()
                .orElseThrow();

        int multa = (int) Math.ceil(d.getApuesta() * 0.1);
        desChar.setOro(desChar.getOro() - multa);

        d.setEstado(Desafio.Estado.RECHAZADO);
        XMLManager.saveDesafios("data/desafios.xml");
        XMLManager.saveCharacters("data/personajes.xml");
    }
}
