package ucu.edu.aed.Taller;

public class Reparacion {

    private static int siguienteId = 1;

    private final int id;
    private final String descripcion;
    private final String tipo;
    private final double costo;

    private EstadoReparacion estado;
    private boolean aprobado;

    public Reparacion(String descripcion, String tipo, double costo) {

        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La descripción no puede estar vacía");
        }

        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El tipo no puede estar vacío");
        }

        if (costo < 0) {
            throw new IllegalArgumentException(
                    "El costo no puede ser negativo");
        }

        this.id = siguienteId++;
        this.descripcion = descripcion.trim();
        this.tipo = tipo.trim();
        this.costo = costo;
        this.estado = EstadoReparacion.Pendiente;
        this.aprobado = false;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public double getCosto() {
        return costo;
    }

    public EstadoReparacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoReparacion estado) {
        if (estado == null) {
            throw new IllegalArgumentException(
                    "El estado no puede ser null");
        }

        this.estado = estado;
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    @Override
    public String toString() {
        return id + " - " + descripcion +
                " - $" + costo +
                " - " + estado;
    }
}