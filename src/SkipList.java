import java.util.ArrayList;

/** SkipList en Java.
 *
 * @version 1.0
 * @since 2019-07-01
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
            ArrayList<Pair<Boolean, Nodo>> punteros = new ArrayList<>(altura - 1);
            for (int i = 0; i < altura - 2; i++) {
                punteros.add(new Pair<>(false, null));
            }
            this.punteros = punteros;
        }
        Nodo(T dato) {
            this(dato, null);
        }

    }

    private final int altura;
    private final ArrayList<Pair<Boolean, Nodo>> cabeza;
    private Nodo sig = null;
    private final int PROBABILIDAD = 50;

    public SkipList(int altura) {
        this.altura = altura;
        ArrayList<Pair<Boolean, Nodo>> cabeza = new ArrayList<>(altura - 1);
        for (int i = 0; i < altura - 2; i++) {
            cabeza.add(new Pair<>(true, null));
        }
        this.cabeza = cabeza;
    }

    /**
     * Revisa si la lista está vacia.
     * @return Un boolean indicando si la lista está vacia.
     * */
    public boolean estaVacia() {
        return sig == null;
    }


    // ==================================
    // Genera el número de niveles que subirá un nodo tras insertarse
    // ==================================
    private int genNumInserciones() {
        int i = -1;
        while (Math.random() * 100 < PROBABILIDAD) {
            i++;
        }
        return i;
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
    // Utiliza los niveles superiores para insertar.
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
     * @param dato El elemento a insertar
     */
    public void insertar(T dato) {
        for (int i = cabeza.size() - 1; i >= 0; i--) {
            Pair<Boolean, Nodo> par = cabeza.get(i);
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
                    incrementarNivel(cabeza, nuevoNodo, nivelActual);
                    nivelActual++;
                }
                return;
            }
        }

        Nodo primerNodo = sig;
        if (primerNodo == null) {
            Nodo nuevoNodo = new Nodo(dato);
            sig = nuevoNodo;
            int numInserciones = genNumInserciones();
            for (int i = 0; i <= numInserciones; i++) {
                incrementarNivel(cabeza, nuevoNodo, i);
            }
        } else if (dato.compareTo(primerNodo.dato) < 0) {
            Nodo nuevoNodo = new Nodo(dato, primerNodo);
            sig = nuevoNodo;
            int numInserciones = genNumInserciones();
            for (int i = 0; i <= numInserciones; i++) {
                incrementarNivel(cabeza, nuevoNodo, i);
            }
        } else {
            Pair<Nodo, Integer> par = insertar(primerNodo, dato);
            Nodo nuevoNodo = par.first;
            int numInserciones = par.second;
            for (int i = 0; i <= numInserciones; i++) {
                incrementarNivel(cabeza, nuevoNodo, i);
            }
        }

    }

    private Nodo buscar(Nodo nodoAct, T dato) {
        if (nodoAct.sig == null) {
            return null;
        }

        int resComparacion = dato.compareTo(nodoAct.sig.dato);
        if (resComparacion < 0) return buscar(nodoAct.sig, dato);
        else if (resComparacion == 0) return nodoAct.sig;
        else return null;
    }

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
     * @param dato El elemento a buscar en la lista
     * @return Un boolean indicando si el elemento existe en la lista.
     * */
    public boolean contiene(T dato) {
        for (int i = cabeza.size() - 1; i >= 0; i--) {
            Pair<Boolean, Nodo> par = cabeza.get(i);
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
        return buscar(sig, dato) != null;
    }

    private void eliminarRapido(Nodo nodo, T dato) {
        if (nodo == null) return;

        for (int i = nodo.punteros.size() - 1; i >= 0; i--) {
            Pair<Boolean, Nodo> par = nodo.punteros.get(i);
            Nodo nodoSig = par.second;
            if (nodoSig == null) continue;
            T datoN = nodoSig.dato;

            int resComparacion = dato.compareTo(datoN);
            if (resComparacion == 0) {
                Pair<Boolean, Nodo> referenciaDestino = nodoSig.punteros.get(i);
                Nodo nodoSigSig = referenciaDestino.second;

                nodo.punteros.set(i, new Pair<>(true, nodoSigSig));

            } else if (resComparacion > 0) {
                eliminarRapido(nodoSig, dato);
            }
        }
    }

    private void eliminar(Nodo nodo, T dato) {
        if (nodo == null || nodo.sig == null) return;

        int resComparacion = dato.compareTo(nodo.sig.dato);
        if (resComparacion == 0) {
            nodo.sig = nodo.sig.sig;
        } else if (resComparacion > 0) {
            eliminar(nodo.sig, dato);
        }
    }

    /**
     * Elimina un elemento de la lista silenciosamente.
     * @param dato El elemento a eliminar
     * */
    public void eliminar(T dato) {
        if (sig == null) return;

        for (int i = cabeza.size() - 1; i >= 0; i--) {
            Pair<Boolean, Nodo> par = cabeza.get(i);
            Nodo nodo = par.second;
            if (nodo == null) continue;
            T datoN = nodo.dato;

            int resComparacion = dato.compareTo(datoN);
            if (resComparacion == 0) {
                Pair<Boolean, Nodo> referenciaDestino = nodo.punteros.get(i);
                Nodo nodoSig = referenciaDestino.second;

                cabeza.set(i, new Pair<>(true, nodoSig));

            } else if (resComparacion > 0) {
                eliminarRapido(nodo, dato);
            }
        }

        if (sig.dato.equals(dato)) {
            sig = sig.sig;
        } else {
            eliminar(sig, dato);
        }
    }

    private String obtCabeza() {
        String dato = sig == null? "null": sig.dato.toString();
        String res = "REF\n< " + dato + " >";
        for (Pair<Boolean, Nodo> p: cabeza) {
            Nodo n = p.second;
            String valorNodo = n == null? "null": n.dato.toString();
            res += "| " + valorNodo + " |";
        }
        return res;
    }

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
        return obtCabeza() + "\n|" + obtHijos("", sig);
    }

}
