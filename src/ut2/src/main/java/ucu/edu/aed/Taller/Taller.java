package ucu.edu.aed.Taller;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.NoSuchElementException;

import ucu.edu.aed.tda.ArbolAVL;
import ucu.edu.aed.tda.Cola;
import ucu.edu.aed.tda.Lista;
import ucu.edu.aed.tda.ListaArreglo;
import ucu.edu.aed.tda.MonticuloBinario;

/**
 * Sistema del taller. Mantiene las operaciones del Hito 1 y agrega índices,
 * prioridad de atención y órdenes ramificadas para el Hito 2.
 */
public class Taller {

    /* Hito 2: reemplaza la cola FIFO de espera. Sin fecha comprometida,
       el comparator conserva FIFO usando ordenLlegada. */
    private final MonticuloBinario<Vehiculo> vehiculosEnEspera;

    /* Hito 1: la espera de repuestos a nivel vehículo se conserva para compatibilidad. */
    private final Cola<Vehiculo> esperandoRepuestos;

    private final Lista<Tallerista> talleristas;
    private final Lista<Vehiculo> vehiculosEnTrabajo;
    private final Lista<Vehiculo> vehiculosProntos;

    /* Hito 2: índices balanceados. */
    private final ArbolAVL<Vehiculo> indiceVehiculosPorPatente;
    private final ArbolAVL<RegistroOrdenFecha> indiceOrdenesPorFecha;

    private long siguienteOrdenLlegada = 1;
    private int siguienteOrdenTrabajo = 1;

    public Taller() {
        Comparator<Vehiculo> criterioAtencion = (a, b) -> {
            LocalDate fa = a.getFechaEntregaComprometida();
            LocalDate fb = b.getFechaEntregaComprometida();

            // Los vehículos con compromiso siempre preceden a los que no tienen fecha.
            if (fa != null && fb == null) return -1;
            if (fa == null && fb != null) return 1;

            // Entre comprometidos, la fecha más cercana tiene mayor prioridad.
            if (fa != null) {
                int cmp = fa.compareTo(fb);
                if (cmp != 0) return cmp;
            }

            // Empates y vehículos sin fecha: FIFO por orden de llegada.
            return Long.compare(a.getOrdenLlegada(), b.getOrdenLlegada());
        };

        vehiculosEnEspera = new MonticuloBinario<>(criterioAtencion);
        esperandoRepuestos = new Cola<>();
        talleristas = new Lista<>();
        vehiculosEnTrabajo = new Lista<>();
        vehiculosProntos = new Lista<>();
        indiceVehiculosPorPatente = new ArbolAVL<>();
        indiceOrdenesPorFecha = new ArbolAVL<>();
    }

    // =========================================================
    // INGRESO DE VEHÍCULOS
    // =========================================================

    /** Compatibilidad Hito 1: sin fecha comprometida, costo 0 y fecha de hoy. */
    public void registrarMantenimientoPlanificado(Vehiculo vehiculo, String descripcionMantenimiento) {
        registrarMantenimientoPlanificadoInterno(
                vehiculo, descripcionMantenimiento, 0.0,
                LocalDate.now(), null, true
        );
    }

    public OrdenTrabajo registrarMantenimientoPlanificado(
            Vehiculo vehiculo,
            String descripcionMantenimiento,
            double costo,
            LocalDate fechaIngreso,
            LocalDate fechaEntregaComprometida) {
        return registrarMantenimientoPlanificadoInterno(
                vehiculo, descripcionMantenimiento, costo,
                fechaIngreso, fechaEntregaComprometida, false
        );
    }

    private OrdenTrabajo registrarMantenimientoPlanificadoInterno(
            Vehiculo vehiculo,
            String descripcionMantenimiento,
            double costo,
            LocalDate fechaIngreso,
            LocalDate fechaEntregaComprometida,
            boolean compatibilidadHito1) {

        validarVehiculoParaIngreso(vehiculo);
        validarDetalleIngreso(descripcionMantenimiento);
        validarFechas(fechaIngreso, fechaEntregaComprometida);

        vehiculo.registrarIngreso(TipoIngreso.MANTENIMIENTO_PLANIFICADO, descripcionMantenimiento);
        if (compatibilidadHito1) {
            vehiculo.agregarReparacion(new Reparacion(
                    descripcionMantenimiento.trim(), "Mantenimiento planificado"));
        }

        OrdenTrabajo orden = crearOrden(vehiculo, fechaIngreso);
        orden.agregarTrabajoPrincipal(
                descripcionMantenimiento.trim(),
                vehiculo.getParteRaiz(),
                costo,
                true // el mantenimiento pedido por el cliente ya está autorizado
        );

        completarIngreso(vehiculo, fechaEntregaComprometida);
        return orden;
    }

    /** Compatibilidad Hito 1: sin fecha comprometida y fecha de hoy. */
    public void registrarProblemaInformado(Vehiculo vehiculo, String problemaInformado) {
        registrarProblemaInformado(vehiculo, problemaInformado, LocalDate.now(), null);
    }

    public OrdenTrabajo registrarProblemaInformado(
            Vehiculo vehiculo,
            String problemaInformado,
            LocalDate fechaIngreso,
            LocalDate fechaEntregaComprometida) {

        validarVehiculoParaIngreso(vehiculo);
        validarDetalleIngreso(problemaInformado);
        validarFechas(fechaIngreso, fechaEntregaComprometida);

        vehiculo.registrarIngreso(TipoIngreso.PROBLEMA_INFORMADO, problemaInformado);
        OrdenTrabajo orden = crearOrden(vehiculo, fechaIngreso);

        // Un problema informado no es todavía una reparación conocida.
        // Se crea un diagnóstico de costo 0 que debe resolverse antes de cerrar la orden.
        orden.agregarDiagnosticoInicial(
                problemaInformado.trim(),
                vehiculo.getParteRaiz()
        );

        completarIngreso(vehiculo, fechaEntregaComprometida);
        return orden;
    }

    private OrdenTrabajo crearOrden(Vehiculo vehiculo, LocalDate fechaIngreso) {
        OrdenTrabajo orden = new OrdenTrabajo(siguienteOrdenTrabajo++, fechaIngreso);
        vehiculo.agregarOrden(orden);
        indiceOrdenesPorFecha.insertar(
                new RegistroOrdenFecha(fechaIngreso, orden.getId(), orden)
        );
        return orden;
    }

    private void completarIngreso(Vehiculo vehiculo, LocalDate fechaEntregaComprometida) {
        vehiculo.configurarPrioridad(fechaEntregaComprometida, siguienteOrdenLlegada++);
        indiceVehiculosPorPatente.insertar(vehiculo);
        vehiculosEnEspera.insertar(vehiculo);
    }

    private void validarVehiculoParaIngreso(Vehiculo vehiculo) {
        if (vehiculo == null) throw new IllegalArgumentException("El vehículo no puede ser null");
        if (vehiculo.tieneIngresoRegistrado()) {
            throw new IllegalStateException("El vehículo ya fue registrado en el taller");
        }
        Vehiculo existente = buscarVehiculoPorPatente(vehiculo.getPatente());
        if (existente != null) {
            throw new IllegalStateException("Ya existe un vehículo registrado con esa patente");
        }
    }

    private void validarDetalleIngreso(String detalle) {
        if (detalle == null || detalle.trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo de ingreso no puede estar vacío");
        }
    }

    private void validarFechas(LocalDate fechaIngreso, LocalDate fechaEntrega) {
        if (fechaIngreso == null) throw new IllegalArgumentException("La fecha de ingreso no puede ser null");
        if (fechaEntrega != null && fechaEntrega.isBefore(fechaIngreso)) {
            throw new IllegalArgumentException("La fecha comprometida no puede ser anterior al ingreso");
        }
    }

    public Vehiculo proximoVehiculo() { return vehiculosEnEspera.primero(); }
    public int cantidadVehiculosEnEspera() { return vehiculosEnEspera.tamaño(); }

    /**
     * Reprograma un vehículo aún en espera. Buscar la patente cuesta O(log n)
     * en el AVL; el vehículo recuerda su posición en el heap, por lo que restaurar
     * la prioridad luego de cambiar la fecha cuesta O(log n).
     */
    public void reprogramarVehiculo(String patente, LocalDate nuevaFecha) {
        Vehiculo vehiculo = buscarVehiculoPorPatente(patente);
        if (vehiculo == null) throw new IllegalArgumentException("No existe el vehículo " + patente);
        if (!vehiculosEnEspera.contiene(vehiculo)) {
            throw new IllegalStateException("El vehículo ya no está en espera de atención");
        }
        OrdenTrabajo orden = vehiculo.getOrdenActual();
        if (nuevaFecha != null && orden != null && nuevaFecha.isBefore(orden.getFechaIngreso())) {
            throw new IllegalArgumentException("La nueva fecha comprometida no puede ser anterior al ingreso");
        }
        vehiculo.reprogramarEntrega(nuevaFecha);
        vehiculosEnEspera.reordenar(vehiculo);
    }

    // =========================================================
    // ÍNDICES / CONSULTAS
    // =========================================================

    /** Búsqueda O(log n) promedio/peor caso por tratarse de un AVL. */
    public Vehiculo buscarVehiculoPorPatente(String patente) {
        if (patente == null || patente.trim().isEmpty()) return null;
        String clave = patente.trim();
        return indiceVehiculosPorPatente.buscar(v -> clave.compareToIgnoreCase(v.getPatente()));
    }

    /** Rango O(log n + k) en un AVL, siendo k la cantidad de órdenes devueltas. */
    public ListaArreglo<OrdenTrabajo> buscarOrdenesEntre(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) throw new IllegalArgumentException("Las fechas son obligatorias");
        ListaArreglo<RegistroOrdenFecha> registros = indiceOrdenesPorFecha.listarRango(
                RegistroOrdenFecha.limiteInferior(desde),
                RegistroOrdenFecha.limiteSuperior(hasta)
        );
        ListaArreglo<OrdenTrabajo> resultado = new ListaArreglo<>();
        for (int i = 0; i < registros.tamaño(); i++) resultado.agregar(registros.obtener(i).getOrden());
        return resultado;
    }

    // =========================================================
    // GESTIÓN DE TALLERISTAS
    // =========================================================

    public void registrarTallerista(Tallerista tallerista) {
        if (tallerista == null) throw new IllegalArgumentException("El tallerista no puede ser null");
        if (buscarTalleristaPorId(tallerista.getId()) != null) {
            throw new IllegalStateException("Ya existe un tallerista con ID " + tallerista.getId());
        }
        talleristas.agregar(tallerista);
    }

    public Tallerista buscarTalleristaPorId(int id) {
        return talleristas.buscar(t -> t.getId() == id);
    }

    public Tallerista buscarTalleristaDisponible() {
        return talleristas.buscar(Tallerista::estaDisponible);
    }

    public Vehiculo atenderSiguiente() {
        if (vehiculosEnEspera.esVacio()) {
            throw new NoSuchElementException("No hay vehículos esperando atención");
        }
        Tallerista tallerista = buscarTalleristaDisponible();
        if (tallerista == null) throw new IllegalStateException("No hay talleristas disponibles");

        Vehiculo vehiculo = vehiculosEnEspera.extraerPrimero();
        tallerista.asignarVehiculo(vehiculo);
        vehiculosEnTrabajo.agregar(vehiculo);
        return vehiculo;
    }

    // =========================================================
    // REPARACIONES DEL HITO 1 + PUENTE AL NUEVO MODELO
    // =========================================================

    public void agregarReparacion(Vehiculo vehiculo, Reparacion reparacion) {
        if (vehiculo == null) throw new IllegalArgumentException("El vehículo no puede ser null");
        vehiculo.agregarReparacion(reparacion);

        // Si el vehículo ya tiene una orden UT2, la operación legacy se representa
        // como un trabajo principal sin costo conocido sobre la raíz del vehículo.
        OrdenTrabajo orden = vehiculo.getOrdenActual();
        if (orden != null) {
            orden.agregarTrabajoPrincipal(
                    reparacion.getDescripcion(), vehiculo.getParteRaiz(), 0.0, true);
        }
    }

    public Reparacion proximaReparacion(Vehiculo vehiculo) {
        if (vehiculo == null) throw new IllegalArgumentException("El vehículo no puede ser null");
        return vehiculo.proximaReparacion();
    }

    public Reparacion realizarProximaReparacion(Vehiculo vehiculo) {
        if (vehiculo == null) throw new IllegalArgumentException("El vehículo no puede ser null");
        return vehiculo.realizarProximaReparacion();
    }

    // =========================================================
    // TRABAJOS RAMIFICADOS DEL HITO 2
    // =========================================================

    public Trabajo registrarTrabajoPrincipal(
            Vehiculo vehiculo, String descripcion, String codigoParte,
            double costo, boolean aprobado) {
        OrdenTrabajo orden = ordenDe(vehiculo);
        ParteVehiculo parte = parteDe(vehiculo, codigoParte);
        return orden.agregarTrabajoPrincipal(descripcion, parte, costo, aprobado);
    }

    public Trabajo registrarTrabajoDerivado(
            Vehiculo vehiculo, int idTrabajoOrigen, String descripcion,
            String codigoParte, double costo) {
        OrdenTrabajo orden = ordenDe(vehiculo);
        ParteVehiculo parte = parteDe(vehiculo, codigoParte);
        return orden.agregarTrabajoDerivado(idTrabajoOrigen, descripcion, parte, costo);
    }

    public void aprobarTrabajo(Vehiculo vehiculo, int idTrabajo) {
        ordenDe(vehiculo).aprobarTrabajo(idTrabajo);
    }

    public void rechazarTrabajo(Vehiculo vehiculo, int idTrabajo) {
        ordenDe(vehiculo).rechazarTrabajo(idTrabajo);
    }

    public void iniciarTrabajo(Vehiculo vehiculo, int idTrabajo) {
        ordenDe(vehiculo).iniciarTrabajo(idTrabajo);
    }

    public void finalizarTrabajo(Vehiculo vehiculo, int idTrabajo) {
        ordenDe(vehiculo).finalizarTrabajo(idTrabajo);
    }

    /**
     * Suspende solo una rama. Si queda otra rama ejecutable, el vehículo sigue
     * en trabajo. Si no queda ninguna, pasa a la cola de repuestos y se libera el tallerista.
     */
    public void suspenderTrabajoPorRepuesto(Vehiculo vehiculo, int idTrabajo) {
        if (!vehiculosEnTrabajo.contiene(vehiculo)) {
            throw new IllegalStateException("El vehículo no está siendo trabajado");
        }
        OrdenTrabajo orden = ordenDe(vehiculo);
        orden.suspenderPorRepuesto(idTrabajo);

        if (!orden.tieneTrabajoEjecutable()) {
            Tallerista tallerista = buscarTalleristaAsignado(vehiculo);
            vehiculosEnTrabajo.remover(vehiculo);
            esperandoRepuestos.poneEnCola(vehiculo);
            tallerista.liberarVehiculo();
        }
    }

    public void reanudarTrabajoPorRepuesto(Vehiculo vehiculo, int idTrabajo) {
        ordenDe(vehiculo).reanudarPorRepuesto(idTrabajo);
    }

    public ListaArreglo<Trabajo> consultarOrdenPendiente(Vehiculo vehiculo) {
        return ordenDe(vehiculo).ordenPendiente();
    }

    public ListaArreglo<Trabajo> consultarTrabajosBloqueados(Vehiculo vehiculo, int idTrabajo) {
        return ordenDe(vehiculo).trabajosBloqueadosPor(idTrabajo);
    }

    public double costoOrden(Vehiculo vehiculo) { return ordenDe(vehiculo).costoPropuestoTotal(); }
    public double costoAutorizadoOrden(Vehiculo vehiculo) { return ordenDe(vehiculo).costoAutorizadoTotal(); }
    public double costoRama(Vehiculo vehiculo, int idTrabajo) {
        return ordenDe(vehiculo).costoPropuestoSubarbol(idTrabajo);
    }

    private OrdenTrabajo ordenDe(Vehiculo vehiculo) {
        if (vehiculo == null || vehiculo.getOrdenActual() == null) {
            throw new IllegalArgumentException("El vehículo no tiene una orden activa");
        }
        return vehiculo.getOrdenActual();
    }

    private ParteVehiculo parteDe(Vehiculo vehiculo, String codigoParte) {
        ParteVehiculo parte = vehiculo.buscarParte(codigoParte);
        if (parte == null) throw new IllegalArgumentException("No existe la parte " + codigoParte);
        return parte;
    }

    // =========================================================
    // ESPERA POR REPUESTOS - OPERACIÓN LEGACY DEL HITO 1
    // =========================================================

    public void esperarRepuestos(Vehiculo vehiculo) {
        if (vehiculo == null) throw new IllegalArgumentException("El vehículo no puede ser null");
        if (!vehiculosEnTrabajo.contiene(vehiculo)) {
            throw new IllegalStateException("El vehículo no está siendo trabajado");
        }
        Tallerista tallerista = buscarTalleristaAsignado(vehiculo);
        vehiculosEnTrabajo.remover(vehiculo);
        esperandoRepuestos.poneEnCola(vehiculo);
        tallerista.liberarVehiculo();
    }

    public Vehiculo proximoEsperandoRepuestos() { return esperandoRepuestos.frente(); }
    public int cantidadEsperandoRepuestos() { return esperandoRepuestos.tamaño(); }

    public Vehiculo continuarConRepuestos() {
        if (esperandoRepuestos.esVacio()) {
            throw new NoSuchElementException("No hay vehículos esperando repuestos");
        }
        Tallerista tallerista = buscarTalleristaDisponible();
        if (tallerista == null) throw new IllegalStateException("No hay talleristas disponibles");
        Vehiculo vehiculo = esperandoRepuestos.quitaDeCola();
        tallerista.asignarVehiculo(vehiculo);
        vehiculosEnTrabajo.agregar(vehiculo);
        return vehiculo;
    }

    // =========================================================
    // FINALIZACIÓN
    // =========================================================

    public void finalizarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) throw new IllegalArgumentException("El vehículo no puede ser null");
        if (!vehiculosEnTrabajo.contiene(vehiculo)) {
            throw new IllegalStateException("El vehículo no está siendo trabajado");
        }
        if (vehiculo.tieneReparacionesPendientes()) {
            throw new IllegalStateException("El vehículo tiene reparaciones pendientes");
        }
        OrdenTrabajo orden = vehiculo.getOrdenActual();
        if (orden != null && !orden.estaCerrada()) {
            throw new IllegalStateException("La orden de trabajo tiene ramas pendientes");
        }

        Tallerista tallerista = buscarTalleristaAsignado(vehiculo);
        vehiculosEnTrabajo.remover(vehiculo);
        tallerista.liberarVehiculo();
        vehiculosProntos.agregar(vehiculo);
    }

    private Tallerista buscarTalleristaAsignado(Vehiculo vehiculo) {
        Tallerista tallerista = talleristas.buscar(t -> t.getVehiculoActual() == vehiculo);
        if (tallerista == null) {
            throw new IllegalStateException("No hay un tallerista asignado a ese vehículo");
        }
        return tallerista;
    }

    public int cantidadVehiculosEnTrabajo() { return vehiculosEnTrabajo.tamaño(); }
    public int cantidadTalleristas() { return talleristas.tamaño(); }
    public int cantidadVehiculosProntos() { return vehiculosProntos.tamaño(); }
    public boolean estaProntoParaRetirar(Vehiculo vehiculo) { return vehiculosProntos.contiene(vehiculo); }
}
