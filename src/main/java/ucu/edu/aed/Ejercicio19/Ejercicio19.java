package ucu.edu.aed.ProblemSet01.Ejercicio19;

public class Ejercicio19 
{
    /*  Se necesita construir un analizador sintáctico para cierto lenguaje de programación y,
        para ello, escribir un método que, dada una entrada representada por la lista de
        caracteres del código fuente, controle si la secuencia de corchetes es correcta o no.
        Ejemplos
        Bien formada: {}{{}}
        Mal formada: {{}{{}

    PARTE 1:
        Función booleana controlCorchetes (listaDeEntrada: Lista de Caracteres) 
    Variables:
        pila: TDAPila de Caracteres
        caracterActual: Caracter
    Inicio
        pila <- nueva Pila() 

        Para cada caracterActual en listaDeEntrada hacer:
            
            Si (caracterActual = '{') entonces:
                pila.mete(caracterActual)
            Sino si (caracterActual = '}') entonces:
                // Si encontramos un cierre pero no hay nada que cerrar
                Si (pila.esVacia()) entonces:
                    Devolver FALSO
                Fin Si
                
                pila.saca()
            FinSi
            
        Fin Para

        // Si al final la pila está vacía, todos los corchetes se cerraron correctamente
        Si (pila.esVacia()) entonces:
            Devolver VERDADERO
        Sino:
            Devolver FALSO
        FinSi
    Fin Función

    PARTE 2;
        Analizar el orden del tiempo de ejecución del algoritmo propuesto:
    • Determinar la complejidad temporal en función de la longitud de la lista de
    entrada.
    • Considerar también el espacio adicional utilizado (pila).

    RESPUESTA: O(N) porque el algoritmo recorre cada elemento de la lista una unica vez, la cantidad de ciclos dependera de la cantidad de datos
    de la lista, las operaciones de la pila se ejecutan en tiempo constante O(1) y no afectan el resultado final.

    */
}
