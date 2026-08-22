package EjSucursales;

import java.util.function.Predicate;

public interface TDALista<T>{
    T buscar(Predicate<T> criterio);
    public boolean insertar(T dato);
    public boolean eliminar(Predicate <T> criterio);
    public boolean esVacia();
    public int cantidad();
    public T obtener(int indice);
    void imprimir(String separador);

    

}