package ucu.edu.aed.Taller;

public class Tallerista {

    private final int id;
    private String nombre;
    private Vehiculo vehiculoActual;
    

    public Tallerista(int id,String nombre) {
        this.nombre = nombre;
        this.vehiculoActual = null;
        this.id = id;
    }

    public int getId(){
        return id;
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tallerista that = (Tallerista) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Tallerista{id=" + id + ", nombre='" + nombre + "'}";
    }
}
