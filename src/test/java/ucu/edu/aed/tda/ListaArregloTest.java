package ucu.edu.aed.tda;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ListaArregloTest extends TestCase {

    public ListaArregloTest(String nombre) {
        super(nombre);
    }

    public static Test suite() {
        return new TestSuite(ListaArregloTest.class);
    }

    // =========================================================
    // LISTA NUEVA
    // =========================================================

    public void testListaNuevaEstaVacia() {
        ListaArreglo<Integer> lista = new ListaArreglo<>();

        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
    }

    // =========================================================
    // AGREGAR
    // =========================================================

    public void testAgregar() {
        ListaArreglo<Integer> lista = new ListaArreglo<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(3, lista.tamaño());
        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(20, lista.obtener(1).intValue());
        assertEquals(30, lista.obtener(2).intValue());
    }

    public void testAgregarPorIndice() {
        ListaArreglo<Integer> lista = new ListaArreglo<>();

        lista.agregar(10);
        lista.agregar(30);

        lista.agregar(1, 20);

        assertEquals(3, lista.tamaño());
        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(20, lista.obtener(1).intValue());
        assertEquals(30, lista.obtener(2).intValue());
    }

    public void testAgregarAlPrincipio() {
        ListaArreglo<Integer> lista = new ListaArreglo<>();

        lista.agregar(20);
        lista.agregar(30);

        lista.agregar(0, 10);

        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(20, lista.obtener(1).intValue());
        assertEquals(30, lista.obtener(2).intValue());
    }

    public void testAgregarAlFinalPorIndice() {
        ListaArreglo<Integer> lista = new ListaArreglo<>();

        lista.agregar(10);
        lista.agregar(20);

        lista.agregar(2, 30);

        assertEquals(3, lista.tamaño());
        assertEquals(30, lista.obtener(2).intValue());
    }

    // =========================================================
    // CRECIMIENTO DEL ARRAY
    // =========================================================

    public void testCreceCuandoSeLlena() {
        ListaArreglo<Integer> lista = new ListaArreglo<>();

        for (int i = 0; i < 25; i++) {
            lista.agregar(i);
        }

        assertEquals(25, lista.tamaño());

        for (int i = 0; i < 25; i++) {
            assertEquals(i, lista.obtener(i).intValue());
        }
    }

    // =========================================================
    // REMOVER
    // =========================================================

    public void testRemoverPorIndice() {
        ListaArreglo<Integer> lista = new ListaArreglo<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(20, lista.remover(1).intValue());

        assertEquals(2, lista.tamaño());
        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(30, lista.obtener(1).intValue());
    }

    public void testRemoverPrimero() {
        ListaArreglo<Integer> lista = new ListaArreglo<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(10, lista.remover(0).intValue());

        assertEquals(20, lista.obtener(0).intValue());
        assertEquals(30, lista.obtener(1).intValue());
    }

    public void testRemoverUltimo() {
        ListaArreglo<Integer> lista = new ListaArreglo<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(30, lista.remover(2).intValue());

        assertEquals(2, lista.tamaño());
        assertEquals(20, lista.obtener(1).intValue());
    }

    public void testRemoverPorElemento() {
        ListaArreglo<Integer> lista = new ListaArreglo<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertTrue(lista.remover(Integer.valueOf(20)));

        assertEquals(2, lista.tamaño());
        assertFalse(lista.contiene(20));
    }

    public void testRemoverElementoInexistente() {
        ListaArreglo<Integer> lista = new ListaArreglo<>();

        lista.agregar(10);
        lista.agregar(20);

        assertFalse(lista.remover(Integer.valueOf(40)));

        assertEquals(2, lista.tamaño());
    }

    // =========================================================
    // BUSQUEDA
    // =========================================================

    public void testContiene() {
        ListaArreglo<Integer> lista = new ListaArreglo<>();

        lista.agregar(10);
        lista.agregar(20);

        assertTrue(lista.contiene(10));
        assertTrue(lista.contiene(20));
        assertFalse(lista.contiene(30));
    }

    public void testIndiceDe() {
        ListaArreglo<Integer> lista = new ListaArreglo<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(0, lista.indiceDe(10));
        assertEquals(1, lista.indiceDe(20));
        assertEquals(2, lista.indiceDe(30));
        assertEquals(-1, lista.indiceDe(40));
    }

    public void testBuscar() {
        ListaArreglo<Integer> lista = new ListaArreglo<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(
                20,
                lista.buscar(x -> x == 20).intValue()
        );
    }

    // =========================================================
    // ORDENAR
    // =========================================================

    public void testOrdenar() {
        ListaArreglo<Integer> lista = new ListaArreglo<>();

        lista.agregar(30);
        lista.agregar(10);
        lista.agregar(20);

        TDALista<Integer> ordenada =
                lista.ordenar(Integer::compareTo);

        assertEquals(10, ordenada.obtener(0).intValue());
        assertEquals(20, ordenada.obtener(1).intValue());
        assertEquals(30, ordenada.obtener(2).intValue());

        // La original no debería modificarse.
        assertEquals(30, lista.obtener(0).intValue());
    }

    // =========================================================
    // VACIAR
    // =========================================================

    public void testVaciar() {
        ListaArreglo<Integer> lista = new ListaArreglo<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        lista.vaciar();

        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
    }
}