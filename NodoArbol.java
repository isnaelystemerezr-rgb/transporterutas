package arbol;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodo del Árbol General.
 * Cada nodo almacena un rótulo (nombre de zona, barrio o parada)
 * y una lista de hijos de tamaño variable, lo que lo hace apto
 * para representar jerarquías con cardinalidad arbitraria.
 *
 * Estructura: Zona → Barrio → Parada  (tres niveles de profundidad)
 */
public class NodoArbol {

    private String etiqueta;            // Nombre del nodo (zona, barrio o parada)
    private String tipo;                // "ZONA", "BARRIO" o "PARADA"
    private List<NodoArbol> hijos;      // Hijos directos (tamaño variable)

    public NodoArbol(String etiqueta, String tipo) {
        this.etiqueta = etiqueta;
        this.tipo     = tipo;
        this.hijos    = new ArrayList<>();
    }

    // ── Operaciones sobre hijos ──────────────────────────────────────────────

    /** Agrega un hijo directo a este nodo. */
    public void agregarHijo(NodoArbol hijo) {
        hijos.add(hijo);
    }

    /** Devuelve true si este nodo no tiene hijos (es hoja = Parada). */
    public boolean esHoja() {
        return hijos.isEmpty();
    }

    // ── Getters ─────────────────────────────────────────────────────────────

    public String getEtiqueta()        { return etiqueta; }
    public String getTipo()            { return tipo;     }
    public List<NodoArbol> getHijos()  { return hijos;    }

    @Override
    public String toString() {
        return String.format("[%s] %s", tipo, etiqueta);
    }
}
