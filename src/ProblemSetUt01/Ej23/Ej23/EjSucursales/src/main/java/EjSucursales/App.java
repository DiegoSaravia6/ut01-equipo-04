package EjSucursales;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class App{
    public static void main(String[]args) throws FileNotFoundException{
        DirectorioSucursales directorio;

        directorio = new DirectorioSucursales();

        cargarArchivo("sucursales.txt", directorio);

        System.out.println("Sucursales");
        directorio.listarSucursales("\n");

        System.out.println("La cantidad de sucursales" + directorio.cantidadSucursales());
        
        directorio = new DirectorioSucursales();
        System.out.println("Cantidad de sucursales:" + directorio.cantidadSucursales());
        
        // sucursal 1
        directorio = new DirectorioSucursales();

        cargarArchivo("suc1.txt", directorio);

        System.out.println();
        System.out.println("sucursal 1:");
        directorio.listarSucursales("\n");

        System.out.println("Cantidad de sucursales: " + directorio.cantidadSucursales());

        directorio.quitarSucursal("Chicago");

        System.out.println();
        System.out.println("SUC1 SIN CHICAGO");

        System.out.println("Sucursales");
        directorio.listarSucursales("\n");


        
        // TAREA 3  suc2.txt
        

        directorio = new DirectorioSucursales();

        cargarArchivo("suc2.txt", directorio);

        directorio.quitarSucursal("Shenzen");
        directorio.quitarSucursal("Tokio");

        System.out.println();
        System.out.println("SUC2 SIN SHENZEN Y TOKIO");

        directorio.listarSucursales("\n");

        System.out.println("Cantidad restante: "+ directorio.cantidadSucursales());


        
        // TAREA 4  suc3.txt
       

        directorio = new DirectorioSucursales();

        cargarArchivo("suc3.txt", directorio);

        System.out.println();
        System.out.println("SUC3");

        directorio.listarSucursales(";");

        System.out.println();
    }


    public static void cargarArchivo(String nombreArchivo, DirectorioSucursales directorio) throws FileNotFoundException {

        File archivo = new File("src/main/resources/" + nombreArchivo);

        Scanner scanner = new Scanner(archivo);

        while (scanner.hasNextLine()) {

            String ciudad = scanner.nextLine().trim();

            directorio.agregarSucursal(ciudad);
        }

        scanner.close();
    }
}


