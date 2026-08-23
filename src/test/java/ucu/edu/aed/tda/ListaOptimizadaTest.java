package ucu.edu.aed.tda;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ListaOptimizadaTest extends TestCase {

    public ListaOptimizadaTest(String nombre) {
        super(nombre);
    }

    public static Test suite() {
        return new TestSuite(ListaOptimizadaTest.class);
    }

    // =========================================================
    // LISTA VACÍA
    // =========================================================

    public void testListaNueva() {

        ListaOptimizada<Integer> lista =
                new ListaOptimizada<>();

        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
    }

    // =========================================================
    // AGREGAR
    // =========================================================

    public void testAgregarUnElemento() {

        ListaOptimizada<Integer> lista =
                new ListaOptimizada<>();

        lista.agregar(10);

        assertFalse(lista.esVacio());
        assertEquals(1, lista.tamaño());
        assertEquals(
                Integer.valueOf(10),
                lista.obtener(0)
        );
    }

    public void testAgregarVariosElementos() {

        ListaOptimizada<Integer> lista =
                new ListaOptimizada<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(3, lista.tamaño());

        assertEquals(
                Integer.valueOf(10),
                lista.obtener(0)
        );

        assertEquals(
                Integer.valueOf(20),
                lista.obtener(1)
        );

        assertEquals(
                Integer.valueOf(30),
                lista.obtener(2)
        );
    }

    // =========================================================
    // AGREGAR POR ÍNDICE
    // =========================================================

    public void testAgregarAlPrincipio() {

        ListaOptimizada<Integer> lista =
                new ListaOptimizada<>();

        lista.agregar(20);
        lista.agregar(30);

        lista.agregar(0, 10);

        assertEquals(3, lista.tamaño());

        assertEquals(
                Integer.valueOf(10),
                lista.obtener(0)
        );

        assertEquals(
                Integer.valueOf(20),
                lista.obtener(1)
        );

        assertEquals(
                Integer.valueOf(30),
                lista.obtener(2)
        );
    }

    public void testAgregarEnMedio() {

        ListaOptimizada<Integer> lista =
                new ListaOptimizada<>();

        lista.agregar(10);
        lista.agregar(30);

        lista.agregar(1, 20);

        assertEquals(3, lista.tamaño());

        assertEquals(
                Integer.valueOf(10),
                lista.obtener(0)
        );

        assertEquals(
                Integer.valueOf(20),
                lista.obtener(1)
        );

        assertEquals(
                Integer.valueOf(30),
                lista.obtener(2)
        );
    }

    public void testAgregarAlFinalPorIndice() {

        ListaOptimizada<Integer> lista =
                new ListaOptimizada<>();

        lista.agregar(10);
        lista.agregar(20);

        lista.agregar(2, 30);

        assertEquals(3, lista.tamaño());

        assertEquals(
                Integer.valueOf(30),
                lista.obtener(2)
        );
    }

    // =========================================================
    // OBTENER
    // =========================================================

    public void testObtener() {

        ListaOptimizada<String> lista =
                new ListaOptimizada<>();

        lista.agregar("A");
        lista.agregar("B");
        lista.agregar("C");

        assertEquals("A", lista.obtener(0));
        assertEquals("B", lista.obtener(1));
        assertEquals("C", lista.obtener(2));
    }

    // =========================================================
    // REMOVER POR ÍNDICE
    // =========================================================

    public void testRemoverPrimero() {

        ListaOptimizada<Integer> lista =
                new ListaOptimizada<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        Integer eliminado = lista.remover(0);

        assertEquals(
                Integer.valueOf(10),
                eliminado
        );

        assertEquals(2, lista.tamaño());
        assertEquals(
                Integer.valueOf(20),
                lista.obtener(0)
        );
    }

    public void testRemoverMedio() {

        ListaOptimizada<Integer> lista =
                new ListaOptimizada<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        Integer eliminado = lista.remover(1);

        assertEquals(
                Integer.valueOf(20),
                eliminado
        );

        assertEquals(2, lista.tamaño());

        assertEquals(
                Integer.valueOf(10),
                lista.obtener(0)
        );

        assertEquals(
                Integer.valueOf(30),
                lista.obtener(1)
        );
    }

    public void testRemoverUltimo() {

        ListaOptimizada<Integer> lista =
                new ListaOptimizada<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        Integer eliminado = lista.remover(2);

        assertEquals(
                Integer.valueOf(30),
                eliminado
        );

        assertEquals(2, lista.tamaño());

        assertEquals(
                Integer.valueOf(20),
                lista.obtener(1)
        );
    }

    public void testRemoverUnicoElemento() {

        ListaOptimizada<Integer> lista =
                new ListaOptimizada<>();

        lista.agregar(10);

        Integer eliminado = lista.remover(0);

        assertEquals(
                Integer.valueOf(10),
                eliminado
        );

        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());

        // Comprobamos que se pueda volver a utilizar.
        lista.agregar(20);

        assertEquals(1, lista.tamaño());
        assertEquals(
                Integer.valueOf(20),
                lista.obtener(0)
        );
    }

    // =========================================================
    // REMOVER POR ELEMENTO
    // =========================================================

    public void testRemoverElemento() {

        ListaOptimizada<String> lista =
                new ListaOptimizada<>();

        lista.agregar("A");
        lista.agregar("B");
        lista.agregar("C");

        assertTrue(lista.remover("B"));

        assertEquals(2, lista.tamaño());
        assertFalse(lista.contiene("B"));
        assertTrue(lista.contiene("A"));
        assertTrue(lista.contiene("C"));
    }

    public void testRemoverElementoInexistente() {

        ListaOptimizada<String> lista =
                new ListaOptimizada<>();

        lista.agregar("A");
        lista.agregar("B");

        assertFalse(lista.remover("C"));
        assertEquals(2, lista.tamaño());
    }

    // =========================================================
    // BÚSQUEDA
    // =========================================================

    public void testContiene() {

        ListaOptimizada<Integer> lista =
                new ListaOptimizada<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertTrue(lista.contiene(20));
        assertFalse(lista.contiene(40));
    }

    public void testIndiceDe() {

        ListaOptimizada<String> lista =
                new ListaOptimizada<>();

        lista.agregar("A");
        lista.agregar("B");
        lista.agregar("C");

        assertEquals(0, lista.indiceDe("A"));
        assertEquals(1, lista.indiceDe("B"));
        assertEquals(2, lista.indiceDe("C"));
        assertEquals(-1, lista.indiceDe("D"));
    }

    public void testBuscar() {

        ListaOptimizada<Integer> lista =
                new ListaOptimizada<>();

        lista.agregar(10);
        lista.agregar(25);
        lista.agregar(30);

        Integer resultado =
                lista.buscar(numero -> numero > 20);

        assertEquals(
                Integer.valueOf(25),
                resultado
        );
    }

    // =========================================================
    // ORDENAMIENTO
    // =========================================================

    public void testOrdenar() {

        ListaOptimizada<Integer> lista =
                new ListaOptimizada<>();

        lista.agregar(30);
        lista.agregar(10);
        lista.agregar(20);

        TDALista<Integer> ordenada =
                lista.ordenar(Integer::compareTo);

        assertEquals(
                Integer.valueOf(10),
                ordenada.obtener(0)
        );

        assertEquals(
                Integer.valueOf(20),
                ordenada.obtener(1)
        );

        assertEquals(
                Integer.valueOf(30),
                ordenada.obtener(2)
        );

        // La lista original no debería modificarse.
        assertEquals(
                Integer.valueOf(30),
                lista.obtener(0)
        );
    }

    // =========================================================
    // VACÍAR
    // =========================================================

    public void testVaciar() {

        ListaOptimizada<Integer> lista =
                new ListaOptimizada<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        lista.vaciar();

        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());

        // Comprobamos que pueda reutilizarse.
        lista.agregar(40);

        assertEquals(1, lista.tamaño());
        assertEquals(
                Integer.valueOf(40),
                lista.obtener(0)
        );
    }

    // =========================================================
    // CASOS BORDE
    // =========================================================

    public void testIndiceNegativo() {

        ListaOptimizada<Integer> lista =
                new ListaOptimizada<>();

        lista.agregar(10);

        try {
            lista.obtener(-1);
            fail("Se esperaba IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Comportamiento esperado.
        }
    }

    public void testIndiceFueraDeRango() {

        ListaOptimizada<Integer> lista =
                new ListaOptimizada<>();

        lista.agregar(10);

        try {
            lista.obtener(1);
            fail("Se esperaba IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Comportamiento esperado.
        }
    }

    public void testRemoverIndiceInvalido() {

        ListaOptimizada<Integer> lista =
                new ListaOptimizada<>();

        try {
            lista.remover(0);
            fail("Se esperaba IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Comportamiento esperado.
        }
    }

    public void testAgregarIndiceInvalido() {

        ListaOptimizada<Integer> lista =
                new ListaOptimizada<>();

        try {
            lista.agregar(1, 10);
            fail("Se esperaba IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Comportamiento esperado.
        }
    }
}