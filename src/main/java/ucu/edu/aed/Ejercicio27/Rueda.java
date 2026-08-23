package ucu.edu.aed.ProblemSet01.Ejercicio27;

import ucu.edu.aed.tda.ListaCircular;

/*
Analisis de orden de tiempo:
- El constructor es O(N), porque crea N cabinas.
- avanzar() es O(1), porque solo actualiza el indice con modulo.
- subirPasajeros() y bajarPasajeros() son O(N) en el peor caso,
  porque obtener(indicePlataforma) recorre la lista circular.
- Las operaciones subir() y bajar() de Cabina son O(1).

Si la rueda pudiera agregar o quitar cabinas durante su funcionamiento,
ListaCircular seguiria siendo util, pero habria que ajustar
indicePlataforma cuando se quite una cabina anterior o la cabina actual.
Una lista circular doblemente enlazada con una referencia directa a la
cabina de la plataforma permitiria avanzar y quitar la cabina actual en O(1).
*/


public class Rueda 
{
    public ListaCircular<Cabina> cabinas;
    public int indicePlataforma;

    public Rueda(int cantidadCabinas, int capacidadCabina) 
    {
        if (cantidadCabinas <= 0 || capacidadCabina < 0) 
        {
            throw new IllegalArgumentException("Parámetros inválidos");
        }
        cabinas = new ListaCircular<>();
        for (int i = 0; i < cantidadCabinas; i++) 
        {
            cabinas.agregar(new Cabina(capacidadCabina));
        }
        indicePlataforma = 0;
    }

    public boolean avanzar()
    {
        if (cabinas == null || cabinas.esVacio()) 
        {
            return false; // No hay cabinas para avanzar
        }

        indicePlataforma = (indicePlataforma + 1) % cabinas.tamaño();
        return true;
    }

    public boolean subirPasajeros(int cantidad) 
    {
        if (cabinas == null || cabinas.esVacio()) 
        {
            return false; // No hay cabinas para subir pasajeros
        }

        Cabina cabinaActual = cabinas.obtener(indicePlataforma);
        return cabinaActual.subir(cantidad);
    }

    public boolean bajarPasajeros() 
    {
        if (cabinas == null || cabinas.esVacio()) 
        {
            return false;
        }

        Cabina cabinaActual = cabinas.obtener(indicePlataforma);
        return cabinaActual.bajar(cabinaActual.getOcupacion());
    }
}
