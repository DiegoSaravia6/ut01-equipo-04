package Ej17;
import java.util.function.Predicate;
// lista enlazada
public class listaEnlazada<T> implements  TDALista<T>{
    Nodo<T> primero; // Referencia al primer nodo de la lista || Si la lista esta vacia, primero vale null
    
    @Override

    // metodo insertar
    public boolean insertar(T dato){ // Metodo para insertar un dato generico en la lista y devuelve true si la insercion se hizo bien
        Nodo<T> nuevoNodo = new Nodo<>(dato); // crea nodo que guarda el dato recibido 
        if(primero == null){ // verifica si la lista esta vacia
            primero = nuevoNodo; // Si está vacía, el nuevo nodo pasa a ser el primer nodo.
        }
        else{ 
            Nodo<T> actual = primero; // Si la lista ya tiene elementos, se crea una referencia auxiliar llamada actual  y apunta al primer nodo
            while (actual.siguiente != null ){ // se recorre la lista mientras exista un nodo luego del nodo actual ||  avanza hasta el ultimo nodo, pero no hasta null
                actual = actual.siguiente; // actual pasa a apuntar al siguiente nodo.
            }
            actual.siguiente = nuevoNodo; // la referencia siguiente apunte al nuevo nodo
        }
        return true; // el dato fue insertado.
    }

    // metodo buscar
    @Override
    public T buscar(Predicate<T> criterio){ // T y no boolean porque devuelve el objeto buscado 
        Nodo<T> actual = primero; // al no estar insertando nada, tengo que crear una referencia auxiliar al primer nodo

        while(actual != null){
            if (criterio.test(actual.dato)){
                return actual.dato;
            }
            actual = actual.siguiente;
        }
        return null; /*
        return null  porque el método buscar devuelve un T, si no encontró ningún objeto que cumpla el criterio, 
        devolvés null para representar que el objeto no fue encontrado.
        */ 

    }

    // contar cantidad de ejempleares
    @Override
    public int cantidad(){
        int contador = 0; // inicializo contador en 0
        Nodo <T> actual = primero; // empiezo en el primer nodo
        while (actual!= null){ // mientras que el nodo actual sea distinto a nulo hacer:
            contador++; // aumenta el contador +1 si el nodo no es nulo
            actual = actual.siguiente; // avavanza hasta el ultimo nodo
        }
        return contador; // devuelve el contador
    }

    @Override
    public boolean eliminar(Predicate<T> criterio){
        Nodo <T> actual = primero;
        Nodo <T> anterior = null;

        while(actual != null ){
            if(criterio.test(actual.dato)){
                if(anterior == null){
                    primero = actual.siguiente;
                    
                }
                else{
                    anterior.siguiente = actual.siguiente;
                }
                return true;        
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return false;
    }


    @Override
    public boolean esVacia(){
        return primero == null;
    }

    
    @Override
    public T obtener (int indice){
        Nodo <T> actual = primero;
        int posicion = 0;

        while(actual != null){
            if(posicion == indice){
                return actual.dato;
            }
            posicion++;
            actual = actual.siguiente;
        }
        return null;
    }
}