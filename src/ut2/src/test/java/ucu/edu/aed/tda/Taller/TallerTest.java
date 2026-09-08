package ucu.edu.aed.tda.Taller;

import java.util.NoSuchElementException;

import ucu.edu.aed.Taller.Taller;
import ucu.edu.aed.Taller.Vehiculo;
import ucu.edu.aed.Taller.Tallerista;
import ucu.edu.aed.Taller.Reparacion;
import ucu.edu.aed.Taller.TipoIngreso;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
public class TallerTest extends TestCase {

    public TallerTest(String nombre) {
        super(nombre);
    }

    public static Test suite() {
        return new TestSuite(TallerTest.class);
    }

    // =========================================================
    // TALLER NUEVO
    // =========================================================

    public void testTallerNuevo() {
        Taller taller = new Taller();

        assertEquals(0, taller.cantidadVehiculosEnEspera());
        assertEquals(0, taller.cantidadVehiculosEnTrabajo());
        assertEquals(0, taller.cantidadTalleristas());
        assertEquals(0, taller.cantidadEsperandoRepuestos());
        assertEquals(0, taller.cantidadVehiculosProntos());
    }

    // =========================================================
    // REGISTRAR VEHÍCULOS
    // =========================================================

    public void testRegistrarProblemaInformado() {
        Taller taller = new Taller();

        Vehiculo vehiculo = new Vehiculo(
                "ABC123",
                "Toyota",
                "Corolla",
                "Juan"
        );

        taller.registrarProblemaInformado(
                vehiculo,
                "Ruido al doblar"
        );

        assertEquals(1, taller.cantidadVehiculosEnEspera());
        assertSame(vehiculo, taller.proximoVehiculo());
        assertEquals(
                TipoIngreso.PROBLEMA_INFORMADO,
                vehiculo.getTipoIngreso()
        );
        assertEquals("Ruido al doblar", vehiculo.getDetalleIngreso());
        assertFalse(vehiculo.tieneReparacionesPendientes());
    }

    public void testRegistrarMantenimientoPlanificado() {
        Taller taller = new Taller();

        Vehiculo vehiculo = new Vehiculo(
                "MNT123",
                "Honda",
                "Civic",
                "Laura"
        );

        taller.registrarMantenimientoPlanificado(
                vehiculo,
                "Cambio de aceite y filtro"
        );

        assertEquals(1, taller.cantidadVehiculosEnEspera());
        assertSame(vehiculo, taller.proximoVehiculo());
        assertEquals(
                TipoIngreso.MANTENIMIENTO_PLANIFICADO,
                vehiculo.getTipoIngreso()
        );
        assertEquals(
                "Cambio de aceite y filtro",
                vehiculo.getDetalleIngreso()
        );
        assertTrue(vehiculo.tieneReparacionesPendientes());
        assertEquals(
                "Cambio de aceite y filtro",
                taller.proximaReparacion(vehiculo).getDescripcion()
        );
    }

    public void testProblemaInformadoRequiereDiagnosticoAntesDeCrearReparacion() {
        Taller taller = new Taller();

        Vehiculo vehiculo = new Vehiculo(
                "PRB123",
                "Ford",
                "Focus",
                "Pedro"
        );

        taller.registrarProblemaInformado(
                vehiculo,
                "Vibra al frenar"
        );

        assertEquals(
                TipoIngreso.PROBLEMA_INFORMADO,
                vehiculo.getTipoIngreso()
        );
        assertEquals("Vibra al frenar", vehiculo.getDetalleIngreso());
        assertFalse(vehiculo.tieneReparacionesPendientes());

        Reparacion diagnosticada = new Reparacion(
                "Cambiar discos de freno",
                "Falla diagnosticada"
        );

        taller.agregarReparacion(vehiculo, diagnosticada);

        assertSame(diagnosticada, taller.proximaReparacion(vehiculo));
    }

    public void testNoSeRegistraDosVecesElMismoVehiculo() {
        Taller taller = new Taller();

        Vehiculo vehiculo = new Vehiculo(
                "DUP123",
                "Toyota",
                "Corolla",
                "Juan"
        );

        taller.registrarProblemaInformado(
                vehiculo,
                "Ruido en el motor"
        );

        try {
            taller.registrarProblemaInformado(
                    vehiculo,
                    "Otro problema"
            );
            fail("Se esperaba IllegalStateException");
        } catch (IllegalStateException e) {
            // comportamiento esperado
        }

        assertEquals(1, taller.cantidadVehiculosEnEspera());
    }

    public void testVehiculosRespetanOrdenDeLlegada() {
        Taller taller = new Taller();

        Vehiculo primero = new Vehiculo(
                "AAA111",
                "Toyota",
                "Corolla",
                "Juan"
        );

        Vehiculo segundo = new Vehiculo(
                "BBB222",
                "Ford",
                "Focus",
                "Pedro"
        );

        Vehiculo tercero = new Vehiculo(
                "CCC333",
                "Chevrolet",
                "Onix",
                "Ana"
        );

        taller.registrarMantenimientoPlanificado(
                primero,
                "Cambio de aceite"
        );
        taller.registrarProblemaInformado(
                segundo,
                "Ruido al frenar"
        );
        taller.registrarMantenimientoPlanificado(
                tercero,
                "Service de 20.000 km"
        );

        assertSame(primero, taller.proximoVehiculo());

        taller.registrarTallerista(
                new Tallerista("Carlos")
        );

        assertSame(primero, taller.atenderSiguiente());
        assertSame(segundo, taller.proximoVehiculo());
    }

    public void testProximoVehiculoConTallerVacio() {
        Taller taller = new Taller();

        try {
            taller.proximoVehiculo();
            fail("Se esperaba NoSuchElementException");
        } catch (NoSuchElementException e) {
            // comportamiento esperado
        }
    }

    // =========================================================
    // TALLERISTAS
    // =========================================================

    public void testRegistrarTallerista() {
        Taller taller = new Taller();

        Tallerista tallerista =
                new Tallerista("Carlos");

        taller.registrarTallerista(tallerista);

        assertEquals(1, taller.cantidadTalleristas());
        assertTrue(tallerista.estaDisponible());
    }

    public void testBuscarTalleristaDisponible() {
        Taller taller = new Taller();

        Tallerista primero =
                new Tallerista("Carlos");

        Tallerista segundo =
                new Tallerista("Pedro");

        taller.registrarTallerista(primero);
        taller.registrarTallerista(segundo);

        assertSame(
                primero,
                taller.buscarTalleristaDisponible()
        );
    }

    public void testAtenderSiguienteAsignaTallerista() {
        Taller taller = new Taller();

        Tallerista tallerista =
                new Tallerista("Carlos");

        Vehiculo vehiculo = new Vehiculo(
                "ABC123",
                "Toyota",
                "Corolla",
                "Juan"
        );

        taller.registrarTallerista(tallerista);
        taller.registrarProblemaInformado(vehiculo, "Problema informado");

        Vehiculo atendido =
                taller.atenderSiguiente();

        assertSame(vehiculo, atendido);
        assertSame(
                vehiculo,
                tallerista.getVehiculoActual()
        );

        assertEquals(
                0,
                taller.cantidadVehiculosEnEspera()
        );

        assertEquals(
                1,
                taller.cantidadVehiculosEnTrabajo()
        );
    }

    public void testNoSePuedeAtenderSinTallerista() {
        Taller taller = new Taller();

        Vehiculo vehiculo = new Vehiculo(
                "ABC123",
                "Toyota",
                "Corolla",
                "Juan"
        );

        taller.registrarProblemaInformado(vehiculo, "Problema informado");

        try {
            taller.atenderSiguiente();
            fail("Se esperaba IllegalStateException");
        } catch (IllegalStateException e) {
            // comportamiento esperado
        }
    }

    // =========================================================
    // REPARACIONES
    // =========================================================

    public void testAgregarReparacion() {
        Taller taller = new Taller();

        Vehiculo vehiculo = new Vehiculo(
                "ABC123",
                "Toyota",
                "Corolla",
                "Juan"
        );

        Reparacion reparacion =
                new Reparacion(
                        "Cambio de aceite",
                        "Mantenimiento"
                );

        taller.agregarReparacion(
                vehiculo,
                reparacion
        );

        assertSame(
                reparacion,
                taller.proximaReparacion(vehiculo)
        );
    }

    public void testReparacionesRespetanPila() {
        Taller taller = new Taller();

        Vehiculo vehiculo = new Vehiculo(
                "ABC123",
                "Toyota",
                "Corolla",
                "Juan"
        );

        Reparacion original =
                new Reparacion(
                        "Cambio de aceite",
                        "Mantenimiento"
                );

        Reparacion adicional =
                new Reparacion(
                        "Cambio de pastillas de freno",
                        "Falla adicional"
                );

        taller.agregarReparacion(
                vehiculo,
                original
        );

        taller.agregarReparacion(
                vehiculo,
                adicional
        );

        assertSame(
                adicional,
                taller.proximaReparacion(vehiculo)
        );

        assertSame(
                adicional,
                taller.realizarProximaReparacion(vehiculo)
        );

        assertSame(
                original,
                taller.proximaReparacion(vehiculo)
        );
    }

    public void testReparacionRealizadaQuedaEnHistorial() {
        Taller taller = new Taller();

        Vehiculo vehiculo = new Vehiculo(
                "ABC123",
                "Toyota",
                "Corolla",
                "Juan"
        );

        Reparacion reparacion =
                new Reparacion(
                        "Cambio de aceite",
                        "Mantenimiento"
                );

        taller.agregarReparacion(
                vehiculo,
                reparacion
        );

        taller.realizarProximaReparacion(vehiculo);

        assertEquals(
                1,
                vehiculo.cantidadReparacionesRealizadas()
        );

        assertSame(
                reparacion,
                vehiculo.getReparacionesRealizadas().obtener(0)
        );
    }

    // =========================================================
    // ESPERA POR REPUESTOS
    // =========================================================

    public void testEsperarRepuestos() {
        Taller taller = new Taller();

        Tallerista tallerista =
                new Tallerista("Carlos");

        Vehiculo vehiculo = new Vehiculo(
                "ABC123",
                "Toyota",
                "Corolla",
                "Juan"
        );

        taller.registrarTallerista(tallerista);
        taller.registrarProblemaInformado(vehiculo, "Problema informado");

        taller.atenderSiguiente();

        assertEquals(
                1,
                taller.cantidadVehiculosEnTrabajo()
        );

        taller.esperarRepuestos(vehiculo);

        assertEquals(
                0,
                taller.cantidadVehiculosEnTrabajo()
        );

        assertEquals(
                1,
                taller.cantidadEsperandoRepuestos()
        );

        assertTrue(
                tallerista.estaDisponible()
        );
    }

    public void testContinuarConRepuestos() {
        Taller taller = new Taller();

        Tallerista tallerista =
                new Tallerista("Carlos");

        Vehiculo vehiculo = new Vehiculo(
                "ABC123",
                "Toyota",
                "Corolla",
                "Juan"
        );

        taller.registrarTallerista(tallerista);
        taller.registrarProblemaInformado(vehiculo, "Problema informado");

        taller.atenderSiguiente();
        taller.esperarRepuestos(vehiculo);

        Vehiculo continuado =
                taller.continuarConRepuestos();

        assertSame(
                vehiculo,
                continuado
        );

        assertEquals(
                1,
                taller.cantidadVehiculosEnTrabajo()
        );

        assertEquals(
                0,
                taller.cantidadEsperandoRepuestos()
        );
    }

    public void testNoPuedeEnviarARepuestosVehiculoFueraDeTrabajo() {
        Taller taller = new Taller();

        Vehiculo vehiculo = new Vehiculo(
                "REP000",
                "Toyota",
                "Yaris",
                "Lucia"
        );

        taller.registrarProblemaInformado(
                vehiculo,
                "No arranca"
        );

        try {
            taller.esperarRepuestos(vehiculo);
            fail("Se esperaba IllegalStateException");
        } catch (IllegalStateException e) {
            // comportamiento esperado
        }

        assertEquals(1, taller.cantidadVehiculosEnEspera());
        assertEquals(0, taller.cantidadEsperandoRepuestos());
    }

    // =========================================================
    // FINALIZACIÓN
    // =========================================================

    public void testFinalizarVehiculo() {
        Taller taller = new Taller();

        Tallerista tallerista =
                new Tallerista("Carlos");

        Vehiculo vehiculo = new Vehiculo(
                "ABC123",
                "Toyota",
                "Corolla",
                "Juan"
        );

        taller.registrarTallerista(tallerista);
        taller.registrarProblemaInformado(vehiculo, "Problema informado");

        taller.atenderSiguiente();

        taller.finalizarVehiculo(vehiculo);

        assertEquals(
                0,
                taller.cantidadVehiculosEnTrabajo()
        );

        assertEquals(
                1,
                taller.cantidadVehiculosProntos()
        );

        assertTrue(
                taller.estaProntoParaRetirar(vehiculo)
        );

        assertTrue(
                tallerista.estaDisponible()
        );
    }

    public void testNoFinalizaConReparacionesPendientes() {
        Taller taller = new Taller();
        Tallerista tallerista = new Tallerista("Carlos");

        Vehiculo vehiculo = new Vehiculo(
                "PEN123",
                "Honda",
                "Civic",
                "Laura"
        );

        taller.registrarTallerista(tallerista);
        taller.registrarMantenimientoPlanificado(
                vehiculo,
                "Cambio de aceite"
        );
        taller.atenderSiguiente();

        try {
            taller.finalizarVehiculo(vehiculo);
            fail("Se esperaba IllegalStateException");
        } catch (IllegalStateException e) {
            // comportamiento esperado
        }

        assertEquals(1, taller.cantidadVehiculosEnTrabajo());
        assertEquals(0, taller.cantidadVehiculosProntos());
        assertFalse(tallerista.estaDisponible());
    }

    // =========================================================
    // FLUJO COMPLETO
    // =========================================================

    public void testFlujoCompletoDelVehiculo() {

        Taller taller = new Taller();

        Tallerista tallerista =
                new Tallerista("Carlos");

        Vehiculo vehiculo = new Vehiculo(
                "ABC123",
                "Toyota",
                "Corolla",
                "Juan"
        );

        taller.registrarTallerista(tallerista);

        // 1. El vehículo llega por un problema informado por el dueño.
        taller.registrarProblemaInformado(
                vehiculo,
                "Ruido al frenar"
        );

        assertEquals(
                1,
                taller.cantidadVehiculosEnEspera()
        );

        // 2. Es atendido.
        taller.atenderSiguiente();

        assertSame(
                vehiculo,
                tallerista.getVehiculoActual()
        );

        // 3. El diagnóstico identifica la reparación original.
        Reparacion original =
                new Reparacion(
                        "Cambiar discos de freno",
                        "Falla diagnosticada"
                );

        taller.agregarReparacion(
                vehiculo,
                original
        );

        // 4. Durante el diagnóstico aparece una
        //    falla adicional.
        Reparacion adicional =
                new Reparacion(
                        "Cambio de pastillas de freno",
                        "Falla adicional"
                );

        taller.agregarReparacion(
                vehiculo,
                adicional
        );

        // 5. La falla adicional se atiende primero.
        assertSame(
                adicional,
                taller.realizarProximaReparacion(vehiculo)
        );

        // 6. Luego se atiende la reparación original.
        assertSame(
                original,
                taller.realizarProximaReparacion(vehiculo)
        );

        // 7. Ambas quedan registradas.
        assertEquals(
                2,
                vehiculo.cantidadReparacionesRealizadas()
        );

        // 8. El vehículo queda pronto.
        taller.finalizarVehiculo(vehiculo);

        assertTrue(
                taller.estaProntoParaRetirar(vehiculo)
        );

        assertEquals(
                1,
                taller.cantidadVehiculosProntos()
        );

        // 9. El tallerista queda libre.
        assertTrue(
                tallerista.estaDisponible()
        );
    }
}