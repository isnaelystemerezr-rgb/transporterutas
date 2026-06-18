El Sistema de Rutas de Transporte Urbano es una aplicación en Java que modela la red de transporte de Ciudad Nexo integrando dos estructuras de datos: un Árbol General para la jerarquía geográfica (Ciudad, Zonas, Barrios y Paradas) y un Grafo Ponderado para las conexiones entre paradas con tiempos de viaje en minutos.

El Árbol General permite insertar zonas, barrios y paradas con validación de duplicados, buscar nodos mediante DFS, y recorrer la jerarquía con BFS. Su método clave es listarParadasDeZona, que obtiene todas las paradas de una zona específica para integrarse con el grafo.

El Grafo utiliza lista de adyacencia para representar conexiones no dirigidas con pesos, permitiendo recorridos BFS y DFS para verificar conectividad. El algoritmo de Dijkstra calcula la ruta mínima entre dos paradas con complejidad O((V+E) log V) usando una cola de prioridad.

La interfaz gráfica con Swing presenta siete opciones: ver jerarquía, buscar en el árbol, insertar elementos, ver el grafo, calcular ruta mínima, listar paradas por zona (integración clave), y verificar conectividad con BFS/DFS.

El sistema incluye datos de prueba con tres zonas, seis barrios, quince paradas y dieciocho conexiones con pesos de dos a doce minutos.

La integración árbol-grafo se demuestra cuando el sistema identifica las paradas de una zona mediante el árbol y calcula rutas entre ellas usando el grafo con Dijkstra, pudiendo la ruta óptima pasar por paradas de otras zonas.
