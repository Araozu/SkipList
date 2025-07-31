# SkipList

Esta es una implementación mejorada de SkipList en Java. Para más
información acerca de los SkipList, ver 
https://es.wikipedia.org/wiki/Skip_list.

La implementación se realizó inicialmente en Kotlin con el fin de poder 
diseñar y probar la lógica y estructura de forma rápida. Luego, se tradujo 
a Java y se mejoró significativamente.

## Mejoras Implementadas

**Rendimiento:**
- ✅ Eliminación ahora opera en O(log n) en lugar de O(n)
- ✅ Generación mejorada de niveles aleatorios
- ✅ Manejo eficiente de memoria

**Calidad de Código:**
- ✅ Validación completa de entrada con mensajes de error claros
- ✅ Documentación JavaDoc comprehensiva
- ✅ Convenciones de nombres estandarizadas
- ✅ Suite de pruebas unitarias completa

**Funcionalidad:**
- ✅ Seguimiento del tamaño de la lista con método `size()`
- ✅ Representación simple con `toSimpleString()`
- ✅ Manejo robusto de casos edge

## Uso

### Archivos Principales

- `/src/SkipList.java`: Implementación principal con todas las operaciones optimizadas
- `/src/Inicio.java`: Programa de prueba básico  
- `/src/Demo.java`: Demostración completa de las mejoras
- `/src/SkipListTest.java`: Suite de pruebas unitarias
- `/src/PerformanceTest.java`: Pruebas de rendimiento
- `/src/NSkipList.kt`: Primera versión en Kotlin (solo inserción)

### Ejemplo de Uso

```java
// Crear SkipList con altura 5
SkipList<Integer> sl = new SkipList<>(5);

// Insertar elementos
sl.insertar(10);
sl.insertar(5);
sl.insertar(15);

// Buscar elementos
boolean existe = sl.contiene(10); // true

// Obtener tamaño
int tamaño = sl.size(); // 3

// Vista simple
System.out.println(sl.toSimpleString()); // [5, 10, 15]

// Eliminar elemento
sl.eliminar(10);
```

### Ejecutar Pruebas

```bash
# Compilar todos los archivos
javac src/*.java

# Ejecutar demostración
java -cp src Demo

# Ejecutar pruebas unitarias
java -cp src SkipListTest

# Ejecutar pruebas de rendimiento
java -cp src PerformanceTest
```

## Rendimiento

La implementación mejorada maneja eficientemente:
- **1000+ elementos**: Inserción ~6ms, Búsqueda ~3ms, Eliminación ~10ms
- **Escalabilidad**: O(log n) para todas las operaciones principales
- **Memoria**: Uso eficiente con clases internas optimizadas

## Consideraciones

✅ **Apto para producción**: Todas las operaciones principales operan en O(log n)
✅ **Robusto**: Manejo completo de errores y validación de entrada
✅ **Probado**: Suite completa de pruebas unitarias y de rendimiento
✅ **Documentado**: JavaDoc completo y ejemplos de uso
