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
                new Reparacion("RAIZ_ORDEN_" + id,"ORDEN",0.0)
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

        Reparacion origen =buscarTrabajoPorId(idTrabajoOrigen);

        if (origen == null) {
            throw new IllegalArgumentException(
                    "No existe el trabajo de origen con id "+ idTrabajoOrigen);
        }

        Reparacion nueva =new Reparacion(descripcion.trim(),parte.getCodigo(),costo);

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
                if (trabajo.getEstado() ==EstadoReparacion.En_Proceso|| trabajo.getEstado() ==EstadoReparacion.Terminado|| trabajo.getEstado() ==EstadoReparacion.Rechazada) {
            throw new IllegalStateException(
                    "No se puede rechazar un trabajo que ya está en progreso, terminado o rechazado");
                }

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

/**
 * Orden de trabajo representada como árbol general.
 * La raíz es técnica y no facturable; sus hijos son los trabajos principales.
 * Los hijos de cada trabajo representan fallas/trabajos que se desprendieron de él.
 */
public class OrdenTrabajo {
    private final int id;
    private final LocalDate fechaIngreso;
    private final ArbolGeneral<Trabajo> trabajos;
    private int siguienteTrabajoId = 1;

    public OrdenTrabajo(int id, LocalDate fechaIngreso) {
        if (fechaIngreso == null) throw new IllegalArgumentException("La fecha de ingreso no puede ser null");
        this.id = id;
        this.fechaIngreso = fechaIngreso;
        this.trabajos = new ArbolGeneral<>(Trabajo.crearRaizTecnica());
    }

    public int getId() { return id; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public ArbolGeneral<Trabajo> getArbolTrabajos() { return trabajos; }

    public Trabajo agregarTrabajoPrincipal(String descripcion, ParteVehiculo parte,
                                            double costo, boolean yaAprobado) {
        Trabajo nuevo = new Trabajo(siguienteTrabajoId++, descripcion, parte, costo,
                yaAprobado ? EstadoAprobacion.APROBADO : EstadoAprobacion.PENDIENTE);
        trabajos.agregarHijo(trabajos.obtenerRaiz(), nuevo);
        return nuevo;
    }

    public Trabajo agregarTrabajoDerivado(int idTrabajoOrigen, String descripcion,
                                           ParteVehiculo parte, double costo) {
        ElementoArbolGeneral<Trabajo> origen = nodoTrabajo(idTrabajoOrigen);
        if (origen.getDato().estaCerrado()) {
            throw new IllegalStateException("No se puede derivar un trabajo desde uno cerrado");
        }
        Trabajo nuevo = new Trabajo(siguienteTrabajoId++, descripcion, parte, costo,
                EstadoAprobacion.PENDIENTE);
        trabajos.agregarHijo(origen, nuevo);
        // Si el trabajo original estaba en progreso, queda pendiente de sus derivados.
        if (origen.getDato().getEstado() == EstadoTrabajo.EN_PROGRESO) {
            origen.getDato().setEstado(EstadoTrabajo.PENDIENTE);
        }
        return nuevo;
    }

    public Trabajo buscarTrabajo(int idTrabajo) {
        ElementoArbolGeneral<Trabajo> n = trabajos.buscarNodo(t -> t.getId() == idTrabajo);
        return n == null ? null : n.getDato();
    }

    private ElementoArbolGeneral<Trabajo> nodoTrabajo(int idTrabajo) {
        if (idTrabajo == 0) throw new IllegalArgumentException("La raíz técnica no es un trabajo operativo");
        ElementoArbolGeneral<Trabajo> n = trabajos.buscarNodo(t -> t.getId() == idTrabajo);
        if (n == null) throw new IllegalArgumentException("No existe el trabajo #" + idTrabajo);
        return n;
    }

    public void aprobarTrabajo(int idTrabajo) {
        Trabajo trabajo = nodoTrabajo(idTrabajo).getDato();
        if (trabajo.getAprobacion() == EstadoAprobacion.RECHAZADO) {
            throw new IllegalStateException("El trabajo ya fue rechazado");
        }
        trabajo.setAprobacion(EstadoAprobacion.APROBADO);
    }

    /**
     * Política del grupo: rechazar una parte cierra esa rama sin ejecutarla.
     * El trabajo y todos sus derivados quedan RECHAZADOS y no se facturan como autorizados.
     */
    public void rechazarTrabajo(int idTrabajo) {
        ElementoArbolGeneral<Trabajo> n = nodoTrabajo(idTrabajo);
        trabajos.recorrerSubarbol(n, t -> {
            if (!t.esRaizTecnica()) {
                t.setAprobacion(EstadoAprobacion.RECHAZADO);
                t.setEstado(EstadoTrabajo.RECHAZADO);
            }
        });
    }

    public void iniciarTrabajo(int idTrabajo) {
        ElementoArbolGeneral<Trabajo> n = nodoTrabajo(idTrabajo);
        Trabajo t = n.getDato();
        if (t.getAprobacion() != EstadoAprobacion.APROBADO) {
            throw new IllegalStateException("El trabajo no está aprobado");
        }
        if (t.getEstado() != EstadoTrabajo.PENDIENTE) {
            throw new IllegalStateException("El trabajo no está pendiente");
        }
        if (!hijosCerrados(n)) {
            throw new IllegalStateException("Hay trabajos derivados que deben resolverse antes");
        }
        t.setEstado(EstadoTrabajo.EN_PROGRESO);
    }

    public void finalizarTrabajo(int idTrabajo) {
        ElementoArbolGeneral<Trabajo> n = nodoTrabajo(idTrabajo);
        Trabajo t = n.getDato();
        if (t.getAprobacion() != EstadoAprobacion.APROBADO) {
            throw new IllegalStateException("El trabajo no está aprobado");
        }
        if (t.getEstado() == EstadoTrabajo.ESPERANDO_REPUESTO) {
            throw new IllegalStateException("El trabajo está esperando repuesto");
        }
        if (t.estaCerrado()) {
            throw new IllegalStateException("El trabajo ya está cerrado");
        }
        if (!hijosCerrados(n)) {
            throw new IllegalStateException("No se puede finalizar mientras haya derivados pendientes");
        }
        t.setEstado(EstadoTrabajo.TERMINADO);
    }

    private boolean hijosCerrados(ElementoArbolGeneral<Trabajo> n) {
        for (int i = 0; i < n.cantidadHijos(); i++) {
            if (!n.obtenerHijo(i).getDato().estaCerrado()) return false;
        }
        return true;
    }

    public void suspenderPorRepuesto(int idTrabajo) {
        ElementoArbolGeneral<Trabajo> n = nodoTrabajo(idTrabajo);
        if (n.getDato().estaCerrado()) throw new IllegalStateException("El trabajo ya está cerrado");
        trabajos.recorrerSubarbol(n, t -> {
            if (!t.estaCerrado()) t.setEstado(EstadoTrabajo.ESPERANDO_REPUESTO);
        });
    }

    public void reanudarPorRepuesto(int idTrabajo) {
        ElementoArbolGeneral<Trabajo> n = nodoTrabajo(idTrabajo);
        trabajos.recorrerSubarbol(n, t -> {
            if (t.getEstado() == EstadoTrabajo.ESPERANDO_REPUESTO) {
                t.setEstado(EstadoTrabajo.PENDIENTE);
            }
        });
    }

    /** Ancestros operativos que quedan bloqueados por la espera del trabajo indicado. */
    public ListaArreglo<Trabajo> trabajosBloqueadosPor(int idTrabajo) {
        ElementoArbolGeneral<Trabajo> n = nodoTrabajo(idTrabajo);
        ListaArreglo<Trabajo> resultado = new ListaArreglo<>();
        ElementoArbolGeneral<Trabajo> actual = n.getPadre();
        while (actual != null && !actual.getDato().esRaizTecnica()) {
            resultado.agregar(actual.getDato());
            actual = actual.getPadre();
        }
        return resultado;
    }

    /**
     * Orden de resolución pendiente: postorden (hijos antes que padre).
     * Entre hermanos se usa LIFO (último detectado primero), preservando la regla del Hito 1.
     */
    public ListaArreglo<Trabajo> ordenPendiente() {
        ListaArreglo<Trabajo> resultado = new ListaArreglo<>();
        ElementoArbolGeneral<Trabajo> raiz = trabajos.obtenerRaiz();
        for (int i = raiz.cantidadHijos() - 1; i >= 0; i--) {
            agregarPendientesPostOrderLifo(raiz.obtenerHijo(i), resultado);
        }
        return resultado;
    }

    private void agregarPendientesPostOrderLifo(ElementoArbolGeneral<Trabajo> n,
                                                 ListaArreglo<Trabajo> resultado) {
        for (int i = n.cantidadHijos() - 1; i >= 0; i--) {
            agregarPendientesPostOrderLifo(n.obtenerHijo(i), resultado);
        }
        if (!n.getDato().estaCerrado()) resultado.agregar(n.getDato());
    }

    public boolean tieneTrabajoEjecutable() {
        ListaArreglo<Trabajo> pendientes = ordenPendiente();
        for (int i = 0; i < pendientes.tamaño(); i++) {
            Trabajo t = pendientes.obtener(i);
            ElementoArbolGeneral<Trabajo> n = trabajos.buscarNodo(x -> x.getId() == t.getId());
            if (t.getEstado() == EstadoTrabajo.PENDIENTE
                    && t.getAprobacion() == EstadoAprobacion.APROBADO
                    && hijosCerrados(n)) return true;
        }
        return false;
    }

    public double costoPropuestoTotal() {
        return costoSubarbolInterno(trabajos.obtenerRaiz(), true);
    }

    public double costoAutorizadoTotal() {
        return costoSubarbolInterno(trabajos.obtenerRaiz(), false);
    }

    public double costoPropuestoSubarbol(int idTrabajo) {
        return costoSubarbolInterno(nodoTrabajo(idTrabajo), true);
    }

    public double costoAutorizadoSubarbol(int idTrabajo) {
        return costoSubarbolInterno(nodoTrabajo(idTrabajo), false);
    }

    private double costoSubarbolInterno(ElementoArbolGeneral<Trabajo> n, boolean incluirRechazados) {
        double total = 0;
        Trabajo t = n.getDato();
        if (!t.esRaizTecnica()) {
            if (incluirRechazados || t.getAprobacion() != EstadoAprobacion.RECHAZADO) {
                total += t.getCosto();
            }
        }
        for (int i = 0; i < n.cantidadHijos(); i++) {
            total += costoSubarbolInterno(n.obtenerHijo(i), incluirRechazados);
        }
        return total;
    }

    /** La orden puede cerrarse si todos los trabajos principales están terminados o rechazados. */
    public boolean estaCerrada() {
        ElementoArbolGeneral<Trabajo> raiz = trabajos.obtenerRaiz();
        if (raiz.cantidadHijos() == 0) return true;
        for (int i = 0; i < raiz.cantidadHijos(); i++) {
            if (!raiz.obtenerHijo(i).getDato().estaCerrado()) return false;
        }
        return true;
    }

    /** Compatibilidad con operaciones del Hito 1. */
    boolean finalizarPrimerTrabajoPorDescripcion(String descripcion) {
        ElementoArbolGeneral<Trabajo> n = trabajos.buscarNodo(t -> !t.esRaizTecnica()
                && !t.estaCerrado()
                && t.getDescripcion().equalsIgnoreCase(descripcion));
        if (n == null) return false;
        Trabajo t = n.getDato();
        if (t.getAprobacion() == EstadoAprobacion.PENDIENTE) t.setAprobacion(EstadoAprobacion.APROBADO);
        if (hijosCerrados(n)) {
            t.setEstado(EstadoTrabajo.TERMINADO);
            return true;
        }
        return false;
    }
}

