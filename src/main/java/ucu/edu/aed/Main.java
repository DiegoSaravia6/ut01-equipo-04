package ucu.edu.aed;

import java.util.NoSuchElementException;
import java.util.Scanner;

import ucu.edu.aed.Taller.Reparacion;
import ucu.edu.aed.Taller.Taller;
import ucu.edu.aed.Taller.Tallerista;
import ucu.edu.aed.Taller.Vehiculo;
import ucu.edu.aed.tda.Lista;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        Taller taller = new Taller();
        Lista<Vehiculo> vehiculosRegistrados = new Lista<>();

        boolean ejecutando = true;

        System.out.println("======================================");
        System.out.println("       SISTEMA DE TALLER MECANICO");
        System.out.println("======================================");

        while (ejecutando) {

            mostrarMenu();

            int opcion = leerEntero("Seleccione una opcion: ");

            System.out.println();

            try {

                switch (opcion) {

                    case 1:
                        registrarTallerista(taller);
                        break;

                    case 2:
                        registrarVehiculo(taller, vehiculosRegistrados);
                        break;

                    case 3:
                        atenderVehiculo(taller);
                        break;

                    case 4:
                        agregarReparacion(taller, vehiculosRegistrados);
                        break;

                    case 5:
                        realizarReparacion(taller, vehiculosRegistrados);
                        break;

                    case 6:
                        enviarARepuestos(taller, vehiculosRegistrados);
                        break;

                    case 7:
                        continuarConRepuestos(taller);
                        break;

                    case 8:
                        finalizarVehiculo(taller, vehiculosRegistrados);
                        break;

                    case 9:
                        mostrarEstado(taller);
                        break;

                    case 0:
                        ejecutando = false;
                        System.out.println("Fin del sistema.");
                        break;

                    default:
                        System.out.println("Opcion invalida.");
                }

            } catch (NoSuchElementException e) {
                System.out.println("Operacion no disponible: "
                        + e.getMessage());

            } catch (IllegalStateException e) {
                System.out.println("Operacion no disponible: "
                        + e.getMessage());

            } catch (IllegalArgumentException e) {
                System.out.println("Dato invalido: "
                        + e.getMessage());
            }

            if (ejecutando) {
                System.out.println();
                System.out.println("Presione ENTER para continuar...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private static void mostrarMenu() {

        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println("              MENU");
        System.out.println("--------------------------------------");
        System.out.println("1. Registrar tallerista");
        System.out.println("2. Registrar vehiculo");
        System.out.println("3. Atender siguiente vehiculo");
        System.out.println("4. Agregar reparacion");
        System.out.println("5. Realizar proxima reparacion");
        System.out.println("6. Enviar vehiculo a espera de repuestos");
        System.out.println("7. Continuar trabajo con repuestos");
        System.out.println("8. Finalizar vehiculo");
        System.out.println("9. Consultar estado del taller");
        System.out.println("0. Salir");
        System.out.println("--------------------------------------");
    }

    private static void registrarTallerista(Taller taller) {

        System.out.println("--- Registrar tallerista ---");

        String nombre = leerTexto("Nombre del tallerista: ");

        Tallerista tallerista = new Tallerista(nombre);

        taller.registrarTallerista(tallerista);

        System.out.println("Tallerista registrado: " + nombre);
        System.out.println("Total de talleristas: "
                + taller.cantidadTalleristas());
    }

    private static void registrarVehiculo(
            Taller taller,
            Lista<Vehiculo> vehiculosRegistrados) {

        System.out.println("--- Registrar vehiculo ---");

        String patente = leerTexto("Patente: ");
        String marca = leerTexto("Marca: ");
        String modelo = leerTexto("Modelo: ");
        String dueño = leerTexto("Dueño: ");

        Vehiculo vehiculo = new Vehiculo(
                patente,
                marca,
                modelo,
                dueño
        );

        taller.registrarVehiculo(vehiculo);
        vehiculosRegistrados.agregar(vehiculo);

        System.out.println();
        System.out.println("Vehiculo registrado correctamente.");
        System.out.println("Patente: " + patente);
        System.out.println("Vehiculos esperando: "
                + taller.cantidadVehiculosEnEspera());
    }

    private static void atenderVehiculo(Taller taller) {

        System.out.println("--- Atender siguiente vehiculo ---");

        Vehiculo vehiculo = taller.atenderSiguiente();

        System.out.println("Vehiculo atendido: "
                + vehiculo.getPatente());

        System.out.println("Marca: "
                + vehiculo.getMarca());

        System.out.println("Modelo: "
                + vehiculo.getModelo());

        System.out.println("Vehiculos en trabajo: "
                + taller.cantidadVehiculosEnTrabajo());
    }

    private static void agregarReparacion(
            Taller taller,
            Lista<Vehiculo> vehiculosRegistrados) {

        System.out.println("--- Agregar reparacion ---");

        Vehiculo vehiculo =
                seleccionarVehiculo(vehiculosRegistrados);

        if (vehiculo == null) {
            return;
        }

        String descripcion =
                leerTexto("Descripcion de la reparacion: ");

        String tipo =
                leerTexto("Tipo de reparacion: ");

        Reparacion reparacion =
                new Reparacion(descripcion, tipo);

        taller.agregarReparacion(
                vehiculo,
                reparacion
        );

        System.out.println();
        System.out.println("Reparacion agregada.");
        System.out.println("Vehiculo: "
                + vehiculo.getPatente());
        System.out.println("Reparacion: "
                + descripcion);
    }

    private static void realizarReparacion(
            Taller taller,
            Lista<Vehiculo> vehiculosRegistrados) {

        System.out.println("--- Realizar proxima reparacion ---");

        Vehiculo vehiculo =
                seleccionarVehiculo(vehiculosRegistrados);

        if (vehiculo == null) {
            return;
        }

        Reparacion reparacion =
                taller.realizarProximaReparacion(vehiculo);

        System.out.println();
        System.out.println("Reparacion realizada:");
        System.out.println(reparacion.getDescripcion());
        System.out.println("Tipo: "
                + reparacion.getTipo());

        System.out.println("Total realizadas: "
                + vehiculo.cantidadReparacionesRealizadas());
    }

    private static void enviarARepuestos(
            Taller taller,
            Lista<Vehiculo> vehiculosRegistrados) {

        System.out.println("--- Espera por repuestos ---");

        Vehiculo vehiculo =
                seleccionarVehiculo(vehiculosRegistrados);

        if (vehiculo == null) {
            return;
        }

        taller.esperarRepuestos(vehiculo);

        System.out.println();
        System.out.println("Vehiculo enviado a espera de repuestos: "
                + vehiculo.getPatente());

        System.out.println("Esperando repuestos: "
                + taller.cantidadEsperandoRepuestos());

        System.out.println("Vehiculos en trabajo: "
                + taller.cantidadVehiculosEnTrabajo());
    }

    private static void continuarConRepuestos(Taller taller) {

        System.out.println("--- Llegada de repuestos ---");

        Vehiculo vehiculo =
                taller.continuarConRepuestos();

        System.out.println("El vehiculo vuelve al trabajo: "
                + vehiculo.getPatente());

        System.out.println("Esperando repuestos: "
                + taller.cantidadEsperandoRepuestos());

        System.out.println("Vehiculos en trabajo: "
                + taller.cantidadVehiculosEnTrabajo());
    }

    private static void finalizarVehiculo(
            Taller taller,
            Lista<Vehiculo> vehiculosRegistrados) {

        System.out.println("--- Finalizar vehiculo ---");

        Vehiculo vehiculo =
                seleccionarVehiculo(vehiculosRegistrados);

        if (vehiculo == null) {
            return;
        }

        taller.finalizarVehiculo(vehiculo);

        System.out.println();
        System.out.println("Vehiculo finalizado: "
                + vehiculo.getPatente());

        System.out.println("Listo para retirar: "
                + taller.estaProntoParaRetirar(vehiculo));

        System.out.println("Vehiculos prontos: "
                + taller.cantidadVehiculosProntos());
    }

    private static void mostrarEstado(Taller taller) {

        System.out.println("======================================");
        System.out.println("          ESTADO DEL TALLER");
        System.out.println("======================================");

        System.out.println("Talleristas: "
                + taller.cantidadTalleristas());

        System.out.println("Esperando atencion: "
                + taller.cantidadVehiculosEnEspera());

        System.out.println("En trabajo: "
                + taller.cantidadVehiculosEnTrabajo());

        System.out.println("Esperando repuestos: "
                + taller.cantidadEsperandoRepuestos());

        System.out.println("Prontos para retirar: "
                + taller.cantidadVehiculosProntos());

        System.out.println("======================================");
    }

    private static Vehiculo seleccionarVehiculo(
            Lista<Vehiculo> vehiculosRegistrados) {

        if (vehiculosRegistrados.esVacio()) {
            System.out.println("No hay vehiculos registrados.");
            return null;
        }

        String patente =
                leerTexto("Ingrese la patente del vehiculo: ");

        for (int i = 0;
             i < vehiculosRegistrados.tamaño();
             i++) {

            Vehiculo vehiculo =
                    vehiculosRegistrados.obtener(i);

            if (vehiculo.getPatente().equalsIgnoreCase(patente)) {
                return vehiculo;
            }
        }

        System.out.println(
                "No se encontro un vehiculo con esa patente."
        );

        return null;
    }

    private static String leerTexto(String mensaje) {

        System.out.print(mensaje);

        String texto = scanner.nextLine().trim();

        if (texto.isEmpty()) {
            throw new IllegalArgumentException(
                    "El dato no puede estar vacio."
            );
        }

        return texto;
    }

    private static int leerEntero(String mensaje) {

        System.out.print(mensaje);

        String entrada = scanner.nextLine().trim();

        try {
            return Integer.parseInt(entrada);

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Debe ingresar un numero."
            );
        }
    }
}

