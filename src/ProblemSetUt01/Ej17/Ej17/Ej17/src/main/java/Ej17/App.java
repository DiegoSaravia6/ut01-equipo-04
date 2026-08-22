package Ej17;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws FileNotFoundException {

        Biblioteca biblioteca = new Biblioteca();

        double valorTotalAgregado = 0;
        int variacionPrestamos = 0;


        // =========================
        // PROCESAR ADQUISICIONES
        // =========================

        File archivoAdquisiciones = new File("src/main/resources/adquisiciones.txt");

        Scanner scannerAdquisiciones =  new Scanner(archivoAdquisiciones);


        while (scannerAdquisiciones.hasNextLine()) {

            String linea = scannerAdquisiciones.nextLine();

            String[] datos = linea.split(",");

            String codigo = datos[0].trim();
            String titulo = datos[1].trim();
            double precio = Double.parseDouble(datos[2].trim());
            int cantidad = Integer.parseInt(datos[3].trim());


            Libro libro = biblioteca.buscarLibro(codigo);


            // Si el libro ya existe
            if (libro != null) {

                biblioteca.agregarEjemplares(codigo, cantidad);

            } else {

                // Si no existe, se crea y se incorpora
                Libro nuevoLibro = new Libro(titulo, precio, codigo,cantidad);

                biblioteca.incorporarLibro(nuevoLibro);
            }


            // Valor agregado por esta adquisición
            valorTotalAgregado += precio * cantidad;
        }


        scannerAdquisiciones.close();


        // =========================
        // PROCESAR PRESTAMOS
        // =========================

        File archivoPrestamos =
                new File("src/main/resources/prestamos.txt");

        Scanner scannerPrestamos =
                new Scanner(archivoPrestamos);


        while (scannerPrestamos.hasNextLine()) {

            String linea = scannerPrestamos.nextLine();

            String[] datos = linea.split(",");

            String codigo = datos[0].trim();
            String tipo = datos[1].trim();
            int cantidad = Integer.parseInt(datos[2].trim());


            if (tipo.equals("PRESTAMO")) {

                // Devuelve cuántos ejemplares
                // realmente se pudieron prestar
                int prestados = biblioteca.registrarPrestamo(codigo, cantidad);

                variacionPrestamos += prestados;

            } else if (tipo.equals("DEVOLUCION")) {

                boolean devolucionRealizada =biblioteca.registrarDevolucion(codigo, cantidad);

                if (devolucionRealizada) {

                    variacionPrestamos -= cantidad;
                }
            }
        }


        scannerPrestamos.close();


        // =========================
        // MOSTRAR RESULTADOS
        // =========================

        System.out.println("Valor total agregado al stock: " + valorTotalAgregado);

        System.out.println("Variacion total de ejemplares prestados: " + variacionPrestamos
        );

        System.out.println();
        System.out.println("Catalogo final:");
        System.out.println("----------------");

        biblioteca.listarLibros();
    }
}