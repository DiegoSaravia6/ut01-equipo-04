package EjSucursales;

public class DirectorioSucursales {

    private final TDALista<String> sucursales;

    // Constructor: crea la lista vacía de sucursales
    public DirectorioSucursales() {
        sucursales = new ListaEnlazada<>();
    }

    // Agrega una nueva sucursal
    public boolean agregarSucursal(String ciudad) {
        return sucursales.insertar(ciudad);
    }

    // Busca una sucursal por nombre
    public String buscarSucursal(String ciudad) {
        return sucursales.buscar(
            sucursal -> sucursal.equals(ciudad)
        );
    }

    // Elimina una sucursal
    public boolean quitarSucursal(String ciudad) {
        return sucursales.eliminar(
            sucursal -> sucursal.equals(ciudad)
        );
    }

    // Lista todas las sucursales usando el separador recibido
    public void listarSucursales(String separador) {
        sucursales.imprimir(separador);
    }

    // Devuelve la cantidad total de sucursales
    public int cantidadSucursales() {
        return sucursales.cantidad();
    }

    // Devuelve true si no hay sucursales
    public boolean estaVacio() {
        return sucursales.esVacia();
    }
}