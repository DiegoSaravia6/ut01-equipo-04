package ucu.edu.aed.Taller;

public class Tallerista {

    private String nombre;
    private Vehiculo vehiculoActual;

    public Tallerista(String nombre) {
        this.nombre = nombre;
        this.vehiculoActual = null;
    }

    public String getNombre() {
        return nombre;
    }

    public Vehiculo getVehiculoActual() {
        return vehiculoActual;
    }

    public boolean estaDisponible() {
        return vehiculoActual == null;
    }

    public void asignarVehiculo(Vehiculo vehiculo) {
        if (!estaDisponible()) {
            throw new IllegalStateException(
                    "El tallerista ya tiene un vehículo asignado"
            );
        }

        if (vehiculo == null) {
            throw new IllegalArgumentException(
                    "El vehículo no puede ser null"
            );
        }

        this.vehiculoActual = vehiculo;
    }

    public void liberarVehiculo() {
        if (estaDisponible()) {
            throw new IllegalStateException(
                    "El tallerista ya está disponible"
            );
        }

        this.vehiculoActual = null;
    }
}
