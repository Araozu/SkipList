# SkipList

Esta es una implementación de SkipList en Java. Para más
información acerca de los SkipList, ver 
https://es.wikipedia.org/wiki/Skip_list.

La implementación se realizo en Kotlin en primera 
instancia con el fin de poder diseñar y probar la lógica
y estructura de forma rápida. Luego, se tradujo a Java.

## Uso

/src/SkipList.java: código con las 4 operaciones 
principales.

/src/Inicio.java: conjunto de pruebas. 

/src/NSkipList.kt: primera versión del código, solo 
cuenta con la operación insertar.

## Consideraciones

La estructura opera eficientemente en todas las
operaciones excepto en la eliminación, donde realiza
O(n) operaciones. No apto para producción :)
