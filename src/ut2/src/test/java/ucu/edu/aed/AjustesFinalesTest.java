package ucu.edu.aed;

import java.time.LocalDate;
import junit.framework.TestCase;
import ucu.edu.aed.Taller.OrdenTrabajo;
import ucu.edu.aed.Taller.ParteVehiculo;
import ucu.edu.aed.Taller.Taller;
import ucu.edu.aed.Taller.Tallerista;
import ucu.edu.aed.Taller.Trabajo;
import ucu.edu.aed.Taller.Vehiculo;

public class AjustesFinalesTest extends TestCase {
    private Vehiculo vehiculo(String patente) {
        return new Vehiculo(patente, "Marca", "Modelo", "Dueño");
    }

    public void testTalleristaSeIdentificaPorIdUnico() {
        Taller taller = new Taller();
        taller.registrarTallerista(new Tallerista(101, "Ana"));
        assertEquals(101, taller.buscarTalleristaPorId(101).getId());
        assertEquals("Ana", taller.buscarTalleristaPorId(101).getNombre());
        try {
            taller.registrarTallerista(new Tallerista(101, "Otra persona"));
            fail("Debió rechazar ID duplicado");
        } catch (IllegalStateException expected) { }
    }

    public void testNombresDuplicadosSonValidosSiIdEsDistinto() {
        Taller taller = new Taller();
        taller.registrarTallerista(new Tallerista(101, "Ana"));
        taller.registrarTallerista(new Tallerista(102, "Ana"));
        assertEquals(2, taller.cantidadTalleristas());
    }

    public void testReprogramacionNoPuedeSerAnteriorAlIngreso() {
        Taller taller = new Taller();
        Vehiculo v = vehiculo("ABC123");
        taller.registrarProblemaInformado(v, "Ruido", LocalDate.of(2026, 9, 9), LocalDate.of(2026, 9, 15));
        try {
            taller.reprogramarVehiculo("ABC123", LocalDate.of(2026, 9, 8));
            fail("Debió rechazar fecha anterior al ingreso");
        } catch (IllegalArgumentException expected) { }
    }

    public void testNoSePuedeRechazarTrabajoEnProgreso() {
        OrdenTrabajo orden = new OrdenTrabajo(1, LocalDate.of(2026, 9, 9));
        Trabajo t = orden.agregarTrabajoPrincipal("Trabajo", new ParteVehiculo("P", "Parte"), 10, true);
        orden.iniciarTrabajo(t.getId());
        try {
            orden.rechazarTrabajo(t.getId());
            fail("Debió impedir rechazo en progreso");
        } catch (IllegalStateException expected) { }
    }

    public void testNoSePuedeRechazarTrabajoTerminado() {
        OrdenTrabajo orden = new OrdenTrabajo(1, LocalDate.of(2026, 9, 9));
        Trabajo t = orden.agregarTrabajoPrincipal("Trabajo", new ParteVehiculo("P", "Parte"), 10, true);
        orden.iniciarTrabajo(t.getId());
        orden.finalizarTrabajo(t.getId());
        try {
            orden.rechazarTrabajo(t.getId());
            fail("Debió impedir rechazo terminado");
        } catch (IllegalStateException expected) { }
    }
}
