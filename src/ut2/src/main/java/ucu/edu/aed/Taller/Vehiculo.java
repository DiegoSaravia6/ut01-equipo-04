package ucu.edu.aed.Taller;

import java.time.LocalDate;

import ucu.edu.aed.tda.ArbolGeneral;
import ucu.edu.aed.tda.Lista;
import ucu.edu.aed.tda.ListaArreglo;
import ucu.edu.aed.tda.IndexableMonticulo;
import ucu.edu.aed.tda.Pila;

/**
 * Vehículo del taller. Conserva las operaciones del Hito 1 y agrega la
 * estructura jerárquica de partes, órdenes y datos de prioridad del Hito 2.
 */
public class Vehiculo implements Comparable<Vehiculo>, IndexableMonticulo {

    private String patente;
    private String marca;
    private String modelo;
    private String dueño;

    // Hito 1: motivo de ingreso.
    private TipoIngreso tipoIngreso;
    private String detalleIngreso;

    // Hito 1: compatibilidad con reparaciones lineales.
    private Pila<Reparacion> reparacionesPendientes;
    private Lista<Reparacion> reparacionesRealizadas;

    // Hito 2: estructura del vehículo y órdenes ramificadas.
    private final ArbolGeneral<ParteVehiculo> estructuraPartes;
    private final ListaArreglo<OrdenTrabajo> ordenes;
    private OrdenTrabajo ordenActual;

    // Hito 2: prioridad de atención.
    private LocalDate fechaEntregaComprometida;
    private long ordenLlegada;
    private int indiceMonticulo = -1;

    public Vehiculo(String patente, String marca, String modelo, String dueño) {
        if (patente == null || patente.trim().isEmpty()) {
            throw new IllegalArgumentException("La patente no puede estar vacía");
        }
        this.patente = patente.trim();
        this.marca = marca;
        this.modelo = modelo;
        this.dueño = dueño;

        this.tipoIngreso = null;
        this.detalleIngreso = null;
        this.reparacionesPendientes = new Pila<>();
        this.reparacionesRealizadas = new Lista<>();

        this.estructuraPartes = new ArbolGeneral<>(
                new ParteVehiculo("VEHICULO", "Vehículo completo")
        );
        this.ordenes = new ListaArreglo<>();
    }

    public String getPatente() { return patente; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public String getDueño() { return dueño; }
    public TipoIngreso getTipoIngreso() { return tipoIngreso; }
    public String getDetalleIngreso() { return detalleIngreso; }
    public LocalDate getFechaEntregaComprometida() { return fechaEntregaComprometida; }
    public long getOrdenLlegada() { return ordenLlegada; }
    @Override public int getIndiceMonticulo() { return indiceMonticulo; }
    @Override public void setIndiceMonticulo(int indice) { this.indiceMonticulo = indice; }

    public boolean tieneIngresoRegistrado() { return tipoIngreso != null; }

    void registrarIngreso(TipoIngreso tipoIngreso, String detalleIngreso) {
        if (tipoIngreso == null) throw new IllegalArgumentException("El tipo de ingreso no puede ser null");
        if (detalleIngreso == null || detalleIngreso.trim().isEmpty()) {
            throw new IllegalArgumentException("El detalle de ingreso no puede estar vacío");
        }
        if (tieneIngresoRegistrado()) {
            throw new IllegalStateException("El vehículo ya fue registrado en el taller");
        }
        this.tipoIngreso = tipoIngreso;
        this.detalleIngreso = detalleIngreso.trim();
    }

    void configurarPrioridad(LocalDate fechaEntregaComprometida, long ordenLlegada) {
        this.fechaEntregaComprometida = fechaEntregaComprometida;
        this.ordenLlegada = ordenLlegada;
    }

    void reprogramarEntrega(LocalDate nuevaFecha) {
        this.fechaEntregaComprometida = nuevaFecha;
    }

    // =========================================================
    // ESTRUCTURA JERÁRQUICA DE PARTES
    // =========================================================

    public ArbolGeneral<ParteVehiculo> getEstructuraPartes() { return estructuraPartes; }

    public ParteVehiculo getParteRaiz() { return estructuraPartes.obtenerRaiz().getDato(); }

    public ParteVehiculo buscarParte(String codigo) {
        if (codigo == null) return null;
        return estructuraPartes.buscar(p -> p.getCodigo().equalsIgnoreCase(codigo));
    }

    public ParteVehiculo agregarParte(String codigoPadre, String codigo, String nombre) {
        if (buscarParte(codigo) != null) {
            throw new IllegalArgumentException("Ya existe una parte con código " + codigo);
        }
        ParteVehiculo nueva = new ParteVehiculo(codigo, nombre);
        estructuraPartes.agregarHijo(
                p -> p.getCodigo().equalsIgnoreCase(codigoPadre),
                nueva
        );
        return nueva;
    }

    // =========================================================
    // ÓRDENES DE TRABAJO
    // =========================================================

    void agregarOrden(OrdenTrabajo orden) {
        if (orden == null) throw new IllegalArgumentException("La orden no puede ser null");
        ordenes.agregar(orden);
        ordenActual = orden;
    }

    public OrdenTrabajo getOrdenActual() { return ordenActual; }
    public ListaArreglo<OrdenTrabajo> getOrdenes() { return ordenes; }

    // =========================================================
    // OPERACIONES DEL HITO 1
    // =========================================================

    public void agregarReparacion(Reparacion reparacion) {
        if (reparacion == null) throw new IllegalArgumentException("La reparación no puede ser null");
        reparacionesPendientes.mete(reparacion);
    }

    public Reparacion proximaReparacion() { return reparacionesPendientes.tope(); }

    public Reparacion realizarProximaReparacion() {
        Reparacion reparacion = reparacionesPendientes.saca();
        reparacionesRealizadas.agregar(reparacion);
        if (ordenActual != null) {
            ordenActual.finalizarPrimerTrabajoPorDescripcion(reparacion.getDescripcion());
        }
        return reparacion;
    }

    public boolean tieneReparacionesPendientes() { return !reparacionesPendientes.esVacio(); }
    public int cantidadReparacionesRealizadas() { return reparacionesRealizadas.tamaño(); }
    public Lista<Reparacion> getReparacionesRealizadas() { return reparacionesRealizadas; }

    @Override
    public int compareTo(Vehiculo otro) {
        return patente.compareToIgnoreCase(otro.patente);
    }

    @Override
    public String toString() { return patente + " - " + marca + " " + modelo; }
}
