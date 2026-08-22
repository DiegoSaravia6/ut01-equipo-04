package ucu.edu.aed.tda;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class QueueTest {
    @Test
    public void testNewQueueIsEmpty() {
        Queue<String> queue = new Queue<>();
        assertTrue(queue.esVacio());
    }

    @Test
    public void testQueueNotEmptyAfterEnqueue() {
        Queue<String> queue = new Queue<>();
        queue.poneEnCola("A");
        assertFalse(queue.esVacio());
    }

    @Test
    public void testFifoOrder() {
        Queue<String> queue = new Queue<>();
        queue.poneEnCola("A");
        queue.poneEnCola("B");
        queue.poneEnCola("C");
        assertEquals("A", queue.quitaDeCola());
        assertEquals("B", queue.quitaDeCola());
        assertEquals("C", queue.quitaDeCola());
    }

    @Test
    public void testFrenteDoesNotRemove() {
        Queue<String> queue = new Queue<>();
        queue.poneEnCola("A");
        assertEquals("A", queue.frente());
        assertFalse(queue.esVacio());
    }

    @Test
    public void testSizeAfterOperations() {
        Queue<String> queue = new Queue<>();
        queue.poneEnCola("A");
        queue.poneEnCola("B");
        assertEquals(2, queue.tamaño());
        queue.quitaDeCola();
        assertEquals(1, queue.tamaño());
    }
}