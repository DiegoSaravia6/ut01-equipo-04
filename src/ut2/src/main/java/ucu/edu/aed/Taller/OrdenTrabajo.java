package ucu.edu.aed.Taller;

import java.time.LocalDate;

import ucu.edu.aed.tda.ArbolGeneral;
import ucu.edu.aed.tda.ElementoArbolGeneral;
import ucu.edu.aed.tda.ListaArreglo;

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

    // Solo existe cuando el vehículo ingresó por un problema informado.
    // Se guarda para poder identificar fácilmente el trabajo de diagnóstico inicial.
    private Trabajo diagnosticoInicial;

    public OrdenTrabajo(int id, LocalDate fechaIngreso) {
        if (fechaIngreso == null) throw new IllegalArgumentException("La fecha de ingreso no puede ser null");
        this.id = id;
        this.fechaIngreso = fechaIngreso;
        this.trabajos = new ArbolGeneral<>(Trabajo.crearRaizTecnica());
    }

    public int getId() { return id; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public ArbolGeneral<Trabajo> getArbolTrabajos() { return trabajos; }
    public Trabajo getDiagnosticoInicial() { return diagnosticoInicial; }

    /**
     * Crea el diagnóstico automático de un ingreso por problema informado.
     * Es un trabajo normal, aprobado y de costo 0, asociado al vehículo completo.
     */
    Trabajo agregarDiagnosticoInicial(String problema, ParteVehiculo parteRaiz) {
        if (diagnosticoInicial != null) {
            throw new IllegalStateException("La orden ya tiene un diagnóstico inicial");
        }
        diagnosticoInicial = agregarTrabajoPrincipal(
                "Diagnosticar: " + problema,
                parteRaiz,
                0.0,
                true
        );
        return diagnosticoInicial;
    }

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

        // Si durante un trabajo aparece otro que depende de él, el padre vuelve
        // a PENDIENTE hasta que se resuelva el trabajo derivado.
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
        Trabajo objetivo = n.getDato();
        if (objetivo.getEstado() == EstadoTrabajo.EN_PROGRESO) {
            throw new IllegalStateException("No se puede rechazar un trabajo en progreso");
        }
        if (objetivo.getEstado() == EstadoTrabajo.TERMINADO) {
            throw new IllegalStateException("No se puede rechazar un trabajo terminado");
        }
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

        // Un trabajo no puede pasar directamente de PENDIENTE a TERMINADO.
        // Primero debe haberse iniciado.
        if (t.getEstado() != EstadoTrabajo.EN_PROGRESO) {
            throw new IllegalStateException("El trabajo debe estar en progreso para poder finalizarlo");
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

    /**
     * Recorre el árbol una sola vez buscando un trabajo que pueda iniciarse.
     * No arma una lista auxiliar ni vuelve a buscar cada trabajo por separado.
     */
    public boolean tieneTrabajoEjecutable() {
        ElementoArbolGeneral<Trabajo> raiz = trabajos.obtenerRaiz();
        for (int i = 0; i < raiz.cantidadHijos(); i++) {
            if (tieneTrabajoEjecutable(raiz.obtenerHijo(i))) return true;
        }
        return false;
    }

    private boolean tieneTrabajoEjecutable(ElementoArbolGeneral<Trabajo> n) {
        // Primero se revisan los hijos porque deben resolverse antes que el padre.
        for (int i = 0; i < n.cantidadHijos(); i++) {
            if (tieneTrabajoEjecutable(n.obtenerHijo(i))) return true;
        }

        Trabajo t = n.getDato();
        return t.getEstado() == EstadoTrabajo.PENDIENTE
                && t.getAprobacion() == EstadoAprobacion.APROBADO
                && hijosCerrados(n);
    }

    public double costoPropuestoTotal() {
        return costoPropuestoInterno(trabajos.obtenerRaiz());
    }

    public double costoAutorizadoTotal() {
        return costoAutorizadoInterno(trabajos.obtenerRaiz());
    }

    public double costoPropuestoSubarbol(int idTrabajo) {
        return costoPropuestoInterno(nodoTrabajo(idTrabajo));
    }

    public double costoAutorizadoSubarbol(int idTrabajo) {
        return costoAutorizadoInterno(nodoTrabajo(idTrabajo));
    }

    /** Suma todos los trabajos presupuestados, estén o no aprobados. */
    private double costoPropuestoInterno(ElementoArbolGeneral<Trabajo> n) {
        double total = 0;
        Trabajo t = n.getDato();
        if (!t.esRaizTecnica()) {
            total += t.getCosto();
        }
        for (int i = 0; i < n.cantidadHijos(); i++) {
            total += costoPropuestoInterno(n.obtenerHijo(i));
        }
        return total;
    }

    /** Suma solamente los trabajos que el cliente aprobó. */
    private double costoAutorizadoInterno(ElementoArbolGeneral<Trabajo> n) {
        double total = 0;
        Trabajo t = n.getDato();
        if (!t.esRaizTecnica() && t.getAprobacion() == EstadoAprobacion.APROBADO) {
            total += t.getCosto();
        }
        for (int i = 0; i < n.cantidadHijos(); i++) {
            total += costoAutorizadoInterno(n.obtenerHijo(i));
        }
        return total;
    }

    /** La orden se cierra cuando tiene trabajos y todos los principales están cerrados. */
    public boolean estaCerrada() {
        ElementoArbolGeneral<Trabajo> raiz = trabajos.obtenerRaiz();
        if (raiz.cantidadHijos() == 0) return false;
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
            // El método legacy "realizar reparación" representa inicio y fin en una sola operación.
            t.setEstado(EstadoTrabajo.TERMINADO);
            return true;
        }
        return false;
    }
}
