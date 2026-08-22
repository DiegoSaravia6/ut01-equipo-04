package ucu.edu.aed.tda;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class CircularQueue<T> implements TDACola<T> {
    T[] vector;
    int front;
    int size;
    int capacity;
    
    public CircularQueue(int capacity) {
        this.capacity = capacity;
        this.vector = (T[]) new Object[capacity];
        this.front = 0;
        this.size = 0;
    }
/*
    PoneEnCola(Elemento):
        Si cantidad == tamaño entonces:
         Devolver error "La cola esta llena"
        Sino
            posicion = (frente + cantidad) mod tamaño
            arreglo[posicion] = elemento
            cantidad = cantidad + 1
        Fin si

    QuitaDeCola():
        Si cantidad == 0:
            Devolver error "La cola esta vacia"
        Sino
            info = arreglo[frente]
            cantidad = cantidad - 1
            frente = (frente + 1) mod tamaño
        Fin si
        Devolver info
*/

    public boolean poneEnCola(T dato) {
        if(size == capacity) {
            throw new IllegalStateException();
        }
        int position = (front + size) % capacity;
        vector[position] = dato;
        size++;
        return true;
    }

    public T quitaDeCola() {
        if(size == 0) {
            throw new NoSuchElementException();        
        }
        T info = vector[front];
        size--;
        front = (front + 1) % capacity;
        return info;
    }
    public boolean esVacio() {
        return size == 0;
    }
    public T frente() {
        if(size == 0) {
            throw new NoSuchElementException();
        }
        return vector[front];
    }

    public int tamaño() {
        return size;
    }
    
    public void agregar(T elem) {
        throw new UnsupportedOperationException();
    }
    public void agregar(int index, T elem) {
        throw new UnsupportedOperationException();
    }
    public T obtener(int index) {
        throw new UnsupportedOperationException();
    }

    public T remover(int index) {
        throw new UnsupportedOperationException();
    }

    public boolean remover(T elem) {
        throw new UnsupportedOperationException();
    }

    public boolean contiene(T elem) {
        throw new UnsupportedOperationException();
    }

    public int indiceDe(T elem) {
        throw new UnsupportedOperationException();
    }

    public T buscar(Predicate<T> criterio) {
        throw new UnsupportedOperationException();
    }

    public TDALista<T> ordenar(Comparator<T> comparator) {
        throw new UnsupportedOperationException();
    }

    public void vaciar() {
        throw new UnsupportedOperationException();
    }
}
/*
Los nodos ocupan más memoria al tener 2 partes, la información
y la dirección hacia el siguiente nodo, sin embargo el vector al
tener limitado desde un inicio la capacidad, los casilleros 
libres que no se están usando están ocupando memoria sin una función
útil, todos los nodos se usan aunque sean más pesados que una casilla
de un vector, en lo que corresponde a orden de tiempo, son igual de rápidos
ambos son de orden 1, salvo por ejemplo tamaño que lo agregue, que el
vector es de orden 1 y en los nodos es de orden n, en los escenarios que
ya tengas delimitado el limite que va a tener y se usen realmente las casillas
conviene más usar vectores pero en situaciones en que se puede agrandar de
manera inesperada como una fila de supermercado o casos incalculables seria
mejor usar nodos.
*/