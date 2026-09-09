package ucu.edu.aed.Taller;

/** Trabajo/falla dentro de una orden ramificada. */
public class Trabajo {
    private final int id;
    private final String descripcion;
    private final ParteVehiculo parte;
    private final double costo;
    private EstadoTrabajo estado;
    private EstadoAprobacion aprobacion;
    private final boolean raizTecnica;

    public Trabajo(int id, String descripcion, ParteVehiculo parte, double costo,
                   EstadoAprobacion aprobacion) {
        this(id, descripcion, parte, costo, aprobacion, false);
    }

    private Trabajo(int id, String descripcion, ParteVehiculo parte, double costo,
                    EstadoAprobacion aprobacion, boolean raizTecnica) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía");
        }
        if (!raizTecnica && parte == null) {
            throw new IllegalArgumentException("Todo trabajo debe estar asociado a una parte");
        }
        if (costo < 0) throw new IllegalArgumentException("El costo no puede ser negativo");
        this.id = id;
        this.descripcion = descripcion.trim();
        this.parte = parte;
        this.costo = costo;
        this.aprobacion = aprobacion == null ? EstadoAprobacion.PENDIENTE : aprobacion;
        this.raizTecnica = raizTecnica;
        this.estado = EstadoTrabajo.PENDIENTE;
    }

    static Trabajo crearRaizTecnica() {
        return new Trabajo(0, "RAIZ TECNICA DE LA ORDEN", null, 0,
                EstadoAprobacion.APROBADO, true);
    }

    public int getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public ParteVehiculo getParte() { return parte; }
    public double getCosto() { return costo; }
    public EstadoTrabajo getEstado() { return estado; }
    public EstadoAprobacion getAprobacion() { return aprobacion; }
    public boolean esRaizTecnica() { return raizTecnica; }

    void setEstado(EstadoTrabajo estado) { this.estado = estado; }
    void setAprobacion(EstadoAprobacion aprobacion) { this.aprobacion = aprobacion; }

    public boolean estaCerrado() {
        return estado == EstadoTrabajo.TERMINADO || estado == EstadoTrabajo.RECHAZADO;
    }

    @Override public String toString() {
        return "#" + id + " " + descripcion + " [" + estado + ", " + aprobacion + "]";
    }
}
