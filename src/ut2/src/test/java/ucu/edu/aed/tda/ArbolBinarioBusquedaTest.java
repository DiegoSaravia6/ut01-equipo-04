package ucu.edu.aed.tda;

import junit.framework.TestCase;

public class ArbolBinarioBusquedaTest extends TestCase {
    private ArbolBinarioBusqueda<Integer> arbol() {
        ArbolBinarioBusqueda<Integer> a = new ArbolBinarioBusqueda<>();
        int[] valores = {50, 30, 70, 20, 40, 60, 80};
        for (int v : valores) a.insertar(v);
        return a;
    }

    public void testVacio() {
        ArbolBinarioBusqueda<Integer> a = new ArbolBinarioBusqueda<>();
        assertTrue(a.esVacio());
        assertNull(a.buscar(10));
    }

    public void testInsertarBuscarYNoDuplicar() {
        ArbolBinarioBusqueda<Integer> a = arbol();
        assertEquals(7, a.cantidadNodos());
        assertEquals(Integer.valueOf(60), a.buscar(60));
        assertNull(a.buscar(99));
        assertFalse(a.insertar(50));
    }

    public void testInOrderQuedaOrdenado() {
        ArbolBinarioBusqueda<Integer> a = arbol();
        ListaArreglo<Integer> r = new ListaArreglo<>();
        a.inOrder(r::agregar);
        int[] esperado = {20,30,40,50,60,70,80};
        for (int i = 0; i < esperado.length; i++) assertEquals(esperado[i], r.obtener(i).intValue());
    }

    public void testEliminarHoja() {
        ArbolBinarioBusqueda<Integer> a = arbol();
        assertTrue(a.eliminar(20));
        assertNull(a.buscar(20));
        assertEquals(6, a.cantidadNodos());
    }

    public void testEliminarNodoConUnHijo() {
        ArbolBinarioBusqueda<Integer> a = arbol();
        a.insertar(65);
        assertTrue(a.eliminar(60));
        assertEquals(Integer.valueOf(65), a.buscar(65));
        assertNull(a.buscar(60));
    }

    public void testEliminarNodoConDosHijos() {
        ArbolBinarioBusqueda<Integer> a = arbol();
        assertTrue(a.eliminar(70));
        assertNull(a.buscar(70));
        assertEquals(6, a.cantidadNodos());
    }

    public void testEliminarRaizConDosHijos() {
        ArbolBinarioBusqueda<Integer> a = arbol();
        assertTrue(a.eliminar(50));
        assertNull(a.buscar(50));
        assertEquals(6, a.cantidadNodos());
    }

    public void testDegenerado() {
        ArbolBinarioBusqueda<Integer> a = new ArbolBinarioBusqueda<>();
        for (int i = 1; i <= 10; i++) a.insertar(i);
        assertEquals(9, a.altura());
    }
}
