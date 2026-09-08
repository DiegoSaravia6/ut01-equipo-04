package ucu.edu.aed.tda;

import java.util.Comparator;
import junit.framework.TestCase;

public class MonticuloBinarioTest extends TestCase {
    public void testVacio() {
        MonticuloBinario<Integer> h = new MonticuloBinario<Integer>((a, b) -> Integer.compare(a, b));
        assertTrue(h.esVacio());
    }

    public void testPrioridad() {
        MonticuloBinario<Integer> h = new MonticuloBinario<Integer>((a, b) -> Integer.compare(a, b));
        h.insertar(5); h.insertar(1); h.insertar(3); h.insertar(2);
        assertEquals(Integer.valueOf(1), h.primero());
        assertEquals(Integer.valueOf(1), h.extraerPrimero());
        assertEquals(Integer.valueOf(2), h.extraerPrimero());
        assertEquals(Integer.valueOf(3), h.extraerPrimero());
        assertEquals(Integer.valueOf(5), h.extraerPrimero());
    }

    public void testCrece() {
        MonticuloBinario<Integer> h = new MonticuloBinario<Integer>((a, b) -> Integer.compare(a, b));
        for (int i = 100; i >= 1; i--) h.insertar(i);
        assertEquals(100, h.tamaño());
        assertEquals(Integer.valueOf(1), h.primero());
    }

    public void testReordenarElementoMutable() {
        class Item { int prioridad; Item(int p) { prioridad = p; } }
        MonticuloBinario<Item> h = new MonticuloBinario<>((a,b) -> Integer.compare(a.prioridad,b.prioridad));
        Item a = new Item(5); Item b = new Item(10); Item c = new Item(20);
        h.insertar(a); h.insertar(b); h.insertar(c);
        c.prioridad = 1;
        assertTrue(h.reordenar(c));
        assertSame(c, h.primero());
    }
}
