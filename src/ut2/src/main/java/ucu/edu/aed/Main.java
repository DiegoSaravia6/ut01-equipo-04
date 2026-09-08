package ucu.edu.aed;

import java.time.LocalDate;

import ucu.edu.aed.Taller.OrdenTrabajo;
import ucu.edu.aed.Taller.Taller;
import ucu.edu.aed.Taller.Tallerista;
import ucu.edu.aed.Taller.Trabajo;
import ucu.edu.aed.Taller.Vehiculo;
import ucu.edu.aed.tda.ListaArreglo;

/**
 * Demostración ejecutable del Segundo Hito.
 * Los casos exhaustivos y borde se encuentran en src/test/java.
 */
public class Main {
    public static void main(String[] args) {
        Taller taller = new Taller();
        taller.registrarTallerista(new Tallerista("Carlos"));

        Vehiculo vehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Juan");
        vehiculo.agregarParte("VEHICULO", "MOTOR", "Motor");
        vehiculo.agregarParte("MOTOR", "DIST", "Distribución");
        vehiculo.agregarParte("DIST", "CORREA", "Correa");
        vehiculo.agregarParte("CORREA", "TENSOR", "Tensor");
        vehiculo.agregarParte("VEHICULO", "TREN", "Tren delantero");
        vehiculo.agregarParte("TREN", "FRENOS", "Frenos delanteros");

        OrdenTrabajo orden = taller.registrarProblemaInformado(
                vehiculo,
                "Ruido al acelerar",
                LocalDate.of(2026, 9, 8),
                LocalDate.of(2026, 9, 12)
        );

        taller.atenderSiguiente();

        Trabajo original = taller.registrarTrabajoPrincipal(
                vehiculo, "Cambiar correa", "CORREA", 4200, true);
        Trabajo adicional = taller.registrarTrabajoDerivado(
                vehiculo, original.getId(), "Cambiar tensor", "TENSOR", 1800);
        taller.aprobarTrabajo(vehiculo, adicional.getId());

        System.out.println("=== ORDEN DE TRABAJO #" + orden.getId() + " ===");
        System.out.println("Costo propuesto: " + taller.costoOrden(vehiculo));
        System.out.println("Orden pendiente (hijos antes que padres):");
        ListaArreglo<Trabajo> pendientes = taller.consultarOrdenPendiente(vehiculo);
        for (int i = 0; i < pendientes.tamaño(); i++) {
            System.out.println(" - " + pendientes.obtener(i));
        }

        taller.iniciarTrabajo(vehiculo, adicional.getId());
        taller.finalizarTrabajo(vehiculo, adicional.getId());
        taller.iniciarTrabajo(vehiculo, original.getId());
        taller.finalizarTrabajo(vehiculo, original.getId());
        taller.finalizarVehiculo(vehiculo);

        System.out.println("Orden cerrada: " + orden.estaCerrada());
        System.out.println("Vehículo pronto: " + taller.estaProntoParaRetirar(vehiculo));
        System.out.println("Búsqueda AVL por patente: "
                + taller.buscarVehiculoPorPatente("ABC123"));
    }
}
