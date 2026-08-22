package Ej17;
import java.util.function.Predicate;


public interface TDALista<T>{
 // metodos de insercion y busqueda

 // estos metodos son llamados en listaEnlazada
 boolean insertar (T dato);
 T buscar (Predicate<T> crieterio);

 boolean eliminar(Predicate<T> criterio);

 boolean esVacia();

 int cantidad();

 T obtener (int indice);
}

