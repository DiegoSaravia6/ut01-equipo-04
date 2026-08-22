package ucu.edu.aed.Taller;

import ucu.edu.aed.tda.Lista;
import ucu.edu.aed.tda.Pila;

public class Vehiculo {

    private String patente;
    private String marca;
    private String modelo;
    private String dueño;

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

    // =========================================================
    // REPARACIONES PENDIENTES
    // =========================================================

    public void agregarReparacion(Reparacion reparacion) {
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