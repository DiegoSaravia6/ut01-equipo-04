package Ej17;

import java.util.Arrays;
import java.util.Comparator;


public class Biblioteca{
    private final TDALista<Libro> catalogo;

    // crea catalogo
    public Biblioteca(){
        catalogo = new listaEnlazada<>();
    }

    // da de alta
    public void incorporarLibro(Libro libro){
        catalogo.insertar(libro);
    }

    // busca
    public Libro buscarLibro(String codigo){
       return catalogo.buscar(libro -> libro.getCodigo().equals(codigo));
    }
    
    // agrega
    public boolean agregarEjemplares(String codigo, int cantidad){
        Libro libro = buscarLibro(codigo);

        if(libro != null){
            libro.insertar(cantidad);
            return true;

        }
        return false;
    }
    
    // prestamo
    public int registrarPrestamo(String codigo, int cantidad){
        Libro libro = buscarLibro(codigo);
        if(libro != null){
            return libro.prestamosLibros(cantidad);
        }
        return 0;
    }

    // devolucion
    public boolean registrarDevolucion(String codigo, int cantidad){
        Libro libro = buscarLibro(codigo);
        if(libro != null){
            return catalogo.insertar(libro);
        }
        return false;
    }

    // retirar  || Intenta eliminar el libro con este codigo. Si existe, devolve true; si no existe, devolve false
    public boolean retirarLibro(String codigo){
        return catalogo.eliminar(libro -> libro.getCodigo().equals(codigo)); 
    }

    public int consultarExistencias(String codigo){
        Libro libro = buscarLibro(codigo);
        if (libro != null) {
        return libro.getCantidadEjemplaresDisponibles(); // porque queremos ver la cantidad, no solamente si existe o no
    }
        return -1;
    }

    public void listarLibros(){
        Libro[] libros = new Libro[catalogo.cantidad()];
        for(int i = 0; i < catalogo.cantidad(); i++){
            libros[i] = catalogo.obtener(i);
        }
        Arrays.sort(libros, Comparator.comparing(Libro::getTitulo));
    
    for(Libro libro : libros){
        System.out.println(libro.getTitulo() + "- stock: " + libro.getCantidadEjemplaresDisponibles());
    }
}

}