package ucu.edu.aed.tda;

import junit.framework.TestCase;

public class ArbolAVLTest extends TestCase {
    public void testRotacionLL() {
        ArbolAVL<Integer> a = new ArbolAVL<>();
        a.insertar(30); a.insertar(20); a.insertar(10);
        assertEquals(Integer.valueOf(20), a.obtenerRaizDato());
        assertTrue(a.estaBalanceado());
    }

    public void testRotacionRR() {
        ArbolAVL<Integer> a = new ArbolAVL<>();
        a.insertar(10); a.insertar(20); a.insertar(30);
        assertEquals(Integer.valueOf(20), a.obtenerRaizDato());
    }

    public void testRotacionLR() {
        ArbolAVL<Integer> a = new ArbolAVL<>();
        a.insertar(30); a.insertar(10); a.insertar(20);
        assertEquals(Integer.valueOf(20), a.obtenerRaizDato());
    }

    public void testRotacionRL() {
        ArbolAVL<Integer> a = new ArbolAVL<>();
        a.insertar(10); a.insertar(30); a.insertar(20);
        assertEquals(Integer.valueOf(20), a.obtenerRaizDato());
    }

    public void testEliminarMantieneBalance() {
        ArbolAVL<Integer> a = new ArbolAVL<>();
        for (int i = 1; i <= 20; i++) a.insertar(i);
        assertTrue(a.eliminar(7));
        assertTrue(a.eliminar(8));
        assertTrue(a.eliminar(9));
        assertTrue(a.estaBalanceado());
        assertNull(a.buscar(7));
    }

    public void testRango() {
        ArbolAVL<Integer> a = new ArbolAVL<>();
        for (int i = 1; i <= 20; i++) a.insertar(i);
        ListaArreglo<Integer> r = a.listarRango(7, 11);
        assertEquals(5, r.tamaño());
        assertEquals(Integer.valueOf(7), r.obtener(0));
        assertEquals(Integer.valueOf(11), r.obtener(4));
    }
}
