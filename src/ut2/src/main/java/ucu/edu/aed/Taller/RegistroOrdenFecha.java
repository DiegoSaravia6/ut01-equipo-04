package ucu.edu.aed.Taller;

import java.time.LocalDate;

public class RegistroOrdenFecha implements Comparable<RegistroOrdenFecha> {

    private final LocalDate fecha;
    private final int idOrden;
    private final OrdenTrabajo orden;

    public RegistroOrdenFecha(
            LocalDate fecha,
            int idOrden,
            OrdenTrabajo orden) {

        if (fecha == null) {
            throw new IllegalArgumentException(
                    "La fecha no puede ser null");
        }

        this.fecha = fecha;
        this.idOrden = idOrden;
        this.orden = orden;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public OrdenTrabajo getOrden() {
        return orden;
    }

    @Override
    public int compareTo(RegistroOrdenFecha otro) {

        int comparacionFecha = fecha.compareTo(otro.fecha);

        if (comparacionFecha != 0) {
            return comparacionFecha;
        }

        return Integer.compare(idOrden, otro.idOrden);
    }

    public static RegistroOrdenFecha limiteInferior(LocalDate fecha) {
        return new RegistroOrdenFecha(
                fecha,
                Integer.MIN_VALUE,
                null
        );
    }

    public static RegistroOrdenFecha limiteSuperior(LocalDate fecha) {
        return new RegistroOrdenFecha(
                fecha,
                Integer.MAX_VALUE,
                null
        );
    }

    @Override
    public String toString() {
        return "Orden " + idOrden + " - Fecha: " + fecha;
    }
}