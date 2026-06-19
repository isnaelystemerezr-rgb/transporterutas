import arbol.ArbolGeneral;
import grafo.Dijkstra;
import grafo.Grafo;

import java.util.List;
import java.util.Scanner;

/**
 * Punto de entrada del sistema de rutas de transporte urbano.
 *
 * Responsabilidades:
 *   1. Inicializar el Árbol General con la jerarquía geográfica de Ciudad Nexo.
 *   2. Inicializar el Grafo con las conexiones entre paradas y sus pesos.
 *   3. Presentar un menú de consola interactivo que integre ambas estructuras.
 *
 * INTEGRACIÓN árbol ↔ grafo:
 *   La opción "Buscar ruta dentro de una zona" demuestra la colaboración:
 *   el árbol resuelve qué paradas pertenecen a la zona, y el grafo
 *   calcula la ruta mínima entre dos de ellas mediante Dijkstra.
 */
public class Main {

    // ── Estructuras globales ─────────────────────────────────────────────────
    static ArbolGeneral arbol;
    static Grafo grafo;
    static Scanner scanner = new Scanner(System.in);

    // ════════════════════════════════════════════════════════════════════════
    // MAIN
    // ════════════════════════════════════════════════════════════════════════

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║   Sistema de Rutas — Ciudad Nexo                 ║");
        System.out.println("║   Estructura de Datos II  |  2026                ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        cargarDatos();

        System.out.println("\n  [✓] Datos de Ciudad Nexo cargados correctamente.");
        System.out.println("  [✓] " + grafo.getNodos().size() + " paradas en el grafo.");

        menuPrincipal();
    }

    // ════════════════════════════════════════════════════════════════════════
    // CARGA DE DATOS PRECARGADOS
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Inicializa el árbol general (jerarquía geográfica)
     * y el grafo (red de conexiones) con los datos de Ciudad Nexo.
     */
    private static void cargarDatos() {

        // ── Árbol General ────────────────────────────────────────────────────
        arbol = new ArbolGeneral("Ciudad Nexo");

        // Zona Norte
        arbol.insertarZona("Zona Norte");
        arbol.insertarBarrio("Zona Norte", "Barrio Altos");
        arbol.insertarParada("Barrio Altos", "Terminal Norte");
        arbol.insertarParada("Barrio Altos", "Plaza Altos");
        arbol.insertarParada("Barrio Altos", "Av. Principal");
        arbol.insertarBarrio("Zona Norte", "Barrio Río");
        arbol.insertarParada("Barrio Río", "Puente Río");
        arbol.insertarParada("Barrio Río", "Costanera");

        // Zona Centro
        arbol.insertarZona("Zona Centro");
        arbol.insertarBarrio("Zona Centro", "Barrio Histórico");
        arbol.insertarParada("Barrio Histórico", "Plaza Central");
        arbol.insertarParada("Barrio Histórico", "Museo");
        arbol.insertarParada("Barrio Histórico", "Catedral");
        arbol.insertarBarrio("Zona Centro", "Barrio Comercial");
        arbol.insertarParada("Barrio Comercial", "Mercado");
        arbol.insertarParada("Barrio Comercial", "Shopping");

        // Zona Sur
        arbol.insertarZona("Zona Sur");
        arbol.insertarBarrio("Zona Sur", "Barrio Industrial");
        arbol.insertarParada("Barrio Industrial", "Fábrica");
        arbol.insertarParada("Barrio Industrial", "Depósito");
        arbol.insertarBarrio("Zona Sur", "Barrio Residencial");
        arbol.insertarParada("Barrio Residencial", "Parque Sur");
        arbol.insertarParada("Barrio Residencial", "Colegio");
        arbol.insertarParada("Barrio Residencial", "Terminal Sur");

        // ── Grafo (conexiones con pesos en minutos) ──────────────────────────
        grafo = new Grafo();

        grafo.agregarArista("Terminal Norte",  "Plaza Altos",    3);
        grafo.agregarArista("Plaza Altos",     "Av. Principal",  4);
        grafo.agregarArista("Av. Principal",   "Puente Río",     5);
        grafo.agregarArista("Puente Río",      "Costanera",      3);
        grafo.agregarArista("Costanera",       "Plaza Central",  6);
        grafo.agregarArista("Av. Principal",   "Plaza Central",  8);
        grafo.agregarArista("Plaza Central",   "Museo",          2);
        grafo.agregarArista("Museo",           "Catedral",       2);
        grafo.agregarArista("Plaza Central",   "Mercado",        4);
        grafo.agregarArista("Mercado",         "Shopping",       3);
        grafo.agregarArista("Shopping",        "Fábrica",        7);
        grafo.agregarArista("Fábrica",         "Depósito",       4);
        grafo.agregarArista("Depósito",        "Parque Sur",     5);
        grafo.agregarArista("Parque Sur",      "Colegio",        3);
        grafo.agregarArista("Colegio",         "Terminal Sur",   4);
        grafo.agregarArista("Catedral",        "Mercado",        3);
        grafo.agregarArista("Terminal Norte",  "Plaza Central",  12);
        grafo.agregarArista("Parque Sur",      "Mercado",        6);
    }

    // ════════════════════════════════════════════════════════════════════════
    // MENÚ PRINCIPAL
    // ════════════════════════════════════════════════════════════════════════

    private static void menuPrincipal() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n╔══════════════════════════════════════════════════╗");
            System.out.println("║               MENÚ PRINCIPAL                     ║");
            System.out.println("╠══════════════════════════════════════════════════╣");
            System.out.println("║  [1] Ver jerarquía geográfica (árbol BFS)        ║");
            System.out.println("║  [2] Buscar parada/zona/barrio en el árbol       ║");
            System.out.println("║  [3] Insertar nueva parada                       ║");
            System.out.println("║  [4] Ver red de conexiones (grafo)               ║");
            System.out.println("║  [5] Ruta mínima entre dos paradas (Dijkstra)    ║");
            System.out.println("║  [6] Paradas de una zona + ruta mínima entre     ║");
            System.out.println("║      ellas  [INTEGRACIÓN árbol + grafo]          ║");
            System.out.println("║  [7] Verificar conectividad del grafo (BFS)      ║");
            System.out.println("║  [8] Recorrido DFS desde una parada              ║");
            System.out.println("║  [0] Salir                                       ║");
            System.out.println("╚══════════════════════════════════════════════════╝");
            System.out.print("  Opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  [!] Ingresá un número válido.");
                continue;
            }

            switch (opcion) {
                case 1 -> arbol.recorrerBFS();
                case 2 -> opcionBuscar();
                case 3 -> opcionInsertar();
                case 4 -> grafo.mostrarGrafo();
                case 5 -> opcionRutaMinima();
                case 6 -> opcionIntegracion();
                case 7 -> opcionBFS();
                case 8 -> opcionDFS();
                case 0 -> System.out.println("\n  Hasta luego.\n");
                default -> System.out.println("  [!] Opción no válida.");
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // OPCIONES DEL MENÚ
    // ════════════════════════════════════════════════════════════════════════

    /** Opción 2 — Buscar en el árbol */
    private static void opcionBuscar() {
        System.out.print("\n  Ingresá el nombre a buscar (zona, barrio o parada): ");
        String nombre = scanner.nextLine().trim();
        arbol.buscar(nombre);
    }

    /** Opción 3 — Insertar nueva parada */
    private static void opcionInsertar() {
        System.out.println("\n  ¿Qué querés insertar?");
        System.out.println("  [1] Zona  [2] Barrio  [3] Parada");
        System.out.print("  Opción: ");
        String sub = scanner.nextLine().trim();

        switch (sub) {
            case "1" -> {
                System.out.print("  Nombre de la nueva zona: ");
                arbol.insertarZona(scanner.nextLine().trim());
            }
            case "2" -> {
                System.out.print("  Nombre de la zona padre: ");
                String zona = scanner.nextLine().trim();
                System.out.print("  Nombre del nuevo barrio: ");
                String barrio = scanner.nextLine().trim();
                arbol.insertarBarrio(zona, barrio);
            }
            case "3" -> {
                System.out.print("  Nombre del barrio padre: ");
                String barrio = scanner.nextLine().trim();
                System.out.print("  Nombre de la nueva parada: ");
                String parada = scanner.nextLine().trim();
                arbol.insertarParada(barrio, parada);

                // Pregunta si también se agrega al grafo
                System.out.print("  ¿Agregar la parada al grafo también? (s/n): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                    grafo.agregarNodo(parada);
                    System.out.println("  [+] Parada '" + parada + "' agregada al grafo.");
                    System.out.println("  (Podés conectarla desde la opción 4 del menú avanzado)");
                }
            }
            default -> System.out.println("  [!] Opción no válida.");
        }
    }

    /** Opción 5 — Ruta mínima (Dijkstra puro) */
    private static void opcionRutaMinima() {
        System.out.print("\n  Parada de ORIGEN: ");
        String origen = scanner.nextLine().trim();
        System.out.print("  Parada de DESTINO: ");
        String destino = scanner.nextLine().trim();

        Dijkstra.Resultado resultado = Dijkstra.calcular(grafo, origen, destino);
        Dijkstra.mostrarResultado(origen, destino, resultado);
    }

    /**
     * Opción 6 — INTEGRACIÓN árbol + grafo.
     *
     * Flujo:
     *   1. El usuario indica una zona.
     *   2. El árbol devuelve las paradas de esa zona.
     *   3. El usuario elige origen y destino entre esas paradas.
     *   4. Dijkstra calcula la ruta mínima en el grafo.
     *
     * Esta es la operación que demuestra la colaboración necesaria
     * entre ambas estructuras: sin el árbol no sabríamos qué paradas
     * pertenecen a la zona; sin el grafo no podríamos calcular la ruta.
     */
    private static void opcionIntegracion() {
        System.out.print("\n  Ingresá el nombre de la zona: ");
        String zona = scanner.nextLine().trim();

        List<String> paradas = arbol.listarParadasDeZona(zona);

        if (paradas.isEmpty()) {
            System.out.println("  [!] No se encontraron paradas para esa zona.");
            return;
        }

        System.out.println("\n  Paradas en " + zona + ":");
        for (int i = 0; i < paradas.size(); i++) {
            System.out.println("    [" + (i + 1) + "] " + paradas.get(i));
        }

        System.out.print("\n  Elegí el número de la parada ORIGEN: ");
        int idxOrigen;
        try {
            idxOrigen = Integer.parseInt(scanner.nextLine().trim()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("  [!] Número inválido.");
            return;
        }

        System.out.print("  Elegí el número de la parada DESTINO: ");
        int idxDestino;
        try {
            idxDestino = Integer.parseInt(scanner.nextLine().trim()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("  [!] Número inválido.");
            return;
        }

        if (idxOrigen < 0 || idxOrigen >= paradas.size() ||
            idxDestino < 0 || idxDestino >= paradas.size()) {
            System.out.println("  [!] Índice fuera de rango.");
            return;
        }

        String origen  = paradas.get(idxOrigen);
        String destino = paradas.get(idxDestino);

        // Dijkstra opera sobre el grafo completo (la ruta puede pasar por otras zonas)
        Dijkstra.Resultado resultado = Dijkstra.calcular(grafo, origen, destino);
        Dijkstra.mostrarResultado(origen, destino, resultado);

        // Nota informativa sobre paradas intermedias fuera de la zona
        if (resultado.alcanzable) {
            long fueraDeZona = resultado.camino.stream()
                .filter(p -> !paradas.contains(p))
                .count();
            if (fueraDeZona > 0) {
                System.out.println("  (Nota: la ruta óptima pasa por " + fueraDeZona +
                    " parada(s) fuera de " + zona + ")");
            }
        }
    }

    /** Opción 7 — BFS de conectividad */
    private static void opcionBFS() {
        System.out.print("\n  Parada de inicio para BFS: ");
        String inicio = scanner.nextLine().trim();
        grafo.BFS(inicio);
    }

    /** Opción 8 — DFS desde una parada */
    private static void opcionDFS() {
        System.out.print("\n  Parada de inicio para DFS: ");
        String inicio = scanner.nextLine().trim();
        grafo.DFS(inicio);
    }
}
