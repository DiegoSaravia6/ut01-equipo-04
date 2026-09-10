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
        taller.registrarTallerista(new Tallerista(1, "Carlos"));

        Vehiculo vehiculo = new Vehiculo("ABC123", "Toyota", "Corolla", "Juan");
        vehiculo.agregarParte("VEHICULO", "MOTOR", "Motor");
        vehiculo.agregarParte("MOTOR", "DIST", "Distribución");
        vehiculo.agregarParte("DIST", "CORREA", "Correa");
        vehiculo.agregarParte("CORREA", "TENSOR", "Tensor");

        OrdenTrabajo orden = taller.registrarProblemaInformado(
                vehiculo,
                "Ruido al acelerar",
                LocalDate.of(2026, 9, 8),
                LocalDate.of(2026, 9, 12)
        );

        taller.atenderSiguiente();

        // El ingreso por problema informado crea automáticamente un diagnóstico.
        Trabajo diagnostico = orden.getDiagnosticoInicial();
        taller.iniciarTrabajo(vehiculo, diagnostico.getId());

        // El diagnóstico detecta un trabajo. Al ser derivado queda pendiente de aprobación.
        Trabajo original = taller.registrarTrabajoDerivado(
                vehiculo, diagnostico.getId(), "Cambiar correa", "CORREA", 4200);
        taller.aprobarTrabajo(vehiculo, original.getId());

        // Durante ese trabajo aparece otro trabajo derivado.
        taller.iniciarTrabajo(vehiculo, original.getId());
        Trabajo adicional = taller.registrarTrabajoDerivado(
                vehiculo, original.getId(), "Cambiar tensor", "TENSOR", 1800);

        System.out.println("=== ORDEN DE TRABAJO #" + orden.getId() + " ===");
        System.out.println("Costo propuesto: " + taller.costoOrden(vehiculo));
        System.out.println("Costo autorizado antes de aprobar el tensor: "
                + taller.costoAutorizadoOrden(vehiculo));

        taller.aprobarTrabajo(vehiculo, adicional.getId());

        System.out.println("Orden pendiente (hijos antes que padres):");
        ListaArreglo<Trabajo> pendientes = taller.consultarOrdenPendiente(vehiculo);
        for (int i = 0; i < pendientes.tamaño(); i++) {
            System.out.println(" - " + pendientes.obtener(i));
        }

        // Se resuelve desde la falla más profunda hacia arriba.
        taller.iniciarTrabajo(vehiculo, adicional.getId());
        taller.finalizarTrabajo(vehiculo, adicional.getId());

        taller.iniciarTrabajo(vehiculo, original.getId());
        taller.finalizarTrabajo(vehiculo, original.getId());

        taller.iniciarTrabajo(vehiculo, diagnostico.getId());
        taller.finalizarTrabajo(vehiculo, diagnostico.getId());

        taller.finalizarVehiculo(vehiculo);

        System.out.println("Orden cerrada: " + orden.estaCerrada());
        System.out.println("Vehículo pronto: " + taller.estaProntoParaRetirar(vehiculo));
        System.out.println("Búsqueda AVL por patente: "
                + taller.buscarVehiculoPorPatente("ABC123"));
    }
}
