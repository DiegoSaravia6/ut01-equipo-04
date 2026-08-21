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
        this.vehiculoActual = vehiculo;
    }

    public void liberarVehiculo() {
        this.vehiculoActual = null;
    }
}