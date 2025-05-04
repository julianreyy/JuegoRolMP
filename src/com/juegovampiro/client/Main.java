package com.juegovampiro.client;

import com.juegovampiro.model.*;
import com.juegovampiro.xml.XMLManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

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
                    case 1 -> OperatorManager.registerOperador();
                    case 2 -> OperatorManager.darBajaUsuario();
                    case 3 -> OperatorManager.editarPersonaje();
                    case 4 -> OperatorManager.anyadirAlPersonaje();
                    case 5 -> OperatorManager.validarDesafio();
                    case 6 -> OperatorManager.blockUser();
                    case 7 -> OperatorManager.unblockUser();
                    case 8 -> logout();
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
        System.out.print("> ");
    }

    private static void showOperatorMenu() {
        System.out.printf("\n--- Menú OPERADOR [%s] ---\n", currentUser.getNick());
        System.out.println("1. Alta Operador");
        System.out.println("2. Baja Usuario/Operador");
        System.out.println("3. Editar Personaje");
        System.out.println("4. Añadir Equipo/Fort/Dev/Esb");
        System.out.println("5. Validar Desafío");
        System.out.println("6. Bloquear Usuario");
        System.out.println("7. Desbloquear Usuario");
        System.out.println("8. Promover Usuario a Operador");
        System.out.println("9. Degradar Operador a Cliente");
        System.out.println("10. Cerrar sesión");
        System.out.print("> ");
    }

    // ───────────── INVITADO ─────────────

    private static void registerUser() throws Exception {
        //System.out.print("Tipo de usuario: 1. Operador, 2. Cliente");   int tipo = sc.nextInt();
        System.out.print("Nombre: ");   String nombre = sc.nextLine().trim();
        System.out.print("Nick: ");     String nick   = sc.nextLine().trim();
        System.out.print("Password: "); String pwd    = sc.nextLine().trim();

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

        Usuario u = new Usuario(tipo, nombre, nick, pwd, reg);
        XMLManager.usuarios.add(u);
        XMLManager.saveAll("data/");
        System.out.println("Registrado. Tu código: " + reg);
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
        System.out.print("Tipo (1=V,2=L,3=C): ");
        int t = readInt();
        System.out.print("Nombre: ");
        String n = sc.nextLine().trim();
        System.out.print("Salud (0–5): ");
        int s = readInt();
        System.out.print("Poder (1–5): ");
        int p = readInt();
        System.out.print("Oro inicial: ");
        int o = readInt();


        Personaje per;
        switch (t) {
            case 1 -> {
                System.out.print("Edad: ");
                int e = readInt();
                per = new Vampiro(n, s, p, o, e);
            }
            case 2 -> per = new Licantropo(n, s, p, o);
            case 3 -> per = new Cazador(n, s, p, o);
            default -> {
                System.out.println("Tipo inválido.");
                return;
            }
        }
        addEsbirros(per, null);



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
        List<Personaje> all = XMLManager.personajes;
        all.sort((a, b) -> b.getOro() - a.getOro());
        for (int i = 0; i < all.size(); i++) {
            Personaje p = all.get(i);
            System.out.printf("%d) %s – Oro: %d\n", i+1, p.getNombre(), p.getOro());
        }
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
