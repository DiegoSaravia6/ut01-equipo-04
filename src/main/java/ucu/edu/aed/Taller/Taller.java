package ucu.edu.aed.Taller;

import java.util.NoSuchElementException;

import ucu.edu.aed.tda.Cola;
import ucu.edu.aed.tda.Lista;

public class Taller {

    private Cola<Vehiculo> vehiculosEnEspera;
    private Cola<Vehiculo> esperandoRepuestos;

    private Lista<Tallerista> talleristas;
    private Lista<Vehiculo> vehiculosEnTrabajo;

    private Lista<Vehiculo> vehiculosProntos;

    public Taller() {
        vehiculosEnEspera = new Cola<>();
        esperandoRepuestos = new Cola<>();

        talleristas = new Lista<>();
        vehiculosEnTrabajo = new Lista<>();

        vehiculosProntos = new Lista<>();
    }

    // =========================================================
    // INGRESO DE VEHÍCULOS
    // =========================================================

    /**
     * Registra un vehículo que llega por un mantenimiento ya planificado.
     * Como el trabajo a realizar ya se conoce, el mantenimiento se registra
     * inmediatamente como una reparación pendiente.
     */
    public void registrarMantenimientoPlanificado(
            Vehiculo vehiculo,
            String descripcionMantenimiento) {

        validarVehiculoParaIngreso(vehiculo);
        validarDetalleIngreso(descripcionMantenimiento);

        vehiculo.registrarIngreso(
                TipoIngreso.MANTENIMIENTO_PLANIFICADO,
                descripcionMantenimiento
        );

        vehiculo.agregarReparacion(
                new Reparacion(
                        descripcionMantenimiento.trim(),
                        "Mantenimiento planificado"
                )
        );

        vehiculosEnEspera.poneEnCola(vehiculo);
    }

    /**
     * Registra un vehículo que llega por un problema informado por el dueño.
     * El problema se conserva como motivo de ingreso, pero no se crea una
     * reparación todavía porque primero debe diagnosticarse la falla.
     */
    public void registrarProblemaInformado(
            Vehiculo vehiculo,
            String problemaInformado) {

        validarVehiculoParaIngreso(vehiculo);
        validarDetalleIngreso(problemaInformado);

        vehiculo.registrarIngreso(
                TipoIngreso.PROBLEMA_INFORMADO,
                problemaInformado
        );

        vehiculosEnEspera.poneEnCola(vehiculo);
    }

    private void validarVehiculoParaIngreso(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException(
                    "El vehículo no puede ser null"
            );
        }

        if (vehiculo.tieneIngresoRegistrado()) {
            throw new IllegalStateException(
                    "El vehículo ya fue registrado en el taller"
            );
        }
    }

    private void validarDetalleIngreso(String detalle) {
        if (detalle == null || detalle.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El motivo de ingreso no puede estar vacío"
            );
        }
    }

    /**
     * Retorna el próximo vehículo que debe ser atendido,
     * sin removerlo de la cola.
     */
    public Vehiculo proximoVehiculo() {
        return vehiculosEnEspera.frente();
    }

    /**
     * Retorna la cantidad de vehículos esperando atención.
     */
    public int cantidadVehiculosEnEspera() {
        return vehiculosEnEspera.tamaño();
    }

    // =========================================================
    // GESTIÓN DE TALLERISTAS
    // =========================================================

    /**
     * Registra un nuevo tallerista.
     */
    public void registrarTallerista(Tallerista tallerista) {
        if (tallerista == null) {
            throw new IllegalArgumentException(
                    "El tallerista no puede ser null"
            );
        }

        talleristas.agregar(tallerista);
    }

    /**
     * Busca el primer tallerista disponible.
     */
    public Tallerista buscarTalleristaDisponible() {
        return talleristas.buscar(Tallerista::estaDisponible);
    }

    /**
     * Atiende al próximo vehículo de la cola y lo asigna
     * al primer tallerista disponible.
     */
    public Vehiculo atenderSiguiente() {

        if (vehiculosEnEspera.esVacio()) {
            throw new NoSuchElementException(
                    "No hay vehículos esperando atención"
            );
        }

        Tallerista tallerista = buscarTalleristaDisponible();

        if (tallerista == null) {
            throw new IllegalStateException(
                    "No hay talleristas disponibles"
            );
        }

        Vehiculo vehiculo = vehiculosEnEspera.quitaDeCola();

        tallerista.asignarVehiculo(vehiculo);
        vehiculosEnTrabajo.agregar(vehiculo);

        return vehiculo;
    }

    // =========================================================
    // REPARACIONES
    // =========================================================

    /**
     * Agrega una reparación pendiente al vehículo.
     *
     * Las reparaciones se almacenan en una pila, por lo que
     * la última reparación agregada será la próxima en realizarse.
     * Para un vehículo que ingresó por problema informado, la primera
     * reparación agregada puede representar el resultado del diagnóstico.
     */
    public void agregarReparacion(
            Vehiculo vehiculo,
            Reparacion reparacion) {

        if (vehiculo == null) {
            throw new IllegalArgumentException(
                    "El vehículo no puede ser null"
            );
        }

        vehiculo.agregarReparacion(reparacion);
    }

    /**
     * Retorna la próxima reparación pendiente del vehículo.
     */
    public Reparacion proximaReparacion(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException(
                    "El vehículo no puede ser null"
            );
        }

        return vehiculo.proximaReparacion();
    }

    /**
     * Realiza la próxima reparación pendiente del vehículo.
     */
    public Reparacion realizarProximaReparacion(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException(
                    "El vehículo no puede ser null"
            );
        }

        return vehiculo.realizarProximaReparacion();
    }

    // =========================================================
    // ESPERA POR REPUESTOS
    // =========================================================

    /**
     * Envía un vehículo a la cola de espera por repuestos.
     */
    public void esperarRepuestos(Vehiculo vehiculo) {

        if (vehiculo == null) {
            throw new IllegalArgumentException(
                    "El vehículo no puede ser null"
            );
        }

        if (!vehiculosEnTrabajo.contiene(vehiculo)) {
            throw new IllegalStateException(
                    "El vehículo no está siendo trabajado"
            );
        }

        Tallerista tallerista = buscarTalleristaAsignado(vehiculo);

        vehiculosEnTrabajo.remover(vehiculo);
        esperandoRepuestos.poneEnCola(vehiculo);
        tallerista.liberarVehiculo();
    }

    /**
     * Retorna el próximo vehículo esperando repuestos.
     */
    public Vehiculo proximoEsperandoRepuestos() {
        return esperandoRepuestos.frente();
    }

    /**
     * Retorna la cantidad de vehículos esperando repuestos.
     */
    public int cantidadEsperandoRepuestos() {
        return esperandoRepuestos.tamaño();
    }

    /**
     * Devuelve el próximo vehículo de la cola de repuestos
     * al trabajo.
     */
    public Vehiculo continuarConRepuestos() {

        if (esperandoRepuestos.esVacio()) {
            throw new NoSuchElementException(
                    "No hay vehículos esperando repuestos"
            );
        }

        Tallerista tallerista = buscarTalleristaDisponible();

        if (tallerista == null) {
            throw new IllegalStateException(
                    "No hay talleristas disponibles"
            );
        }

        Vehiculo vehiculo = esperandoRepuestos.quitaDeCola();

        tallerista.asignarVehiculo(vehiculo);
        vehiculosEnTrabajo.agregar(vehiculo);

        return vehiculo;
    }

    // =========================================================
    // FINALIZACIÓN
    // =========================================================

    /**
     * Finaliza el trabajo de un vehículo y libera al tallerista.
     */
    public void finalizarVehiculo(Vehiculo vehiculo) {

        if (vehiculo == null) {
            throw new IllegalArgumentException(
                    "El vehículo no puede ser null"
            );
        }

        if (!vehiculosEnTrabajo.contiene(vehiculo)) {
            throw new IllegalStateException(
                    "El vehículo no está siendo trabajado"
            );
        }

        if (vehiculo.tieneReparacionesPendientes()) {
            throw new IllegalStateException(
                    "El vehículo tiene reparaciones pendientes"
            );
        }

        Tallerista tallerista = buscarTalleristaAsignado(vehiculo);

        vehiculosEnTrabajo.remover(vehiculo);
        tallerista.liberarVehiculo();
        vehiculosProntos.agregar(vehiculo);
    }

    /**
     * Busca al tallerista que tiene asignado el vehículo. La búsqueda se
     * realiza antes de modificar las estructuras para conservar el estado
     * del sistema si la relación vehículo/tallerista no es válida.
     */
    private Tallerista buscarTalleristaAsignado(Vehiculo vehiculo) {

        Tallerista tallerista = talleristas.buscar(
                t -> t.getVehiculoActual() == vehiculo
        );

        if (tallerista == null) {
            throw new IllegalStateException(
                    "No hay un tallerista asignado a ese vehículo"
            );
        }

        return tallerista;
    }

    // =========================================================
    // CONSULTAS
    // =========================================================

    /**
     * Retorna la cantidad de vehículos actualmente en trabajo.
     */
    public int cantidadVehiculosEnTrabajo() {
        return vehiculosEnTrabajo.tamaño();
    }

    public int cantidadTalleristas() {
        return talleristas.tamaño();
    }

    public int cantidadVehiculosProntos() {
        return vehiculosProntos.tamaño();
    }

    public boolean estaProntoParaRetirar(Vehiculo vehiculo) {
        return vehiculosProntos.contiene(vehiculo);
    }
}
