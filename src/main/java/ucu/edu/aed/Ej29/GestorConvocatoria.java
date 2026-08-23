package ucu.edu.aed.Ej29;

import ucu.edu.aed.tda.Lista;
import ucu.edu.aed.tda.TDALista;

public class GestorConvocatoria {

    private static final int CUPOS = 20;

    public ResultadoConvocatoria armarConvocatoria(
            TDALista<Jugador> jugadores) {

        TDALista<Jugador> habilitados = new Lista<>();
        TDALista<Jugador> noHabilitados = new Lista<>();

        // Separamos los jugadores según su estado,
        // manteniendo el orden de registro.
        for (int i = 0; i < jugadores.tamaño(); i++) {

            Jugador jugador = jugadores.obtener(i);

            if (jugador.getEstado() == Estado.HABILITADO) {
                habilitados.agregar(jugador);
            } else {
                noHabilitados.agregar(jugador);
            }
        }

        // Ordenamos los habilitados según:
        // 1. Mayor división.
        // 2. Menor cantidad de partidas jugadas.
        // 3. Orden de registro en caso de empate.
        TDALista<Jugador> habilitadosOrdenados =
                ordenarHabilitados(habilitados);

        // Los no habilitados se ordenan por mayor división.
        // En caso de empate se conserva el orden de registro.
        TDALista<Jugador> noHabilitadosOrdenados =
                ordenarNoHabilitados(noHabilitados);

        TDALista<Jugador> convocados = new Lista<>();
        TDALista<Jugador> suplentes = new Lista<>();
        TDALista<Jugador> pendientes = new Lista<>();

        // Primero se convocan los jugadores habilitados.
        for (int i = 0; i < habilitadosOrdenados.tamaño(); i++) {

            Jugador jugador = habilitadosOrdenados.obtener(i);

            if (convocados.tamaño() < CUPOS) {
                convocados.agregar(jugador);
            } else {
                // Los habilitados que no entran entre los 20
                // titulares pasan a ser suplentes.
                suplentes.agregar(jugador);
            }
        }

        // Si no se alcanzan los 20 cupos con habilitados,
        // se completa con lesionados o suspendidos.
        for (int i = 0;
             i < noHabilitadosOrdenados.tamaño()
                     && convocados.tamaño() < CUPOS;
             i++) {

            Jugador jugador = noHabilitadosOrdenados.obtener(i);

            convocados.agregar(jugador);
            pendientes.agregar(jugador);
        }

        // Si no se alcanzaron los 20 convocados,
        // se informa el déficit.
        int deficit = CUPOS - convocados.tamaño();

        if (deficit < 0) {
            deficit = 0;
        }

        return new ResultadoConvocatoria(
                convocados,
                suplentes,
                pendientes,
                deficit);
    }

    private TDALista<Jugador> ordenarHabilitados(
            TDALista<Jugador> jugadores) {

        Lista<Jugador> ordenados = new Lista<>();

        for (int i = 0; i < jugadores.tamaño(); i++) {

            Jugador jugador = jugadores.obtener(i);

            int posicion = 0;

            while (posicion < ordenados.tamaño()
                    && jugador.compararPrioridad(
                    ordenados.obtener(posicion)) >= 0) {

                posicion++;
            }

            ordenados.agregar(posicion, jugador);
        }

        return ordenados;
    }

    private TDALista<Jugador> ordenarNoHabilitados(
            TDALista<Jugador> jugadores) {

        Lista<Jugador> ordenados = new Lista<>();

        for (int i = 0; i < jugadores.tamaño(); i++) {

            Jugador jugador = jugadores.obtener(i);

            int posicion = 0;

            while (posicion < ordenados.tamaño()
                    && jugador.getDivision().prioridad()
                    <= ordenados.obtener(posicion)
                    .getDivision().prioridad()) {

                posicion++;
            }

            ordenados.agregar(posicion, jugador);
        }

        return ordenados;
    }
}
