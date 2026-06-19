package arbol;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Árbol General que modela la jerarquía geográfica de Ciudad Nexo.
 *
 * Estructura de tres niveles:
 *   Raíz (ciudad) → Zonas → Barrios → Paradas
 *
 * Operaciones implementadas (requisito del enunciado):
 *   1. insertar()  — agrega zona, barrio o parada en el nivel correcto
 *   2. buscar()    — localiza un nodo por etiqueta (DFS recursivo)
 *   3. recorrerBFS() — recorrido por niveles (imprime la jerarquía completa)
 *   4. listarParadasDeZona() — integración con el Grafo: devuelve nombres
 *      de todas las paradas pertenecientes a una zona dada.
 */
public class ArbolGeneral {

    private NodoArbol raiz;     // Nodo raíz = la ciudad completa

    // ── Constructor ─────────────────────────────────────────────────────────

    public ArbolGeneral(String nombreCiudad) {
        this.raiz = new NodoArbol(nombreCiudad, "CIUDAD");
    }





    // ────────────────────────────────────────────────────────────────────────
    // 1. INSERCIÓN
    // ────────────────────────────────────────────────────────────────────────

    public List<String> buscarParadas(){
        List<NodoArbol> zonas = buscarPorTipo(raiz, "Parada", new ArrayList<>());

        return zonas.stream().map(b -> b.getEtiqueta()).toList();
    }

    public List<String> buscarBarrios(){
        List<NodoArbol> barrios = buscarPorTipo(raiz, "Barrio", new ArrayList<>());

        return barrios.stream().map(b -> b.getEtiqueta()).toList();
    }

    public List<String> buscaZonas(){
        List<NodoArbol> zonas = buscarPorTipo(raiz, "Zona", new ArrayList<>());

        return zonas.stream().map(b -> b.getEtiqueta()).toList();
    }





    /**
     * Inserta una nueva ZONA directamente bajo la raíz (ciudad).
     * Si la zona ya existe, no la duplica.
     *
     * @param nombreZona nombre de la zona a insertar.
     */
    public String insertarZona(String nombreZona) {
        for (NodoArbol hijo : raiz.getHijos()) {
            if (hijo.getEtiqueta().equalsIgnoreCase(nombreZona)) {
               return "  [!] La zona '" + nombreZona + "' ya existe.";
            }
        }
        raiz.agregarHijo(new NodoArbol(nombreZona, "ZONA"));
        return "  [+] Zona '" + nombreZona + "' insertada.";
    }

    /**
     * Inserta un BARRIO como hijo de una zona existente.
     *
     * @param nombreZona  zona padre (debe existir).
     * @param nombreBarrio nombre del barrio a insertar.
     */
    public String insertarBarrio(String nombreZona, String nombreBarrio) {
        NodoArbol zona = buscarNodo(raiz, nombreZona);
        if (zona == null || !zona.getTipo().equals("ZONA")) {
            return "  [!] Zona '" + nombreZona + "' no encontrada.";
        }
        for (NodoArbol hijo : zona.getHijos()) {
            if (hijo.getEtiqueta().equalsIgnoreCase(nombreBarrio)) {
                return "  [!] El barrio '" + nombreBarrio + "' ya existe en '" + nombreZona + "'.";
            }
        }
        zona.agregarHijo(new NodoArbol(nombreBarrio, "BARRIO"));
        return "  [+] Barrio '" + nombreBarrio + "' insertado en zona '" + nombreZona + "'.";
    }

    /**
     * Inserta una PARADA como hoja bajo un barrio existente.
     *
     * @param nombreBarrio barrio padre (debe existir en el árbol).
     * @param nombreParada nombre de la parada a insertar.
     */
    public String insertarParada(String nombreBarrio, String nombreParada) {
        NodoArbol barrio = buscarNodo(raiz, nombreBarrio);
        if (barrio == null || !barrio.getTipo().equals("BARRIO")) {
           return "  [!] Barrio '" + nombreBarrio + "' no encontrado.";
        }
        for (NodoArbol hijo : barrio.getHijos()) {
            if (hijo.getEtiqueta().equalsIgnoreCase(nombreParada)) {
                return  "  [!] La parada '" + nombreParada + "' ya existe en '" + nombreBarrio + "'.";
            }
        }
        barrio.agregarHijo(new NodoArbol(nombreParada, "PARADA"));
       return "  [+] Parada '" + nombreParada + "' insertada en barrio '" + nombreBarrio + "'.";
    }

    // ────────────────────────────────────────────────────────────────────────
    // 2. BÚSQUEDA (DFS recursivo)
    // ────────────────────────────────────────────────────────────────────────

    /**
     * Busca un nodo por etiqueta recorriendo el árbol en profundidad (DFS).
     * Devuelve el nodo si lo encuentra, null en caso contrario.
     *
     * @param etiqueta nombre a buscar (insensible a mayúsculas).
     * @return NodoArbol encontrado o null.
     */
    public NodoArbol buscar(String etiqueta) {
        NodoArbol resultado = buscarNodo(raiz, etiqueta);
        if (resultado != null) {
            JOptionPane.showMessageDialog(null,"  [✓] Encontrado: " + resultado);
        } else {
            JOptionPane.showMessageDialog(null,"  [✗] '" + etiqueta + "' no encontrado en el árbol.");
        }
        return resultado;
    }

    /**
     * Método privado DFS recursivo usado internamente por buscar() e insertar().
     */
    private NodoArbol buscarNodo(NodoArbol actual, String etiqueta) {
        if (actual.getEtiqueta().equalsIgnoreCase(etiqueta)) {
            return actual;
        }
        for (NodoArbol hijo : actual.getHijos()) {
            NodoArbol resultado = buscarNodo(hijo, etiqueta);
            if (resultado != null) return resultado;
        }
        return null;
    }


    private List<NodoArbol> buscarPorTipo(NodoArbol actual, String tipo, List<NodoArbol> lista){
        if (actual == null) return lista;

        if (actual.getTipo().equalsIgnoreCase(tipo)) {
            lista.add(actual);
        }

        for (NodoArbol hijo : actual.getHijos()) {
            lista = buscarPorTipo(hijo, tipo, lista);
        }

        return lista;
    }

    // ────────────────────────────────────────────────────────────────────────
    // 3. RECORRIDO BFS (por niveles)
    // ────────────────────────────────────────────────────────────────────────

    /**
     * Recorre el árbol en anchura (BFS) e imprime la jerarquía completa
     * con sangría según el nivel de profundidad.
     * Complejidad: O(n) donde n = cantidad total de nodos.
     */
    public String recorrerBFS() {
        if (raiz == null) return null;

        System.out.println("\n  === Jerarquía geográfica de " + raiz.getEtiqueta() + " ===");

        StringBuilder sb = new StringBuilder();

        Queue<NodoArbol> cola   = new LinkedList<>();
        Queue<Integer>   niveles = new LinkedList<>();

        cola.add(raiz);
        niveles.add(0);

        while (!cola.isEmpty()) {
            NodoArbol actual = cola.poll();
            int nivel        = niveles.poll();

            // Sangría proporcional al nivel
            String sangria = "  " + "    ".repeat(nivel);
            sb.append(sangria + actual).append("\n");
            System.out.println(sangria + actual);

            for (NodoArbol hijo : actual.getHijos()) {
                cola.add(hijo);
                niveles.add(nivel + 1);
            }
        }

        System.out.println();

        return sb.toString();
    }

    // ────────────────────────────────────────────────────────────────────────
    // 4. INTEGRACIÓN — Listar paradas de una zona
    // ────────────────────────────────────────────────────────────────────────

    /**
     * Devuelve los nombres de todas las PARADAS pertenecientes a una zona.
     * Este método es el puente de integración con el Grafo: permite obtener
     * el subconjunto de nodos del grafo que pertenecen a una zona geográfica
     * y luego aplicar Dijkstra solo entre ellos.
     *
     * @param nombreZona zona a consultar.
     * @return lista de nombres de paradas (puede estar vacía).
     */
    public List<String> listarParadasDeZona(String nombreZona) {
        List<String> paradas = new ArrayList<>();
        NodoArbol zona = buscarNodo(raiz, nombreZona);

        if (zona == null || !zona.getTipo().equals("ZONA")) {
            System.out.println("  [!] Zona '" + nombreZona + "' no encontrada.");
            return paradas;
        }

        // Recorre barrios → paradas dentro de la zona
        for (NodoArbol barrio : zona.getHijos()) {
            for (NodoArbol parada : barrio.getHijos()) {
                paradas.add(parada.getEtiqueta());
            }
        }
        return paradas;
    }

    // ── Getter de raíz (útil para pruebas) ──────────────────────────────────
    public NodoArbol getRaiz() { return raiz; }
}
