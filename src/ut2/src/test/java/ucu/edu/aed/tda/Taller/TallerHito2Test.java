package ucu.edu.aed.tda.Taller;

import java.time.LocalDate;

import junit.framework.TestCase;
import ucu.edu.aed.Taller.OrdenTrabajo;
import ucu.edu.aed.Taller.Taller;
import ucu.edu.aed.Taller.Tallerista;
import ucu.edu.aed.Taller.Trabajo;
import ucu.edu.aed.Taller.EstadoTrabajo;
import ucu.edu.aed.Taller.EstadoAprobacion;
import ucu.edu.aed.Taller.Vehiculo;
import ucu.edu.aed.tda.ListaArreglo;

public class TallerHito2Test extends TestCase {

    private Vehiculo vehiculo(String patente) {
        return new Vehiculo(patente, "Marca", "Modelo", "Dueño");
    }

    private void prepararEstructura(Vehiculo v) {
        v.agregarParte("VEHICULO", "MOTOR", "Motor");
        v.agregarParte("MOTOR", "DIST", "Distribución");
        v.agregarParte("DIST", "CORREA", "Correa");
        v.agregarParte("CORREA", "TENSOR", "Tensor");
        v.agregarParte("VEHICULO", "TREN", "Tren delantero");
        v.agregarParte("TREN", "FRENOS", "Frenos delanteros");
    }

    public void testEstructuraVehiculoPuedeTenerProfundidadVariable() {
        Vehiculo v = vehiculo("ARB001");
        prepararEstructura(v);
        assertEquals("Tensor", v.buscarParte("TENSOR").getNombre());
        assertEquals(4, v.getEstructuraPartes().altura());
    }

    public void testTrabajoDerivadoBloqueaCierreDelPadre() {
        Taller taller = new Taller();
        Vehiculo v = vehiculo("RAM001");
        prepararEstructura(v);
        OrdenTrabajo orden = taller.registrarProblemaInformado(
                v, "Ruido en distribución", LocalDate.of(2026, 9, 1), null);

        Trabajo padre = taller.registrarTrabajoPrincipal(v, "Cambiar correa", "CORREA", 5000, true);
        Trabajo derivado = taller.registrarTrabajoDerivado(v, padre.getId(), "Cambiar tensor", "TENSOR", 2500);

        try {
            taller.finalizarTrabajo(v, padre.getId());
            fail("El padre no debería poder finalizar con un hijo pendiente");
        } catch (IllegalStateException e) {
            // esperado
        }

        assertEquals(EstadoAprobacion.PENDIENTE, derivado.getAprobacion());
        taller.aprobarTrabajo(v, derivado.getId());
        taller.iniciarTrabajo(v, derivado.getId());
        taller.finalizarTrabajo(v, derivado.getId());
        taller.iniciarTrabajo(v, padre.getId());
        taller.finalizarTrabajo(v, padre.getId());
        assertTrue(orden.estaCerrada());
    }

    public void testRamificacionDeMasDeDosNiveles() {
        Taller taller = new Taller();
        Vehiculo v = vehiculo("RAM002");
        prepararEstructura(v);
        taller.registrarProblemaInformado(v, "Problema complejo", LocalDate.of(2026, 9, 2), null);

        Trabajo a = taller.registrarTrabajoPrincipal(v, "A", "MOTOR", 100, true);
        Trabajo b = taller.registrarTrabajoDerivado(v, a.getId(), "B", "DIST", 200);
        taller.aprobarTrabajo(v, b.getId());
        Trabajo c = taller.registrarTrabajoDerivado(v, b.getId(), "C", "CORREA", 300);
        taller.aprobarTrabajo(v, c.getId());
        Trabajo d = taller.registrarTrabajoDerivado(v, c.getId(), "D", "TENSOR", 400);
        taller.aprobarTrabajo(v, d.getId());

        ListaArreglo<Trabajo> orden = taller.consultarOrdenPendiente(v);
        assertEquals("D", orden.obtener(0).getDescripcion());
        assertEquals("C", orden.obtener(1).getDescripcion());
        assertEquals("B", orden.obtener(2).getDescripcion());
        assertEquals("A", orden.obtener(3).getDescripcion());
    }

    public void testHermanosSeResuelvenLifo() {
        Taller taller = new Taller();
        Vehiculo v = vehiculo("LIFO01");
        prepararEstructura(v);
        taller.registrarProblemaInformado(v, "Problema", LocalDate.of(2026, 9, 2), null);
        Trabajo padre = taller.registrarTrabajoPrincipal(v, "Original", "MOTOR", 100, true);
        Trabajo primero = taller.registrarTrabajoDerivado(v, padre.getId(), "Falla 1", "DIST", 10);
        Trabajo segundo = taller.registrarTrabajoDerivado(v, padre.getId(), "Falla 2", "CORREA", 20);
        taller.aprobarTrabajo(v, primero.getId());
        taller.aprobarTrabajo(v, segundo.getId());

        ListaArreglo<Trabajo> pendientes = taller.consultarOrdenPendiente(v);
        assertSame(segundo, pendientes.obtener(0));
        assertSame(primero, pendientes.obtener(1));
        assertSame(padre, pendientes.obtener(2));
    }

    public void testRechazoCierraRamaYNoSumaAlCostoAutorizado() {
        Taller taller = new Taller();
        Vehiculo v = vehiculo("APR001");
        prepararEstructura(v);
        taller.registrarProblemaInformado(v, "Problema", LocalDate.of(2026, 9, 3), null);
        Trabajo padre = taller.registrarTrabajoPrincipal(v, "Trabajo base", "MOTOR", 1000, true);
        Trabajo extra = taller.registrarTrabajoDerivado(v, padre.getId(), "Trabajo opcional", "DIST", 500);

        assertEquals(1500.0, taller.costoOrden(v), 0.001);
        taller.rechazarTrabajo(v, extra.getId());
        assertEquals(1000.0, taller.costoAutorizadoOrden(v), 0.001);
        assertEquals(EstadoTrabajo.RECHAZADO, extra.getEstado());

        taller.iniciarTrabajo(v, padre.getId());
        taller.finalizarTrabajo(v, padre.getId());
        assertTrue(v.getOrdenActual().estaCerrada());
    }

    public void testSuspenderUnaRamaDejaDisponibleOtra() {
        Taller taller = new Taller();
        Vehiculo v = vehiculo("REP201");
        prepararEstructura(v);
        Tallerista t = new Tallerista("Ana");
        taller.registrarTallerista(t);
        taller.registrarProblemaInformado(v, "Dos problemas", LocalDate.of(2026, 9, 4), null);
        Trabajo a = taller.registrarTrabajoPrincipal(v, "Motor", "MOTOR", 100, true);
        Trabajo b = taller.registrarTrabajoPrincipal(v, "Frenos", "FRENOS", 100, true);
        taller.atenderSiguiente();

        taller.suspenderTrabajoPorRepuesto(v, a.getId());

        assertEquals(1, taller.cantidadVehiculosEnTrabajo());
        assertEquals(0, taller.cantidadEsperandoRepuestos());
        assertFalse(t.estaDisponible());
        assertEquals(EstadoTrabajo.ESPERANDO_REPUESTO, a.getEstado());
        assertEquals(EstadoTrabajo.PENDIENTE, b.getEstado());
    }

    public void testSuspenderUnicaRamaLiberaTallerista() {
        Taller taller = new Taller();
        Vehiculo v = vehiculo("REP202");
        prepararEstructura(v);
        Tallerista t = new Tallerista("Ana");
        taller.registrarTallerista(t);
        taller.registrarProblemaInformado(v, "Un problema", LocalDate.of(2026, 9, 4), null);
        Trabajo a = taller.registrarTrabajoPrincipal(v, "Motor", "MOTOR", 100, true);
        taller.atenderSiguiente();

        taller.suspenderTrabajoPorRepuesto(v, a.getId());

        assertEquals(0, taller.cantidadVehiculosEnTrabajo());
        assertEquals(1, taller.cantidadEsperandoRepuestos());
        assertTrue(t.estaDisponible());
    }

    public void testInformaAncestrosBloqueados() {
        Taller taller = new Taller();
        Vehiculo v = vehiculo("BLQ001");
        prepararEstructura(v);
        taller.registrarProblemaInformado(v, "Problema", LocalDate.of(2026, 9, 5), null);
        Trabajo a = taller.registrarTrabajoPrincipal(v, "A", "MOTOR", 1, true);
        Trabajo b = taller.registrarTrabajoDerivado(v, a.getId(), "B", "DIST", 1);
        taller.aprobarTrabajo(v, b.getId());
        Trabajo c = taller.registrarTrabajoDerivado(v, b.getId(), "C", "CORREA", 1);

        ListaArreglo<Trabajo> bloqueados = taller.consultarTrabajosBloqueados(v, c.getId());
        assertSame(b, bloqueados.obtener(0));
        assertSame(a, bloqueados.obtener(1));
    }

    public void testBuscarVehiculoPorPatente() {
        Taller taller = new Taller();
        Vehiculo a = vehiculo("AAA111");
        Vehiculo b = vehiculo("ZZZ999");
        taller.registrarProblemaInformado(a, "A");
        taller.registrarProblemaInformado(b, "B");
        assertSame(b, taller.buscarVehiculoPorPatente("zzz999"));
        assertNull(taller.buscarVehiculoPorPatente("NOEXISTE"));
    }

    public void testNoPermitePatenteDuplicadaEnObjetosDistintos() {
        Taller taller = new Taller();
        taller.registrarProblemaInformado(vehiculo("DUP999"), "A");
        try {
            taller.registrarProblemaInformado(vehiculo("dup999"), "B");
            fail("Se esperaba duplicado de patente");
        } catch (IllegalStateException e) {
            // esperado
        }
    }

    public void testBuscarOrdenesEnRangoSinRecorrerTodo() {
        Taller taller = new Taller();
        taller.registrarProblemaInformado(vehiculo("F001"), "A", LocalDate.of(2026, 8, 1), null);
        taller.registrarProblemaInformado(vehiculo("F002"), "B", LocalDate.of(2026, 8, 15), null);
        taller.registrarProblemaInformado(vehiculo("F003"), "C", LocalDate.of(2026, 9, 1), null);

        ListaArreglo<OrdenTrabajo> r = taller.buscarOrdenesEntre(
                LocalDate.of(2026, 8, 10), LocalDate.of(2026, 8, 31));
        assertEquals(1, r.tamaño());
        assertEquals(LocalDate.of(2026, 8, 15), r.obtener(0).getFechaIngreso());
    }

    public void testFechaComprometidaTienePrioridadSobreSinFecha() {
        Taller taller = new Taller();
        Vehiculo sinFecha = vehiculo("PRI001");
        Vehiculo conFecha = vehiculo("PRI002");
        taller.registrarProblemaInformado(sinFecha, "A", LocalDate.of(2026, 9, 1), null);
        taller.registrarProblemaInformado(conFecha, "B", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 20));
        assertSame(conFecha, taller.proximoVehiculo());
    }

    public void testEntreFechasComprometidasVaPrimeroLaMasCercana() {
        Taller taller = new Taller();
        Vehiculo tarde = vehiculo("PRI003");
        Vehiculo temprano = vehiculo("PRI004");
        taller.registrarProblemaInformado(tarde, "A", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 20));
        taller.registrarProblemaInformado(temprano, "B", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 10));
        assertSame(temprano, taller.proximoVehiculo());
    }

    public void testSinFechaMantieneFIFO() {
        Taller taller = new Taller();
        Vehiculo a = vehiculo("PRI005");
        Vehiculo b = vehiculo("PRI006");
        taller.registrarProblemaInformado(a, "A");
        taller.registrarProblemaInformado(b, "B");
        assertSame(a, taller.proximoVehiculo());
    }

    public void testReprogramarVehiculoCambiaPrioridad() {
        Taller taller = new Taller();
        Vehiculo a = vehiculo("PRI007");
        Vehiculo b = vehiculo("PRI008");
        taller.registrarProblemaInformado(a, "A", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 20));
        taller.registrarProblemaInformado(b, "B", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 25));
        assertSame(a, taller.proximoVehiculo());
        taller.reprogramarVehiculo("PRI008", LocalDate.of(2026, 9, 5));
        assertSame(b, taller.proximoVehiculo());
    }

    public void testNoFinalizaVehiculoConRamaPendiente() {
        Taller taller = new Taller();
        Vehiculo v = vehiculo("FIN201");
        prepararEstructura(v);
        taller.registrarTallerista(new Tallerista("Ana"));
        taller.registrarProblemaInformado(v, "Problema");
        taller.registrarTrabajoPrincipal(v, "Trabajo", "MOTOR", 100, true);
        taller.atenderSiguiente();
        try {
            taller.finalizarVehiculo(v);
            fail("No debería finalizar con una rama pendiente");
        } catch (IllegalStateException e) {
            // esperado
        }
    }
}
