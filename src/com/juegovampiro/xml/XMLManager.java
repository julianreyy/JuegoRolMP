package com.juegovampiro.xml;

import com.juegovampiro.model.Usuario;
import com.juegovampiro.model.Operador;
import com.juegovampiro.model.Personaje;
import com.juegovampiro.model.Vampiro;
import com.juegovampiro.model.Licantropo;
import com.juegovampiro.model.Cazador;
import com.juegovampiro.model.Combate;
import com.juegovampiro.model.Ronda;
import com.juegovampiro.model.Desafio;
import com.juegovampiro.model.*;

import java.lang.reflect.Field;


import java.io.File;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLManager {

    // Listas en memoria
    public static List<Usuario> usuarios    = new ArrayList<>();
    public static List<Personaje> personajes = new ArrayList<>();
    public static List<Combate> combates    = new ArrayList<>();
    public static List<Desafio> desafios = new ArrayList<>();
    public static List<Armadura> armaduras = new ArrayList<>();
    public static List<Arma> armas = new ArrayList<>();
    public static List<Modificador> modificadores = new ArrayList<>();
    public static List<Don> dones = new ArrayList<>();
    public static List<Disciplina> disciplinas = new ArrayList<>();
    public static List<Talento> talentos = new ArrayList<>();

    public static void loadAll(String dataDir) throws Exception {
        loadUsers(dataDir + "users.xml");
        loadCharacters(dataDir + "characters.xml");
        loadCombates(dataDir + "combates.xml");
        loadDesafios(dataDir + "desafios.xml");
        loadArmors(dataDir + "armors.xml");
        loadWeapons(dataDir + "weapons.xml");
        loadModifiers(dataDir + "modifiers.xml");
        loadGifts(dataDir + "gifts.xml");
        loadDisciplines(dataDir + "disciplines.xml");
        loadTalents(dataDir + "talents.xml");
    }

    public static void saveAll(String dataDir) throws Exception {
        saveUsers(dataDir + "users.xml");
        saveCharacters(dataDir + "characters.xml");
        saveCombates(dataDir + "combates.xml");
        saveDesafios(dataDir + "desafios.xml");
    }

    public static void loadDesafios(String path) throws Exception {
        desafios.clear();
        File f = new File(path);
        if (!f.exists()) return;
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.parse(f);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        NodeList nodes = doc.getElementsByTagName("desafio");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            String rnick = e.getAttribute("retadorNick");
            String rc    = e.getAttribute("retadorChar");
            String dnick = e.getAttribute("desafiadoNick");
            String dc    = e.getAttribute("desafiadoChar");
            int ap       = Integer.parseInt(e.getAttribute("apuesta"));
            Date fecha   = df.parse(e.getAttribute("fecha"));
            Desafio.Estado est = Desafio.Estado.valueOf(e.getAttribute("estado"));
            Desafio d = new Desafio(rnick, rc, dnick, dc, ap);
            d.setEstado(est);
            // forzamos la fecha original
            Field fDate = Desafio.class.getDeclaredField("fecha");
            fDate.setAccessible(true);
            fDate.set(d, fecha);
            desafios.add(d);
        }
    }

    public static void saveDesafios(String path) throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.newDocument();
        Element root = doc.createElement("desafios");
        doc.appendChild(root);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        for (Desafio d : desafios) {
            Element e = doc.createElement("desafio");
            e.setAttribute("retadorNick",   d.getRetadorNick());
            e.setAttribute("retadorChar",   d.getRetadorChar());
            e.setAttribute("desafiadoNick", d.getDesafiadoNick());
            e.setAttribute("desafiadoChar", d.getDesafiadoChar());
            e.setAttribute("apuesta",       String.valueOf(d.getApuesta()));
            e.setAttribute("estado",        d.getEstado().name());
            e.setAttribute("fecha",         df.format(d.getFecha()));
            root.appendChild(e);
        }
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(doc), new StreamResult(new File(path)));
    }


    // —— Usuarios —————————————————————————————————————————

    public static void loadUsers(String path) throws Exception {
        usuarios.clear();
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("Aviso: no existe " + path + ", se creará al guardar.");
            return;
        }
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.parse(f);
        NodeList nodes = doc.getElementsByTagName("usuario");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            String tipo   = e.getAttribute("tipo");
            String nombre = e.getElementsByTagName("nombre"  ).item(0).getTextContent();
            String nick   = e.getElementsByTagName("nick"    ).item(0).getTextContent();
            String pwd    = e.getElementsByTagName("password").item(0).getTextContent();
            Usuario u;
            if ("operador".equalsIgnoreCase(tipo)) {
                u = new Operador(nombre, nick, pwd);
            } else {
                String reg = e.getElementsByTagName("registro").item(0).getTextContent();
                u = new Usuario(nombre, nick, pwd, reg);
            }
            // cargar atributo bloqueado
            String blk = e.getAttribute("bloqueado");
            if ("true".equalsIgnoreCase(blk)) {
                u.setBloqueado(true);
            }
            usuarios.add(u);
        }
    }

    public static void saveUsers(String path) throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.newDocument();

        Element root = doc.createElement("usuarios");
        doc.appendChild(root);

        for (Usuario u : usuarios) {
            Element eu = doc.createElement("usuario");
            eu.setAttribute("tipo", u instanceof Operador ? "operador" : "cliente");
            eu.setAttribute("bloqueado", String.valueOf(u.isBloqueado()));

            Element en = doc.createElement("nombre");
            en.setTextContent(u.getNombre());
            eu.appendChild(en);

            Element ek = doc.createElement("nick");
            ek.setTextContent(u.getNick());
            eu.appendChild(ek);

            Element ep = doc.createElement("password");
            ep.setTextContent(u.getPassword());
            eu.appendChild(ep);

            if (!(u instanceof Operador)) {
                Element er = doc.createElement("registro");
                er.setTextContent(u.getRegistro());
                eu.appendChild(er);
            }

            root.appendChild(eu);
        }

        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(doc), new StreamResult(new File(path)));
    }

    // —— Personajes —————————————————————————————————————————

    public static void loadCharacters(String path) throws Exception {
        personajes.clear();
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("Aviso: no existe " + path + ", se creará al guardar.");
            return;
        }
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.parse(f);
        NodeList nodes = doc.getElementsByTagName("personaje");

        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            String tipo      = e.getAttribute("tipo");
            String ownerNick = e.getElementsByTagName("nickOwner").item(0).getTextContent();

            Usuario owner = usuarios.stream()
                    .filter(u -> u.getNick().equals(ownerNick))
                    .findFirst().orElse(null);
            if (owner == null) continue;

            String nombre = e.getElementsByTagName("nombre").item(0).getTextContent();
            int salud      = Integer.parseInt(e.getElementsByTagName("salud").item(0).getTextContent());
            int poder      = Integer.parseInt(e.getElementsByTagName("poder").item(0).getTextContent());
            int oro        = Integer.parseInt(e.getElementsByTagName("oro").item(0).getTextContent());

            Personaje p = null;
            switch (tipo.toLowerCase()) {
                case "vampiro": {
                    int edad   = Integer.parseInt(e.getElementsByTagName("edad").item(0).getTextContent());
                    int sangre = Integer.parseInt(e.getElementsByTagName("sangre").item(0).getTextContent());
                    Vampiro v = new Vampiro(nombre, salud, poder, oro, edad);
                    v.setSangre(sangre);
                    p = v;
                    break;
                }
                case "licantropo": {
                    Licantropo l = new Licantropo(nombre, salud, poder, oro);
                    int rabia = Integer.parseInt(e.getElementsByTagName("rabia").item(0).getTextContent());
                    l.setRabia(rabia);
                    p = l;
                    break;
                }
                case "cazador": {
                    Cazador c = new Cazador(nombre, salud, poder, oro);
                    int voluntad = Integer.parseInt(e.getElementsByTagName("voluntad").item(0).getTextContent());
                    c.setVoluntad(voluntad);
                    p = c;
                    break;
                }
            }
            if (p != null) {
                personajes.add(p);
                owner.addPersonaje(p);
            }
        }
    }

    public static void saveCharacters(String path) throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.newDocument();

        Element root = doc.createElement("personajes");
        doc.appendChild(root);

        for (Usuario u : usuarios) {
            for (Personaje p : u.getPersonajes()) {
                Element pe = doc.createElement("personaje");
                if (p instanceof Vampiro) {
                    pe.setAttribute("tipo", "vampiro");
                } else if (p instanceof Licantropo) {
                    pe.setAttribute("tipo", "licantropo");
                } else if (p instanceof Cazador) {
                    pe.setAttribute("tipo", "cazador");
                }

                Element owner = doc.createElement("nickOwner");
                owner.setTextContent(u.getNick());
                pe.appendChild(owner);

                Element en = doc.createElement("nombre");
                en.setTextContent(p.getNombre());
                pe.appendChild(en);

                Element es = doc.createElement("salud");
                es.setTextContent(String.valueOf(p.getSalud()));
                pe.appendChild(es);

                Element ep = doc.createElement("poder");
                ep.setTextContent(String.valueOf(p.getPoder()));
                pe.appendChild(ep);

                Element eo = doc.createElement("oro");
                eo.setTextContent(String.valueOf(p.getOro()));
                pe.appendChild(eo);

                Element ar = doc.createElement("armas");
                ar.setTextContent(String.valueOf(p.getArmas()));
                pe.appendChild(ar);

                Element ad = doc.createElement("armaActiva");
                ad.setTextContent(String.valueOf(p.getArmasActivas()));
                pe.appendChild(ad);

                Element am = doc.createElement("armaduraActiva");
                am.setTextContent(String.valueOf(p.getArmaduraActiva()));
                pe.appendChild(am);

                Element de = doc.createElement("debilidades");
                de.setTextContent(String.valueOf(p.getDebilidades()));
                pe.appendChild(de);

                Element fo = doc.createElement("fortalezas");
                fo.setTextContent(String.valueOf(p.getFortalezas()));
                pe.appendChild(fo);

                if (p instanceof Vampiro) {
                    Vampiro v = (Vampiro) p;
                    Element eEdad = doc.createElement("edad");
                    eEdad.setTextContent(String.valueOf(v.getEdad()));
                    pe.appendChild(eEdad);
                    Element eSang = doc.createElement("sangre");
                    eSang.setTextContent(String.valueOf(v.getSangre()));
                    pe.appendChild(eSang);
                } else if (p instanceof Licantropo) {
                    Licantropo l = (Licantropo) p;
                    Element eRabia = doc.createElement("rabia");
                    eRabia.setTextContent(String.valueOf(l.getRabia()));
                    pe.appendChild(eRabia);
                } else if (p instanceof Cazador) {
                    Cazador c = (Cazador) p;
                    Element eVol = doc.createElement("voluntad");
                    eVol.setTextContent(String.valueOf(c.getVoluntad()));
                    pe.appendChild(eVol);
                }

                root.appendChild(pe);
            }
        }

        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(doc), new StreamResult(new File(path)));
    }

    // —— Armaduras —————————————————————————————————————————


    public static void loadArmors(String path) throws Exception {
        armaduras.clear();
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("Aviso: no existe " + path + ", se creará al guardar.");
            return;
        }
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.parse(f);
        NodeList nodes = doc.getElementsByTagName("armadura");

        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            String tipo      = e.getAttribute("tipo");
            String ownerNick = e.getElementsByTagName("nickOwner").item(0).getTextContent();

            Usuario owner = usuarios.stream()
                    .filter(u -> u.getNick().equals(ownerNick))
                    .findFirst().orElse(null);
            if (owner == null) continue;

            String nombre = e.getElementsByTagName("nombre").item(0).getTextContent();
            int salud      = Integer.parseInt(e.getElementsByTagName("salud").item(0).getTextContent());
            int poder      = Integer.parseInt(e.getElementsByTagName("poder").item(0).getTextContent());
            int oro        = Integer.parseInt(e.getElementsByTagName("oro").item(0).getTextContent());

            Personaje p = null;
            switch (tipo.toLowerCase()) {
                case "vampiro": {
                    int edad   = Integer.parseInt(e.getElementsByTagName("edad").item(0).getTextContent());
                    int sangre = Integer.parseInt(e.getElementsByTagName("sangre").item(0).getTextContent());
                    Vampiro v = new Vampiro(nombre, salud, poder, oro, edad);
                    v.setSangre(sangre);
                    p = v;
                    break;
                }
                case "licantropo": {
                    Licantropo l = new Licantropo(nombre, salud, poder, oro);
                    int rabia = Integer.parseInt(e.getElementsByTagName("rabia").item(0).getTextContent());
                    l.setRabia(rabia);
                    p = l;
                    break;
                }
                case "cazador": {
                    Cazador c = new Cazador(nombre, salud, poder, oro);
                    int voluntad = Integer.parseInt(e.getElementsByTagName("voluntad").item(0).getTextContent());
                    c.setVoluntad(voluntad);
                    p = c;
                    break;
                }
            }
            if (p != null) {
                personajes.add(p);
                owner.addPersonaje(p);
            }
        }
    }

    // —— Armas —————————————————————————————————————————

    public static void loadWeapons(String path) throws Exception {
        armas.clear();
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("Aviso: no existe " + path + ", se creará al guardar.");
            return;
        }
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.parse(f);
        NodeList nodes = doc.getElementsByTagName("arma");

        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            String tipo      = e.getAttribute("tipo");
            String ownerNick = e.getElementsByTagName("nickOwner").item(0).getTextContent();

            Usuario owner = usuarios.stream()
                    .filter(u -> u.getNick().equals(ownerNick))
                    .findFirst().orElse(null);
            if (owner == null) continue;

            String nombre = e.getElementsByTagName("nombre").item(0).getTextContent();
            int salud      = Integer.parseInt(e.getElementsByTagName("salud").item(0).getTextContent());
            int poder      = Integer.parseInt(e.getElementsByTagName("poder").item(0).getTextContent());
            int oro        = Integer.parseInt(e.getElementsByTagName("oro").item(0).getTextContent());

            Personaje p = null;
            switch (tipo.toLowerCase()) {
                case "vampiro": {
                    int edad   = Integer.parseInt(e.getElementsByTagName("edad").item(0).getTextContent());
                    int sangre = Integer.parseInt(e.getElementsByTagName("sangre").item(0).getTextContent());
                    Vampiro v = new Vampiro(nombre, salud, poder, oro, edad);
                    v.setSangre(sangre);
                    p = v;
                    break;
                }
                case "licantropo": {
                    Licantropo l = new Licantropo(nombre, salud, poder, oro);
                    int rabia = Integer.parseInt(e.getElementsByTagName("rabia").item(0).getTextContent());
                    l.setRabia(rabia);
                    p = l;
                    break;
                }
                case "cazador": {
                    Cazador c = new Cazador(nombre, salud, poder, oro);
                    int voluntad = Integer.parseInt(e.getElementsByTagName("voluntad").item(0).getTextContent());
                    c.setVoluntad(voluntad);
                    p = c;
                    break;
                }
            }
            if (p != null) {
                personajes.add(p);
                owner.addPersonaje(p);
            }
        }
    }

    // —— Combates —————————————————————————————————————————

    public static void loadCombates(String path) throws Exception {
        combates.clear();
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("Aviso: no existe " + path + ", se creará al guardar.");
            return;
        }
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.parse(f);
        NodeList nodes = doc.getElementsByTagName("combate");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            String retNick = e.getElementsByTagName("retador").item(0).getTextContent();
            String desNick = e.getElementsByTagName("desafiado").item(0).getTextContent();
            int apuesta    = Integer.parseInt(e.getElementsByTagName("apuesta").item(0).getTextContent());
            String ganador = e.getElementsByTagName("ganador").item(0).getTextContent();
            Date fecha     = df.parse(e.getElementsByTagName("fecha").item(0).getTextContent());

            List<Ronda> rondas = new ArrayList<>();
            Element rondasNode = (Element) e.getElementsByTagName("rondas").item(0);
            NodeList rList = rondasNode.getElementsByTagName("ronda");
            for (int j = 0; j < rList.getLength(); j++) {
                Element re = (Element) rList.item(j);
                int num  = Integer.parseInt(re.getAttribute("numero"));
                int dr   = Integer.parseInt(re.getElementsByTagName("danioRetador").item(0).getTextContent());
                int dd   = Integer.parseInt(re.getElementsByTagName("danioDesafiado").item(0).getTextContent());
                rondas.add(new Ronda(num, dr, dd));
            }

            combates.add(new Combate(retNick, desNick, apuesta, rondas, ganador, fecha));
        }
    }

    public static void saveCombates(String path) throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.newDocument();
        Element root = doc.createElement("combates");
        doc.appendChild(root);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        for (Combate c : combates) {
            Element ce = doc.createElement("combate");

            Element er = doc.createElement("retador");
            er.setTextContent(c.getRetadorNick());
            ce.appendChild(er);

            Element ed = doc.createElement("desafiado");
            ed.setTextContent(c.getDesafiadoNick());
            ce.appendChild(ed);

            Element ea = doc.createElement("apuesta");
            ea.setTextContent(String.valueOf(c.getApuesta()));
            ce.appendChild(ea);

            Element eg = doc.createElement("ganador");
            eg.setTextContent(c.getGanadorNick());
            ce.appendChild(eg);

            Element ef = doc.createElement("fecha");
            ef.setTextContent(df.format(c.getFecha()));
            ce.appendChild(ef);

            Element rondasEl = doc.createElement("rondas");
            for (Ronda r : c.getRondas()) {
                Element re = doc.createElement("ronda");
                re.setAttribute("numero", String.valueOf(r.getNumero()));

                Element dr = doc.createElement("danioRetador");
                dr.setTextContent(String.valueOf(r.getDanioRetador()));
                re.appendChild(dr);

                Element dd = doc.createElement("danioDesafiado");
                dd.setTextContent(String.valueOf(r.getDanioDesafiado()));
                re.appendChild(dd);

                rondasEl.appendChild(re);
            }
            ce.appendChild(rondasEl);

            root.appendChild(ce);
        }

        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(doc), new StreamResult(new File(path)));
    }

    public static void loadModifiers(String path) throws Exception {
        modificadores.clear();
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("Aviso: no existe " + path + ", se creará al guardar.");
            return;
        }
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.parse(f);
        NodeList nodes = doc.getElementsByTagName("modificadores");

        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            String tipo      = e.getAttribute("tipo");
            String ownerNick = e.getElementsByTagName("nickOwner").item(0).getTextContent();

            Usuario owner = usuarios.stream()
                    .filter(u -> u.getNick().equals(ownerNick))
                    .findFirst().orElse(null);
            if (owner == null) continue;

            String nombre = e.getElementsByTagName("nombre").item(0).getTextContent();
            int salud      = Integer.parseInt(e.getElementsByTagName("salud").item(0).getTextContent());
            int poder      = Integer.parseInt(e.getElementsByTagName("poder").item(0).getTextContent());
            int oro        = Integer.parseInt(e.getElementsByTagName("oro").item(0).getTextContent());

            Personaje p = null;
            switch (tipo.toLowerCase()) {
                case "vampiro": {
                    int edad   = Integer.parseInt(e.getElementsByTagName("edad").item(0).getTextContent());
                    int sangre = Integer.parseInt(e.getElementsByTagName("sangre").item(0).getTextContent());
                    Vampiro v = new Vampiro(nombre, salud, poder, oro, edad);
                    v.setSangre(sangre);
                    p = v;
                    break;
                    
            if (p != null) {
                personajes.add(p);
                owner.addPersonaje(p);
            }
        }
    }

    public static void loadGifts(String path) throws Exception {}

    public static void loadDisciplines(String path) throws Exception {}

    public static void loadTalents(String path) throws Exception {}

}
