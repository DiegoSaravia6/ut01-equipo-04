package ucu.edu.aed.Ej29;

public class Jugador {

    private String nombre;
    private Division division;
    private int partidasJugadas;
    private Estado estado;

    public Jugador(String nombre, Division division, int partidasJugadas, Estado estado) {
        this.nombre = nombre;
        this.division = division;
        this.partidasJugadas = partidasJugadas;
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public Division getDivision() {
        return division;
    }

    public int getPartidasJugadas() {
        return partidasJugadas;
    }

    public Estado getEstado() {
        return estado;
    }

    public int compararPrioridad(Jugador otro) {

        // Primero se compara la división.
        if (this.division.prioridad() > otro.division.prioridad()) {
            return -1;
        }

        if (this.division.prioridad() < otro.division.prioridad()) {
            return 1;
        }

        // Si tienen la misma división,
        // tiene prioridad el que jugó menos partidas.
        if (this.partidasJugadas < otro.partidasJugadas) {
            return -1;
        }

        if (this.partidasJugadas > otro.partidasJugadas) {
            return 1;
        }

        // Si también tienen las mismas partidas,
        // se mantiene el orden de registro.
        return 0;
    }
}
