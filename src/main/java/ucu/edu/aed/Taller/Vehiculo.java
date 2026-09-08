package ucu.edu.aed.Taller;

import ucu.edu.aed.tda.Lista;
import ucu.edu.aed.tda.Pila;

public class Vehiculo {

    private String patente;
    private String marca;
    private String modelo;
    private String dueño;

    // Se completa cuando el vehículo es aceptado por el taller.
    private TipoIngreso tipoIngreso;
    private String detalleIngreso;

    // Reparaciones que todavía deben realizarse.
    private Pila<Reparacion> reparacionesPendientes;

    // Historial de reparaciones ya realizadas.
    private Lista<Reparacion> reparacionesRealizadas;

    public Vehiculo(
            String patente,
            String marca,
            String modelo,
            String dueño) {

        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.dueño = dueño;

        this.tipoIngreso = null;
        this.detalleIngreso = null;

        reparacionesPendientes = new Pila<>();
        reparacionesRealizadas = new Lista<>();
    }

    public String getPatente() {
        return patente;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getDueño() {
        return dueño;
    }

    public TipoIngreso getTipoIngreso() {
        return tipoIngreso;
    }

    public String getDetalleIngreso() {
        return detalleIngreso;
    }

    public boolean tieneIngresoRegistrado() {
        return tipoIngreso != null;
    }

    /**
     * Registra una sola vez el motivo por el que el vehículo ingresa.
     * El método tiene visibilidad de paquete para que el ingreso sea
     * coordinado por Taller y no desde cualquier parte de la aplicación.
     */
    void registrarIngreso(
            TipoIngreso tipoIngreso,
            String detalleIngreso) {

        if (tipoIngreso == null) {
            throw new IllegalArgumentException(
                    "El tipo de ingreso no puede ser null"
            );
        }

        if (detalleIngreso == null || detalleIngreso.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El detalle de ingreso no puede estar vacío"
            );
        }

        if (tieneIngresoRegistrado()) {
            throw new IllegalStateException(
                    "El vehículo ya fue registrado en el taller"
            );
        }

        this.tipoIngreso = tipoIngreso;
        this.detalleIngreso = detalleIngreso.trim();
    }

    // =========================================================
    // REPARACIONES PENDIENTES
    // =========================================================

    public void agregarReparacion(Reparacion reparacion) {
        if (reparacion == null) {
            throw new IllegalArgumentException(
                    "La reparación no puede ser null"
            );
        }

        reparacionesPendientes.mete(reparacion);
    }

    public Reparacion proximaReparacion() {
        return reparacionesPendientes.tope();
    }

    public Reparacion realizarProximaReparacion() {

        Reparacion reparacion =
                reparacionesPendientes.saca();

        reparacionesRealizadas.agregar(reparacion);

        return reparacion;
    }

    public boolean tieneReparacionesPendientes() {
        return !reparacionesPendientes.esVacio();
    }

    // =========================================================
    // HISTORIAL
    // =========================================================

    public int cantidadReparacionesRealizadas() {
        return reparacionesRealizadas.tamaño();
    }

    public Lista<Reparacion> getReparacionesRealizadas() {
        return reparacionesRealizadas;
    }
}
