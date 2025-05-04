package com.juegovampiro.client;

import com.juegovampiro.model.*;
import com.juegovampiro.xml.XMLManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.juegovampiro.xml.XMLManager.personajes;

public class Main {
    private static Usuario currentUser = null;
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        XMLManager.loadAll("data/");
        boolean running = true;
        while (running) {
            if (currentUser == null) {
                showGuestMenu();
                switch (readInt()) {
                    case 1 -> registerUser();
                    case 2 -> login();
                    case 3 -> running = false;
                    case 4 -> {
                        clearDataFiles();
                        XMLManager.loadAll("data/");
                    }
                    default -> System.out.println("Opción inválida.");
                }

            } else if (currentUser instanceof Operador) {
                showOperatorMenu();
                switch (readInt()) {
                    //case 1 -> OperatorManager.registerOperador();
                    case 1 -> darBajaUsuario();
                    case 2 -> editarPersonaje();
                    case 3 -> addAlPersonaje();
                    case 4 -> validarDesafio();
                    case 5 -> blockUser();
                    case 6 -> unblockUser();
                    case 7 -> logout();
                    default -> System.out.println("Opción inválida.");
                }
            } else {
                showUserMenu();
                switch (readInt()) {
                    case 1 -> crearPersonaje();
                    case 2 -> listarPersonajes();
                    case 3 -> equiparPersonaje();
                    case 4 -> desafiarUsuario();
                    case 5 -> gestionarDesafios();
                    case 6 -> verHistorialCombates();
                    case 7 -> verRankingGlobal();
                    case 8 -> logout();
                    case 9 -> borrarPersonaje();
                    case 10 -> borrarUsuario();
                    default -> System.out.println("Opción inválida.");
            }
            }
        }
        XMLManager.saveAll("data/");
        System.out.println("¡Hasta luego!");
    }

    private static void showGuestMenu() {
        System.out.println("\n--- Invitado ---");
        System.out.println("1. Registrarse");
        System.out.println("2. Entrar");
        System.out.println("3. Salir");
        System.out.println("4. Borrar datos guardados");
        System.out.print("> ");
    }

    private static void showUserMenu() {
        System.out.printf("\n--- %s (%s) ---\n", currentUser.getNombre(), currentUser.getNick());
        System.out.println("1. Crear personaje");
        System.out.println("2. Ver listado de personajes");
        System.out.println("3. Equipar personaje");
        System.out.println("4. Desafiar usuario");
        System.out.println("5. Gestionar desafíos");
        System.out.println("6. Ver historial de combates");
        System.out.println("7. Ver ranking global");
        System.out.println("8. Cerrar sesión");
        System.out.println("9. Borrar personaje");
        System.out.println("10. Borrar usuario");
        System.out.print("> ");
    }

    private static void showOperatorMenu() {
        System.out.printf("\n--- Menú OPERADOR [%s] ---\n", currentUser.getNick());
        System.out.println("1. Baja Usuario/Operador");
        System.out.println("2. Editar Personaje");
        System.out.println("3. Añadir Equipo/Fort/Dev/Esb");
        System.out.println("4. Validar Desafío");
        System.out.println("5. Bloquear Usuario");
        System.out.println("6. Desbloquear Usuario");
        System.out.println("7. Cerrar sesión");
        System.out.print("> ");
    }

    // ───────────── INVITADO ─────────────

    private static void registerUser() throws Exception {
        System.out.print("Tipo de usuario: 1. Operador, 2. Cliente");   int tipo = sc.nextInt();
        System.out.print("Nombre: ");   String nombre = sc.nextLine().trim();
        System.out.print("Nick: ");     String nick   = sc.nextLine().trim();
        System.out.print("Password: "); String pwd    = sc.nextLine().trim();

        if (tipo == 1) {
            // Generar registro único LNNLL usando variable final en la lambda
            String reg;
            while (true) {
                String candidate = randomRegistro();
                boolean exists = XMLManager.usuarios.stream()
                        .anyMatch(u -> candidate.equals(u.getRegistro()));
                if (!exists) {
                    reg = candidate;
                    break;
                }
            }

            Usuario u = new Usuario(nombre, nick, pwd, reg);
            XMLManager.usuarios.add(u);
            XMLManager.saveAll("data/");
            System.out.println("Registrado. Tu código: " + reg);
        }
        else if (tipo == 2){
            Usuario u = new Operador(nombre, nick, pwd);
            XMLManager.usuarios.add(u);
            XMLManager.saveAll("data/");
        }
        else {
            System.out.print("No ha sido posible especificar tipo de usuario");
        }
    }

    private static void login() throws Exception {
        System.out.print("Nick: ");     String nick = sc.nextLine().trim();
        System.out.print("Password: "); String pwd  = sc.nextLine().trim();
        currentUser = XMLManager.usuarios.stream()
                .filter(u -> u.getNick().equals(nick) && u.getPassword().equals(pwd))
                .findFirst().orElse(null);
        if (currentUser == null) {
            System.out.println("Credenciales inválidas.");
        } else {
            System.out.println("¡Bienvenido, " + currentUser.getNick() + "!");
        }
    }

    // ───────────── USUARIO ─────────────

    private static void logout() {
        System.out.println("Sesión cerrada: " + currentUser.getNick());
        currentUser = null;
    }

    private static void crearPersonaje() throws Exception {
        System.out.print("Tipo (1=V,2=L,3=C): "); int t = readInt();
        System.out.print("Nombre: ");                 String n = sc.nextLine().trim();
        System.out.print("Salud (0–5): ");            int s = readInt();
        System.out.print("Poder (1–5): ");            int p = readInt();
        System.out.print("Oro inicial: ");            int o = readInt();

        Personaje per;
        switch (t) {
            case 1 -> {
                System.out.print("Edad: ");
                int e = readInt();
                int i = 0;
                System.out.println("Elija Disciplina");
                for (Disciplina disciplina :XMLManager.disciplinas){
                    System.out.printf(i+":"+ disciplina.getNombre());
                    i++;
                }
                int dis = readInt();
                Disciplina d = XMLManager.disciplinas.get(dis);

                per = new Vampiro(n, s, p, o, e,d);
            }
            case 2 -> {
                System.out.print("Peso: ");
                int w = readInt();
                System.out.print("Altura: ");
                int a = readInt();
                int i = 0;
                System.out.println("Elija don");
                for (Don don :XMLManager.dones){
                    System.out.printf(i+":"+ don.getNombre());
                    i++;
                }
                int don = readInt();
                Don d = XMLManager.dones.get(don);
                per = new Licantropo(n, s, p, o, w, a,d);
            }
            case 3 -> {
                int i = 0;
                System.out.println("Elija Talento");
                for (Talento talento :XMLManager.talentos){
                    System.out.printf(i+":"+ talento.getNombre());
                    i++;
                }
                int ta = readInt();
                Talento talent = XMLManager.talentos.get(ta);
                per = new Cazador(n, s, p, o,talent);
            }
            default -> {
                System.out.println("Tipo inválido.");
                return;
            }
        }
        addEsbirros(per, null);
        addModificadores(per);


        currentUser.addPersonaje(per);
        XMLManager.saveAll("data/");
        System.out.println("Personaje creado.");
    }

    private static void addEsbirros(Personaje per , Demonio demonio ) {
        Esbirro e;
        int op;
        boolean stop  = false;
        do{
        System.out.print("Tipo (1=D,2=G 3=H 4:no aniadir): ");
        op = readInt();
        String name = null;
        int h = 0;

        if (op > 0 && op < 4) {
            System.out.println("Nombre");
            name = sc.nextLine().trim();
            System.out.println("Salud:");
            h = readInt();
        }

        switch (op) {
            case 1 -> {
                System.out.println("Nombre Pacto:");
                String pacto = sc.nextLine().trim();
                e = new Demonio(name, h, pacto);
                System.out.println("1:aniadir esbirros del demonio , 2=no aniadir");
                int a = readInt();
                if (a == 1) {
                    addEsbirros(per, (Demonio)e);
                }
                if(demonio !=null){
                    demonio.addEsbirro(e);
                }
                else {
                    per.addEsbirro(e);
                }
            }
            case 2 -> {
                System.out.println("Dependencia(1-5):");int d = readInt();
                e = new Ghoul(name, h, d);
                if(demonio !=null){
                    demonio.addEsbirro(e);
                }
                else {
                    per.addEsbirro(e);
                }
            }
            case 3 -> {
                System.out.println("Lealtad(ALTA, NORMAL, BAJA):");Humano.Lealtad l = Humano.Lealtad.valueOf(sc.nextLine().trim());
                e = new Humano(name, h, l);
                if(demonio !=null){
                    demonio.addEsbirro(e);
                }
                else {
                    per.addEsbirro(e);
                }
            }
            case 4->{
                stop = true;
            }
            default -> {
                stop = true;
            }
        }
        } while (!stop);
    }
    private static void  addModificadores (Personaje per){
        List<Debilidad> debilidades = new ArrayList<>();
        List<Fortaleza> fortalezas = new ArrayList<>();
        for(Modificador modificador: XMLManager.modificadores){
            if(modificador instanceof Debilidad){debilidades.add((Debilidad) modificador);}
            else {fortalezas.add((Fortaleza) modificador);}
        }
        int deb = -1;
        do{
            System.out.println("elija debilidad o ponga -1 si no quiere mas");
            int i = 0;
            for(Debilidad debilidad: debilidades){
                System.out.printf(i+":"+debilidad.getNombre()+"valor:"+debilidad.getValor()+"/n");
                i++;
            }
            deb = readInt() ;
            per.addDebilidad(debilidades.get(deb));
        }while (deb > -1);

        int fort = -1;
        do{
            System.out.println("elija fortaleza o ponga -1 si no quiere mas");
            int i = 0;
            for(Fortaleza fortaleza: fortalezas){
                System.out.printf(i+":"+fortaleza.getNombre()+"valor:"+fortaleza.getValor()+"/n");
                i++;
            }
            fort = readInt() ;
            per.addFortaleza(fortalezas.get(fort));

        }while (fort >-1);

    }

    private static void listarPersonajes() {
        List<Personaje> lst = currentUser.getPersonajes();
        if (lst.isEmpty()) {
            System.out.println("No tienes personajes.");
            return;
        }
        for (int i = 0; i < lst.size(); i++) {
            System.out.printf("%d) %s\n", i+1, lst.get(i));
        }
    }


    private static void equiparPersonaje() throws Exception {
        List<Personaje> lst = currentUser.getPersonajes();
        if (lst.isEmpty()) { System.out.println("No tienes personajes."); return; }
        System.out.print("Elige nº de personaje: "); Personaje p = lst.get(readInt()-1);
        System.out.println("1) Añadir arma  2) Poner armadura");
        if (readInt() == 1) {
            System.out.print("Nombre arma: "); String n = sc.nextLine();
            System.out.print("ModAtq (1–3): "); int ma = readInt();
            System.out.print("ModDef (1–3): "); int md = readInt();
            System.out.print("Manos(1|2): ");   int m  = readInt();
            Arma a = new Arma(n, ma, md, m);
            if (p.activarArma(a)) System.out.println("Arma activa.");
            else System.out.println("No se ha podido activar el arma.");
        } else {
            System.out.print("Nombre armadura: "); String n2 = sc.nextLine();
            System.out.print("ModDef(1–3): ");       int md2 = readInt();
            System.out.print("ModAtq(1–3): ");       int ma2 = readInt();
            Armadura ar = new Armadura(n2, ma2, md2);
            p.setArmaduraActiva(ar);
            System.out.println("Armadura puesta.");
        }
        XMLManager.saveAll("data/");
    }

    private static void desafiarUsuario() throws Exception {
        System.out.print("Nick rival: "); String dn = sc.nextLine().trim();
        Usuario des = XMLManager.usuarios.stream()
                .filter(u -> u.getNick().equals(dn))
                .findFirst().orElse(null);
        if (des == null) { System.out.println("No existe."); return; }

        List<Personaje> mine = currentUser.getPersonajes();
        if (mine.isEmpty()) { System.out.println("Sin personajes."); return; }
        listarPersonajes();
        Personaje mio = mine.get(readInt()-1);

        List<Personaje> theirs = des.getPersonajes();
        for (int i = 0; i < theirs.size(); i++)
            System.out.printf("%d) %s\n", i+1, theirs.get(i));
        Personaje otro = theirs.get(readInt()-1);

        System.out.print("Apuesta: "); int ap = readInt();
        DesafioManager.lanzarDesafio(currentUser, mio, dn, otro.getNombre(), ap);
        System.out.println("Desafío lanzado.");
    }

    private static void gestionarDesafios() throws Exception {
        List<Desafio> pend = DesafioManager.pendientesPara(currentUser);
        if (pend.isEmpty()) { System.out.println("Sin desafíos"); return; }
        for (int i = 0; i < pend.size(); i++)
            System.out.printf("%d) %s\n", i+1, pend.get(i));
        Desafio d = pend.get(readInt()-1);
        System.out.println("1) Aceptar  2) Rechazar");
        if (readInt() == 1) {
            Combate c = DesafioManager.aceptarDesafio(d);
            System.out.println("¡Combate finalizado! Ganador: " + c.getGanadorNick());
        } else {
            DesafioManager.rechazarDesafio(d);
            System.out.println("Desafío rechazado (pagaste 10%).");
        }
        XMLManager.saveAll("data/");
    }

    private static void verHistorialCombates() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (Combate c : XMLManager.combates) {
            if (c.getRetadorNick().equals(currentUser.getNick())
                    || c.getDesafiadoNick().equals(currentUser.getNick())) {
                System.out.printf("%s vs %s | %d oro | Ganador: %s | %s\n",
                        c.getRetadorNick(), c.getDesafiadoNick(),
                        c.getApuesta(), c.getGanadorNick(),
                        df.format(c.getFecha()));
            }
        }
    }

    private static void verRankingGlobal() {
        List<Personaje> all = personajes;
        all.sort((a, b) -> b.getOro() - a.getOro());
        for (int i = 0; i < all.size(); i++) {
            Personaje p = all.get(i);
            System.out.printf("%d) %s – Oro: %d\n", i+1, p.getNombre(), p.getOro());
        }
    }

    private static void borrarPersonaje() throws Exception {
        List<Personaje> lst = currentUser.getPersonajes();
        if (lst.isEmpty()) {
            System.out.println("No tienes personajes para borrar.");
            return;
        }
        System.out.println("¿Qué personaje quieres borrar?");
        for (int i = 0; i < lst.size(); i++) {
            System.out.printf("%d) %s\n", i+1, lst.get(i).getNombre());
        }
        System.out.print("> ");
        int idx = readInt() - 1;
        if (idx < 0 || idx >= lst.size()) {
            System.out.println("Opción inválida.");
            return;
        }
        Personaje eliminado = lst.remove(idx);
        // Si mantenías también una lista global de personajes:
        personajes.remove(eliminado);
        XMLManager.saveAll("data/");
        System.out.println("Personaje \"" + eliminado.getNombre() + "\" borrado.");
    }

    private static void borrarUsuario() throws Exception {
        System.out.print("¿Estás seguro de que quieres borrar tu usuario? (s/N): ");
        String resp = sc.nextLine().trim().toLowerCase();
        if (!resp.equals("s") && !resp.equals("si")) {
            System.out.println("Operación cancelada.");
            return;
        }
        // Sacamos todos sus personajes de la lista global
        for (Personaje p : currentUser.getPersonajes()) {
            personajes.remove(p);
        }
        // Borramos al usuario
        XMLManager.usuarios.remove(currentUser);
        XMLManager.saveAll("data/");
        System.out.println("Tu usuario \"" + currentUser.getNick() + "\" ha sido borrado.");
        currentUser = null;
    }

    // ───────────── OPERADOR ─────────────
    private static void darBajaUsuario() throws Exception {
        System.out.print("Nick a dar de baja: ");
        String nick = sc.nextLine().trim();
        Usuario u = XMLManager.usuarios.stream()
                .filter(x -> x.getNick().equals(nick))
                .findFirst().orElse(null);
        if (u == null) {
            System.out.println("No existe usuario con ese nick.");
            return;
        }
        XMLManager.usuarios.remove(u);
        XMLManager.saveAll("data/");
        System.out.println("Usuario/Operador dado de baja.");
    }


    private static void editarPersonaje() throws Exception {
        System.out.print("Nick del dueño: ");
        String ownerNick = sc.nextLine().trim();
        Usuario owner = XMLManager.usuarios.stream()
                .filter(u -> u.getNick().equals(ownerNick))
                .findFirst().orElse(null);
        if (owner == null) {
            System.out.println("Usuario no encontrado.");
            return;
        }
        List<Personaje> ps = owner.getPersonajes();
        if (ps.isEmpty()) {
            System.out.println("Este usuario no tiene personajes.");
            return;
        }
        for (int i = 0; i < ps.size(); i++) {
            System.out.printf("%d) %s\n", i+1, ps.get(i));
        }
        System.out.print("Elige personaje (nº): ");
        Personaje p = ps.get(readInt()-1);

        System.out.println("¿Qué quieres modificar?");
        System.out.println("1=Nombre  2=Salud  3=Poder  4=Oro  5=Campo específico");
        System.out.print("> ");
        int campo = readInt();
        System.out.print("Nuevo valor: ");
        String val = sc.nextLine().trim();

        switch (campo) {
            case 1 -> p.setNombre(val);
            case 2 -> p.setSalud(Integer.parseInt(val));
            case 3 -> p.setPoder(Integer.parseInt(val));
            case 4 -> p.setOro(Integer.parseInt(val));
            case 5 -> {
                if (p instanceof Vampiro) {
                    ((Vampiro)p).setEdad(Integer.parseInt(val));
                } else if (p instanceof Licantropo) {
                    ((Licantropo)p).setRabia(Integer.parseInt(val));
                } else if (p instanceof Cazador) {
                    ((Cazador)p).setVoluntad(Integer.parseInt(val));
                } else {
                    System.out.println("Tipo de personaje sin campo específico.");
                    return;
                }
            }
            default -> {
                System.out.println("Opción inválida.");
                return;
            }
        }

        XMLManager.saveAll("data/");
        System.out.println("Personaje modificado.");
    }


    private static void addAlPersonaje() throws Exception {
        System.out.print("Nick del dueño: ");
        String ownerNick = sc.nextLine().trim();
        Usuario owner = XMLManager.usuarios.stream()
                .filter(u -> u.getNick().equals(ownerNick))
                .findFirst().orElse(null);
        if (owner == null) {
            System.out.println("Usuario no encontrado.");
            return;
        }
        List<Personaje> ps = owner.getPersonajes();
        if (ps.isEmpty()) {
            System.out.println("Este usuario no tiene personajes.");
            return;
        }
        for (int i = 0; i < ps.size(); i++) {
            System.out.printf("%d) %s\n", i+1, ps.get(i));
        }
        System.out.print("Elige personaje (nº): ");
        Personaje p = ps.get(readInt()-1);

        System.out.println("¿Qué añadir?");
        System.out.println("1=Arma  2=Armadura  3=Fortaleza  4=Debilidad  " +
                "5=Humano  6=Ghoul  7=Demonio");
        System.out.print("> ");
        switch (readInt()) {
            case 1 -> {
                System.out.print("Nombre arma: ");
                String n = sc.nextLine();
                System.out.print("ModAtaque: ");
                int ma = readInt();
                System.out.print("ModDefensa: ");
                int md = readInt();
                System.out.print("Manos(1|2): ");
                int m  = readInt();
                p.addArma(new Arma(n, ma, md, m));
            }
            case 2 -> {
                System.out.print("Nombre armadura: ");
                String n2 = sc.nextLine();
                System.out.print("ModAtaque: ");
                int ma2 = readInt();
                System.out.print("ModDefensa: ");
                int md2 = readInt();
                p.setArmaduraActiva(new Armadura(n2, ma2, md2));
            }
            case 3 -> {
                System.out.print("Nombre fortaleza: ");
                String fn = sc.nextLine();
                System.out.print("Valor(1–5): ");
                int fv = readInt();
                p.addFortaleza(new Fortaleza(fn, fv));
            }
            case 4 -> {
                System.out.print("Nombre debilidad: ");
                String dn = sc.nextLine();
                System.out.print("Valor(1–5): ");
                int dv = readInt();
                p.addDebilidad(new Debilidad(dn, dv));
            }
            case 5 -> {
                System.out.print("Nombre humano: ");
                String hn = sc.nextLine();
                System.out.print("Salud(1–3): ");
                int hs = readInt();
                System.out.print("Lealtad(ALTA,NORMAL,BAJA): ");
                Humano.Lealtad l = Humano.Lealtad.valueOf(sc.nextLine());
                p.addEsbirro(new Humano(hn, hs, l));
            }
            case 6 -> {
                System.out.print("Nombre ghoul: ");
                String gn = sc.nextLine();
                System.out.print("Salud(1–3): ");
                int gs = readInt();
                System.out.print("Dependencia(1–5): ");
                int gd = readInt();
                p.addEsbirro(new Ghoul(gn, gs, gd));
            }
            case 7 -> {
                System.out.print("Nombre demonio: ");
                String dn2 = sc.nextLine();
                System.out.print("Salud(1–3): ");
                int ds = readInt();
                System.out.print("Pacto: ");
                String pact = sc.nextLine();
                p.addEsbirro(new Demonio(dn2, ds, pact));
            }
            default -> {
                System.out.println("Opción inválida.");
                return;
            }
        }

        XMLManager.saveAll("data/");
        System.out.println("Añadido con éxito.");
    }


    private static void validarDesafio() throws Exception {
        List<Desafio> pend = new ArrayList<>();
        for (Desafio d : XMLManager.desafios) {
            if (d.getEstado() == Desafio.Estado.PENDIENTE) {
                pend.add(d);
            }
        }
        if (pend.isEmpty()) {
            System.out.println("No hay desafíos pendientes.");
            return;
        }
        for (int i = 0; i < pend.size(); i++) {
            System.out.printf("%d) %s\n", i+1, pend.get(i));
        }
        System.out.print("Elige desafío a validar (nº): ");
        Desafio d = pend.get(readInt()-1);

        // Mostrar fortalezas/debilidades de ambos personajes
        Personaje r = XMLManager.usuarios.stream()
                .filter(u -> u.getNick().equals(d.getRetadorNick()))
                .findFirst().get()
                .getPersonajes().stream()
                .filter(p -> p.getNombre().equals(d.getRetadorChar()))
                .findFirst().get();
        Personaje s = XMLManager.usuarios.stream()
                .filter(u -> u.getNick().equals(d.getDesafiadoNick()))
                .findFirst().get()
                .getPersonajes().stream()
                .filter(p -> p.getNombre().equals(d.getDesafiadoChar()))
                .findFirst().get();

        System.out.println("Fortalezas retador: "   + r.getFortalezas());
        System.out.println("Debilidades retador: "  + r.getDebilidades());
        System.out.println("Fortalezas desafiado: " + s.getFortalezas());
        System.out.println("Debilidades desafiado:" + s.getDebilidades());

        // Marcar como VALIDADO si tuvieras ese estado, aquí lo dejamos PENDIENTE
        System.out.println("Validación completa. (estado sigue PENDIENTE)");
        XMLManager.saveAll("data/");
    }


    private static void blockUser() throws Exception {
        System.out.print("Nick a bloquear: ");
        String nick = sc.nextLine().trim();
        Usuario u = XMLManager.usuarios.stream()
                .filter(x -> x.getNick().equals(nick))
                .findFirst().orElse(null);
        if (u == null) {
            System.out.println("Usuario no encontrado.");
            return;
        }
        u.setBloqueado(true);
        XMLManager.saveAll("data/");
        System.out.println("Usuario bloqueado.");
    }


    private static void unblockUser() throws Exception {
        System.out.print("Nick a desbloquear: ");
        String nick = sc.nextLine().trim();
        Usuario u = XMLManager.usuarios.stream()
                .filter(x -> x.getNick().equals(nick))
                .findFirst().orElse(null);
        if (u == null) {
            System.out.println("Usuario no encontrado.");
            return;
        }
        u.setBloqueado(false);
        XMLManager.saveAll("data/");
        System.out.println("Usuario desbloqueado.");
    }


    // ─────────────── UTIL ────────────────

    private static int readInt() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private static String randomRegistro() {
        Random r = new Random();
        return "" +
                (char)('A' + r.nextInt(26)) +
                (char)('0' + r.nextInt(10)) +
                (char)('0' + r.nextInt(10)) +
                (char)('A' + r.nextInt(26)) +
                (char)('A' + r.nextInt(26));
    }

    private static void clearDataFiles() {
        File dataDir = new File("data");
        if (!dataDir.exists() || !dataDir.isDirectory()) {
            System.out.println("Directorio data/ no encontrado.");
            return;
        }
        File[] xmlFiles = dataDir.listFiles((dir,name)->name.toLowerCase().endsWith(".xml"));
        if (xmlFiles == null || xmlFiles.length == 0) {
            System.out.println("No hay ficheros XML para borrar en data/.");
            return;
        }
        int deleted = 0;
        for (File f: xmlFiles) {
            if (f.delete()) deleted++;
            System.err.println("No se pudo borrar: " + f.getName());
        }
        System.out.printf("Borrados %d ficheros XML de data/.\n", deleted);
    }
}
