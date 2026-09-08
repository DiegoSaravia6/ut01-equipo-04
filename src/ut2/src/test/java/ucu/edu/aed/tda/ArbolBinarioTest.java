package ucu.edu.aed.tda;

import junit.framework.TestCase;

public class ArbolBinarioTest extends TestCase {
    public void testVacio() {
        ArbolBinario<Integer> a = new ArbolBinario<>();
        assertTrue(a.esVacio());
        assertEquals(-1, a.altura());
    }

    public void testInsercionPorNivelesYRecorridos() {
        ArbolBinario<Integer> a = new ArbolBinario<>();
        for (int i = 1; i <= 7; i++) assertTrue(a.insertar(i));
        ListaArreglo<Integer> niveles = new ListaArreglo<>();
        a.porNiveles(niveles::agregar);
        for (int i = 0; i < 7; i++) assertEquals(i + 1, niveles.obtener(i).intValue());
        assertEquals(2, a.altura());
        assertEquals(4, a.cantidadHojas());
    }

    public void testNoDuplica() {
        ArbolBinario<String> a = new ArbolBinario<>();
        assertTrue(a.insertar("A"));
        assertFalse(a.insertar("A"));
    }

    public void testEliminar() {
        ArbolBinario<Integer> a = new ArbolBinario<>();
        for (int i = 1; i <= 5; i++) a.insertar(i);
        assertTrue(a.eliminar(2));
        assertFalse(a.contiene(2));
        assertEquals(4, a.cantidadNodos());
        assertFalse(a.eliminar(99));
    }
}
