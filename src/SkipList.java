import java.util.ArrayList;

/** SkipList en Java.
 * 
 * Una implementación eficiente de Skip List que proporciona operaciones
 * de inserción, búsqueda y eliminación en tiempo O(log n) promedio.
 * 
 * @version 2.0
 * @since 2019-07-01
 * @param <T> El tipo de datos que almacena la lista, debe ser Comparable
 * */
public class SkipList<T extends Comparable<T>> {

    // ==================================
    // Representa los resultados al incrementar el nivel de un nodo
    // ==================================
    enum ResIncremento {
        EXITO,
        DELEGADO,
        ERROR
    }

    // ==================================
    // Clase que contiene 2 valores genéricos
    // ==================================
    private class Pair<D, E> {
        final D first;
        final E second;
        Pair(D first, E second) {
            this.first = first;
            this.second = second;
        }
    }

    // ==================================
    // Clase que contiene 3 valores genéricos
    // ==================================
    private class Triple<D, E, F> {
        final D first;
        final E second;
        final F third;
        Triple(D first, E second, F third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }
    }

    // ==================================
    // Clase que representa un nodo del SkipList.
    // Es una clase interna para evitar complicar el código con los parámetros genéricos
    // ==================================
    private class Nodo {
        final T dato;
        // Puntero al siguiente nodo
        Nodo sig;
        // Contiene las referencias de los niveles superiores.
        ArrayList<Pair<Boolean, Nodo>> punteros;

        Nodo(T dato, Nodo sig) {
            this.dato = dato;
            this.sig = sig;
            ArrayList<Pair<Boolean, Nodo>> punteros = new ArrayList<>(height - 1);
            for (int i = 0; i < height - 2; i++) {
                punteros.add(new Pair<>(false, null));
            }
            this.punteros = punteros;
        }
        Nodo(T dato) {
            this(dato, null);
        }

    }

    private final int height;
    private final ArrayList<Pair<Boolean, Nodo>> head;
    private Nodo next = null;
    private final int PROBABILITY = 50; // Kept for backward compatibility

    /**
     * Constructor del SkipList.
     * @param altura La altura máxima de la estructura (número de niveles)
     */
    public SkipList(int altura) {
        this.height = altura;
        ArrayList<Pair<Boolean, Nodo>> head = new ArrayList<>(altura - 1);
        for (int i = 0; i < altura - 2; i++) {
            head.add(new Pair<>(true, null));
        }
        this.head = head;
    }

    /**
     * Revisa si la lista está vacia.
     * @return Un boolean indicando si la lista está vacia.
     * */
    public boolean estaVacia() {
        return next == null;
    }


    // ==================================
    // Genera el número de niveles que subirá un nodo tras insertarse
    // Improved algorithm with better distribution and max level cap
    // ==================================
    private int genNumInserciones() {
        int level = 0;
        // Use 0.5 probability for better theoretical performance
        // and cap at maximum useful height
        while (Math.random() < 0.5 && level < height - 2) {
            level++;
        }
        return level;
    }

    // ==================================
    // Intenta incrementar el nivel del nodo. Si falla, el responsable es quien llamó al método
    // ==================================
    private ResIncremento incrementarNivel(ArrayList<Pair<Boolean, Nodo>> objetivo, Nodo nodo, int nivel) {
        if (nivel >= objetivo.size()) return ResIncremento.ERROR;
        Pair<Boolean, Nodo> par = objetivo.get(nivel);
        boolean anterior = par.first;
        Nodo nodo2 = par.second;

        if (anterior) {
            nodo.punteros.set(nivel, new Pair<>(true, nodo2));
            objetivo.set(nivel, new Pair<>(true, nodo));

            return ResIncremento.EXITO;
        }

        return ResIncremento.DELEGADO;
    }

    // Método 'helper' para la inserción.
    private Triple<Nodo, Integer, Integer> helper(Nodo nodo, Nodo nuevoNodo, int numInserciones, int nivelActual) {
        int insercionesRestantes = numInserciones;
        while (insercionesRestantes > -1) {
            ResIncremento resIncremento = incrementarNivel(nodo.punteros, nuevoNodo, nivelActual);
            if (resIncremento == ResIncremento.ERROR || resIncremento == ResIncremento.DELEGADO)
                break;
            nivelActual++;
            insercionesRestantes--;
        }
        return new Triple<>(nuevoNodo, nivelActual, insercionesRestantes);
    }

    // ==================================
    // Utiliza las vías rápidas para insertar.
    // ==================================
    private Triple<Nodo, Integer, Integer> insertarRapido(Nodo nodo, T dato) {

        for (int i = nodo.punteros.size() - 1; i >= 0; i--) {
            Pair<Boolean, Nodo> par = nodo.punteros.get(i);
            Nodo nodoP = par.second;
            if (nodoP == null) continue;
            T datoN = nodoP.dato;
            if (dato.compareTo(datoN) > 0) {
                Triple<Nodo, Integer, Integer> triple = insertarRapido(nodoP, dato);
                Nodo nuevoNodo = triple.first;
                int nivel = triple.second;
                int numInserciones = triple.third;

                return helper(nodo, nuevoNodo, numInserciones, nivel);
            }
        }

        Pair<Nodo, Integer> par = insertar(nodo, dato);
        Nodo nuevoNodo = par.first;
        int numInserciones = par.second;
        int nivelActual = 0;
        return helper(nodo, nuevoNodo, numInserciones, nivelActual);
    }

    // ==================================
    // Utiliza el último nivel para insertar.
    // ==================================
    private Pair<Nodo, Integer> insertar(Nodo nodo, T dato) {
        Nodo nodoSig = nodo.sig;
        if (nodoSig == null) {
            Nodo nuevoNodo = new Nodo(dato);
            nodo.sig = nuevoNodo;
            int numInserciones = genNumInserciones();
            return new Pair<>(nuevoNodo, numInserciones);
        } else if (dato.compareTo(nodoSig.dato) <= 0) {
            int numInserciones = genNumInserciones();
            Nodo nuevoNodo = new Nodo(dato, nodoSig);
            nodo.sig = nuevoNodo;
            return new Pair<>(nuevoNodo, numInserciones);
        } else {
            return insertar(nodoSig, dato);
        }
    }

    /**
     * Inserta un elemento en el skip list, y genera sus niveles automáticamente.
     * Complejidad temporal: O(log n) promedio
     * @param dato El elemento a insertar
     */
    public void insertar(T dato) {
        for (int i = head.size() - 1; i >= 0; i--) {
            Pair<Boolean, Nodo> par = head.get(i);
            Nodo nodo = par.second;
            if (nodo == null) continue;
            T datoN = nodo.dato;

            if (dato.compareTo(datoN) > 0) {
                Triple<Nodo, Integer, Integer> triple = insertarRapido(nodo, dato);
                Nodo nuevoNodo = triple.first;
                int nivel = triple.second;
                int numInserciones = triple.third;

                int nivelActual = nivel;
                for (int j = 0; j <= numInserciones; j++ ) {
                    incrementarNivel(head, nuevoNodo, nivelActual);
                    nivelActual++;
                }
                return;
            }
        }

        Nodo primerNodo = next;
        if (primerNodo == null) {
            Nodo nuevoNodo = new Nodo(dato);
            next = nuevoNodo;
            int numInserciones = genNumInserciones();
            for (int i = 0; i <= numInserciones; i++) {
                incrementarNivel(head, nuevoNodo, i);
            }
        } else if (dato.compareTo(primerNodo.dato) < 0) {
            Nodo nuevoNodo = new Nodo(dato, primerNodo);
            next = nuevoNodo;
            int numInserciones = genNumInserciones();
            for (int i = 0; i <= numInserciones; i++) {
                incrementarNivel(head, nuevoNodo, i);
            }
        } else {
            Pair<Nodo, Integer> par = insertar(primerNodo, dato);
            Nodo nuevoNodo = par.first;
            int numInserciones = par.second;
            for (int i = 0; i <= numInserciones; i++) {
                incrementarNivel(head, nuevoNodo, i);
            }
        }

    }

    // ==================================
    // Busca un nodo usando el último nivel
    // ==================================
    private Nodo buscar(Nodo nodoAct, T dato) {
        if (nodoAct.sig == null) {
            return null;
        }

        int resComparacion = dato.compareTo(nodoAct.sig.dato);
        if (resComparacion < 0) return buscar(nodoAct.sig, dato);
        else if (resComparacion == 0) return nodoAct.sig;
        else return null;
    }

    // ==================================
    // Busca un nodo usando las vías rápidas
    // ==================================
    private Nodo buscarRapido(Nodo nodoAct, T dato) {
        if (nodoAct.dato.equals(dato)) return nodoAct;
        for (int i = nodoAct.punteros.size() - 1; i >= 0; i--) {
            Pair<Boolean, Nodo> par = nodoAct.punteros.get(i);
            Nodo nodo = par.second;
            if (nodo == null) continue;
            T datoN = nodo.dato;

            int resComparacion = dato.compareTo(datoN);
            if (resComparacion > 0) {
                return buscarRapido(nodo, dato);
            } else if (resComparacion == 0) {
                return nodo;
            }
        }
        return buscar(nodoAct, dato);
    }

    /**
     * Revisa si un elemento existe en la lista
     * Complejidad temporal: O(log n) promedio
     * @param dato El elemento a buscar en la lista
     * @return Un boolean indicando si el elemento existe en la lista.
     * */
    public boolean contiene(T dato) {
        for (int i = head.size() - 1; i >= 0; i--) {
            Pair<Boolean, Nodo> par = head.get(i);
            Nodo nodo = par.second;
            if (nodo == null) continue;
            T datoN = nodo.dato;

            int resComparacion = dato.compareTo(datoN);
            if (resComparacion > 0) {
                return buscarRapido(nodo, dato) != null;
            } else if (resComparacion == 0) {
                return true;
            }
        }
        return buscar(next, dato) != null;
    }

    /**
     * Elimina un elemento de la lista silenciosamente.
     * Complejidad temporal: O(log n) promedio - MEJORADO
     * @param dato El elemento a eliminar
     * */
    public void eliminar(T dato) {
        if (next == null) return;
        
        // Array to store the predecessors at each level that need updating
        ArrayList<Nodo> update = new ArrayList<>(height - 1);
        for (int i = 0; i < height - 1; i++) {
            update.add(null);
        }
        
        // Start from the top level and find predecessors
        Nodo current = null;
        
        // Search through header levels first
        for (int level = head.size() - 1; level >= 0; level--) {
            Pair<Boolean, Nodo> headerPair = head.get(level);
            current = headerPair.second;
            
            // If header points directly to target or beyond, update from header
            if (current == null || dato.compareTo(current.dato) <= 0) {
                update.set(level, null); // Will update header directly
                continue;
            }
            
            // Traverse forward at this level
            while (current != null && level < current.punteros.size()) {
                Pair<Boolean, Nodo> nextPair = current.punteros.get(level);
                Nodo next = nextPair.second;
                if (next == null || dato.compareTo(next.dato) <= 0) {
                    break;
                }
                current = next;
            }
            update.set(level, current);
        }
        
        // Find the node to delete at the bottom level
        Nodo nodeToDelete = null;
        if (update.get(0) == null) {
            // Check if it's the first node
            if (next != null && next.dato.equals(dato)) {
                nodeToDelete = next;
            }
        } else {
            // Check the next node from the last update position
            if (update.get(0).sig != null && update.get(0).sig.dato.equals(dato)) {
                nodeToDelete = update.get(0).sig;
            }
        }
        
        if (nodeToDelete == null) return; // Node not found
        
        // Update pointers at all levels
        for (int level = 0; level < Math.min(height - 1, nodeToDelete.punteros.size()); level++) {
            Nodo predecessor = update.get(level);
            Nodo successorAtLevel = null;
            
            if (level < nodeToDelete.punteros.size()) {
                Pair<Boolean, Nodo> successorPair = nodeToDelete.punteros.get(level);
                successorAtLevel = successorPair.second;
            }
            
            if (predecessor == null) {
                // Update header
                head.set(level, new Pair<>(true, successorAtLevel));
            } else {
                // Update predecessor's pointer
                predecessor.punteros.set(level, new Pair<>(true, successorAtLevel));
            }
        }
        
        // Update bottom level pointer
        if (update.get(0) == null) {
            // Deleting first node
            next = nodeToDelete.sig;
        } else {
            // Update predecessor's sig pointer
            update.get(0).sig = nodeToDelete.sig;
        }
    }

    // ==================================
    // Método que genera un String representando la cabeza.
    // ==================================
    private String obtCabeza() {
        String dato = next == null? "null": next.dato.toString();
        String res = "REF\n< " + dato + " >";
        for (Pair<Boolean, Nodo> p: head) {
            Nodo n = p.second;
            String valorNodo = n == null? "null": n.dato.toString();
            res += "| " + valorNodo + " |";
        }
        return res;
    }

    // ==================================
    // Método que recursivamente genera un String representando la lista.
    // ==================================
    private String obtHijos(String acc, Nodo nodo) {
        if (nodo == null) return acc;
        String sup = "< " + nodo.dato + " >";
        String inf = "|";
        for (int i = 0; i <= sup.length() - 3; i++) inf += " ";
        inf += "|";
        for (Pair<Boolean, Nodo> p: nodo.punteros) {
            boolean existe = p.first;
            Nodo sig = p.second;
            String infTemp = sig != null? " " + sig.dato + " |": "      ";
            boolean esPar = (infTemp.length() - 1) % 2 == 0;
            int pasos = (infTemp.length() - 1) / 2;
            String pad = "";
            if (esPar)
                for (int i = 2; i <= pasos; i++) pad += " ";
            else for (int i = 1; i <= pasos; i++) pad += " ";
            String final_ = pad +
                    (existe? "X": "") + pad +
                    (esPar? " ": "") +
                    (existe? "|": "");
            sup += final_;
            inf += infTemp;
        }

        return obtHijos(acc + "\n" + sup + "\n" + inf + "\n", nodo.sig);
    }

    @Override
    public String toString() {
        return obtCabeza() + "\n|" + obtHijos("", next);
    }

}
