package ucu.edu.aed.Ej29;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import ucu.edu.aed.tda.Lista;
import ucu.edu.aed.tda.TDALista;

public class GestorConvocatoriaTest extends TestCase {

    public GestorConvocatoriaTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(GestorConvocatoriaTest.class);
    }

    public void testConvocarJugadoresHabilitados() {

        TDALista<Jugador> jugadores = new Lista<>();

        jugadores.agregar(new Jugador(
                "Ana",
                Division.ORO,
                10,
                Estado.HABILITADO));

        jugadores.agregar(new Jugador(
                "Bruno",
                Division.PLATINO,
                20,
                Estado.HABILITADO));

        jugadores.agregar(new Jugador(
                "Carlos",
                Division.PLATA,
                5,
                Estado.HABILITADO));

        GestorConvocatoria gestor = new GestorConvocatoria();

        ResultadoConvocatoria resultado =
                gestor.armarConvocatoria(jugadores);

        assertEquals(3, resultado.getConvocados().tamaño());
        assertEquals(17, resultado.getDeficit());
    }

    public void testPrioridadPorDivision() {

        TDALista<Jugador> jugadores = new Lista<>();

        jugadores.agregar(new Jugador(
                "Ana",
                Division.ORO,
                10,
                Estado.HABILITADO));

        jugadores.agregar(new Jugador(
                "Bruno",
                Division.PLATINO,
                20,
                Estado.HABILITADO));

        jugadores.agregar(new Jugador(
                "Carlos",
                Division.PLATA,
                5,
                Estado.HABILITADO));

        GestorConvocatoria gestor = new GestorConvocatoria();

        ResultadoConvocatoria resultado =
                gestor.armarConvocatoria(jugadores);

        assertEquals(
                "Bruno",
                resultado.getConvocados().obtener(0).getNombre());

        assertEquals(
                "Ana",
                resultado.getConvocados().obtener(1).getNombre());

        assertEquals(
                "Carlos",
                resultado.getConvocados().obtener(2).getNombre());
    }

    public void testPrioridadPorMenosPartidas() {

        TDALista<Jugador> jugadores = new Lista<>();

        jugadores.agregar(new Jugador(
                "Ana",
                Division.ORO,
                20,
                Estado.HABILITADO));

        jugadores.agregar(new Jugador(
                "Bruno",
                Division.ORO,
                10,
                Estado.HABILITADO));

        GestorConvocatoria gestor = new GestorConvocatoria();

        ResultadoConvocatoria resultado =
                gestor.armarConvocatoria(jugadores);

        assertEquals(
                "Bruno",
                resultado.getConvocados().obtener(0).getNombre());

        assertEquals(
                "Ana",
                resultado.getConvocados().obtener(1).getNombre());
    }

    public void testMantenerOrdenDeRegistro() {

        TDALista<Jugador> jugadores = new Lista<>();

        jugadores.agregar(new Jugador(
                "Diana",
                Division.ORO,
                10,
                Estado.HABILITADO));

        jugadores.agregar(new Jugador(
                "Carlos",
                Division.ORO,
                10,
                Estado.HABILITADO));

        GestorConvocatoria gestor = new GestorConvocatoria();

        ResultadoConvocatoria resultado =
                gestor.armarConvocatoria(jugadores);

        assertEquals(
                "Diana",
                resultado.getConvocados().obtener(0).getNombre());

        assertEquals(
                "Carlos",
                resultado.getConvocados().obtener(1).getNombre());
    }

    public void testSuplentes() {

        TDALista<Jugador> jugadores = new Lista<>();

        for (int i = 1; i <= 25; i++) {
            jugadores.agregar(new Jugador(
                    "Jugador" + i,
                    Division.ORO,
                    i,
                    Estado.HABILITADO));
        }

        GestorConvocatoria gestor = new GestorConvocatoria();

        ResultadoConvocatoria resultado =
                gestor.armarConvocatoria(jugadores);

        assertEquals(20, resultado.getConvocados().tamaño());
        assertEquals(5, resultado.getSuplentes().tamaño());
        assertEquals(0, resultado.getDeficit());
    }

    public void testLesionadosCompletanLosCupos() {

        TDALista<Jugador> jugadores = new Lista<>();

        for (int i = 1; i <= 18; i++) {
            jugadores.agregar(new Jugador(
                    "Habilitado" + i,
                    Division.ORO,
                    i,
                    Estado.HABILITADO));
        }

        jugadores.agregar(new Jugador(
                "Lesionado1",
                Division.PLATINO,
                10,
                Estado.LESIONADO));

        jugadores.agregar(new Jugador(
                "Lesionado2",
                Division.ORO,
                10,
                Estado.LESIONADO));

        GestorConvocatoria gestor = new GestorConvocatoria();

        ResultadoConvocatoria resultado =
                gestor.armarConvocatoria(jugadores);

        assertEquals(20, resultado.getConvocados().tamaño());
        assertEquals(2, resultado.getPendientes().tamaño());
        assertEquals(0, resultado.getDeficit());
    }

    public void testDeficit() {

        TDALista<Jugador> jugadores = new Lista<>();

        jugadores.agregar(new Jugador(
                "Ana",
                Division.ORO,
                10,
                Estado.HABILITADO));

        jugadores.agregar(new Jugador(
                "Bruno",
                Division.PLATINO,
                10,
                Estado.LESIONADO));

        jugadores.agregar(new Jugador(
                "Carlos",
                Division.PLATA,
                10,
                Estado.SUSPENDIDO));

        GestorConvocatoria gestor = new GestorConvocatoria();

        ResultadoConvocatoria resultado =
                gestor.armarConvocatoria(jugadores);

        assertEquals(3, resultado.getConvocados().tamaño());
        assertEquals(17, resultado.getDeficit());
    }

    public void testListaVacia() {

        TDALista<Jugador> jugadores = new Lista<>();

        GestorConvocatoria gestor = new GestorConvocatoria();

        ResultadoConvocatoria resultado =
                gestor.armarConvocatoria(jugadores);

        assertEquals(0, resultado.getConvocados().tamaño());
        assertEquals(20, resultado.getDeficit());
    }
}
