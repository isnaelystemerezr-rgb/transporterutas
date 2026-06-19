package grafo;

import java.util.*;

/**
 * Grafo no dirigido y ponderado implementado con Lista de Adyacencia.
 *
 * Decisión de diseño:
 *   Se eligió Lista de Adyacencia sobre Matriz de Adyacencia porque la red
 *   de transporte es dispersa (cada parada conecta con pocas otras), lo que
 *   hace que la lista sea más eficiente en memoria: O(V + E) vs O(V²).
 *
 * Cada nodo es identificado por el nombre de la parada (String).
 * Cada arista almacena el nodo destino y el peso (tiempo en minutos).
 *
 * Operaciones implementadas:
 *   - agregarNodo()    → agrega una parada al grafo
 *   - agregarArista()  → conecta dos paradas con un peso
 *   - BFS()            → recorrido en anchura desde un origen
 *   - DFS()            → recorrido en profundidad desde un origen
 *   - mostrarGrafo()   → imprime la lista de adyacencia completa
 *   - existeNodo()     → verifica si una parada existe en el grafo
 *   - getVecinos()     → devuelve las aristas de un nodo (usado por Dijkstra)
 */
public class Grafo {

    /**
     * Clase interna que representa una arista con destino y peso.
     */
    public static class Arista {
        public String destino;
        public int peso;           // Tiempo en minutos

        public Arista(String destino, int peso) {
            this.destino = destino;
            this.peso    = peso;
        }

        @Override
        public String toString() {
            return String.format("→ %s (%d min)", destino, peso);
        }
    }

    // Lista de adyacencia: nombre de parada → lista de aristas
    private Map<String, List<Arista>> listaAdyacencia;

    // ── Constructor ─────────────────────────────────────────────────────────

    public Grafo() {
        listaAdyacencia = new LinkedHashMap<>();  // LinkedHashMap mantiene orden de inserción
    }

    // ────────────────────────────────────────────────────────────────────────
    // AGREGAR NODO
    // ────────────────────────────────────────────────────────────────────────

    /**
     * Agrega una parada como nodo del grafo.
     * Si ya existe, no hace nada.
     *
     * @param nombreParada nombre único de la parada.
     */
    public void agregarNodo(String nombreParada) {
        listaAdyacencia.putIfAbsent(nombreParada, new ArrayList<>());
    }

    // ────────────────────────────────────────────────────────────────────────
    // AGREGAR ARISTA
    // ────────────────────────────────────────────────────────────────────────

    /**
     * Conecta dos paradas con una arista no dirigida y ponderada.
     * Al ser no dirigido, se agrega la arista en ambas direcciones.
     *
     * @param origen  nombre de la parada origen.
     * @param destino nombre de la parada destino.
     * @param peso    tiempo de viaje en minutos (debe ser positivo).
     */
    public void agregarArista(String origen, String destino, int peso) {
        // Asegura que ambos nodos existan
        agregarNodo(origen);
        agregarNodo(destino);

        // No dirigido: agrega en ambos sentidos
        listaAdyacencia.get(origen).add(new Arista(destino, peso));
        listaAdyacencia.get(destino).add(new Arista(origen, peso));
    }

    // ────────────────────────────────────────────────────────────────────────
    // BFS — Recorrido en anchura
    // ────────────────────────────────────────────────────────────────────────

    /**
     * Recorre el grafo en anchura (BFS) desde una parada origen.
     * Útil para verificar conectividad: si todos los nodos son visitados,
     * el grafo es conexo.
     * Complejidad: O(V + E)
     *
     * @param origen parada desde donde comienza el recorrido.
     */
    public String BFS(String origen) {
        if (!existeNodo(origen)) {
            return "  [!] Parada '" + origen + "' no existe en el grafo.";
        }

        StringBuilder sb = new StringBuilder();

        Set<String> visitados  = new LinkedHashSet<>();
        Queue<String> cola     = new LinkedList<>();

        cola.add(origen);
        visitados.add(origen);

       sb.append("\n  === BFS desde '" + origen + "' ===").append("\n");

        while (!cola.isEmpty()) {
            String actual = cola.poll();
            sb.append("  Visitando: " + actual).append("\n");

            for (Arista arista : listaAdyacencia.get(actual)) {
                if (!visitados.contains(arista.destino)) {
                    visitados.add(arista.destino);
                    cola.add(arista.destino);
                }
            }
        }

        // Verifica conectividad
        int totalNodos = listaAdyacencia.size();
        sb.append("\n  Nodos visitados: " + visitados.size() + " / " + totalNodos).append("\n");
        if (visitados.size() == totalNodos) {
            sb.append("  [✓] El grafo es CONEXO: todas las paradas están conectadas.").append("\n");
        } else {
            sb.append("  [!] El grafo NO es completamente conexo.").append("\n");
        }

        return sb.toString();
    }

    // ────────────────────────────────────────────────────────────────────────
    // DFS — Recorrido en profundidad
    // ────────────────────────────────────────────────────────────────────────

    /**
     * Recorre el grafo en profundidad (DFS) desde una parada origen.
     * Complejidad: O(V + E)
     *
     * @param origen parada desde donde comienza el recorrido.
     */
    public String DFS(String origen) {
        if (!existeNodo(origen)) {
            return "  [!] Parada '" + origen + "' no existe en el grafo.";
        }

        StringBuilder sb = new StringBuilder();

        Set<String> visitados = new LinkedHashSet<>();
        sb.append("\n  === DFS desde '" + origen + "' ===").append("\n");
        dfsRecursivo(origen, visitados, sb);

        return sb.toString();
    }

    /** Método auxiliar DFS recursivo. */
    private void dfsRecursivo(String actual, Set<String> visitados, StringBuilder sb) {
        visitados.add(actual);
        sb.append("  Visitando: " + actual).append("\n");

        for (Arista arista : listaAdyacencia.get(actual)) {
            if (!visitados.contains(arista.destino)) {
                dfsRecursivo(arista.destino, visitados, sb);
            }
        }
    }

    // ────────────────────────────────────────────────────────────────────────
    // MOSTRAR GRAFO
    // ────────────────────────────────────────────────────────────────────────

    /**
     * Imprime la lista de adyacencia completa del grafo.
     * Muestra cada parada y sus conexiones con sus respectivos pesos.
     */
    public String mostrarGrafo() {
        System.out.println("\n  === Lista de adyacencia del grafo ===");
        StringBuilder sb = new StringBuilder();


        for (Map.Entry<String, List<Arista>> entrada : listaAdyacencia.entrySet()) {
            System.out.print("  " + entrada.getKey() + ": ");
            sb.append("  " + entrada.getKey() + ": ").append("\n");


            if (entrada.getValue().isEmpty()) {
                sb.append("[sin conexiones]").append("\n");
            } else {
                List<String> conexiones = new ArrayList<>();
                for (Arista a : entrada.getValue()) {
                    conexiones.add(a.toString());
                }
                sb.append(String.join("  ", conexiones)).append("\n");
            }
        }
        return sb.toString();
    }

    // ────────────────────────────────────────────────────────────────────────
    // UTILIDADES
    // ────────────────────────────────────────────────────────────────────────

    /** Verifica si una parada existe en el grafo. */
    public boolean existeNodo(String nombre) {
        return listaAdyacencia.containsKey(nombre);
    }

    /** Devuelve las aristas de un nodo (usado por Dijkstra). */
    public List<Arista> getVecinos(String nombre) {
        return listaAdyacencia.getOrDefault(nombre, new ArrayList<>());
    }

    /** Devuelve el conjunto de todos los nodos del grafo. */
    public Set<String> getNodos() {
        return listaAdyacencia.keySet();
    }
}
