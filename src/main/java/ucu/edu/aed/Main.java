package ucu.edu.aed;

import ucu.edu.aed.Taller.Reparacion;
import ucu.edu.aed.Taller.Taller;
import ucu.edu.aed.Taller.Tallerista;
import ucu.edu.aed.Taller.Vehiculo;

public class Main {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("       SISTEMA DE TALLER MECANICO");
        System.out.println("======================================");

        // Crear el taller
        Taller taller = new Taller();

        // =========================================
        // REGISTRAR TALLERISTAS
        // =========================================

        Tallerista carlos = new Tallerista("Carlos");
        Tallerista pedro = new Tallerista("Pedro");

        taller.registrarTallerista(carlos);
        taller.registrarTallerista(pedro);

        System.out.println("\nTalleristas registrados: "
                + taller.cantidadTalleristas());

        // =========================================
        // REGISTRAR VEHICULOS
        // =========================================

        Vehiculo vehiculo1 = new Vehiculo(
                "ABC123",
                "Toyota",
                "Corolla",
                "Juan"
        );

        Vehiculo vehiculo2 = new Vehiculo(
                "DEF456",
                "Ford",
                "Focus",
                "Pedro"
        );

        Vehiculo vehiculo3 = new Vehiculo(
                "GHI789",
                "Chevrolet",
                "Onix",
                "Ana"
        );

        taller.registrarVehiculo(vehiculo1);
        taller.registrarVehiculo(vehiculo2);
        taller.registrarVehiculo(vehiculo3);

        System.out.println("\nVehiculos registrados: "
                + taller.cantidadVehiculosEnEspera());

        System.out.println("Proximo vehiculo: "
                + taller.proximoVehiculo().getPatente());

        // =========================================
        // ATENDER VEHICULOS
        // =========================================

        System.out.println("\n--- Atencion de vehiculos ---");

        Vehiculo atendido1 = taller.atenderSiguiente();

        System.out.println("Atendiendo: "
                + atendido1.getPatente());

        System.out.println("Tallerista asignado: "
                + carlos.getNombre());

        Vehiculo atendido2 = taller.atenderSiguiente();

        System.out.println("Atendiendo: "
                + atendido2.getPatente());

        System.out.println("Vehiculos en trabajo: "
                + taller.cantidadVehiculosEnTrabajo());

        System.out.println("Proximo en espera: "
                + taller.proximoVehiculo().getPatente());

        // =========================================
        // REPARACIONES
        // =========================================

        System.out.println("\n--- Reparaciones ---");

        Reparacion mantenimiento = new Reparacion(
                "Cambio de aceite",
                "Mantenimiento"
        );

        taller.agregarReparacion(
                vehiculo1,
                mantenimiento
        );

        System.out.println("Reparacion registrada: "
                + mantenimiento.getDescripcion());

        // Durante la inspeccion aparece una falla adicional.
        Reparacion fallaAdicional = new Reparacion(
                "Cambio de pastillas de freno",
                "Falla adicional"
        );

        taller.agregarReparacion(
                vehiculo1,
                fallaAdicional
        );

        System.out.println("Falla adicional registrada: "
                + fallaAdicional.getDescripcion());

        // La falla adicional se atiende primero por usar una pila.
        Reparacion realizada =
                taller.realizarProximaReparacion(vehiculo1);

        System.out.println("Primera reparacion realizada: "
                + realizada.getDescripcion());

        realizada =
                taller.realizarProximaReparacion(vehiculo1);

        System.out.println("Segunda reparacion realizada: "
                + realizada.getDescripcion());

        System.out.println("Reparaciones realizadas: "
                + vehiculo1.cantidadReparacionesRealizadas());

        // =========================================
        // ESPERA POR REPUESTOS
        // =========================================

        System.out.println("\n--- Espera por repuestos ---");

        taller.esperarRepuestos(vehiculo1);

        System.out.println("Vehiculo enviado a espera de repuestos: "
                + vehiculo1.getPatente());

        System.out.println("Vehiculos esperando repuestos: "
                + taller.cantidadEsperandoRepuestos());

        System.out.println("Vehiculos en trabajo: "
                + taller.cantidadVehiculosEnTrabajo());

        System.out.println("Carlos esta disponible: "
                + carlos.estaDisponible());

        // =========================================
        // CONTINUAR TRABAJO
        // =========================================

        System.out.println("\n--- Llegada de repuestos ---");

        Vehiculo continuado =
                taller.continuarConRepuestos();

        System.out.println("Continua el trabajo de: "
                + continuado.getPatente());

        System.out.println("Vehiculos esperando repuestos: "
                + taller.cantidadEsperandoRepuestos());

        System.out.println("Vehiculos en trabajo: "
                + taller.cantidadVehiculosEnTrabajo());

        // =========================================
        // FINALIZAR VEHICULO
        // =========================================

        System.out.println("\n--- Finalizacion ---");

        taller.finalizarVehiculo(vehiculo1);

        System.out.println("Vehiculo finalizado: "
                + vehiculo1.getPatente());

        System.out.println("Vehiculos en trabajo: "
                + taller.cantidadVehiculosEnTrabajo());

        System.out.println("Vehiculos prontos: "
                + taller.cantidadVehiculosProntos());

        System.out.println("Listo para retirar: "
                + taller.estaProntoParaRetirar(vehiculo1));

        // =========================================
        // ESTADO FINAL
        // =========================================

        System.out.println("\n======================================");
        System.out.println("           ESTADO FINAL");
        System.out.println("======================================");

        System.out.println("Esperando atencion: "
                + taller.cantidadVehiculosEnEspera());

        System.out.println("En trabajo: "
                + taller.cantidadVehiculosEnTrabajo());

        System.out.println("Esperando repuestos: "
                + taller.cantidadEsperandoRepuestos());

        System.out.println("Prontos para retirar: "
                + taller.cantidadVehiculosProntos());

        System.out.println("======================================");
        System.out.println("       FIN DE LA DEMOSTRACION");
        System.out.println("======================================");
    }
}