package grafo;

import javax.swing.*;
import java.util.*;

/**
 * Implementación del algoritmo de Dijkstra para camino mínimo.
 *
 * Justificación del algoritmo:
 *   Dijkstra es la elección correcta porque:
 *   - El grafo tiene pesos POSITIVOS (tiempos en minutos > 0).
 *   - Necesitamos el camino de menor costo desde UN origen a UN destino.
 *   - Bellman-Ford sería innecesariamente costoso (O(V·E)) para grafos
 *     sin pesos negativos. Dijkstra con cola de prioridad: O((V+E) log V).
 *
 * Estrategia:
 *   Usa una PriorityQueue (min-heap) que siempre extrae el nodo no visitado
 *   con menor distancia acumulada. Mantiene un mapa de distancias y un mapa
 *   de predecesores para reconstruir el camino.
 */
public class Dijkstra {

    /**
     * Clase interna para encapsular el resultado de Dijkstra:
     *   - distancia mínima al destino
     *   - camino reconstruido (lista ordenada de paradas)
     */
    public static class Resultado {
        public int distancia;           // Tiempo total en minutos
        public List<String> camino;     // Secuencia de paradas del camino mínimo
        public boolean alcanzable;      // false si no hay camino entre origen y destino

        public Resultado(int distancia, List<String> camino, boolean alcanzable) {
            this.distancia  = distancia;
            this.camino     = camino;
            this.alcanzable = alcanzable;
        }
    }

    /**
     * Ejecuta el algoritmo de Dijkstra desde 'origen' hasta 'destino'.
     *
     * Complejidad temporal: O((V + E) log V)  con PriorityQueue
     * Complejidad espacial: O(V)              para distancias y predecesores
     *
     * @param grafo   el grafo sobre el que se ejecuta.
     * @param origen  nombre de la parada de inicio.
     * @param destino nombre de la parada de llegada.
     * @return Resultado con distancia mínima, camino y flag de alcanzabilidad.
     */
    public static Resultado calcular(Grafo grafo, String origen, String destino) {

        // Validaciones previas
        if (!grafo.existeNodo(origen)) {
           JOptionPane.showMessageDialog(null,"  [!] Parada origen '" + origen + "' no existe.");
            return new Resultado(-1, new ArrayList<>(), false);
        }
        if (!grafo.existeNodo(destino)) {
            JOptionPane.showMessageDialog(null, "  [!] Parada destino '" + destino + "' no existe.");
            return new Resultado(-1, new ArrayList<>(), false);
        }
        if (origen.equalsIgnoreCase(destino)) {
            List<String> camino = new ArrayList<>();
            camino.add(origen);
            return new Resultado(0, camino, true);
        }

        // ── Inicialización ───────────────────────────────────────────────────

        // Distancias inicializadas en "infinito"
        Map<String, Integer> distancias = new HashMap<>();
        for (String nodo : grafo.getNodos()) {
            distancias.put(nodo, Integer.MAX_VALUE);
        }
        distancias.put(origen, 0);

        // Predecesores para reconstruir el camino
        Map<String, String> predecesores = new HashMap<>();

        // PriorityQueue ordena por distancia acumulada (menor primero)
        // Entrada: [distancia, nombreNodo]
        PriorityQueue<int[]> colaPrioridad = new PriorityQueue<>(
            Comparator.comparingInt(entrada -> entrada[0])
        );
        // Mapeamos nombres a índices para la cola
        Map<String, Integer> indiceNodo = new HashMap<>();
        List<String> nodos = new ArrayList<>(grafo.getNodos());
        for (int i = 0; i < nodos.size(); i++) {
            indiceNodo.put(nodos.get(i), i);
        }

        // Cola de prioridad con pares (distancia, índice)
        PriorityQueue<long[]> pq = new PriorityQueue<>(
            Comparator.comparingLong(e -> e[0])
        );
        pq.offer(new long[]{0, indiceNodo.get(origen)});

        Set<String> visitados = new HashSet<>();

        // ── Algoritmo principal ──────────────────────────────────────────────

        while (!pq.isEmpty()) {
            long[] actual = pq.poll();
            int distActual    = (int) actual[0];
            String nodoActual = nodos.get((int) actual[1]);

            // Si ya fue procesado con una distancia menor, ignorar
            if (visitados.contains(nodoActual)) continue;
            visitados.add(nodoActual);

            // Si llegamos al destino, podemos terminar
            if (nodoActual.equalsIgnoreCase(destino)) break;

            // Relajar aristas vecinas
            for (Grafo.Arista arista : grafo.getVecinos(nodoActual)) {
                if (visitados.contains(arista.destino)) continue;

                int nuevaDist = distActual + arista.peso;
                if (nuevaDist < distancias.get(arista.destino)) {
                    distancias.put(arista.destino, nuevaDist);
                    predecesores.put(arista.destino, nodoActual);
                    pq.offer(new long[]{nuevaDist, indiceNodo.get(arista.destino)});
                }
            }
        }

        // ── Reconstrucción del camino ────────────────────────────────────────

        int distanciaFinal = distancias.get(destino);

        if (distanciaFinal == Integer.MAX_VALUE) {
            // No hay camino entre origen y destino
            return new Resultado(-1, new ArrayList<>(), false);
        }

        // Reconstruye el camino siguiendo los predecesores hacia atrás
        List<String> camino = new ArrayList<>();
        String paso = destino;
        while (paso != null) {
            camino.add(0, paso);    // Inserta al inicio para invertir el orden
            paso = predecesores.get(paso);
        }

        return new Resultado(distanciaFinal, camino, true);
    }

    /**
     * Muestra el resultado de Dijkstra de forma formateada en consola.
     *
     * @param origen   parada de origen.
     * @param destino  parada de destino.
     * @param resultado objeto Resultado devuelto por calcular().
     */
    public static String mostrarResultado(String origen, String destino, Resultado resultado) {
        StringBuilder sb = new StringBuilder();

       sb.append("\n  === Ruta mínima: " + origen + " → " + destino + " ===").append("\n");

        if (!resultado.alcanzable) {
            sb.append("  [✗] No existe ruta entre '" + origen + "' y '" + destino + "'.");
            return sb.toString();
        }

        // Imprime el camino con flechas
        sb.append("  Recorrido: ").append("\n");
        sb.append(String.join(" → ", resultado.camino)).append("\n");

        // Imprime tiempo total
        sb.append("  Tiempo total: " + resultado.distancia + " minutos").append("\n");
        sb.append("  Paradas intermedias: " + (resultado.camino.size() - 2));

        return sb.toString();
    }
}
