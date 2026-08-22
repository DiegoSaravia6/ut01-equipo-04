package Ej17;

public class Libro{
    private final String titulo;
    private final double precioReposicion;
    private final String codigo;
    private  int cantidadEjemplaresDisponibles;

    public Libro(String titulo, double precioReposicion, String codigo, int cantidadEjemplaresDisponibles){
        this.titulo = titulo;
        this.precioReposicion = precioReposicion;
        this.codigo = codigo;
        this.cantidadEjemplaresDisponibles = cantidadEjemplaresDisponibles;
    }

    // getters de los atributos privados
    public String getTitulo(){
        return titulo;
    }

    public double getPrecioReposicion(){
        return precioReposicion;
    }
    public String getCodigo(){
        return codigo;
    }
    public int getCantidadEjemplaresDisponibles(){
        return cantidadEjemplaresDisponibles;
    }

    public void insertar(int agregados){
        cantidadEjemplaresDisponibles += agregados;
    }

    public int prestamosLibros(int pedidos) {
    if (pedidos <= cantidadEjemplaresDisponibles) {
        cantidadEjemplaresDisponibles -= pedidos;
        return pedidos;
    }

    int librosPrestados = cantidadEjemplaresDisponibles;
    cantidadEjemplaresDisponibles = 0;

    return librosPrestados;
}

}