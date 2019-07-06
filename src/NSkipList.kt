/** Implementación de SkipList en Kotlin.
 * Se escogió Kotlin para poder desarrollar la lógica del programa fácilmente, y luego
 * se re-escribió en Java.
 *
 * @version 1.0
 * @since 2019-07-01
 * */

class NSkipList<T: Comparable<T>> (private val altura: Int) {

    private inner class Nodo (
        val dato: T,
        var sig: Nodo? = null,
        val punteros: Array<Pair<Boolean, Nodo?>> = Array(altura - 1) { Pair(false, null) }
    )

    private val cabeza: Array<Pair<Boolean, Nodo?>> = Array(altura - 1) { Pair(true, null) }
    private var sig: Nodo? = null
    private val _probabilidad: Byte = 35 // %

    private fun genNumInserciones(): Int {
        var res = -1
        // No tengo ni idea de porque, pero si cambias a (altura - 2) es imposible
        // que la lista alcance el último nivel ...
        for (i in 0 until altura - 1) {
            if (Math.random() * 100 < _probabilidad) res++
        }
        // println("Res Ran ${res + 1}")
        return res
    }

    private fun incrementarNivel(objetivo: Array<Pair<Boolean, Nodo?>>, nodo: Nodo, nivel: Int): Boolean? {
        return if (nivel >= objetivo.size) null
        else {
            val (anterior, nodo2) = objetivo[nivel]
            if (anterior) {

                if (nodo2 != null && nodo.dato > nodo2.dato)
                    throw IllegalArgumentException("Error. Estado ilegal.\n" +
                            "Se intentó elevar ${nodo.dato} y hacer que apunte a ${nodo2.dato}, " +
                            "lo que causaría un bucle infinito.")

                nodo.punteros[nivel] = Pair(true, nodo2)
                objetivo[nivel] = Pair(true, nodo)

                true
            } else {
                false
            }
        }
    }

    private fun insertarRapido(nodo: Nodo, dato: T): Triple<Nodo, Int, Int> {

        for (i in (nodo.punteros.size - 1) downTo 0) {
            val (_, nodoP) = nodo.punteros[i]
            val datoN = nodoP?.dato ?: continue
            if (dato > datoN) {
                val (nuevoNodo, nivel, numInserciones) = insertarRapido(nodoP, dato)
                var nivelActual = nivel
                var insercionesRestantes = numInserciones
                while (insercionesRestantes > -1) {
                    try {
                        val resInsercion = incrementarNivel(nodo.punteros, nuevoNodo, nivelActual) ?: break
                        if (!resInsercion) {
                            break
                        }
                        nivelActual++
                        insercionesRestantes--
                    } catch (e: java.lang.IllegalArgumentException) {
                        System.err.println("Más datos:\n" +
                                "Nodo en el que se inserta: ${nodo.dato}\n" +
                                "Nodo resultado: ${nuevoNodo.dato}\n" +
                                "Nivel en el que insertar: $nivelActual\n" +
                                "Inserciones restantes: ${insercionesRestantes + 1}\n" +
                                "Referencias del nivel: ${nodo.punteros[nivelActual].first}, ${nodo.punteros[nivelActual].second?.dato}")
                        throw e
                    }
                }
                return Triple(nuevoNodo, nivelActual, insercionesRestantes)
            }
        }

        val (nuevoNodo, numInserciones) = insertar(nodo, dato)
        var nivelAct = 0
        var insercionesRestantes = numInserciones
        while (insercionesRestantes > -1) {
            val resInsercion = incrementarNivel(nodo.punteros, nuevoNodo, nivelAct) ?: break
            if (!resInsercion) {
                break
            }
            nivelAct++
            insercionesRestantes--
        }

        return Triple(nuevoNodo, nivelAct, insercionesRestantes)
    }

    private fun insertar(nodo: Nodo, dato: T): Pair<Nodo, Int> {
        val nodoSig = nodo.sig
        return if (nodoSig == null) {
            val numInserciones = genNumInserciones()
            val nuevoNodo = Nodo(dato)
            nodo.sig = nuevoNodo
            Pair(nuevoNodo, numInserciones)
        } else if (dato <= nodoSig.dato) {
            val numInserciones = genNumInserciones()
            val nuevoNodo = Nodo(dato, nodoSig)
            nodo.sig = nuevoNodo
            Pair(nuevoNodo, numInserciones)
        } else {
            insertar(nodoSig, dato)
        }
    }

    fun insertar(dato: T) {
        /* Intentar insertar a través de las lineas rápidas */
        for (i in (cabeza.size - 1) downTo 0) {
            val (_, nodo) = cabeza[i]
            val datoN = nodo?.dato ?: continue
            if (dato > datoN) {
                val (nuevoNodo, nivel, numInserciones) = insertarRapido(nodo, dato)
                var nivelActual = nivel
                for (j in 0..numInserciones) {
                    incrementarNivel(cabeza, nuevoNodo, nivelActual)
                    nivelActual++
                }
                return
            }
        }

        val primerNodo = sig
        if (primerNodo == null) {
            val nuevoNodo = Nodo(dato)
            sig = nuevoNodo
            val numInserciones = genNumInserciones()
            for (i in 0..numInserciones) {
                incrementarNivel(cabeza, nuevoNodo, i)
            }
        } else if (dato <= primerNodo.dato) {
            val nuevoNodo = Nodo(dato, primerNodo)
            sig = nuevoNodo
            val numInserciones = genNumInserciones()
            for (i in 0..numInserciones) {
                incrementarNivel(cabeza, nuevoNodo, i)
            }
        } else {
            val (nuevoNodo, numInserciones) = insertar(primerNodo, dato)
            for (i in 0..numInserciones) {
                incrementarNivel(cabeza, nuevoNodo, i)
            }
        }
    }

    private fun obtCabeza(): String {
        var res = "REF\n< ${sig?.dato} >"
        for ((_, n) in cabeza) {
            res += "|${n?.dato}| "
        }
        return res
    }

    private tailrec fun obtHijos(acc: String, nodo: Nodo?): String {
        return if (nodo == null) acc else {

            var sup = "< ${nodo.dato} >"
            var inf = "|"
            for (i in 0..(sup.length - 3)) inf += " "
            inf += "|"
            for ((existe, sig) in nodo.punteros) {
                val infTemp = if (sig != null) " ${sig.dato} |" else "      "
                val esPar = (infTemp.length - 1) % 2 == 0
                val pasos = (infTemp.length - 1) / 2
                var pad = ""
                if (esPar) for (i in 2..pasos) pad += " " else for (i in 1..pasos) pad += " "
                val final =
                        pad +
                        (if (existe) "X" else "") + pad +
                        (if (esPar) " " else "") +
                        (if (existe) "|" else "")
                sup += final
                inf += infTemp
            }

            obtHijos("$acc\n$sup\n$inf\n|", nodo.sig)
        }
    }

    override fun toString(): String {
        return "${obtCabeza()}\n|${obtHijos("", sig)}"
    }

}
