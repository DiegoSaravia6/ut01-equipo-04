package Ej18;

import junit.framework.TestCase;

public class listaEnlazadaTest extends TestCase {

    // Caso 1: quitar un elemento que existe
    public void testQuitarElementoExistente() {

        listaEnlazada<String> lista = new listaEnlazada<>();

        lista.insertar("A");
        lista.insertar("B");
        lista.insertar("C");

        String quitado = lista.quitar(
            dato -> dato.equals("B")
        );

        assertEquals("B", quitado);
        assertEquals(2, lista.cantidad());
        assertNull(lista.buscar(dato -> dato.equals("B")));
    }


    // Caso 2: quitar un elemento que NO existe
    public void testQuitarElementoInexistente() {

        listaEnlazada<String> lista = new listaEnlazada<>();

        lista.insertar("A");
        lista.insertar("B");
        lista.insertar("C");

        String quitado = lista.quitar(
            dato -> dato.equals("X")
        );

        assertNull(quitado);
        assertEquals(3, lista.cantidad());
    }


    // Caso 3: intentar quitar de una lista vacía
    public void testQuitarListaVacia() {

        listaEnlazada<String> lista = new listaEnlazada<>();

        String quitado = lista.quitar(
            dato -> dato.equals("A")
        );

        assertNull(quitado);
        assertTrue(lista.esVacia());
        assertEquals(0, lista.cantidad());
    }
}