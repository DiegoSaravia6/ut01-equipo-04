package ucu.edu.aed.Taller;

import java.time.LocalDate;

import ucu.edu.aed.tda.ArbolGeneral;
import ucu.edu.aed.tda.ElementoArbolGeneral;
import ucu.edu.aed.tda.ListaArreglo;

public class OrdenTrabajo {

    private final int id;
    private final LocalDate fechaIngreso;

    /*
     * Raíz técnica.
     * No representa un trabajo real.
     * Permite tener varios trabajos principales.
     */
    private final ArbolGeneral<Reparacion> trabajos;

    public OrdenTrabajo(int id, LocalDate fechaIngreso) {

        if (fechaIngreso == null) {
            throw new IllegalArgumentException(
                    "La fecha de ingreso no puede ser null");
        }

        this.id = id;
        this.fechaIngreso = fechaIngreso;

        this.trabajos = new ArbolGeneral<>(
                new Reparacion(
                        "RAIZ_ORDEN_" + id,
                        "ORDEN",
                        0.0
                )
        );
    }

    public int getId() {
        return id;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public ArbolGeneral<Reparacion> getTrabajos() {
        return trabajos;
    }

    // =========================================================
    // AGREGAR TRABAJOS
    // =========================================================

    public Reparacion agregarTrabajoPrincipal(
            String descripcion,
            ParteVehiculo parte,
            double costo,
            boolean aprobado) {

        validarTrabajo(descripcion, parte, costo);

        Reparacion reparacion =
                new Reparacion(
                        descripcion.trim(),
                        parte.getCodigo(),
                        costo
                );

        reparacion.setAprobado(aprobado);

        trabajos.agregarHijo(
                trabajos.obtenerRaiz(),
                reparacion
        );

        return reparacion;
    }

    public Reparacion agregarTrabajoDerivado(
            int idTrabajoOrigen,
            String descripcion,
            ParteVehiculo parte,
            double costo) {

        validarTrabajo(descripcion, parte, costo);

        Reparacion origen =
                buscarTrabajoPorId(idTrabajoOrigen);

        if (origen == null) {
            throw new IllegalArgumentException(
                    "No existe el trabajo de origen con id "
                            + idTrabajoOrigen);
        }

        Reparacion nueva =
                new Reparacion(
                        descripcion.trim(),
                        parte.getCodigo(),
                        costo
                );

        nueva.setAprobado(false);

        ElementoArbolGeneral<Reparacion> nodoOrigen =
                trabajos.buscarNodo(r -> r == origen);

        trabajos.agregarHijo(
                nodoOrigen,
                nueva
        );

        return nueva;
    }

    private void validarTrabajo(
            String descripcion,
            ParteVehiculo parte,
            double costo) {

        if (descripcion == null ||
                descripcion.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "La descripción no puede estar vacía");
        }

        if (parte == null) {
            throw new IllegalArgumentException(
                    "La parte no puede ser null");
        }

        if (costo < 0) {
            throw new IllegalArgumentException(
                    "El costo no puede ser negativo");
        }
    }

    // =========================================================
    // BÚSQUEDA
    // =========================================================

    public Reparacion buscarTrabajoPorId(int idTrabajo) {

        Reparacion encontrado =
                trabajos.buscar(
                        r -> r.getId() == idTrabajo
                );

        if (encontrado == null) {
            return null;
        }

        if (encontrado == trabajos.obtenerRaiz().getDato()) {
            return null;
        }

        return encontrado;
    }

    // =========================================================
    // APROBAR / RECHAZAR
    // =========================================================

    public void aprobarTrabajo(int idTrabajo) {

        Reparacion trabajo =
                obtenerTrabajo(idTrabajo);

        if (trabajo.getEstado() ==
                EstadoReparacion.Rechazada) {

            throw new IllegalStateException(
                    "No se puede aprobar un trabajo rechazado");
        }

        trabajo.setAprobado(true);
    }

    public void rechazarTrabajo(int idTrabajo) {

        Reparacion trabajo =
                obtenerTrabajo(idTrabajo);

        ElementoArbolGeneral<Reparacion> nodo =
                trabajos.buscarNodo(r -> r == trabajo);

        /*
         * Política elegida:
         * rechazar un trabajo rechaza toda su rama.
         */
        trabajos.recorrerSubarbol(
                nodo,
                r -> {
                    r.setAprobado(false);

                    if (r.getEstado() !=
                            EstadoReparacion.Lista) {

                        r.setEstado(
                                EstadoReparacion.Rechazada
                        );
                    }
                }
        );
    }

    // =========================================================
    // EJECUCIÓN
    // =========================================================

    public void iniciarTrabajo(int idTrabajo) {

        Reparacion trabajo =
                obtenerTrabajo(idTrabajo);

        if (!trabajo.isAprobado()) {
            throw new IllegalStateException(
                    "No se puede iniciar un trabajo que no fue aprobado");
        }

        if (trabajo.getEstado() ==
                EstadoReparacion.Rechazada) {

            throw new IllegalStateException(
                    "No se puede iniciar un trabajo rechazado");
        }

        if (tieneAncestroEsperandoRepuesto(trabajo)) {

            throw new IllegalStateException(
                    "El trabajo está bloqueado por un repuesto");
        }

        trabajo.setEstado(
                EstadoReparacion.En_Proceso
        );
    }

    public void finalizarTrabajo(int idTrabajo) {

        Reparacion trabajo =
                obtenerTrabajo(idTrabajo);

        if (tieneAncestroEsperandoRepuesto(trabajo)) {

            throw new IllegalStateException(
                    "El trabajo está bloqueado por un repuesto");
        }

        ElementoArbolGeneral<Reparacion> nodo =
                trabajos.buscarNodo(r -> r == trabajo);

        /*
         * Un trabajo padre solamente puede finalizar
         * cuando todos sus hijos terminaron o fueron rechazados.
         */
        for (int i = 0;
             i < nodo.cantidadHijos();
             i++) {

            Reparacion hijo =
                    nodo.obtenerHijo(i).getDato();

            if (hijo.getEstado() !=
                        EstadoReparacion.Lista
                    && hijo.getEstado() !=
                        EstadoReparacion.Rechazada) {

                throw new IllegalStateException(
                        "No se puede finalizar el trabajo "
                                + trabajo.getId()
                                + " porque todavía tiene trabajos derivados pendientes"
                );
            }
        }

        trabajo.setEstado(
                EstadoReparacion.Lista
        );
    }

    // =========================================================
    // REPUESTOS
    // =========================================================

    public void suspenderTrabajoPorRepuesto(
            int idTrabajo) {

        Reparacion trabajo =
                obtenerTrabajo(idTrabajo);

        if (trabajo.getEstado() ==
                    EstadoReparacion.Lista
                || trabajo.getEstado() ==
                    EstadoReparacion.Rechazada) {

            throw new IllegalStateException(
                    "El trabajo ya está terminado");
        }

        trabajo.setEstado(
                EstadoReparacion.Esperando_Repuesto
        );
    }

    public void reanudarTrabajoPorRepuesto(
            int idTrabajo) {

        Reparacion trabajo =
                obtenerTrabajo(idTrabajo);

        if (trabajo.getEstado() !=
                EstadoReparacion.Esperando_Repuesto) {

            throw new IllegalStateException(
                    "El trabajo no está esperando un repuesto");
        }

        if (!trabajo.isAprobado()) {

            throw new IllegalStateException(
                    "No se puede reanudar un trabajo no aprobado");
        }

        trabajo.setEstado(
                EstadoReparacion.En_Proceso
        );
    }

    // =========================================================
    // TRABAJOS EJECUTABLES
    // =========================================================

    public boolean tieneTrabajoEjecutable() {

        final boolean[] existe = {false};

        trabajos.preOrder(reparacion -> {

            if (esTrabajoReal(reparacion)
                    && reparacion.isAprobado()
                    && reparacion.getEstado() ==
                        EstadoReparacion.Pendiente
                    && !tieneAncestroEsperandoRepuesto(
                        reparacion)) {

                existe[0] = true;
            }
        });

        return existe[0];
    }

    // =========================================================
    // CONSULTAS
    // =========================================================

    public ListaArreglo<Reparacion> ordenPendiente() {

        ListaArreglo<Reparacion> resultado =
                new ListaArreglo<>();

        trabajos.preOrder(r -> {

            if (esTrabajoReal(r)
                    && r.getEstado() ==
                        EstadoReparacion.Pendiente
                    && r.isAprobado()
                    && !tieneAncestroEsperandoRepuesto(r)) {

                resultado.agregar(r);
            }
        });

        return resultado;
    }

    public ListaArreglo<Reparacion> trabajosBloqueadosPor(
            int idTrabajo) {

        ListaArreglo<Reparacion> resultado =
                new ListaArreglo<>();

        Reparacion trabajo =
                buscarTrabajoPorId(idTrabajo);

        if (trabajo == null) {
            return resultado;
        }

        ElementoArbolGeneral<Reparacion> nodo =
                trabajos.buscarNodo(r -> r == trabajo);

        trabajos.recorrerSubarbol(
                nodo,
                r -> {

                    if (r != trabajo
                            && esTrabajoReal(r)) {

                        resultado.agregar(r);
                    }
                }
        );

        return resultado;
    }

    // =========================================================
    // COSTOS
    // =========================================================

    public double costoPropuestoTotal() {

        final double[] total = {0};

        trabajos.preOrder(r -> {

            if (esTrabajoReal(r)) {
                total[0] += r.getCosto();
            }
        });

        return total[0];
    }

    public double costoAutorizadoTotal() {

        final double[] total = {0};

        trabajos.preOrder(r -> {

            if (esTrabajoReal(r)
                    && r.isAprobado()) {

                total[0] += r.getCosto();
            }
        });

        return total[0];
    }

    public double costoPropuestoSubarbol(
            int idTrabajo) {

        Reparacion trabajo =
                buscarTrabajoPorId(idTrabajo);

        if (trabajo == null) {

            throw new IllegalArgumentException(
                    "No existe el trabajo con id "
                            + idTrabajo);
        }

        final double[] total = {0};

        ElementoArbolGeneral<Reparacion> nodo =
                trabajos.buscarNodo(r -> r == trabajo);

        trabajos.recorrerSubarbol(
                nodo,
                r -> total[0] += r.getCosto()
        );

        return total[0];
    }

    // =========================================================
    // CIERRE
    // =========================================================

    public boolean estaCerrada() {

        final boolean[] hayTrabajos = {false};
        final boolean[] cerrada = {true};

        trabajos.preOrder(r -> {

            if (!esTrabajoReal(r)) {
                return;
            }

            hayTrabajos[0] = true;

            if (r.getEstado() !=
                        EstadoReparacion.Lista
                    && r.getEstado() !=
                        EstadoReparacion.Rechazada) {

                cerrada[0] = false;
            }
        });

        return hayTrabajos[0] && cerrada[0];
    }

    // =========================================================
    // COMPATIBILIDAD HITO 1
    // =========================================================

    public void finalizarPrimerTrabajoPorDescripcion(
            String descripcion) {

        if (descripcion == null) {
            return;
        }

        Reparacion reparacion =
                trabajos.buscar(
                        r -> esTrabajoReal(r)
                                && r.getDescripcion()
                                .equalsIgnoreCase(descripcion)
                );

        if (reparacion != null) {
            finalizarTrabajo(reparacion.getId());
        }
    }

    // =========================================================
    // AUXILIARES
    // =========================================================

    private Reparacion obtenerTrabajo(int idTrabajo) {

        Reparacion trabajo =
                buscarTrabajoPorId(idTrabajo);

        if (trabajo == null) {

            throw new IllegalArgumentException(
                    "No existe el trabajo con id "
                            + idTrabajo);
        }

        return trabajo;
    }

    private boolean esTrabajoReal(
            Reparacion reparacion) {

        return reparacion !=
                trabajos.obtenerRaiz().getDato();
    }

    private boolean tieneAncestroEsperandoRepuesto(
            Reparacion trabajo) {

        ElementoArbolGeneral<Reparacion> nodo =
                trabajos.buscarNodo(r -> r == trabajo);

        if (nodo == null) {
            return false;
        }

        ElementoArbolGeneral<Reparacion> padre =
                nodo.getPadre();

        while (padre != null) {

            Reparacion reparacionPadre =
                    padre.getDato();

            if (esTrabajoReal(reparacionPadre)
                    && reparacionPadre.getEstado() ==
                        EstadoReparacion.Esperando_Repuesto) {

                return true;
            }

            padre = padre.getPadre();
        }

        return false;
    }
}