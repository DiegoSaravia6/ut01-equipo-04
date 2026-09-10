package ucu.edu.aed.Taller;

public class Tallerista {

    private final int id;
    private final String nombre;
    private Vehiculo vehiculoActual;

    public Tallerista(int id, String nombre) {
        if (id <= 0) throw new IllegalArgumentException("El ID del tallerista debe ser positivo");
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del tallerista no puede estar vacío");
        }
        this.id = id;
        this.nombre = nombre.trim();
        this.vehiculoActual = null;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public Vehiculo getVehiculoActual() { return vehiculoActual; }
    public boolean estaDisponible() { return vehiculoActual == null; }

    public void asignarVehiculo(Vehiculo vehiculo) {
        if (!estaDisponible()) throw new IllegalStateException("El tallerista ya tiene un vehículo asignado");
        if (vehiculo == null) throw new IllegalArgumentException("El vehículo no puede ser null");
        this.vehiculoActual = vehiculo;
    }

    public void liberarVehiculo() {
        if (estaDisponible()) throw new IllegalStateException("El tallerista ya está disponible");
        this.vehiculoActual = null;
    }
}
