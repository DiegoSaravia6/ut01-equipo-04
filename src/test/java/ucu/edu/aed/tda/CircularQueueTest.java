package ucu.edu.aed.tda;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CircularQueueTest {
    @Test
    public void testColaVacia() {
        CircularQueue<String> cola = new CircularQueue<>(5);
        assertTrue(cola.esVacio());
    }
    @Test(expected = IllegalStateException.class)
    public void testColaLlena() {
        CircularQueue<String> cola = new CircularQueue<>(2);
        cola.poneEnCola("A");
        cola.poneEnCola("B");
        cola.poneEnCola("C");
    }
    @Test
    public void testWrapAround() {
        CircularQueue<String> cola = new CircularQueue<>(3);
        cola.poneEnCola("A");
        cola.poneEnCola("B");
        cola.poneEnCola("C");
        cola.quitaDeCola();
        cola.quitaDeCola();
        cola.poneEnCola("D");
        cola.poneEnCola("E");
        assertEquals("C", cola.quitaDeCola());
        assertEquals("D", cola.quitaDeCola());
        assertEquals("E", cola.quitaDeCola());
    }
}