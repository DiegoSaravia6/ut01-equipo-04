package ucu.edu.aed.Ej29;
import ucu.edu.aed.tda.TDALista;

public class ResultadoConvocatoria {
    
    private TDALista<Jugador> convocados;
    private TDALista<Jugador> suplentes;
    private TDALista<Jugador> pendientes;
    private int deficit;

    public ResultadoConvocatoria(TDALista<Jugador> convocados, TDALista<Jugador> suplentes, TDALista<Jugador> pendientes, int deficit) {
        this.convocados = convocados;
        this.suplentes = suplentes;
        this.pendientes = pendientes;
        this.deficit = deficit;
    }

    public TDALista<Jugador> getConvocados() {
        return convocados;
    }

    public TDALista<Jugador> getSuplentes() {
        return suplentes;
    }

    public TDALista<Jugador> getPendientes() {
        return pendientes;
    }

    public int getDeficit() {
        return deficit;
    }
}
