package ucu.edu.aed.tda;

import junit.framework.TestCase;

/** Evidencia explícita de árbol degenerado vs AVL balanceado. */
public class ComparacionBalanceTest extends TestCase {
    public void testMismaSecuenciaProduceAlturasMuyDistintas() {
        ArbolBinarioBusqueda<Integer> abb = new ArbolBinarioBusqueda<>();
        ArbolAVL<Integer> avl = new ArbolAVL<>();
        for (int i = 1; i <= 1000; i++) {
            abb.insertar(i);
            avl.insertar(i);
        }
        assertEquals(999, abb.altura());
        assertTrue(avl.altura() < 20);
        assertTrue(avl.estaBalanceado());
        assertEquals(Integer.valueOf(1000), abb.buscar(1000));
        assertEquals(Integer.valueOf(1000), avl.buscar(1000));
    }
}
