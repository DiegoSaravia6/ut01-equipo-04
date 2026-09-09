package ucu.edu.aed.Taller;

public class ParteVehiculo {

    private final String codigo;
    private final String nombreParte;

    public ParteVehiculo(String codigo, String nombreParte) {
        this.codigo = codigo;
        this.nombreParte = nombreParte;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombreParte() {
        return nombreParte;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombreParte;
    }
}