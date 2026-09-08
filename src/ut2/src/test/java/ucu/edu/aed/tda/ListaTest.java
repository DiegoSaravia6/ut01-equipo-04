package ucu.edu.aed.tda;

import java.util.Comparator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ListaTest extends TestCase {

    public ListaTest(String nombre) {
        super(nombre);
    }

    public static Test suite() {
        return new TestSuite(ListaTest.class);
    }

    private Lista<Integer> crearLista(Integer... elementos) {
        Lista<Integer> lista = new Lista<>();

        for (Integer elemento : elementos) {
            lista.agregar(elemento);
        }

        return lista;
    }

    // =========================================================
    // AGREGAR
    // =========================================================

    public void testAgregarAlFinal() {
        Lista<Integer> lista = new Lista<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(3, lista.tamaño());
        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(20, lista.obtener(1).intValue());
        assertEquals(30, lista.obtener(2).intValue());
    }

    public void testAgregarPorIndiceAlPrincipio() {
        Lista<Integer> lista = crearLista(20, 30);

        lista.agregar(0, 10);

        assertEquals(3, lista.tamaño());
        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(20, lista.obtener(1).intValue());
        assertEquals(30, lista.obtener(2).intValue());
    }

    public void testAgregarPorIndiceEnMedio() {
        Lista<Integer> lista = crearLista(10, 30);

        lista.agregar(1, 20);

        assertEquals(3, lista.tamaño());
        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(20, lista.obtener(1).intValue());
        assertEquals(30, lista.obtener(2).intValue());
    }

    public void testAgregarPorIndiceAlFinal() {
        Lista<Integer> lista = crearLista(10, 20);

        lista.agregar(2, 30);

        assertEquals(3, lista.tamaño());
        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(20, lista.obtener(1).intValue());
        assertEquals(30, lista.obtener(2).intValue());
    }

    public void testAgregarPorIndiceEnListaVacia() {
        Lista<Integer> lista = new Lista<>();

        lista.agregar(0, 10);

        assertEquals(1, lista.tamaño());
        assertEquals(10, lista.obtener(0).intValue());
    }

    public void testAgregarConIndiceNegativo() {
        Lista<Integer> lista = crearLista(10, 20);

        try {
            lista.agregar(-1, 30);
            fail("Se esperaba IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Correcto
        }
    }

    public void testAgregarConIndiceMayorAlTamaño() {
        Lista<Integer> lista = crearLista(10, 20);

        try {
            lista.agregar(3, 30);
            fail("Se esperaba IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Correcto
        }
    }

    // =========================================================
    // OBTENER
    // =========================================================

    public void testObtener() {
        Lista<Integer> lista = crearLista(10, 20, 30);

        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(20, lista.obtener(1).intValue());
        assertEquals(30, lista.obtener(2).intValue());
    }

    public void testObtenerIndiceNegativo() {
        Lista<Integer> lista = crearLista(10, 20);

        try {
            lista.obtener(-1);
            fail("Se esperaba IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Correcto
        }
    }

    public void testObtenerIndiceIgualAlTamaño() {
        Lista<Integer> lista = crearLista(10, 20);

        try {
            lista.obtener(2);
            fail("Se esperaba IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Correcto
        }
    }

    public void testObtenerListaVacia() {
        Lista<Integer> lista = new Lista<>();

        try {
            lista.obtener(0);
            fail("Se esperaba IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Correcto
        }
    }

    // =========================================================
    // REMOVER POR ÍNDICE
    // =========================================================

    public void testRemoverPorIndiceAlPrincipio() {
        Lista<Integer> lista = crearLista(10, 20, 30);

        int eliminado = lista.remover(0);

        assertEquals(10, eliminado);
        assertEquals(2, lista.tamaño());
        assertEquals(20, lista.obtener(0).intValue());
        assertEquals(30, lista.obtener(1).intValue());
    }

    public void testRemoverPorIndiceEnMedio() {
        Lista<Integer> lista = crearLista(10, 20, 30);

        int eliminado = lista.remover(1);

        assertEquals(20, eliminado);
        assertEquals(2, lista.tamaño());
        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(30, lista.obtener(1).intValue());
    }

    public void testRemoverPorIndiceAlFinal() {
        Lista<Integer> lista = crearLista(10, 20, 30);

        int eliminado = lista.remover(2);

        assertEquals(30, eliminado);
        assertEquals(2, lista.tamaño());
        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(20, lista.obtener(1).intValue());
    }

    public void testRemoverIndiceInvalido() {
        Lista<Integer> lista = crearLista(10, 20);

        try {
            lista.remover(2);
            fail("Se esperaba IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Correcto
        }
    }

    // =========================================================
    // REMOVER POR ELEMENTO
    // =========================================================

    public void testRemoverElementoExistente() {
        Lista<Integer> lista = crearLista(10, 20, 30);

        assertTrue(lista.remover(Integer.valueOf(20)));

        assertEquals(2, lista.tamaño());
        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(30, lista.obtener(1).intValue());
    }

    public void testRemoverElementoInexistente() {
        Lista<Integer> lista = crearLista(10, 20, 30);

        assertFalse(lista.remover(Integer.valueOf(40)));

        assertEquals(3, lista.tamaño());
    }

    public void testRemoverPrimeraOcurrencia() {
        Lista<Integer> lista = crearLista(10, 20, 10, 30);

        assertTrue(lista.remover(Integer.valueOf(10)));

        assertEquals(3, lista.tamaño());
        assertEquals(20, lista.obtener(0).intValue());
        assertEquals(10, lista.obtener(1).intValue());
        assertEquals(30, lista.obtener(2).intValue());
    }

    // =========================================================
    // CONTIENE
    // =========================================================

    public void testContieneElementoExistente() {
        Lista<Integer> lista = crearLista(10, 20, 30);

        assertTrue(lista.contiene(20));
    }

    public void testNoContieneElementoInexistente() {
        Lista<Integer> lista = crearLista(10, 20, 30);

        assertFalse(lista.contiene(40));
    }

    // =========================================================
    // INDICE DE
    // =========================================================

    public void testIndiceDeElementoExistente() {
        Lista<Integer> lista = crearLista(10, 20, 30);

        assertEquals(1, lista.indiceDe(20));
    }

    public void testIndiceDeElementoInexistente() {
        Lista<Integer> lista = crearLista(10, 20, 30);

        assertEquals(-1, lista.indiceDe(40));
    }

    public void testIndiceDePrimeraOcurrencia() {
        Lista<Integer> lista = crearLista(10, 20, 10, 30);

        assertEquals(0, lista.indiceDe(10));
    }

    // =========================================================
    // BUSCAR
    // =========================================================

    public void testBuscarElemento() {
        Lista<Integer> lista = crearLista(10, 15, 20, 25);

        Integer resultado = lista.buscar(
                numero -> numero > 18
        );

        assertEquals(20, resultado.intValue());
    }

    public void testBuscarRetornaPrimeraCoincidencia() {
        Lista<Integer> lista = crearLista(10, 20, 30, 40);

        Integer resultado = lista.buscar(
                numero -> numero % 20 == 0
        );

        assertEquals(20, resultado.intValue());
    }

    public void testBuscarSinCoincidencia() {
        Lista<Integer> lista = crearLista(10, 20, 30);

        Integer resultado = lista.buscar(
                numero -> numero > 100
        );

        assertNull(resultado);
    }

    // =========================================================
    // ORDENAR
    // =========================================================

    public void testOrdenarAscendente() {
        Lista<Integer> lista = crearLista(5, 1, 4, 2, 3);

        TDALista<Integer> resultado =
                lista.ordenar(Comparator.naturalOrder());

        assertEquals(5, resultado.tamaño());
        assertEquals(1, resultado.obtener(0).intValue());
        assertEquals(2, resultado.obtener(1).intValue());
        assertEquals(3, resultado.obtener(2).intValue());
        assertEquals(4, resultado.obtener(3).intValue());
        assertEquals(5, resultado.obtener(4).intValue());
    }

    public void testOrdenarDescendente() {
        Lista<Integer> lista = crearLista(1, 2, 3, 4, 5);

        TDALista<Integer> resultado =
                lista.ordenar(Comparator.reverseOrder());

        assertEquals(5, resultado.obtener(0).intValue());
        assertEquals(4, resultado.obtener(1).intValue());
        assertEquals(3, resultado.obtener(2).intValue());
        assertEquals(2, resultado.obtener(3).intValue());
        assertEquals(1, resultado.obtener(4).intValue());
    }

    public void testOrdenarNoModificaOriginal() {
        Lista<Integer> lista = crearLista(3, 1, 2);

        TDALista<Integer> resultado =
                lista.ordenar(Comparator.naturalOrder());

        // Original
        assertEquals(3, lista.obtener(0).intValue());
        assertEquals(1, lista.obtener(1).intValue());
        assertEquals(2, lista.obtener(2).intValue());

        // Resultado
        assertEquals(1, resultado.obtener(0).intValue());
        assertEquals(2, resultado.obtener(1).intValue());
        assertEquals(3, resultado.obtener(2).intValue());
    }

    // =========================================================
    // TAMAÑO Y VACÍO
    // =========================================================

    public void testListaNuevaEstaVacia() {
        Lista<Integer> lista = new Lista<>();

        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
    }

    public void testListaDejaDeEstarVaciaAlAgregar() {
        Lista<Integer> lista = new Lista<>();

        lista.agregar(10);

        assertFalse(lista.esVacio());
        assertEquals(1, lista.tamaño());
    }

    public void testTamañoSeActualizaAlAgregarYRemover() {
        Lista<Integer> lista = new Lista<>();

        assertEquals(0, lista.tamaño());

        lista.agregar(10);
        assertEquals(1, lista.tamaño());

        lista.agregar(20);
        assertEquals(2, lista.tamaño());

        lista.remover(0);
        assertEquals(1, lista.tamaño());

        lista.remover(0);
        assertEquals(0, lista.tamaño());
    }

    // =========================================================
    // VACIAR
    // =========================================================

    public void testVaciar() {
        Lista<Integer> lista = crearLista(10, 20, 30);

        lista.vaciar();

        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
    }

    public void testAgregarDespuesDeVaciar() {
        Lista<Integer> lista = crearLista(10, 20);

        lista.vaciar();
        lista.agregar(30);

        assertEquals(1, lista.tamaño());
        assertEquals(30, lista.obtener(0).intValue());
    }
}