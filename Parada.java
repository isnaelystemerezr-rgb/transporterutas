package modelo;

/**
 * Representa una parada de transporte público dentro de la red urbana.
 * Es la unidad atómica del sistema: aparece como hoja en el Árbol General
 * y como nodo en el Grafo de conexiones.
 */
public class Parada {

    private String nombre;      // Identificador único de la parada
    private String barrio;      // Barrio al que pertenece (referencia informativa)
    private String zona;        // Zona al que pertenece (referencia informativa)

    public Parada(String nombre, String barrio, String zona) {
        this.nombre = nombre;
        this.barrio = barrio;
        this.zona   = zona;
    }

    // ── Getters ─────────────────────────────────────────────────────────────

    public String getNombre() { return nombre; }
    public String getBarrio() { return barrio; }
    public String getZona()   { return zona;   }

    // ── Utilidades ──────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return String.format("Parada[%s | Barrio: %s | Zona: %s]", nombre, barrio, zona);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Parada)) return false;
        Parada otra = (Parada) obj;
        return this.nombre.equalsIgnoreCase(otra.nombre);
    }

    @Override
    public int hashCode() {
        return nombre.toLowerCase().hashCode();
    }
}
