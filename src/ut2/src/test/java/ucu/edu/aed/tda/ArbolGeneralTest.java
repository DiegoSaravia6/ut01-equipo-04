package ucu.edu.aed.tda;

import junit.framework.TestCase;

public class ArbolGeneralTest extends TestCase {
    public void testNivelesVariables() {
        ArbolGeneral<String> a = new ArbolGeneral<>("Vehiculo");
        ElementoArbolGeneral<String> motor = a.agregarHijo(a.obtenerRaiz(), "Motor");
        ElementoArbolGeneral<String> distribucion = a.agregarHijo(motor, "Distribucion");
        ElementoArbolGeneral<String> correa = a.agregarHijo(distribucion, "Correa");
        a.agregarHijo(correa, "Tensor");
        a.agregarHijo(a.obtenerRaiz(), "Tren delantero");
        assertEquals(4, a.altura());
        assertEquals(6, a.cantidadNodos());
    }

    public void testRecorridoPorNiveles() {
        ArbolGeneral<String> a = new ArbolGeneral<>("A");
        ElementoArbolGeneral<String> b = a.agregarHijo(a.obtenerRaiz(), "B");
        a.agregarHijo(a.obtenerRaiz(), "C");
        a.agregarHijo(b, "D");
        ListaArreglo<String> r = new ListaArreglo<>();
        a.porNiveles(r::agregar);
        assertEquals("A", r.obtener(0));
        assertEquals("B", r.obtener(1));
        assertEquals("C", r.obtener(2));
        assertEquals("D", r.obtener(3));
    }

    public void testEliminarSubarbol() {
        ArbolGeneral<String> a = new ArbolGeneral<>("A");
        ElementoArbolGeneral<String> b = a.agregarHijo(a.obtenerRaiz(), "B");
        a.agregarHijo(b, "D");
        a.agregarHijo(a.obtenerRaiz(), "C");
        assertTrue(a.eliminarSubarbol(x -> x.equals("B")));
        assertEquals(2, a.cantidadNodos());
        assertNull(a.buscar(x -> x.equals("D")));
    }

    public void testAncestros() {
        ArbolGeneral<String> a = new ArbolGeneral<>("A");
        ElementoArbolGeneral<String> b = a.agregarHijo(a.obtenerRaiz(), "B");
        ElementoArbolGeneral<String> c = a.agregarHijo(b, "C");
        ListaArreglo<String> anc = a.ancestros(c);
        assertEquals("B", anc.obtener(0));
        assertEquals("A", anc.obtener(1));
    }
}
