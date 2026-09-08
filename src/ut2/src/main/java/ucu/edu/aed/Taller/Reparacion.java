package ucu.edu.aed.Taller;

public class Reparacion {

    private String descripcion;
    private String tipo;

    public Reparacion(String descripcion, String tipo) {
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTipo() {
        return tipo;
    }
}