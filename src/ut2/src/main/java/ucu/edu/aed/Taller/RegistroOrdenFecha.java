package ucu.edu.aed.Taller;

import java.time.LocalDate;

/** Entrada del índice AVL de órdenes por fecha de ingreso. */
public class RegistroOrdenFecha implements Comparable<RegistroOrdenFecha> {
    private final LocalDate fecha;
    private final int ordenId;
    private final OrdenTrabajo orden;

    public RegistroOrdenFecha(LocalDate fecha, int ordenId, OrdenTrabajo orden) {
        this.fecha = fecha;
        this.ordenId = ordenId;
        this.orden = orden;
    }

    public static RegistroOrdenFecha limiteInferior(LocalDate fecha) {
        return new RegistroOrdenFecha(fecha, Integer.MIN_VALUE, null);
    }
    public static RegistroOrdenFecha limiteSuperior(LocalDate fecha) {
        return new RegistroOrdenFecha(fecha, Integer.MAX_VALUE, null);
    }

    public LocalDate getFecha() { return fecha; }
    public int getOrdenId() { return ordenId; }
    public OrdenTrabajo getOrden() { return orden; }

    @Override
    public int compareTo(RegistroOrdenFecha otra) {
        int cmp = fecha.compareTo(otra.fecha);
        if (cmp != 0) return cmp;
        return Integer.compare(ordenId, otra.ordenId);
    }
}
