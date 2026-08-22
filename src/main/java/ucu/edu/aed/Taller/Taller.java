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
    // GESTIÓN DE VEHÍCULOS
    // =========================================================

    /**
     * Registra un vehículo y lo coloca al final de la cola
     * de espera.
     */
    public void registrarVehiculo(Vehiculo vehiculo) {
        vehiculosEnEspera.poneEnCola(vehiculo);
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
     */
    public void agregarReparacion(
            Vehiculo vehiculo,
            Reparacion reparacion) {

        vehiculo.agregarReparacion(reparacion);
    }

    /**
     * Retorna la próxima reparación pendiente del vehículo.
     */
    public Reparacion proximaReparacion(Vehiculo vehiculo) {
        return vehiculo.proximaReparacion();
    }

    /**
     * Realiza la próxima reparación pendiente del vehículo.
     */
    public Reparacion realizarProximaReparacion(Vehiculo vehiculo) {
        return vehiculo.realizarProximaReparacion();
    }

    // =========================================================
    // ESPERA POR REPUESTOS
    // =========================================================

    /**
     * Envía un vehículo a la cola de espera por repuestos.
     */
    public void esperarRepuestos(Vehiculo vehiculo) {

        vehiculosEnTrabajo.remover(vehiculo);

        esperandoRepuestos.poneEnCola(vehiculo);

        liberarTallerista(vehiculo);
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

    Tallerista tallerista =
            buscarTalleristaDisponible();

    if (tallerista == null) {
        throw new IllegalStateException(
                "No hay talleristas disponibles"
        );
    }

    Vehiculo vehiculo =
            esperandoRepuestos.quitaDeCola();

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

    vehiculosEnTrabajo.remover(vehiculo);

    liberarTallerista(vehiculo);

    vehiculosProntos.agregar(vehiculo);
    }

    /**
     * Libera al tallerista que estaba trabajando con el vehículo.
     */
    private void liberarTallerista(Vehiculo vehiculo) {

        for (int i = 0; i < talleristas.tamaño(); i++) {

            Tallerista tallerista = talleristas.obtener(i);

            if (tallerista.getVehiculoActual() == vehiculo) {
                tallerista.liberarVehiculo();
                return;
            }
        }
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

    /**
     * Retorna la cantidad de talleristas registrados.
     */
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