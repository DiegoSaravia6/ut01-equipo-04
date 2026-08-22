package ucu.edu.aed.tda;

import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.Comparator;

public class Queue<T> implements TDACola<T> {
    Node<T> first;
    Node<T> last;

    public Queue() {
        this.first = null;
        this.last = null;
    }
    
    public boolean esVacio() {
        return first == null;
    }

    public boolean poneEnCola(T dato) {
        Node<T> newNode = new Node<>(dato);
        if (first == null) {
            first = newNode;
            last = newNode;
        }
        else {
            last.next = newNode;
            last = newNode;
        }
        return true;
    }
    public T quitaDeCola() {
        if(first == null){
            throw new NoSuchElementException();
        }
        T info = first.data;
        first = first.next;
        if(first == null) {
            last = null;
        }
        
        return info;
    }
    public T frente() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        else {
            return first.data;
        }
    }
    public int tamaño() {
        int counter = 0;
        Node<T> current = first;
        while (current != null) {
            counter += 1;
            current = current.next;
        }
        return counter;
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

