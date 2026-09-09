package ucu.edu.aed.Taller;

import java.util.Objects;

/** Parte de la estructura jerárquica de un vehículo. */
public class ParteVehiculo {
    private final String codigo;
    private final String nombre;

    public ParteVehiculo(String codigo, String nombre) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de la parte no puede estar vacío");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la parte no puede estar vacío");
        }
        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ParteVehiculo)) return false;
        ParteVehiculo otra = (ParteVehiculo) obj;
        return codigo.equalsIgnoreCase(otra.codigo);
    }

    @Override public int hashCode() { return Objects.hash(codigo.toUpperCase()); }
    @Override public String toString() { return codigo + " - " + nombre; }
}
